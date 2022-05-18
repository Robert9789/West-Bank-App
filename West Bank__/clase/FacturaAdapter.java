package com.bank.westbank.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.bank.westbank.Factura.Facturi;
import com.bank.westbank.R;
import java.util.List;

public class FacturaAdapter extends  RecyclerView.Adapter<FacturaAdapter.CardViewObjectHolder> {

    private Context mContext;
    private List<Facturi> list;

    public FacturaAdapter(Context mContext, List<Facturi> list) {
        this.mContext = mContext;
        this.list = list;
    }
    public class CardViewObjectHolder extends RecyclerView.ViewHolder{
        public TextView textViewFacturaTip;
        public TextView textViewFacturaSuma;
        public TextView textViewFacturaData;
        public CardView cardViewFactura;
        public CardViewObjectHolder(View view){
            super(view);
            textViewFacturaTip = view.findViewById(R.id.textView_bill_type);
            textViewFacturaSuma = view.findViewById(R.id.textView_bill_amount);
            textViewFacturaData = view.findViewById(R.id.textView_bill_date);
            cardViewFactura = view.findViewById(R.id.cardView_bill);
        }
    }
    @NonNull
    @Override
    public CardViewObjectHolder onCreateViewHolder(@NonNull ViewGroup parinte, int viewTip) {
        View itemView = LayoutInflater.from(parinte.getContext())
                .inflate(R.layout.bill_cardview,parinte,false);
        return new CardViewObjectHolder(itemView);
    }
    @Override
    public void onBindViewHolder(@NonNull CardViewObjectHolder holder, int position) {
        Facturi facturi = list.get(position);
        holder.textViewFacturaTip.setText(facturi.getType());
        holder.textViewFacturaSuma.setText(facturi.getAmount()+" TL");
        String dateStr = facturi.getDate().getDay() +"."+ facturi.getDate().getMonth() +
                "."+ facturi.getDate().getYear();
        holder.textViewFacturaData.setText(dateStr);
    }
    @Override
    public int getItemCount()
    {
        return list.size();
    }
}
