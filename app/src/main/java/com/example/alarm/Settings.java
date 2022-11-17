package com.example.alarm;

import static java.lang.Boolean.getBoolean;
import static java.lang.Boolean.valueOf;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

public class Settings extends AppCompatActivity {
    Intent intentVibrate;
    public static int curValue, maxVolume;
    public static Switch vib, loud;
    Button save;
    @SuppressLint("StaticFieldLeak")
    public static SeekBar volumeControl, minutes;
    public static SharedPreferences prefs;
    AudioManager audioManager;
    public static boolean vibr = true, loudB = true, minut;
    @SuppressLint("ServiceCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        save = findViewById(R.id.save);
        vib = findViewById(R.id.vibrationS);
        loud = findViewById(R.id.moreLoudS);
        volumeControl = findViewById(R.id.volumeControlS);
        minutes = findViewById(R.id.minuteIntS);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        curValue = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volumeControl.setMax(maxVolume);
        volumeControl.setMin(0);
        volumeControl.setProgress(curValue);
        volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, Settings.volumeControl.getProgress());
                newA.progress = progress;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        minutes.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                minutes.setProgress(progress);
                if (progress!=0) minut = true;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        prefs = getSharedPreferences("test", Context.MODE_PRIVATE);
        boolean vibSwitchState = prefs.getBoolean("vibr", vib.isChecked());
        boolean loudSwitchState = prefs.getBoolean("loud", loud.isChecked());

        vib.setChecked(vibSwitchState);
        loud.setChecked(loudSwitchState);

        vib.setOnClickListener(t -> {
            vib.isChecked();
            vibr = true;
        });

        loud.setOnClickListener(k->{
            loud.isChecked();
            loudB = true;
        });
        save.setOnClickListener(save -> {
                SharedPreferences.Editor ed = getSharedPreferences("test", Context.MODE_PRIVATE).edit();

                ed.putBoolean("vibr", vib.isChecked());
                ed.putBoolean("loud", loud.isChecked());
                ed.putInt("vol", volumeControl.getProgress());
                ed.putInt("min", minutes.getProgress());
                ed.apply();
                Toast.makeText(this, "Настройки по умолчанию сохранены", Toast.LENGTH_SHORT).show();

        });

    }

    public void music(View view) {
        Intent intent = new Intent(Settings.this, Music.class);
        startActivity(intent);
    }
    public void back(View view) {
        Intent intent = new Intent(Settings.this, MainActivity.class);
        startActivity(intent);}

}