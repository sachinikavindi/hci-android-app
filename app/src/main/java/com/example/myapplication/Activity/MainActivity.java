package com.example.myapplication.Activity;

import android.os.Bundle;

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
initRecyclerview();

    }

    private void initRecyclerview() {
        ArrayList<FoodDomain> Items=new ArrayList<>();
        Items.add(new FoodDomain("Chocolate Cake","" ,"high_1",15,20,120,4));
        Items.add(new FoodDomain("Chocolate Cake","" ,"high_2",10,25,200,5));
        Items.add(new FoodDomain("Chocolate Cake","" ,"high_1",13,30,100,4.5));

        recyclerViewFood=findViewById(R.id.recyclerViewFood);
        recyclerViewFood.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));

        adapterFoodList=new FoodListAdapter(Items);
        recyclerViewFood.setAdapter(adapterFoodList);

    }
}