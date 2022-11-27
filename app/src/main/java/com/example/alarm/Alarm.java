package com.example.alarm;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
    TextView textView, message;
    Calendar date;
    Button off, out;
    Vibrator vibrator;
    long[] pattern = {0, 1000, 1000, 1000, 1000, 1000};
    AudioManager audioManager;
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);



        textView = findViewById(R.id.text);
        message = findViewById(R.id.messageT);
            message.setTextSize(20);
            message.setText(NewOrChangedAlarm.message);


        off = findViewById(R.id.offAlarm);
        out = findViewById(R.id.outAlarm);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
            date = Calendar.getInstance();
            textView.setText(sdf.format(date.getTime()));
        }, 0, 1, TimeUnit.SECONDS);
        if (NewOrChangedAlarm.CheckedMusic != null) ringtone = NewOrChangedAlarm.CheckedMusic;
        else ringtone = MediaPlayer.create(getApplicationContext(), R.raw.music);

        audioManager.adjustVolume(AudioManager.MODE_NORMAL, NewOrChangedAlarm.progress);
        ringtone.start();

        if (NewOrChangedAlarm.vibNew.isChecked()){
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(pattern, 6);
        }

        if (NewOrChangedAlarm.loudNew.isChecked()) {
            audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            audioManager.adjustVolume(AudioManager.MODE_NORMAL, NewOrChangedAlarm.progress);

            Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() ->
                    audioManager.adjustVolume(AudioManager.ADJUST_RAISE ,AudioManager.FLAG_PLAY_SOUND), 0, 5, TimeUnit.SECONDS);
        }

        off.setOnClickListener(off ->{
            if (NewOrChangedAlarm.vibNew.isChecked()) vibrator.cancel();
                ringtone.stop();

            Intent intent1 = new Intent(Alarm.this, MainActivity.class);
            startActivity(intent1);
        });

        out.setOnClickListener(out -> {
                if (NewOrChangedAlarm.vibNew.isChecked()) vibrator.cancel();
                if (ringtone != null && ringtone.isPlaying()) {
                    ringtone.stop();
                }
                try {
                    Thread.sleep((long) NewOrChangedAlarm.progressM*1000*60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                textView.setText(sdf.format(date.getTime()));
                ringtone.start();
                if (NewOrChangedAlarm.vibNew.isChecked()) vibrator.vibrate(pattern, 5);

        });
    }

    }