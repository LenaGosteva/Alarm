package com.example.alarm;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alarm.databinding.ActivitySettingsBinding;

public class Settings extends AppCompatActivity {
    ActivitySettingsBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.save.setOnClickListener(save -> {
                SharedPreferences.Editor ed = getSharedPreferences("test", Context.MODE_PRIVATE).edit();

                ed.apply();
                Toast.makeText(this, "Настройки по умолчанию сохранены", Toast.LENGTH_SHORT).show();

        });

    }

    public void back(View view) {finish();}

}