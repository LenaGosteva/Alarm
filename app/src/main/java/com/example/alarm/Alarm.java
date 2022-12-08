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
    ActivityAlarmBinding binding;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAlarmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


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
            ringtone = RingtoneManager.getRingtone(this, uri);
        }
        audioManager.adjustVolume(AudioManager.MODE_NORMAL, NewOrChangedAlarm.progress);
        ringtone.play();

        if (NewOrChangedAlarm.vibNew) {
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
            vibrator.vibrate(pattern, 6);
        }

        if (NewOrChangedAlarm.loudNew) {
            audioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
            audioManager.adjustVolume(AudioManager.MODE_NORMAL, NewOrChangedAlarm.progress);

            Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() ->
                    audioManager.adjustVolume(AudioManager.ADJUST_RAISE, AudioManager.FLAG_PLAY_SOUND), 0, 5, TimeUnit.SECONDS);
        }

        binding.offAlarm.setOnClickListener(off -> {
            if (NewOrChangedAlarm.vibNew) vibrator.cancel();
            ringtone.stop();

            Intent intent1 = new Intent(Alarm.this, MainActivity.class);
            startActivity(intent1);
        });

        binding.outAlarm.setOnClickListener(out -> {
            if (NewOrChangedAlarm.vibNew) vibrator.cancel();
            if (ringtone != null && ringtone.isPlaying()) {
                ringtone.stop();
            }
            try {
                Thread.sleep((long) NewOrChangedAlarm.progressM * 1000 * 60);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            binding.text.setText(sdf.format(date.getTime()));
            ringtone.play();
            if (NewOrChangedAlarm.vibNew) vibrator.vibrate(pattern, 5);

        });
    }
}