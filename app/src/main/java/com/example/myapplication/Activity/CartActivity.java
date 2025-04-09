package com.example.myapplication.Activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.CartAdapter;
import com.example.myapplication.Domain.FoodDomain;
import com.example.myapplication.R;

import java.util.ArrayList;
import java.util.UUID;

public class CartActivity extends AppCompatActivity {
    private RecyclerView.Adapter adapter;
    private RecyclerView recyclerViewCart;
    private ImageView backBtn;
    private TextView totalFeeTxt, deliveryTxt, taxTxt, totalTxt, addressTxt;
    private ArrayList<FoodDomain> cartItems;
    private double tax = 0.02; // 2% tax
    private double deliveryFee = 150; // Fixed delivery fee
    private Button payButton;
    private double totalAmount = 0.0; // Initialize this with your cart total

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

        // Initialize the pay button
        payButton = findViewById(R.id.button2);

        // Calculate total amount from cart items
        calculateTotalAmount();

        // Set up payment button click listener
        payButton.setOnClickListener(v -> initiatePayHerePayment());
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

    private void calculateTotalAmount() {
        // Get your cart total here
        // Example: totalAmount = cartItems.stream().mapToDouble(item -> item.getPrice() * item.getQuantity()).sum();
        // Add tax and delivery fee
        double tax = totalAmount * 0.02; // 2% tax
        double deliveryFee = 150; // Fixed delivery fee
        totalAmount = totalAmount + tax + deliveryFee;
    }

    private void initiatePayHerePayment() {
        // Generate a unique order ID
        String orderId = "ORDER_" + UUID.randomUUID().toString().substring(0, 8);
        
        // Create PayHere payment URL with parameters
        Uri.Builder builder = Uri.parse("https://www.payhere.lk/pay/checkout").buildUpon();
        builder.appendQueryParameter("merchant_id", "YOUR_MERCHANT_ID")
               .appendQueryParameter("return_url", "https://yourapp.com/return")
               .appendQueryParameter("cancel_url", "https://yourapp.com/cancel")
               .appendQueryParameter("notify_url", "https://yourapp.com/notify")
               .appendQueryParameter("order_id", orderId)
               .appendQueryParameter("items", "Food Order")
               .appendQueryParameter("currency", "LKR")
               .appendQueryParameter("amount", String.valueOf(totalAmount))
               .appendQueryParameter("first_name", "Customer Name")
               .appendQueryParameter("email", "customer@email.com")
               .appendQueryParameter("phone", "0771234567")
               .appendQueryParameter("address", "Delivery Address")
               .appendQueryParameter("city", "Colombo")
               .appendQueryParameter("country", "Sri Lanka");

        String paymentUrl = builder.build().toString();

        try {
            // Open PayHere payment page in browser
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(paymentUrl));
            startActivity(intent);
            
            // Save order details locally
            saveOrderDetails(orderId);
            
        } catch (Exception e) {
            Toast.makeText(this, "Error opening payment page", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void saveOrderDetails(String orderId) {
        // TODO: Save order details to local storage or your backend
        // This should include:
        // - Order ID
        // - Cart items
        // - Total amount
        // - Customer details
        // - Order status (pending payment)
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Check for payment status when returning from payment page
        // You might want to implement a way to check if payment was successful
        // This could be through your return_url handling or backend notification
    }
} 