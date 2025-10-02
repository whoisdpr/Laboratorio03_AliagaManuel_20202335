package com.example.iot_lab4_20202335;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnOpen = findViewById(R.id.btnOpenApp);
        btnOpen.setOnClickListener(v -> {
            Intent i = new Intent(MainActivity.this, com.example.iot_lab4_20202335.ui.AppActivity.class);
            startActivity(i);
        });
    }
}