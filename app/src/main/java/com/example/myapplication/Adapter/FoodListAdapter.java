package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Domain.FoodDomain;
import com.example.myapplication.R;

import java.util.ArrayList;

public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.ViewHolder> {
ArrayList<FoodDomain>    items;
Context context;

    public FoodListAdapter(ArrayList<FoodDomain> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View inflate= LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_food_list,parent,false);
            context=parent.getContext();
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
holder.titleTxt.setText(items.get(position).getTitle());
holder.priceTxt.setText(" RS"+items.get(position).getPrice());
holder.ScoreTxt.setText(""+items.get(position).getScore());
int drawableResourcesId=holder.itemView.getResources().getIdentifier(items.get(position).getPicUrl(),"drawable",holder.itemView.getContext().getPackageName());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
TextView titleTxt,priceTxt,ScoreTxt;
ImageView pic;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTxt=itemView.findViewById(R.id.titleTxt);
            priceTxt=itemView.findViewById(R.id.priceTxt);
            ScoreTxt=itemView.findViewById(R.id.scoreTxt);
            pic=itemView.findViewById(R.id.pic);


        }
    }
}
