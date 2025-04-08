package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.FoodListAdapter;
import com.example.myapplication.Domain.FoodDomain;
import com.example.myapplication.R;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
private RecyclerView.Adapter adapterFoodList;
private RecyclerView recyclerViewFood;
private TextView addressTextView;
private ImageView homeBtn, cartBtn, supportBtn;
private ArrayList<FoodDomain> foodItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        
        // Initialize views
        initViews();
        
//        // Get the user address from the intent
//        String userAddress = getIntent().getStringExtra("USER_ADDRESS");
//
//        // Set the user address in the TextView if it's not null
//        if (userAddress != null && !userAddress.isEmpty()) {
//            addressTextView.setText(userAddress);
//        }
//
//        // Initialize RecyclerView with food items
//        initRecyclerview();
//
//        // Set up click listeners for navigation buttons
//        setupNavigation();
//    }

    private void initViews() {
        recyclerViewFood = findViewById(R.id.recyclerViewFood);
        addressTextView = findViewById(R.id.textView5);
        homeBtn = findViewById(R.id.imageViewhome);
        cartBtn = findViewById(R.id.imageViewcart);
        supportBtn = findViewById(R.id.imageViewsupport);
    }

    private void setupNavigation() {
        // Using lambda instead of anonymous inner class
        homeBtn.setOnClickListener(v -> {
            // Already on home screen, do nothing or refresh
        });
        
        cartBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, CartActivity.class);
            intent.putExtra("USER_ADDRESS", addressTextView.getText().toString());
            startActivity(intent);
        });
        
        supportBtn.setOnClickListener(v -> {
            // For demo purposes, navigate to the detail page of the first item
            if (!foodItems.isEmpty()) {
                Intent intent = new Intent(MainActivity.this, DetailActivity.class);
                intent.putExtra("SELECTED_FOOD", foodItems.get(0));
                startActivity(intent);
            }
        });
    }

    private void initRecyclerview() {
        foodItems = new ArrayList<>();
        foodItems.add(new FoodDomain("Chocolate Cake", "Rich chocolate cake with icing", "high_1", 15, 20, 120, 4));
        foodItems.add(new FoodDomain("Strawberry Cake", "Fresh strawberry cake", "high_2", 10, 25, 200, 5));
        foodItems.add(new FoodDomain("Vanilla Cake", "Classic vanilla cake", "high_1", 13, 30, 100, 4.5));

        recyclerViewFood.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        FoodListAdapter adapter = new FoodListAdapter(foodItems);
        adapterFoodList = adapter;
        recyclerViewFood.setAdapter(adapterFoodList);
        
        // Set click listener for food items in the RecyclerView
        adapter.setOnItemClickListener(position -> {
            // When a food item is clicked, open DetailActivity with the selected food
            FoodDomain selectedFood = foodItems.get(position);
            Intent intent = new Intent(MainActivity.this, DetailActivity.class);
            intent.putExtra("SELECTED_FOOD", selectedFood);
            intent.putExtra("USER_ADDRESS", addressTextView.getText().toString());
            startActivity(intent);
        });
    }
}