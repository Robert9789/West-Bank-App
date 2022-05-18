package com.bank.westbank.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.bank.westbank.Credit.Credit;
import com.bank.westbank.Credit.CustomEventListener;
import com.bank.westbank.R;
import com.bank.westbank.Sign.SignIn;
import com.bank.westbank.UserInfo.ContBancar;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.List;

import static com.bank.westbank.Sign.SignIn.mainUser;
import static com.parse.Parse.getApplicationContext;

public class CreditAdapter extends RecyclerView.Adapter<CreditAdapter.CardViewObjectHolder> {
    private Context mContext;
    private List<Credit> list;
    private CustomEventListener listener;
    public void setListener(CustomEventListener listener){
        this.listener=listener;
    }
    public CreditAdapter(Context mContext, List<Credit> list) {
        this.mContext = mContext;
        this.list = list;
    }
    public class CardViewObjectHolder extends RecyclerView.ViewHolder{
        public TextView textViewRata;
        public TextView textViewRataDobanzi;
        public TextView textViewSuma;
        public Button buttonCreditPlata;
        public CardView cardViewCredit;
        public CardViewObjectHolder(View view){
            super(view);
            textViewRata = view.findViewById(R.id.textView_installment);
            textViewRataDobanzi = view.findViewById(R.id.textView_interest_rate);
            textViewSuma = view.findViewById(R.id.textView_credit_amount);
            buttonCreditPlata = view.findViewById(R.id.buttonCreditPay);
            cardViewCredit = view.findViewById(R.id.cardView_credit);
        }
    }
    @NonNull
    @Override
    public CreditAdapter.CardViewObjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.credit_cardview,parent,false);
        return new CreditAdapter.CardViewObjectHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull CreditAdapter.CardViewObjectHolder holder, int position) {
        Credit credit = list.get(position);
        holder.textViewRata.setText(String.valueOf(credit.getRata())+" luni");
        holder.textViewRataDobanzi.setText("%"+credit.getRataDobanzi());
        holder.textViewSuma.setText(credit.getSumaDePlata()+" TL");
        holder.cardViewCredit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(listener!=null){
                    payCredit(credit,position);
                    listener.MyEventListener();
                }
            }
        });
    }
    @Override
    public int getItemCount() {
        return list.size();
    }
    public void payCredit(Credit tempCredit,int position){
        int max=Integer.MIN_VALUE;
        int index=-1;
        for(int i = 0; i<SignIn.mainUser.getConturiBancare().size(); i++){
            if(SignIn.mainUser.getConturiBancare().get(i).getCash()>max){
                max=SignIn.mainUser.getConturiBancare().get(i).getCash();
                index=i;
            }
        }
        if((SignIn.mainUser.getConturiBancare().size()>0) && ((SignIn.mainUser.getConturiBancare().get(index).getCash()) >= (tempCredit.getSumaDePlata()) )){
            SignIn.mainUser.getConturiBancare().get(index).setCash(SignIn.mainUser.getConturiBancare().get(index).getCash()-tempCredit.getSumaDePlata());
            updateBankAccount(SignIn.mainUser.getConturiBancare().get(index));
            //stergerea cardului de credit din baza de date si a utilizatorului

            deleteCreditFromDatabase(tempCredit);
            list.remove(position);
            mainUser.setCredite(new ArrayList<Credit>(list));
            Toast.makeText(getApplicationContext(),"Credit platit",Toast.LENGTH_LONG).show();
        }
        else {
        }
    }
    public void deleteCreditFromDatabase(Credit tempCredit){
        String sumaDePlata = String.valueOf(tempCredit.getSumaDePlata());
        String rata= String.valueOf(tempCredit.getRata());
        String rataDobanzi = String.valueOf(tempCredit.getRataDobanzi());
        String suma = String.valueOf(tempCredit.getSuma());
        ParseQuery<ParseObject> queryDel2=ParseQuery.getQuery("Credit");
        queryDel2.whereEqualTo("utilizator", SignIn.mainUser.getId());
        queryDel2.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e!=null){
                    e.printStackTrace();
                }else{
                    if(objects.size()>0){
                        for (ParseObject object: objects){

                            if(object.getString("suma").equals(suma) && object.getString("rate").equals(rata) &&
                            object.getString("rataDobanzii").equals(rataDobanzi) && object.getString("sumaPlatita").equals(sumaDePlata)){
                                object.deleteInBackground();
                            }
                        }
                    } }
                }
        });
    }
    public void updateBankAccount(ContBancar bankac){
        ParseQuery<ParseObject> queryBankAccount=ParseQuery.getQuery("BankAccount");
        queryBankAccount.whereEqualTo("accountNo", bankac.getNrCont());
        queryBankAccount.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if(e!=null){
                    e.printStackTrace();
                }else{
                    if(objects.size()>0){
                        for(ParseObject object:objects){
                            object.deleteInBackground();
                            accountsToDatabase(bankac);
                        }
                    }
                } }
        });
    }
    public void accountsToDatabase(ContBancar bankAc){
        ParseObject object=new ParseObject("ContBancar");
        object.put("NrCont",bankAc.getNrCont());
        object.put("CNP", SignIn.mainUser.getId());
        object.put("cash",String.valueOf(bankAc.getCash()));
        object.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e != null){
                    //Toast.makeText(getApplicationContext(),e.getLocalizedMessage().toString(),Toast.LENGTH_LONG).show();
                }
                else{
                }
            }
        });
    }
}
