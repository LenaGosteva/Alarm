package com.example.alarm;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class Alarm extends AppCompatActivity {

    public static Ringtone ringtone;
    TextView textView;
    Calendar date;
    Intent intentVibrate;
    Uri notif;
    Button off, out;
    protected static float d;
    SharedPreferences prefs;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        textView = findViewById(R.id.text);
        off = findViewById(R.id.offAlarm);
        out = findViewById(R.id.outAlarm);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                date = Calendar.getInstance();
                textView.setText(sdf.format(date.getTime()));

            }

        }, 0, 1, TimeUnit.SECONDS);
        prefs = getSharedPreferences("test", Context.MODE_PRIVATE);

        Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(this, notificationUri);
        if (ringtone == null) {
            notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            ringtone = RingtoneManager.getRingtone(this, notificationUri);
            ringtone.setVolume(Settings.progress);
        }
        if (ringtone != null) {
            ringtone.play();
        }

        if (Settings.loudB) {

            Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {

                @SuppressLint("NewApi")
                @Override
                public void run() {
                    d = ringtone.getVolume();
                    while (d <= Settings.maxVolume) {
                        d += 1.0;
                        ringtone.setVolume(d);
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }, 0, 3, TimeUnit.SECONDS);
        }

        off.setOnClickListener(off ->{
            if (ringtone != null && ringtone.isPlaying()) {
                ringtone.stop();
            }
            Intent intent = new Intent(Alarm.this, MainActivity.class);
            startActivity(intent);
        });

        out.setOnClickListener(out -> {

            ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
            service.schedule((Runnable) () -> {
                if (ringtone != null && ringtone.isPlaying()) {
                    ringtone.stop();
                }
            }, Settings.minutes.getProgress(), TimeUnit.MINUTES);
            if (Settings.minut) {
                Settings.minut = false;
            }
        });
    }

}