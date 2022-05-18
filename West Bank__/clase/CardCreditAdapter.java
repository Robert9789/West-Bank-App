package com.bank.westbank.Adapters;

import android.app.Activity;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bank.westbank.R;
import com.bank.westbank.Sign.SignIn;
import com.bank.westbank.UserInfo.ContBancar;
import com.bank.westbank.UserInfo.CreditCard;
import com.bank.westbank.UserInfo.Istoric;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Stack;

import static com.bank.westbank.Sign.SignIn.mainUser;
import static com.parse.Parse.getApplicationContext;

public class CardCreditAdapter extends RecyclerView.Adapter<CardCreditAdapter.ViewHolder> {
    ArrayList<ContBancar> ConturileBancarePersonale;
    ArrayList<CreditCard> CardurileDeCredit;
    Activity stareaActuala;
    RecyclerView recyclerViewbankCont;
    TextView text_view_total_bani;

    public CardCreditAdapter(ArrayList<CreditCard> dateleCardCredit, Activity activity, ArrayList<ContBancar> ConturileBancarePersonale, RecyclerView recyclerViewbankCont) {
        this.CardurileDeCredit = dateleCardCredit;
        this.stareaActuala = activity;
        this.ConturileBancarePersonale = ConturileBancarePersonale;
        this.recyclerViewbankCont = recyclerViewbankCont;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewTip) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.credit_car_cardview,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
    @NonNull

