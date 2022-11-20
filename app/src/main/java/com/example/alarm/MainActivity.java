package com.example.alarm;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alarm.databinding.ActivityMainBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    TextView text;
    public static Calendar date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        com.example.alarm.databinding.ActivityMainBinding binding;
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_main);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Click click = new Click();
        binding.settingsMain.setOnClickListener(click);
        binding.plusMain.setOnClickListener(click);
        text = findViewById(R.id.textt);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {

            date = Calendar.getInstance();
            text.setText(sdf.format(date.getTime()));
        }, 0, 1, TimeUnit.MILLISECONDS);

    }


    public class Click implements View.OnClickListener{

        @SuppressLint({"ResourceAsColor", "UseCompatLoadingForDrawables", "NonConstantResourceId", "DefaultLocale"})
        @Override
        public void onClick(View view){
            switch (view.getId()){
                case R.id.settingsMain:
                    Intent intentSettings = new Intent(MainActivity.this, Settings.class);
                    startActivity(intentSettings);
                    break;

                case R.id.plusMain:
                    CreateNewAlarm newAlarm = new CreateNewAlarm();
                    Intent intentNew = new Intent(MainActivity.this, NewOrChangedAlarm.class);
                    intentNew.putExtra("NewAlarm", newAlarm);
                    startActivity(intentNew);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + view.getId());
            }
        }

    }

}