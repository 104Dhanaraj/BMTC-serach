package com.example.test3;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button adminButton, userButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        adminButton = findViewById(R.id.adminButton);
        userButton = findViewById(R.id.userButton);

        adminButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AdminActivity.class)));
        userButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, UserActivity.class)));
    }
}
