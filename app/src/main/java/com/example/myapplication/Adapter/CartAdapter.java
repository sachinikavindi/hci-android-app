package com.example.myapplication.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.myapplication.Domain.FoodDomain;
import com.example.myapplication.R;

import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private ArrayList<FoodDomain> cartItems;
    private Context context;
    private ChangeNumberItemsListener changeNumberItemsListener;

    public CartAdapter(ArrayList<FoodDomain> cartItems, Context context, ChangeNumberItemsListener changeNumberItemsListener) {
        this.cartItems = cartItems;
        this.context = context;
        this.changeNumberItemsListener = changeNumberItemsListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflate = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.viewholder_cart, parent, false);
        return new ViewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FoodDomain item = cartItems.get(position);
        
        holder.title.setText(item.getTitle());
        holder.totalPrice.setText("Rs." + (item.getNumberinCart() * item.getPrice()));
        holder.quantity.setText(String.valueOf(item.getNumberinCart()));
        
        int drawableResourceId = holder.itemView.getContext().getResources()
                .getIdentifier(item.getPicUrl(), "drawable", 
                        holder.itemView.getContext().getPackageName());
        
        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .into(holder.pic);
        
        // Set click listeners for plus and minus buttons
        holder.plusBtn.setOnClickListener(v -> {
            int newQuantity = item.getNumberinCart() + 1;
            item.setNumberinCart(newQuantity);
            holder.quantity.setText(String.valueOf(newQuantity));
            holder.totalPrice.setText("Rs." + (newQuantity * item.getPrice()));
            changeNumberItemsListener.changed();
        });
        
        holder.minusBtn.setOnClickListener(v -> {
            if (item.getNumberinCart() > 1) {
                int newQuantity = item.getNumberinCart() - 1;
                item.setNumberinCart(newQuantity);
                holder.quantity.setText(String.valueOf(newQuantity));
                holder.totalPrice.setText("Rs." + (newQuantity * item.getPrice()));
                changeNumberItemsListener.changed();
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, totalPrice, quantity;
        ImageView pic, plusBtn, minusBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.titleTxt);
            totalPrice = itemView.findViewById(R.id.feeTxt);
            quantity = itemView.findViewById(R.id.numberItemTxt);
            pic = itemView.findViewById(R.id.picCard);
            plusBtn = itemView.findViewById(R.id.plusCardBtn);
            minusBtn = itemView.findViewById(R.id.minusCardBtn);
        }
    }

    public interface ChangeNumberItemsListener {
        void changed();
    }
} 