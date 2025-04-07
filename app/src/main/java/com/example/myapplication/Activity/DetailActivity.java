package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.myapplication.Domain.FoodDomain;
import com.example.myapplication.R;

public class DetailActivity extends AppCompatActivity {
    private TextView titleTxt, priceTxt, timeTxt, scoreTxt, energyTxt;
    private ImageView foodPic, backButton;
    private Button addToCartBtn;
    private FoodDomain selectedFood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_detail);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        initViews();
        
        // Get data from intent
        getIntentData();
        
        // Set up back button click listener
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Go back to previous activity
            }
        });
        
        // Set up add to cart button click listener
        addToCartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedFood != null) {
                    selectedFood.setNumberinCart(1); // Set default quantity to 1
                    Toast.makeText(DetailActivity.this, "Added to Cart", Toast.LENGTH_SHORT).show();
                    
                    // Navigate to CartActivity
                    Intent intent = new Intent(DetailActivity.this, CartActivity.class);
                    intent.putExtra("SELECTED_FOOD", selectedFood);
                    intent.putExtra("USER_ADDRESS", getIntent().getStringExtra("USER_ADDRESS"));
                    startActivity(intent);
                }
            }
        });
    }

    private void initViews() {
        titleTxt = findViewById(R.id.titleTxt);
        priceTxt = findViewById(R.id.priceTxt);
        timeTxt = findViewById(R.id.textView21); // Time TextView ID
        foodPic = findViewById(R.id.foodPic);
        backButton = findViewById(R.id.imageView15); // Back arrow
        addToCartBtn = findViewById(R.id.button4);
    }
    
    private void getIntentData() {
        if (getIntent().hasExtra("SELECTED_FOOD")) {
            // Handle API 33+ compatibility for getSerializableExtra
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                selectedFood = getIntent().getSerializableExtra("SELECTED_FOOD", FoodDomain.class);
            } else {
                selectedFood = (FoodDomain) getIntent().getSerializableExtra("SELECTED_FOOD");
            }
            
            if (selectedFood != null) {
                // Set food details in views
                titleTxt.setText(selectedFood.getTitle());
                priceTxt.setText("Rs." + selectedFood.getPrice());
                timeTxt.setText(selectedFood.getTime() + " min");
                
                // Load image with Glide
                int drawableResourceId = getResources().getIdentifier(
                        selectedFood.getPicUrl(), "drawable", getPackageName());
                
                Glide.with(this)
                        .load(drawableResourceId)
                        .into(foodPic);
            }
        } else {
            // If no data, show default values
            titleTxt.setText("Food Item");
            priceTxt.setText("Rs.0");
            timeTxt.setText("0 min");
        }
    }
}