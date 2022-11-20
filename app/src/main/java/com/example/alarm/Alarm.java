package com.example.alarm;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
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

    MediaPlayer ringtone;
    TextView textView;
    Calendar date;
    Button off, out;
    Vibrator vibrator;
    SharedPreferences prefs;
    long[] pattern = {0, 1000, 1000, 1000, 1000, 1000};
    AudioManager audioManager;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        textView = findViewById(R.id.text);
        off = findViewById(R.id.offAlarm);
        out = findViewById(R.id.outAlarm);
        Intent intent = new Intent();
        CreateNewAlarm PlayingAlarm = (CreateNewAlarm) intent.getSerializableExtra("CreatedNew") ;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                date = Calendar.getInstance();
                textView.setText(sdf.format(date.getTime()));
            }

        }, 0, 1, TimeUnit.SECONDS);
        prefs = getSharedPreferences("test", Context.MODE_PRIVATE);
        ringtone = MediaPlayer.create(Alarm.this, R.raw.music);
        if (ringtone == null) {
            audioManager.adjustVolume(AudioManager.RINGER_MODE_NORMAL, PlayingAlarm.vol);
        }
        if (ringtone != null) {
                ringtone.start();
        }

        if (PlayingAlarm.vib){
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(pattern, 2);
        }

        if (PlayingAlarm.more) {
            audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            audioManager.adjustVolume(AudioManager.RINGER_MODE_NORMAL, PlayingAlarm.vol);

            Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
                audioManager.adjustVolume(AudioManager.ADJUST_RAISE ,AudioManager.FLAG_PLAY_SOUND);

                }, 0, 5, TimeUnit.SECONDS);
        }

        off.setOnClickListener(off ->{
            if (PlayingAlarm.vib) vibrator.cancel();
            if (ringtone != null && ringtone.isPlaying()) {
                ringtone.pause();
            }
            Intent intent1 = new Intent(Alarm.this, MainActivity.class);
            startActivity(intent1);
        });

        out.setOnClickListener(out -> {
            if (PlayingAlarm.minute !=0) {
                if (PlayingAlarm.vib) vibrator.cancel();
                if (ringtone != null && ringtone.isPlaying()) {
                    ringtone.pause();
                }
                try {
                    Thread.sleep((long) PlayingAlarm.minute*1000*60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                textView.setText(sdf.format(date.getTime()));
                ringtone.start();
                if (PlayingAlarm.vib) vibrator.vibrate(pattern, 2);
            }

        });
    }
    }