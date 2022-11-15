package com.example.alarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
    Switch vib;
    public static boolean vibr = false;
    Calendar calendar;
    protected boolean alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        setAlarm = findViewById(R.id.alarm_button);
        plus = findViewById(R.id.button6);
        vib = findViewById(R.id.vibration);
        vib.setChecked(bool());

        setAlarm.setOnClickListener(v -> {
            MaterialTimePicker Time = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(0)
                    .setMinute(0)
                    .setTitleText("Выберите время для будильника")
                    .build();

            Time.addOnPositiveButtonClickListener(view -> {
                calendar = Calendar.getInstance();
                calendar.set(Calendar.MINUTE, Time.getMinute());
                calendar.set(Calendar.HOUR_OF_DAY, Time.getHour());

                setAlarm.setTextSize(38);
                setAlarm.setText(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE)));

            });
            Time.show(getSupportFragmentManager(), "tag_picker");
            alarm = true;
        });

        vib.setOnClickListener(t -> {
            vibr = true;
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

    public boolean bool() {
        if(getIntent().getBooleanExtra("vibration", newA.vibr)) return true;
        return false;
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