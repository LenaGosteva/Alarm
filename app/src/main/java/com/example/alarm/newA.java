package com.example.alarm;

import static java.lang.Boolean.getBoolean;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class newA extends AppCompatActivity {
    Button setAlarm;
    Button plus;
    Switch vibNew, loudNew;
    Calendar calendar;
    SeekBar volume;
    protected boolean alarm;
    SharedPreferences prefs;
    AudioManager audioManager;
    public static float progress;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);

        prefs = getSharedPreferences("test", Context.MODE_PRIVATE);


        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        setAlarm = findViewById(R.id.alarm_button);
        plus = findViewById(R.id.button6);
        volume = findViewById(R.id.volumeControl);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        int curValue = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volume.setMax(100);
        volume.setProgress(curValue);

        volume.setProgress(prefs.getInt("vol", volume.getProgress()));

        boolean vibSwitchState = prefs.getBoolean("vibr", true);
        boolean loudSwitchState = prefs.getBoolean("loud", true);

        vibNew = findViewById(R.id.vibration);
        loudNew = findViewById(R.id.moreLoud);

        vibNew.setChecked(vibSwitchState);
        loudNew.setChecked(loudSwitchState);

        volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
                newA.progress = volume.getProgress();
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        setAlarm.setOnClickListener(v -> {
            MaterialTimePicker Time = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(Calendar.HOUR_OF_DAY)
                    .setMinute(Calendar.MINUTE)
                    .setTitleText("Выберите время для будильника")
                    .build();

            Time.addOnPositiveButtonClickListener(view -> {
                calendar = Calendar.getInstance();
                calendar.set(Calendar.MINUTE, Time.getMinute());
                calendar.set(Calendar.HOUR_OF_DAY, Time.getHour());

                setAlarm.setTextSize(38);
                setAlarm.setText(sdf.format(calendar.getTime()));

            });
            Time.show(getSupportFragmentManager(), "tag_picker");
            alarm = true;
        });

        vibNew.setOnClickListener(t -> {
            if(vibNew.isChecked()){
                vibNew.isChecked();
                Settings.vibr = true; }
            else{
                Settings.vibr = false;
            }
        });
        
        plus.setOnClickListener(v -> {

            if(alarm) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), getAlarmInfoPendingIntent());

                alarmManager.setAlarmClock(alarmClockInfo, getAlarmActionPendingIntent());
                Toast.makeText(this, "Будильник установлен на " + sdf.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
            }
            Intent intent = new Intent(newA.this, MainActivity.class);
            startActivity(intent);
        });
    }

    private PendingIntent getAlarmInfoPendingIntent() {
        Intent alarmInfoIntent = new Intent(this, MainActivity.class);
        alarmInfoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(this, 0, alarmInfoIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private PendingIntent getAlarmActionPendingIntent() {
        Intent intent = new Intent(this, Alarm.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
    public void music(View view) {
        Intent intent = new Intent(newA.this, Music.class);
        startActivity(intent);
    }

    public void back(View view) {
        Intent intent = new Intent(newA.this, MainActivity.class);
        startActivity(intent);
    }

}