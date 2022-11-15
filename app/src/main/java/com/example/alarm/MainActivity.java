package com.example.alarm;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class MainActivity extends AppCompatActivity {
    TextView text;
    Calendar date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                text = findViewById(R.id.textt);
                date = Calendar.getInstance();
                text.setText(String.valueOf(date.getTime().getHours()+":"+date.getTime().getMinutes()));
            }
        }, 0, 1, TimeUnit.MILLISECONDS);

    };
    public void alarm(View view) {
        Intent intent = new Intent(MainActivity.this, newA.class);
        startActivity(intent);
    }

    public void settings(View view) {
        Intent intent = new Intent(MainActivity.this, Settings.class);
        startActivity(intent);
    }


}