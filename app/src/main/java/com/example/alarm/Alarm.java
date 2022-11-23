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

    Ringtone ringtone;
    TextView textView, Message;
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
        Message = findViewById(R.id.messageT);
        Message.setTextSize(20);
        Message.setText(NewOrChangedAlarm.textM);
        off = findViewById(R.id.offAlarm);
        out = findViewById(R.id.outAlarm);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);


        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                date = Calendar.getInstance();
                textView.setText(sdf.format(date.getTime()));
            }

        }, 0, 1, TimeUnit.SECONDS);
        prefs = getSharedPreferences("test", Context.MODE_PRIVATE);
//        ringtone = MediaPlayer.create(Alarm.this, R.raw.music);
        ringtone = NewOrChangedAlarm.CheckedMusic;
        if(ringtone == null){
            Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            ringtone = RingtoneManager.getRingtone(this, notificationUri);
        }
        if (audioManager.isMusicActive())
        audioManager.adjustVolume(AudioManager.MODE_NORMAL, NewOrChangedAlarm.progress);
        ringtone.play();

        if (NewOrChangedAlarm.isValumeCanVibr){
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(pattern, 2);
        }

        if (NewOrChangedAlarm.isValumeIncreasingGradually) {
            audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            audioManager.adjustVolume(AudioManager.MODE_NORMAL, NewOrChangedAlarm.progress);

            Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
                audioManager.adjustVolume(AudioManager.ADJUST_RAISE ,AudioManager.FLAG_PLAY_SOUND);

                }, 0, 5, TimeUnit.SECONDS);
        }

        off.setOnClickListener(off ->{
            if (NewOrChangedAlarm.isValumeCanVibr) vibrator.cancel();
                ringtone.stop();

            Intent intent1 = new Intent(Alarm.this, MainActivity.class);
            startActivity(intent1);
        });

        out.setOnClickListener(out -> {
            if (NewOrChangedAlarm.progressM !=0) {
                if (NewOrChangedAlarm.isValumeCanVibr) vibrator.cancel();
                if (ringtone != null && ringtone.isPlaying()) {
                    ringtone.stop();
                }
                try {
                    Thread.sleep((long) NewOrChangedAlarm.progressM*1000*60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                textView.setText(sdf.format(date.getTime()));
                ringtone.play();
                if (NewOrChangedAlarm.isValumeCanVibr) vibrator.vibrate(pattern, 2);
            }

        });
    }

    }