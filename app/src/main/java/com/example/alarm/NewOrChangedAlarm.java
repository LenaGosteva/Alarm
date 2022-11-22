package com.example.alarm;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewOrChangedAlarm extends AppCompatActivity {
     public static Ringtone CheckedMusic;
    private static String mSound;
    private static AlarmManager alarmManager;
    Button setAlarm;
    Button plus;
    public static boolean isValumeCanVibr = true, isValumeIncreasingGradually = false;
    @SuppressLint({"StaticFieldLeak", "UseSwitchCompatOrMaterialCode"})
    protected Switch vibNew, loudNew;
    Calendar calendar;
    SeekBar volume, minute;
    protected boolean alarm;
    SharedPreferences prefs;
    AudioManager audioManager;
    protected static int progressM = 2, progress;
    Button music;
    int curValue;
    ToggleButton Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday;
    static AlarmManager.AlarmClockInfo alarmClockInfo;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_new);
        super.onCreate(savedInstanceState);
        Click click = new Click();

//        Monday = findViewById(R.id.line1);
//        Tuesday = findViewById(R.id.line2);
//        Wednesday = findViewById(R.id.line3);
//        Thursday = findViewById(R.id.line4);
//        Friday = findViewById(R.id.line5);
//        Saturday = findViewById(R.id.line6);
//        Sunday = findViewById(R.id.line7);
//        if (Monday.isChecked()) {
//            AlarmDay(2, calendar, getAlarmActionPendingIntent());
//        } else if (Tuesday.isChecked()) {
//            AlarmDay(3, calendar, getAlarmActionPendingIntent());
//        } else if (Wednesday.isChecked()) {
//            AlarmDay(4, calendar, getAlarmActionPendingIntent());
//        } else if (Thursday.isChecked()) {
//            AlarmDay(5, calendar, getAlarmActionPendingIntent());
//        } else if (Friday.isChecked()) {
//            AlarmDay(6, calendar, getAlarmActionPendingIntent());
//        } else if (Saturday.isChecked()) {
//            AlarmDay(7, calendar, getAlarmActionPendingIntent());
//        } else if (Sunday.isChecked()) {
//            AlarmDay(1, calendar, getAlarmActionPendingIntent());
//        }
//



        music = findViewById(R.id.musicButton);
        music.setOnClickListener(click);
        EditText text = findViewById(R.id.textMessage);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        prefs = getSharedPreferences("test", Context.MODE_PRIVATE);

        Intent intentMain = getIntent();
        CreateNewAlarm newAlarm = (CreateNewAlarm)intentMain.getSerializableExtra("NewAlarm");


        minute = findViewById(R.id.minuteInt);
        minute.setMin(0);
        minute.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean f) {
                minute.setProgress(progress);
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
        volume.setProgress(curValue);
        volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, progress, audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM));
                volume.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });


        setAlarm = findViewById(R.id.alarm_button);
        setAlarm.setOnClickListener(v -> {
            MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(12)
                    .setMinute(0)
                    .setTitleText("Выберите время для будильника")
                    .build();

            materialTimePicker.addOnPositiveButtonClickListener(view -> {
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.MINUTE, materialTimePicker.getMinute());
                calendar.set(Calendar.HOUR_OF_DAY, materialTimePicker.getHour());
                setAlarm.setTextSize(38);
                setAlarm.setText(sdf.format(calendar.getTime()));
                alarm = true;

                Toast.makeText(this, "Будильник установлен на " + sdf.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
            });
            materialTimePicker.show(getSupportFragmentManager(), "tag_picker");

        });


        vibNew = findViewById(R.id.vibration);
        vibNew.setChecked(isValumeCanVibr);
        vibNew.setOnClickListener(t -> {
            if (vibNew.isChecked() ? (isValumeCanVibr = true) : (isValumeCanVibr = false)) ;
        });

        loudNew = findViewById(R.id.moreLoud);
        loudNew.setChecked(isValumeIncreasingGradually);
        loudNew.setOnClickListener(k -> {
            if (loudNew.isChecked() ? (isValumeIncreasingGradually = true) : (isValumeIncreasingGradually = false))
                ;
        });


        plus = findViewById(R.id.createdNewAlarm);
        plus.setOnClickListener(v -> {

            if (alarm) {
                progressM = minute.getProgress();
                progress = volume.getProgress();
                newAlarm.more = isValumeIncreasingGradually;
//                newAlarm.time = calendar.getTime().toString();
                newAlarm.vib = isValumeCanVibr;
                newAlarm.vol = progress;
                newAlarm.textMessange = text.getText().toString();
                newAlarm.minute = progressM;
                Intent intent1 = new Intent(NewOrChangedAlarm.this, Alarm.class);
                intent1.putExtra("CreatedNew", newAlarm);
                startActivity(intent1);
                alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmClockInfo = new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), getAlarmInfoPendingIntent());

                alarmManager.setAlarmClock(alarmClockInfo, getAlarmActionPendingIntent());
            } else {
                Toast.makeText(this, "Вы не можете установить будильник без времени", Toast.LENGTH_SHORT).show();
            }


        });

    }
    public void AlarmDay(int weekno, Calendar cal, PendingIntent pendingIntent){
        cal.set(Calendar.DAY_OF_WEEK, weekno);

        NewOrChangedAlarm.alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 1 * 60 * 60 * 1000, pendingIntent);
    }
    class Click implements View.OnClickListener {


        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.musicButton:
                    Intent intent = new Intent("android.intent.action.RINGTONE_PICKER");
                    startActivityForResult(intent, 1);
                    break;

            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        switch (resultCode){
            case 1:
            Uri uri = intent.getData();
            CheckedMusic = RingtoneManager.getRingtone(NewOrChangedAlarm.this, uri);
            break;
        }
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

    public void back(View view) {
        Intent intent = new Intent(NewOrChangedAlarm.this, MainActivity.class);
        startActivity(intent);
    }

}
