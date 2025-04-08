package com.example.myapplication.Activity;

import android.app.Activity;
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
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity implements PaymentResultListener {
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

        // Initialize Razorpay
        Checkout.preload(getApplicationContext());

        // Initialize the pay button
        payButton = findViewById(R.id.button2);

        // Calculate total amount from cart items
        calculateTotalAmount();

        // Set up payment button click listener
        payButton.setOnClickListener(v -> startPayment());
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

    private void startPayment() {
        Checkout checkout = new Checkout();
        checkout.setKeyID("YOUR_RAZORPAY_KEY_ID"); // Replace with your Razorpay key

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Food App");
            options.put("description", "Order Payment");
            options.put("currency", "INR");
            options.put("amount", (int)(totalAmount * 100)); // Amount in smallest currency unit (paise)
            
            // Prefill customer details
            JSONObject prefill = new JSONObject();
            prefill.put("email", "customer@example.com");
            prefill.put("contact", "9876543210");
            options.put("prefill", prefill);
            
            checkout.open(this, options);

        } catch (Exception e) {
            Toast.makeText(this, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            // Payment successful
            Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
            
            // TODO: Update order status in your database
            // TODO: Clear cart
            // TODO: Navigate to order confirmation screen
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment Failed: " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
} 