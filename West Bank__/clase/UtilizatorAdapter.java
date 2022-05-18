package com.bank.westbank.Adapters;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bank.westbank.R;
import com.bank.westbank.UserInfo.User;
import java.util.ArrayList;

public class UtilizatorAdapter extends RecyclerView.Adapter<UtilizatorAdapter.ViewHolder> {
    ArrayList<User> users;
    Activity context;
    public UtilizatorAdapter(ArrayList<User> myMovieData, Activity activity) {
        this.users = myMovieData;
        this.context = activity;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.user_admin_card_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
    @NonNull
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User account = users.get(position);
        holder.textviewmoney.setText(account.getNume());
        holder.textviewbankno.setText(account.getId());
        holder.photo.setImageBitmap(account.getFotografie());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder buyCryptoPopup=new AlertDialog.Builder(context);
                buyCryptoPopup.setTitle("Informati utilizator");
                LayoutInflater inflater = context.getLayoutInflater();
                View dialogView= inflater.inflate(R.layout.admin_screen_user_popup, null);
                buyCryptoPopup.setView(dialogView);
                TextView phone= dialogView.findViewById(R.id.text_view_phone_number);
                TextView adres= dialogView.findViewById(R.id.text_view_adres);
                TextView job= dialogView.findViewById(R.id.text_view_job);
                TextView bankcount= dialogView.findViewById(R.id.text_view_bank_account_counter);
                TextView creditcount= dialogView.findViewById(R.id.text_view_credit_card_counter);
                TextView interestRate= dialogView.findViewById(R.id.text_view_interest_rate);
                interestRate.setText("Rata: " + account.getJob().getInterestRate() +"\n" + "Suma maxima a creditulului: " + account.getJob().getMaxCreditAmount());
                phone.setText("Nr. de telefon: +" + account.getTelefon());
                adres.setText("Adresa: " + account.addressWrite());
                job.setText("Job-ul: " + account.getJob().getName());
                String auxStr = "Bank Accounts:\n";

                for (int i = 0; i < account.getConturiBancare().size(); i++) {
                    auxStr+= account.getConturiBancare().get(i).getNrCont() + ": " + account.getConturiBancare().get(i).getCash() + "$";
                    if (account.getConturiBancare().size()-1!=i){
                        auxStr+= "\n";
                    }
                }
                String auxCC = "Carduri de credit:\n";
                for (int i = 0; i < account.getCarduriDeCredit().size(); i++) {
                    auxCC+= account.getCarduriDeCredit().get(i).getCardCreditNr() + ": " + account.getCarduriDeCredit().get(i).getLimit() + "$";
                    if (account.getCarduriDeCredit().size()-1!=i){
                        auxCC+= "\n";
                    }
                }
                bankcount.setText(auxStr);
                creditcount.setText(auxCC);
                buyCryptoPopup.setNegativeButton("Iesire", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                buyCryptoPopup.create().show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return users.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textviewbankno;
        TextView textviewmoney;
        ImageView photo;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textviewbankno = itemView.findViewById(R.id.text_view_bank_account_no);
            textviewmoney = itemView.findViewById(R.id.text_view_bank_account_money);
            photo = itemView.findViewById(R.id.bank_account_ImageView);
        }
    }
}
