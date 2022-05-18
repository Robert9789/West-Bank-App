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

import com.bank.westbank.R;
import com.bank.westbank.UserInfo.Istoric;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class IstoricAdapter extends RecyclerView.Adapter<IstoricAdapter.PostHolder> {
    private Context context;
    private ArrayList<Istoric> istoricArrayList;
    private Activity activity;

    public class PostHolder extends RecyclerView.ViewHolder {
        private  Context context;
        private TextView TextViewData, textViewProces;
        private ImageView imageViewHistory;
        public PostHolder(@NonNull @NotNull View itemView, Context context) {
            super(itemView);
            this.context=context;
            TextViewData = itemView.findViewById(R.id.text_view_history_date);
            textViewProces = itemView.findViewById(R.id.text_view_history_process);
            imageViewHistory = itemView.findViewById(R.id.image_view_history);
        }
    }
    public IstoricAdapter(ArrayList<Istoric> history, Activity activity, Context context){
        this.istoricArrayList =history;
        this.activity = activity;
        this.context = context;
    }
    @NonNull
    @NotNull
    @Override
    public IstoricAdapter.PostHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater=LayoutInflater.from(parent.getContext());
        View view =layoutInflater.inflate(R.layout.history_card_view,parent,false);
        return new IstoricAdapter.PostHolder(view,context);
    }
    @Override
    public void onBindViewHolder(@NonNull @NotNull PostHolder holder, int position) {
        holder.textViewProces.setText(istoricArrayList.get(position).getProcesare());
        holder.TextViewData.setText((CharSequence) istoricArrayList.get(position).getDateString());
    }
    @Override
    public int getItemCount() {
        return istoricArrayList.size();
    }
}