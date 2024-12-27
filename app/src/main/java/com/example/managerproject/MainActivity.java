package com.example.managerproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // زر لفتح adminDashboard
        Button openAdminDashboardButton = findViewById(R.id.open_admin_dashboard_button);
        openAdminDashboardButton.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, adminDashboard.class);
            startActivity(intent);
        });
    }
}
