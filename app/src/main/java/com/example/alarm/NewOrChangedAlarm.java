package com.example.alarm;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alarm.databinding.ActivityNewBinding;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewOrChangedAlarm extends AppCompatActivity{
    public static MediaPlayer CheckedMusic;
    MaterialTimePicker materialTimePicker;
    SeekBar volume;
    protected boolean alarm;
    public static boolean vibNew = false, loudNew = false;
    AudioManager audioManager;
    protected static int progressM = 2, progress;
    public static String message = "", days = "";
    AlarmManager alarmManager;
    AlarmManager.AlarmClockInfo alarmClockInfo;
    CreateNewAlarm newAlarm;
    SimpleDateFormat sdf;
    Calendar calendar;
    Intent intent;TextView nameOfMusic;
    ActivityNewBinding binding;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = ActivityNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());




        sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        newAlarm = new CreateNewAlarm();
        message += binding.textMessage.getText().toString();

        binding.musicButton.setOnClickListener(h ->{
            intent = new Intent("android.intent.action.RINGTONE_PICKER");
            startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI), 1);
        });






        binding.backToMain.setOnClickListener( f -> {
            finish();
        });

        binding.minuteInt.setMin(3);
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
        binding.volumeControl.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        binding.volumeControl.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        binding.volumeControl.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
                binding.volumeControl.setProgress(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });




        binding.alarmButton.setOnClickListener( n -> {
            calendar = Calendar.getInstance();
            materialTimePicker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(calendar.getTime().getHours())
                    .setMinute(calendar.getTime().getMinutes())
                    .setTitleText("Выберите время для будильника")
                    .build();

            materialTimePicker.addOnPositiveButtonClickListener(view -> {

                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.MINUTE, materialTimePicker.getMinute());
                calendar.set(Calendar.HOUR_OF_DAY, materialTimePicker.getHour());
                newAlarm.timeName = sdf.format(calendar.getTime());
                alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                alarmClockInfo = new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), getAlarmInfoPendingIntent());

                binding.alarmButton.setTextSize(90);
                binding.alarmButton.setText(sdf.format(calendar.getTime()));

                alarm = true;
                Toast.makeText(this, "Будильник установлен на " + sdf.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
            });

            materialTimePicker.show(getSupportFragmentManager(), "tag_picker");
        });


        binding.createdNewAlarm.setOnClickListener(c -> {
            if (alarm) {
                if (binding.line1.isChecked()){
                    days += "M ";
                    AlarmDay(2, calendar, getAlarmActionPendingIntent(), alarmManager);}

                if (binding.line2.isChecked()){
                    days += "TU ";
                    AlarmDay(3, calendar, getAlarmActionPendingIntent(), alarmManager);
                }

                if (binding.line3.isChecked()) {
                    days +="W ";
                    AlarmDay(4, calendar, getAlarmActionPendingIntent(), alarmManager);
                }

                if (binding.line4.isChecked()) {
                    days +="TH ";
                    AlarmDay(5, calendar, getAlarmActionPendingIntent(), alarmManager);
                }

                if (binding.line5.isChecked()) {
                    days +="F ";
                    AlarmDay(6, calendar, getAlarmActionPendingIntent(), alarmManager);
                }

                if (binding.line6.isChecked()) {
                    days +="SA ";
                    AlarmDay(7, calendar, getAlarmActionPendingIntent(), alarmManager);
                }
                if (binding.line7.isChecked()) {
                    days +="SU ";
                    AlarmDay(1, calendar, getAlarmActionPendingIntent(), alarmManager);
                }


                if (CheckedMusic == null) CheckedMusic = MediaPlayer.create(getApplicationContext(), R.raw.music);
                if (binding.moreLoud.isChecked()) loudNew = true;
                if (binding.vibration.isChecked()) vibNew = true;
                // заполнение параметров будильника; после передачи по ключу они все равны нулл
                progressM = binding.minuteInt.getProgress();
                progress = volume.getProgress();
                newAlarm.time = calendar.getTimeInMillis();
                newAlarm.timeName= sdf.format(calendar.getTime());
                newAlarm.more = binding.moreLoud.isChecked();
                newAlarm.music = CheckedMusic;
                newAlarm.days = days;
                newAlarm.vib = binding.vibration.isChecked();
                newAlarm.vol = progress;
                newAlarm.textMessange = message;
                newAlarm.minute = progressM;


                alarmManager.setAlarmClock(alarmClockInfo, getAlarmActionPendingIntent());
                Intent intent1 = new Intent(NewOrChangedAlarm.this, MainActivity.class);
                intent1.putExtra("NEW", newAlarm);
                setResult(3, intent1);
                startActivityForResult(intent1, 3);


            }
            else{
                Toast.makeText(this, "Вы не можете установить будильник без времени", Toast.LENGTH_SHORT).show();
            }


        });
    }

    public void AlarmDay(int week, Calendar cal, PendingIntent pendingIntent, AlarmManager alarmManager) {
        cal.set(Calendar.DAY_OF_WEEK, week);

        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), 24*60 * 60 * 1000, pendingIntent);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if(resultCode==RESULT_OK) {
            switch (requestCode) {
                case 1:
                    Uri uri = intent.getData();

                    CheckedMusic = MediaPlayer.create(getApplicationContext(), uri);
                    nameOfMusic = findViewById(R.id.nameOfCheckedMusic);
                    nameOfMusic.setText(uri.getEncodedUserInfo());
                    break;
            }
        }
        else{
            CheckedMusic = MediaPlayer.create(getApplicationContext(), R.raw.music);
        }

    }

    PendingIntent getAlarmInfoPendingIntent() {
        Intent alarmInfoIntent = new Intent(this, MainActivity.class);
        alarmInfoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(this, 0, alarmInfoIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    PendingIntent getAlarmActionPendingIntent() {
        Intent intent = new Intent(this, Alarm.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}





