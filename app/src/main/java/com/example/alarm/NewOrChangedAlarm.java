package com.example.alarm;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alarm.databinding.ActivityNewBinding;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewOrChangedAlarm extends AppCompatActivity{
    public static MediaPlayer CheckedMusic = new MediaPlayer();
    private static String mSound;
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
    private ActivityNewBinding binding;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_new);super.onCreate(savedInstanceState);
        Click click = new Click();
        CheckedMusic = MediaPlayer.create(NewOrChangedAlarm.this,R.raw.music);
        binding = ActivityNewBinding.inflate(getLayoutInflater());
        music = findViewById(R.id.musicButton);
        music.setOnClickListener(click);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        prefs = getSharedPreferences("test", Context.MODE_PRIVATE);

        Intent intentMain = getIntent();
        CreateNewAlarm newAlarm = (CreateNewAlarm) intentMain.getSerializableExtra("NewAlarm");



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
        curValue = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        volume = findViewById(R.id.volumeControl);
        volume.setMin(0);
        volume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volume.setProgress(curValue);
        volume.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
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



        vibNew = findViewById(R.id.vibration);
        vibNew.setChecked(isValumeCanVibr);
        vibNew.setOnClickListener(t -> {
            if (vibNew.isChecked() ? (isValumeCanVibr = true) : (isValumeCanVibr = false)) ;
        });

        loudNew = findViewById(R.id.moreLoud);
        loudNew.setChecked(isValumeIncreasingGradually);
        loudNew.setOnClickListener(k -> {
            if (loudNew.isChecked() ? (isValumeIncreasingGradually = true) : (isValumeIncreasingGradually = false));
        });


        plus = findViewById(R.id.createdNewAlarm);
        plus.setOnClickListener(v -> {

            if (alarm) {
                progressM = minute.getProgress();
                progress = volume.getProgress();
                newAlarm.more = isValumeIncreasingGradually;
                newAlarm.time = calendar.getTime().toString();
                newAlarm.vib = isValumeCanVibr;
                newAlarm.vol = progress;
                newAlarm.textMessange = binding.textMessage.getText().toString();
                newAlarm.music = CheckedMusic;
                newAlarm.minute = progressM;
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Toast.makeText(NewOrChangedAlarm.this, String.valueOf(minute.getProgress()), Toast.LENGTH_SHORT).show();
                AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), getAlarmInfoPendingIntent());

                alarmManager.setAlarmClock(alarmClockInfo, getAlarmActionPendingIntent());
                Toast.makeText(this, "Будильник установлен на " + sdf.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(NewOrChangedAlarm.this, Alarm.class);
                intent.putExtra("CreatedNew", newAlarm);
                startActivity(intent);

            } else {
                Toast.makeText(this, "Вы не можете установить будильник без времени", Toast.LENGTH_SHORT).show();
            }



        });
    }
 class  Click implements View.OnClickListener{


     @RequiresApi(api = Build.VERSION_CODES.Q)
     @SuppressLint({"ResourceAsColor", "UseCompatLoadingForDrawables", "NonConstantResourceId", "DefaultLocale", "WrongConstant"})
     @Override
     public void onClick(View view){
         switch (view.getId()){
             case R.id.musicButton:
                 Intent intent = new Intent("android.intent.action.RINGTONE_PICKER");
                 intent.putExtra("android.intent.extra.ringtone.SHOW_SILENT", false);
                 intent.putExtra("android.intent.extra.ringtone.TYPE", 4);
                 String string2 = NewOrChangedAlarm.mSound;
                 Uri uri = string2 != null ? Uri.parse(string2) : null;
                 CheckedMusic = MediaPlayer.create(NewOrChangedAlarm.this, uri);
                 startActivity(intent);
                 break;
         }
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
        startActivity(intent);}

}
