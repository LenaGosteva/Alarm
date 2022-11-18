package com.example.alarm;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alarm.databinding.ActivityMainBinding;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewOrChangedAlarm extends AppCompatActivity {
    Button setAlarm;
    Button plus;

    @SuppressLint({"StaticFieldLeak", "UseSwitchCompatOrMaterialCode"})
    protected static Switch vibNew, loudNew;
    Calendar calendar;
    SeekBar volume, minute;
    protected boolean alarm;
    SharedPreferences prefs;
    AudioManager audioManager;
    int curValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        com.example.alarm.databinding.ActivityMainBinding binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setContentView(R.layout.activity_new);
        super.onCreate(savedInstanceState);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        prefs = getSharedPreferences("test", Context.MODE_PRIVATE);


        Intent intentMain = getIntent();
        CreateNewAlarm newAlarm = (CreateNewAlarm) intentMain.getSerializableExtra("NewAlarm");

        minute = findViewById(R.id.minuteInt);
        minute.setProgress(prefs.getInt("min", Settings.progressM));
        minute.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean f) {
                if (progress!=0) Settings.minut = true;
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        curValue = audioManager.getStreamVolume(AudioManager.STREAM_SYSTEM);
        volume = findViewById(R.id.volumeControl);
        volume.setMin(0);
        volume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM));
        volume.setProgress(prefs.getInt("vol", Settings.progress));
        volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressv, boolean fromUser) {

                audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, progressv, audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM));
                volume.setProgress(progressv);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Settings.progress = volume.getProgress();
            }
        });


        setAlarm = findViewById(R.id.alarm_button);
        setAlarm.setOnClickListener(v -> {
            MaterialTimePicker Time = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_12H)
                    .setHour(MainActivity.date.get(Calendar.HOUR_OF_DAY))
                    .setMinute(MainActivity.date.get(Calendar.MINUTE))
                    .setTitleText("Выберите время для будильника")
                    .build();

            Time.addOnPositiveButtonClickListener(view -> {
                calendar = Calendar.getInstance();
                calendar.set(Calendar.MINUTE, Time.getMinute());
                calendar.set(Calendar.HOUR_OF_DAY, Time.getHour());

                setAlarm.setTextSize(38);
                setAlarm.setText(sdf.format(calendar.getTime()));
                alarm = true;
            });
            Time.show(getSupportFragmentManager(), "tag_picker");

        });


        boolean vibSwitchState = prefs.getBoolean("vibr", true);

        vibNew = findViewById(R.id.vibration);
        vibNew.setChecked(vibSwitchState);
        vibNew.setOnClickListener(t -> {
            if (vibNew.isChecked()? (Settings.isValumeCanVibr = true) : (Settings.isValumeCanVibr = false));
        });


        boolean loudSwitchState = prefs.getBoolean("loud", true);
        loudNew = findViewById(R.id.moreLoud);
        loudNew.setChecked(loudSwitchState);
        loudNew.setOnClickListener(k->{
            if (loudNew.isChecked()? (Settings.isValumeIncreasingGradually = true) : (Settings.isValumeIncreasingGradually = false));
        });


        plus = findViewById(R.id.createdNewAlarm);
        plus.setOnClickListener(v -> {

            if(alarm) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), getAlarmInfoPendingIntent());

                alarmManager.setAlarmClock(alarmClockInfo, getAlarmActionPendingIntent());
                Toast.makeText(this, "Будильник установлен на " + sdf.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(NewOrChangedAlarm.this, MainActivity.class);intent.putExtra("CreatedNew", newAlarm);
                startActivity(intent);
            } else{
                Toast.makeText(this, "Вы не можете установить будильник без времени", Toast.LENGTH_SHORT).show();
            }

            newAlarm.more = Settings.isValumeIncreasingGradually;
            newAlarm.time = calendar.getTime().toString();
            newAlarm.vib = Settings.isValumeCanVibr;
            newAlarm.vol = Settings.progress;


        });
    }


    @SuppressLint("UnspecifiedImmutableFlag")
    private PendingIntent getAlarmActionPendingIntent() {
        Intent intent = new Intent(this, Alarm.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
    @SuppressLint("UnspecifiedImmutableFlag")
    private PendingIntent getAlarmInfoPendingIntent() {
        Intent alarmInfoIntent = new Intent(this, MainActivity.class);
        alarmInfoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(this, 0, alarmInfoIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
    public void music(View view) {
        Intent intent = new Intent(NewOrChangedAlarm.this, Music.class);
        startActivity(intent);
    }

    public void back(View view) {
        Intent intent = new Intent(NewOrChangedAlarm.this, MainActivity.class);
        startActivity(intent);
    }

}