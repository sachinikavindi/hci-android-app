package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.example.myapplication.R;

public class SplashActivity extends AppCompatActivity {
    private static final long SPLASH_DELAY = 2000; // 2 seconds

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_up);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                // Start sing_in activity
                Intent intent = new Intent(SplashActivity.this, sing_in.class);
                startActivity(intent);
                finish(); // Close splash activity
            }
        }, SPLASH_DELAY);
    }
} 