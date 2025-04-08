package com.example.myapplication.Activity;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.CartAdapter;
import com.example.myapplication.Domain.FoodDomain;
import com.example.myapplication.R;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewCart;
    private ImageView backBtn;
    private TextView totalFeeTxt, deliveryTxt, taxTxt, totalTxt, addressTxt;
    private ArrayList<FoodDomain> cartItems;
    private double tax = 0.02; // 2% tax
    private double deliveryFee = 150; // Fixed delivery fee

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        // Initialize views and data
        initViews();
        initCartItems();
        calculateCartTotal();
        setListeners();
        
        // Set up RecyclerView
        setupRecyclerView();
    }

    private void initViews() {
        recyclerViewCart = findViewById(R.id.view3);
        backBtn = findViewById(R.id.backBtn);
        totalFeeTxt = findViewById(R.id.totalFeeTxt);
        deliveryTxt = findViewById(R.id.deliveryTxt);
        taxTxt = findViewById(R.id.taxTxt);
        totalTxt = findViewById(R.id.totalTxt);
        addressTxt = findViewById(R.id.textView20); // Address TextView
        
        // Get the user address from intent
        String userAddress = getIntent().getStringExtra("USER_ADDRESS");
        if (userAddress != null && !userAddress.isEmpty()) {
            addressTxt.setText(userAddress);
        }
    }

    private void initCartItems() {
        cartItems = new ArrayList<>();
        
        // Check if we're receiving a new food item to add to cart
        if (getIntent().hasExtra("SELECTED_FOOD")) {
            FoodDomain newItem;
            
            // Handle API 33+ compatibility
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                newItem = getIntent().getSerializableExtra("SELECTED_FOOD", FoodDomain.class);
            } else {
                newItem = (FoodDomain) getIntent().getSerializableExtra("SELECTED_FOOD");
            }
            
            if (newItem != null) {
                // Check if item already exists in cart
                boolean itemExists = false;
                for (FoodDomain item : cartItems) {
                    if (item.getTitle().equals(newItem.getTitle())) {
                        item.setNumberinCart(item.getNumberinCart() + newItem.getNumberinCart());
                        itemExists = true;
                        break;
                    }
                }
                
                // If item doesn't exist in cart, add it
                if (!itemExists) {
                    cartItems.add(newItem);
                }
            }
        }
        
        // If cart is empty, add some sample items
        if (cartItems.isEmpty()) {
            cartItems.add(new FoodDomain("Chocolate Cake", "Delicious chocolate cake", "high_1", 15, 20, 120, 4));
            cartItems.get(0).setNumberinCart(1);
        }
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerViewCart.setLayoutManager(linearLayoutManager);
        
        // Create and set adapter
        adapter = new CartAdapter(cartItems, this, new CartAdapter.ChangeNumberItemsListener() {
            @Override
            public void changed() {
                calculateCartTotal();
            }
        });
        
        recyclerViewCart.setAdapter(adapter);
    }

    private void setListeners() {
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void calculateCartTotal() {
        double subTotal = 0;
        
        for (FoodDomain item : cartItems) {
            subTotal += (item.getPrice() * item.getNumberinCart());
        }
        
        double taxAmount = subTotal * tax;
        double total = subTotal + taxAmount + deliveryFee;
        
        // Format currency and set text
        totalFeeTxt.setText("Rs." + String.format("%.2f", subTotal));
        deliveryTxt.setText("Rs." + String.format("%.2f", deliveryFee));
        taxTxt.setText("Rs." + String.format("%.2f", taxAmount));
        totalTxt.setText("Rs." + String.format("%.2f", total));
    }
} 