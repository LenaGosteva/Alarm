package com.example.alarm;


import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.media.VolumeProvider;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;



public class Alarm extends AppCompatActivity {
    public static Ringtone ringtone;
    TextView textView;
    Calendar date;
    Intent intentVibrate;
    Uri notif;
    AudioManager audioManager;

    SharedPreferences prefs;
    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.P)
            @Override
            public void run() {
                textView = findViewById(R.id.text);
                date = Calendar.getInstance();
                textView.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);
                textView.setText(sdf.format(date.getTime()));

            }

        }, 0, 1, TimeUnit.SECONDS);
        prefs = getSharedPreferences("test", Context.MODE_PRIVATE);

        notif = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(this, notif);
        ringtone.setVolume(newA.progress);
        if (ringtone == null) {
            notif = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            ringtone = RingtoneManager.getRingtone(this, notif);

        }
        if (ringtone != null) {
            ringtone.play();
        }

        if(Settings.vibr){
            intentVibrate = new Intent(getApplicationContext(), VibrateService.class);
            startService(intentVibrate);
        }

        if(Settings.loudB){

        }

    }
    public void off(View view) {
        stopService(intentVibrate);
        if (ringtone != null && ringtone.isPlaying()) {
            ringtone.stop();
        }

        super.onDestroy();
    }
}