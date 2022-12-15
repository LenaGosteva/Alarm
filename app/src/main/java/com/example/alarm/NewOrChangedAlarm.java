package com.example.alarm;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.SeekBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alarm.databinding.ActivityNewBinding;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewOrChangedAlarm extends AppCompatActivity {
    MaterialTimePicker materialTimePicker;
    protected boolean alarm;
    public static boolean vibNew = false, loudNew = false;
    AudioManager audioManager;
    protected static int progressM = 2, progress;
    public String days = "";

    CreateNewAlarm newAlarm;
    SimpleDateFormat sdf;
    Calendar calendar = Calendar.getInstance();
    Intent intent;
    ActivityNewBinding binding;
    public static Uri CheckedMusic = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALL);

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        newAlarm = (CreateNewAlarm) getIntent().getSerializableExtra("Cr");
        if (newAlarm == null){
            newAlarm = new CreateNewAlarm();
        }
        super.onCreate(savedInstanceState);
        binding.alarmButton.setTextSize(90);
        binding.alarmButton.setText(newAlarm.timeName);

        CheckedMusic = newAlarm.music;
        sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        binding.musicButton.setOnClickListener(h -> {
            intent = new Intent("android.intent.action.RINGTONE_PICKER");
            startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI), 1);
        });


        binding.backToMain.setOnClickListener(f -> {
            finish();
        });


        binding.minuteInt.setMin(3);
        binding.minuteInt.setProgress(newAlarm.minute);
        binding.minuteInt.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean f) {
                binding.minuteInt.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });


        binding.volumeControl.setMin(0);
        binding.volumeControl.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM));
        binding.volumeControl.setProgress(newAlarm.vol);
        binding.volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                audioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, progress, audioManager.getStreamMaxVolume(AudioManager.STREAM_SYSTEM));
                binding.volumeControl.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        binding.vibration.setChecked(newAlarm.vib);
        binding.moreLoud.setChecked(newAlarm.more);


        final int id = (int) System.currentTimeMillis();
        binding.alarmButton.setOnClickListener(n -> {
            materialTimePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(newAlarm.hours)
                    .setMinute(newAlarm.minutes)
                    .setTitleText("Выберите время для будильника")
                    .build();

            materialTimePicker.addOnPositiveButtonClickListener(view -> {

                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.MINUTE, materialTimePicker.getMinute());
                calendar.set(Calendar.HOUR_OF_DAY, materialTimePicker.getHour());


                binding.alarmButton.setTextSize(90);
                binding.alarmButton.setText(sdf.format(calendar.getTime()));
                alarm = true;

            });

            materialTimePicker.show(getSupportFragmentManager(), "tag_picker");
        });


        binding.createdNewAlarm.setOnClickListener(c -> {
            if (alarm) {
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                {
                    if (binding.line1.isChecked()) {
                        AlarmDay(2, getAlarmActionPendingIntent(id), alarmManager, "M ");
                    }

                    if (binding.line2.isChecked()) {
                        AlarmDay(3, getAlarmActionPendingIntent(id), alarmManager, "TU ");
                    }

                    if (binding.line3.isChecked()) {
                        AlarmDay(4, getAlarmActionPendingIntent(id), alarmManager, "W ");
                    }

                    if (binding.line4.isChecked()) {
                        AlarmDay(5, getAlarmActionPendingIntent(id), alarmManager, "TH ");
                    }

                    if (binding.line5.isChecked()) {
                        AlarmDay(6, getAlarmActionPendingIntent(id), alarmManager, "F ");
                    }

                    if (binding.line6.isChecked()) {
                        AlarmDay(7, getAlarmActionPendingIntent(id), alarmManager, "SA ");
                    }
                    if (binding.line7.isChecked()) {
                        AlarmDay(1, getAlarmActionPendingIntent(id), alarmManager, "SU ");
                    }
                }
                if (days.isEmpty()) days = binding.today.isChecked()?"Today": "Tomorrow";
                if (binding.moreLoud.isChecked()) loudNew = true;
                if (binding.vibration.isChecked()) vibNew = true;
                newAlarm = new CreateNewAlarm(binding.alarmButton.getText().toString(), binding.minuteInt.getProgress(),
                        id, binding.volumeControl.getProgress(), binding.textMessage.getText().toString(),
                        days,calendar.getTimeInMillis(), CheckedMusic,
                        binding.moreLoud.isChecked(), binding.vibration.isChecked(),
                        calendar.getTime().getHours(), calendar.getTime().getMinutes());
                if (!binding.today.isChecked() && calendar.getTimeInMillis() < System.currentTimeMillis()) {
                    setAlarm(alarmManager, id, calendar.getTimeInMillis() + 1000 * 60 * 60 * 24);
                } else if (binding.today.isChecked() && calendar.getTimeInMillis() < System.currentTimeMillis()) {
                    alarm = true;
                    Toast.makeText(this, "Вы не можете установить будильник на сегодня на время " + sdf.format(calendar.getTime()) + ", т.к. это время уже прошло", Toast.LENGTH_SHORT).show();
                } else if (binding.today.isChecked() && (calendar.getTimeInMillis() >= System.currentTimeMillis())) {

                } else if (!binding.today.isChecked() && calendar.getTimeInMillis() >= System.currentTimeMillis()){
                    setAlarm(alarmManager, id, calendar.getTimeInMillis());
                }

            } else {
                Toast.makeText(this, "Вы не можете установить будильник без времени", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void AlarmDay(int week, PendingIntent pendingIntent, AlarmManager alarmManager, String day) {
        if (!days.contains(day)) days += day;
        if (week < Calendar.DAY_OF_WEEK) calendar.set(Calendar.WEEK_OF_MONTH, Calendar.getInstance().get(Calendar.WEEK_OF_MONTH)+1);
        calendar.set(Calendar.DAY_OF_WEEK, week);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 7 * 24 * 60 * 60 * 1000, pendingIntent);
    }
    public void setAlarm(AlarmManager alarmManager, int id, long time){
        Toast.makeText(this, "Будильник установлен на " + sdf.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
        Intent alarmInfoIntent = new Intent(this, MainActivity.class);
        alarmInfoIntent.putExtra("NEW", newAlarm);
        alarmInfoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(time, PendingIntent.getActivity(this, id, alarmInfoIntent, PendingIntent.FLAG_UPDATE_CURRENT));
        alarmManager.setAlarmClock(alarmClockInfo, getAlarmActionPendingIntent(id));
        setResult(RESULT_OK, alarmInfoIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    Uri uri = intent.getData();
                    CheckedMusic = uri;
                    binding.nameOfCheckedMusic.setText(uri.getPath());
                    break;
            }
        } else {
            CheckedMusic = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALL);
        }
    }


    PendingIntent getAlarmActionPendingIntent(int id) {
        Intent intent = new Intent(this, Alarm.class);
        intent.putExtra("Message", binding.textMessage.getText().toString());
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    
}




