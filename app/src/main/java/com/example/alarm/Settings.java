package com.example.alarm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Settings extends AppCompatActivity {
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        save = findViewById(R.id.save);
        save.setOnClickListener(save -> {
                SharedPreferences.Editor ed = getSharedPreferences("test", Context.MODE_PRIVATE).edit();

                ed.apply();
                Toast.makeText(this, "Настройки по умолчанию сохранены", Toast.LENGTH_SHORT).show();

        });

    }

    public void back(View view) {
        Intent intent = new Intent(Settings.this, MainActivity.class);
        startActivity(intent);}

}