    @Override
    public void onBindViewHolder(@NonNull ViewHolder titular, int pozitia) {
        text_view_total_bani = stareaActuala.findViewById(R.id.text_view_total_money);
        final CreditCard CreditCard = CardurileDeCredit.get(pozitia);
        titular.textCreditCardNumar.setText(CreditCard.getCardCreditNr());
        titular.textCardCreditLimita.setText("$ "+String.valueOf(CreditCard.getLimit()));

        titular.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConturileBancarePersonale.size()==0){
                    AlertDialog.Builder ad = new AlertDialog.Builder(stareaActuala);
                    ad.setTitle("Nu aveti nici un cont bancar.Adaugati un cont bancar inainte de a plati datoria cardului de credit.");
                    ad.setNegativeButton("CLOSE", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialogInterface, int i){
                        }
                    });
                    ad.create().show();
                }
                else{
                    final EditText editText = new EditText(stareaActuala);
                    editText.setHint("Cat doriti sa platiti?");
                    editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                    AlertDialog.Builder ad = new AlertDialog.Builder(stareaActuala);
                    ad.setTitle("Cu ce Cont Bancar doriti sa platiti?");
                    ad.setIcon(R.drawable.icon_credit_card);
                    ad.setView(editText);
                    String[] items = new String[ConturileBancarePersonale.size()];
                    for (int i = 0; i< ConturileBancarePersonale.size(); i++){
                        String data= ConturileBancarePersonale.get(i).getNrCont() + "  $" + Integer.toString(ConturileBancarePersonale.get(i).getCash());
                        items[i] = data;
                    }
                    final int[] checkedItem = {0};
                    ad.setSingleChoiceItems(items, checkedItem[0], new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            switch (i) {
                                case 0:
                                    checkedItem[0] =i;
                                    break;
                                case 1:
                                    checkedItem[0] =i;
                                    break;
                                case 2:
                                    checkedItem[0] =i;
                                    break;
                                case 3:
                                    checkedItem[0] =i;
                                    break;
                                case 4:
                                    checkedItem[0] =i;
                                    break;
                            }
                        }
                    });
                    ad.setNegativeButton("Plateste", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            i= checkedItem[0];
                            CardurileDeCredit.get(pozitia).setLimit(CardurileDeCredit.get(pozitia).getLimit() + Integer.parseInt(editText.getText().toString()));
                            titular.textCardCreditLimita.setText(String.valueOf(CreditCard.getLimit()));
                            ConturileBancarePersonale.get(i).setCash(ConturileBancarePersonale.get(i).getCash() -Integer.parseInt(editText.getText().toString()));
                            updateBankAccount(ConturileBancarePersonale.get(i));
                            updateCreditCards(CardurileDeCredit.get(pozitia));
                            setTotalMoney(ConturileBancarePersonale);
                            ContBancarAdapter contBancarAdapter = new ContBancarAdapter(ConturileBancarePersonale, stareaActuala);
                            recyclerViewbankCont.setAdapter(contBancarAdapter);
                            Istoric hs = new Istoric(mainUser.getId(),"Card Credit platit.",getDate() );
                            mainUser.getIstoric().push(hs);
                            historyToDatabase(hs);
                        }
                    });
                    ad.create().show();
                } }
        });
    }
    @Override
    public int getItemCount() {
        return CardurileDeCredit.size();
    }
    public void setTotalMoney(ArrayList<ContBancar> MyBankAccounts){
        int totalBani = 0;
        for (int i = 0; i<MyBankAccounts.size();i++){
            totalBani += MyBankAccounts.get(i).getCash();
        }
        text_view_total_bani.setText(Integer.toString(totalBani));
    }
    public void accountsToDatabase(ContBancar bankAc){
        ParseObject object=new ParseObject("BankAccount");
        object.put("contNr",bankAc.getNrCont());
        object.put("userId", SignIn.mainUser.getId());
        object.put("cash",String.valueOf(bankAc.getCash()));
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"datele banci",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public void cardsToDatabase(CreditCard card){
        ParseObject object=new ParseObject("CreditCard");
        object.put("cardCreditNr",card.getCardCreditNr());
        object.put("CNP", SignIn.mainUser.getId());

        object.put("limita",String.valueOf(card.getLimit()));
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"date",Toast.LENGTH_LONG).show();

                }
            }
        });
    }
    public void updateCreditCards(CreditCard card){
        ParseQuery<ParseObject> interogareContBancar=ParseQuery.getQuery("CardCredit");
        interogareContBancar.whereEqualTo("cardCreditNr", card.getCardCreditNr());
        interogareContBancar.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e!=null){
                    e.printStackTrace();
                }else{
                    if(objects.size()>0){
                        for(ParseObject object:objects){
                            object.deleteInBackground();
                            Toast.makeText(getApplicationContext(),"sildi",Toast.LENGTH_LONG).show();
                            cardsToDatabase(card);
                        }
                    }
                }
            }
        });
    }
    public void updateBankAccount(ContBancar contbancar){
        ParseQuery<ParseObject> interogareContBancar=ParseQuery.getQuery("ContBancar");
        interogareContBancar.whereEqualTo("NrCont", contbancar.getNrCont());
        interogareContBancar.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e!=null){
                    e.printStackTrace();
                }else{
                    if(objects.size()>0){
                        for(ParseObject object:objects){
                            object.deleteInBackground();
                            Toast.makeText(getApplicationContext(),".",Toast.LENGTH_LONG).show();
                            accountsToDatabase(contbancar);
                        }
                    }
                }
            }
        });
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageCreditCard;
        TextView textCardCreditLimita;
        TextView textCreditCardNumar;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageCreditCard = itemView.findViewById(R.id.image_view_add_credit_card);
            textCardCreditLimita = itemView.findViewById(R.id.text_view_credit_card_limit);
            textCreditCardNumar = itemView.findViewById(R.id.text_view_credit_card_no);
            cardView = itemView.findViewById(R.id.card_view_credit_card);
        }
    }
    public void historyToDatabase(Istoric istoric){
        ParseObject object=new ParseObject("Istoric");
        object.put("procesare",istoric.getProcesare());
        object.put("CNP", mainUser.getId());
        object.put("data",istoric.getDateDate());
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    Toast.makeText(getApplicationContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"istoric date",Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    public ArrayList<Istoric> stackToArrayList(Stack<Istoric> stack){
        ArrayList<Istoric> arraylistHistory = new ArrayList<>();
        while (stack.size() !=0){
            arraylistHistory.add(stack.pop());
        }
        for (int i =arraylistHistory.size()-1;i>=0; i-- ) {
            mainUser.getIstoric().push(arraylistHistory.get(i));
        }
        return arraylistHistory;
    }
    public Date getDate(){
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        Date timpulActual = Calendar.getInstance().getTime();
        return timpulActual;
    }
}
