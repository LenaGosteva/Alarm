package com.example.alarm;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class Alarm extends AppCompatActivity {

    public static Ringtone ringtone;
    TextView textView;
    Calendar date;
    Intent intentVibrate;
    Uri notif;
    Button off, out;
    Vibrator vibrator;
    protected static float d;
    SharedPreferences prefs;
    long[] pattern = {0, 2000, 1000, 2000, 1000, 2000};
    AudioManager audioManager;
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

        Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        ringtone = RingtoneManager.getRingtone(this, notificationUri);
        if (ringtone == null) {
            notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
            ringtone = RingtoneManager.getRingtone(this, notificationUri);
            audioManager.adjustVolume(AudioManager.RINGER_MODE_NORMAL, Settings.progress);
        }
        if (ringtone != null) {
                ringtone.play();
        }

        if (Settings.isValumeCanVibr){
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(pattern, 2);
        }

        if (Settings.isValumeIncreasingGradually) {
            audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
                @Override
                public void run() {
                    audioManager.adjustVolume(AudioManager.ADJUST_LOWER, AudioManager.FLAG_PLAY_SOUND);
                }

            }, 0, 1, TimeUnit.SECONDS);
        }

        off.setOnClickListener(off ->{
            if (Settings.isValumeCanVibr) vibrator.cancel();
            if (ringtone != null && ringtone.isPlaying()) {
                ringtone.stop();
            }
            Intent intent = new Intent(Alarm.this, MainActivity.class);
            startActivity(intent);
        });

        out.setOnClickListener(out -> {


            if (Settings.minut) {

                if (ringtone != null && ringtone.isPlaying()) {
                    ringtone.stop();
                }
                try {
                    Thread.sleep((long) Settings.progressM *1000*60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Settings.minut = false;
                ringtone.play();
            };

        });
    }
    }