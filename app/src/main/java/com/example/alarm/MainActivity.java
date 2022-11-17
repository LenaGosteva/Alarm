package com.example.alarm;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

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
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                text = findViewById(R.id.textt);
                date = Calendar.getInstance();
                text.setText(sdf.format(date.getTime()));
            }
        }, 0, 1, TimeUnit.MILLISECONDS);

    };
    public void alarm(View view) {
        CreateNewAlarm newAlarm = new CreateNewAlarm(Settings.progress, Settings.progressM, Settings.vibr, Settings.loudB);
        Intent intent = new Intent(MainActivity.this, newA.class);

        startActivity(intent);
    }

    public void settings(View view) {
        CreateNewAlarm newAlarm = new CreateNewAlarm(Settings.progress, Settings.progressM, Settings.vibr, Settings.loudB);
        Intent intent = new Intent(MainActivity.this, Settings.class);
        intent.putExtra("NewAlarm", newAlarm);
        startActivity(intent);
    }


}