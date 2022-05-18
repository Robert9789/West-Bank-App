package com.bank.westbank.Adapters;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bank.westbank.R;
import com.bank.westbank.UserInfo.ContBancar;
import java.util.ArrayList;

public class ContBancarAdapter extends RecyclerView.Adapter<ContBancarAdapter.ViewHolder> {


    ArrayList<ContBancar> ContrileBancarePersonale;
    Activity stareaActuala;
    public ContBancarAdapter(ArrayList<ContBancar> DatelePersonale, Activity activity) {
        this.ContrileBancarePersonale = DatelePersonale;
        this.stareaActuala = activity;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewTip) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.bank_account_cardview,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }
    @NonNull

    @Override
    public void onBindViewHolder(@NonNull ViewHolder titular, int position) {
        final ContBancar account = ContrileBancarePersonale.get(position);
        titular.textviewmoney.setText("$ " +String.valueOf(account.getCash()));
        titular.textviewbankno.setText(account.getNrCont());
    }
    @Override
    public int getItemCount() {
        return ContrileBancarePersonale.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textviewbankno;
        TextView textviewmoney;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            textviewbankno = itemView.findViewById(R.id.text_view_bank_account_no);
            textviewmoney = itemView.findViewById(R.id.text_view_bank_account_money);
        }
    }
}
