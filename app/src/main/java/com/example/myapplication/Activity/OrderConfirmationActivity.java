package com.example.myapplication.Activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class OrderConfirmationActivity extends AppCompatActivity {
    private TextView orderIdTxt;
    private Button backToHomeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        // Initialize views
        orderIdTxt = findViewById(R.id.orderIdTxt);
        backToHomeBtn = findViewById(R.id.backToHomeBtn);

        // Get payment ID from intent
        String paymentId = getIntent().getStringExtra("PAYMENT_ID");
        orderIdTxt.setText("Order ID: " + paymentId);

        // Set up back to home button
        backToHomeBtn.setOnClickListener(v -> {
            finish(); // Close this activity and return to MainActivity
        });
    }
} 