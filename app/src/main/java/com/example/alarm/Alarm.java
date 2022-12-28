package com.example.alarm;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alarm.databinding.ActivityAlarmBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class Alarm extends AppCompatActivity {

    Ringtone ringtone;
    Calendar date;
    Vibrator vibrator;
    long[] pattern = {0, 1000, 1000, 1000, 1000, 1000};
    AudioManager audioManager;
    boolean on = false;
    ActivityAlarmBinding binding;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlarmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int id = getIntent().getExtras().getInt("ALARM");
        boolean loud = getIntent().getExtras().getBoolean("loud");
        boolean vibration = getIntent().getExtras().getBoolean("vibration");
        int progress = getIntent().getExtras().getInt("progress");
        int progressM = getIntent().getExtras().getInt("progressM");
        Log.d("IDI",String.valueOf(id));
        for (CreateNewAlarm i : MainActivity.news) {
            if(id == i.id){
                on = i.on;
                break;
            }
        }
        if (on) {
            binding.messageT.setTextSize(20);
            binding.messageT.setText(getIntent().getStringExtra("Message"));


            audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

            Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {
                date = Calendar.getInstance();
                binding.text.setText(sdf.format(date.getTime()));
            }, 0, 1, TimeUnit.SECONDS);
            ringtone = RingtoneManager.getRingtone(this, NewOrChangedAlarm.CheckedMusic);
            if (ringtone == null) {
                Uri uri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
                ringtone = RingtoneManager.getRingtone(this, RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALL));
            }
            audioManager.adjustVolume(AudioManager.MODE_NORMAL, progress);
            ringtone.play();

            if (vibration) {
                vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(pattern, 3);
            }

            if (loud) {
                audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
                audioManager.adjustVolume(AudioManager.MODE_NORMAL, progress);

                Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() ->
                        audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND), 0, 5, TimeUnit.SECONDS);
            }

            binding.offAlarm.setOnClickListener(off -> {
                if (vibration) vibrator.cancel();
                ringtone.stop();

                startActivity(new Intent(this, MathTrainer.class));
                finish();
            });

            binding.outAlarm.setOnClickListener(out -> {
                if (vibration) vibrator.cancel();
                if (ringtone != null && ringtone.isPlaying()) {
                    ringtone.stop();
                }
                try {
                    Thread.sleep((long) progressM * 1000 * 60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                binding.text.setText(sdf.format(date.getTime()));
                ringtone.play();
                if (vibration) vibrator.vibrate(pattern, 5);

            });
        }
        else{
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}