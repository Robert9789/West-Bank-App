package com.bank.westbank.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahmadrosid.svgloader.SvgLoader;
import com.bank.westbank.MainScreen.FinanceScreen.CryptoModel;
import com.bank.westbank.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CryptoAdapter extends RecyclerView.Adapter<CryptoAdapter.PostHolder> {
    private Context context;
    private ArrayList<CryptoModel> models;
    private Activity activity;
    class PostHolder extends RecyclerView.ViewHolder{
        private  Context context;
        private TextView TextNumeCrypto, cryptoSimbolText, TextSumaCrypto, TextPretCrypto;
        private ImageView cryptoImageView;

        public PostHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context=context;
            TextNumeCrypto =itemView.findViewById(R.id.crypto_symbol_text);
            TextSumaCrypto =itemView.findViewById(R.id.crypto_name_text);
            cryptoImageView=itemView.findViewById(R.id.crypto_imageview);
            TextPretCrypto =itemView.findViewById(R.id.crypto_price_text_view);
        }
    }
    public CryptoAdapter(ArrayList<CryptoModel> models, Activity activity, Context context) {
        this.models = models;
        this.activity=activity;
        this.context=context;
    }
    @NonNull
    @Override
    public CryptoAdapter.PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view =layoutInflater.inflate(R.layout.custom_view,parent,false);
        return new CryptoAdapter.PostHolder(view,context);
    }
    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        holder.TextNumeCrypto.setText(models.get(position).getCurrencyName());
        holder.TextSumaCrypto.setText(models.get(position).getAmount());
        holder.TextPretCrypto.setText(models.get(position).getPrice()+" $");
        //pentru salvarea fișierului imagine
        if(models.get(position).getLogoUrl().substring(models.get(position).getLogoUrl().length()-3).equalsIgnoreCase("svg")){
            SvgLoader.pluck()
                    .with(activity)
                    .setPlaceHolder(R.mipmap.ic_launcher, R.mipmap.ic_launcher)
                    .load(models.get(position).getLogoUrl(),holder.cryptoImageView);
        }else{//pentru toate fișierele imagine fără .svg
            Picasso.get().load(models.get(position).getLogoUrl()).into(holder.cryptoImageView);
        }
    }
    @Override
    public int getItemCount() {
        return models.size();
    }
}