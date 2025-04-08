package com.example.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.R;

public class sing_in extends AppCompatActivity {
    private EditText editTextName;
    private EditText editTextEmail;
    private EditText editTextLocation;
    private EditText editTextPassword;
    private Button signInButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sing_in);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize UI elements
        initializeViews();
        
        // Set up button click listener
        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
        });
    }

    private void initializeViews() {
        editTextName = findViewById(R.id.editTextTextname);
        editTextEmail = findViewById(R.id.editTextTextEmail);
        editTextLocation = findViewById(R.id.editTextTextlocation);
        editTextPassword = findViewById(R.id.editTextTextpassword);
        signInButton = findViewById(R.id.button);
        
        // Change button text from "Button" to "Sign In"
        signInButton.setText("Sign In");
    }

    private void signIn() {
        // Get values from input fields
        String name = editTextName.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String location = editTextLocation.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        // Validate input fields
        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Name is required");
            return;
        }

        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Email is required");
            return;
        }

        if (!isValidEmail(email)) {
            editTextEmail.setError("Please enter a valid email");
            return;
        }

        if (TextUtils.isEmpty(location)) {
            editTextLocation.setError("Location is required");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Password is required");
            return;
        }

        if (password.length() < 6) {
            editTextPassword.setError("Password must be at least 6 characters");
            return;
        }

        // In a real app, you would save user data to a database or SharedPreferences
        // For this example, we'll just show a success message and navigate to MainActivity
        Toast.makeText(this, "Sign in successful!", Toast.LENGTH_SHORT).show();
        
        // Navigate to MainActivity and pass the address
        Intent intent = new Intent(sing_in.this, MainActivity.class);
        intent.putExtra("USER_ADDRESS", location);
        startActivity(intent);
        finish(); // Close the sign-in activity so user can't go back with back button
    }

    // Simple email validation
    private boolean isValidEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
}