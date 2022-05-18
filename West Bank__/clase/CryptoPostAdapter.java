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
import com.bank.westbank.MainScreen.FinanceScreen.ItemClickListener;
import com.bank.westbank.R;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

public class CryptoPostAdapter  extends RecyclerView.Adapter<CryptoPostAdapter.PostHolder> {
    private Context context;
    private ArrayList<CryptoModel> models;
    private Activity activity;
    private ItemClickListener itemClickListener;

    class PostHolder extends RecyclerView.ViewHolder{
        private  Context context;
        private TextView TextNumeCrypto, TextSimbolCrypto, TextPretCrypto;
        private ImageView CryptoImageView;
        public PostHolder(@NonNull View itemView, Context context) {
            super(itemView);
            this.context=context;
            TextNumeCrypto =itemView.findViewById(R.id.crypto_name_text);
            TextSimbolCrypto =itemView.findViewById(R.id.crypto_symbol_text);
            TextPretCrypto =itemView.findViewById(R.id.crypto_price_text_view);
            CryptoImageView =itemView.findViewById(R.id.crypto_imageview);
        }
    }
    public CryptoPostAdapter(ArrayList<CryptoModel> models, Activity activity, Context context, ItemClickListener itemClickListener) {
        this.models = models;
        this.activity=activity;
        this.context=context;
        this.itemClickListener=itemClickListener;
    }
    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view =layoutInflater.inflate(R.layout.custom_view,parent,false);
        return new PostHolder(view,context);
    }
    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        holder.TextNumeCrypto.setText(models.get(position).getCurrencyName());
        holder.TextSimbolCrypto.setText(models.get(position).getCurrencySymbol());
        holder.TextPretCrypto.setText(models.get(position).getPrice());

        //for .svg image file
        if(models.get(position).getLogoUrl().substring(models.get(position).getLogoUrl().length()-3).equalsIgnoreCase("svg")){
            SvgLoader.pluck()
                    .with(activity)
                    .setPlaceHolder(R.mipmap.ic_launcher, R.mipmap.ic_launcher)
                    .load(models.get(position).getLogoUrl(),holder.CryptoImageView);
        }else{//for all image file without .svg
            Picasso.get().load(models.get(position).getLogoUrl()).into(holder.CryptoImageView);
        }
        models.get(position).setImage(holder.CryptoImageView);
        final CryptoModel cryptoModel = models.get(position);
        // set click listener
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onItemClicked(holder, cryptoModel, position);
            }
        });
    }
    @Override
    public int getItemCount() {
        return models.size();
    }
}
