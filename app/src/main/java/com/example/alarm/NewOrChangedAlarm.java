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
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class NewOrChangedAlarm extends AppCompatActivity{
    public static MediaPlayer CheckedMusic;
    public EditText text;
    MaterialTimePicker materialTimePicker;
    Button setAlarm, back;
    Button plus;
    @SuppressLint({"StaticFieldLeak", "UseSwitchCompatOrMaterialCode"})
    protected static Switch vibNew, loudNew;
    SeekBar volume, minute;
    protected boolean alarm;
    AudioManager audioManager;
    protected static int progressM = 2, progress;
    Button music;
    public static String message = "";
    AlarmManager alarmManager;
    AlarmManager.AlarmClockInfo alarmClockInfo;
    ToggleButton Monday, Tuesday, Wednesday, Thursday, Friday, Saturday, Sunday;
    CreateNewAlarm newAlarm;
    SimpleDateFormat sdf;
    Calendar calendar;
    Intent intent;TextView nameOfMusic;
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_new);
        super.onCreate(savedInstanceState);
        Click click = new Click();

        Monday = findViewById(R.id.line1);
        Tuesday = findViewById(R.id.line2);
        Wednesday = findViewById(R.id.line3);
        Thursday = findViewById(R.id.line4);
        Friday = findViewById(R.id.line5);
        Saturday = findViewById(R.id.line6);
        Sunday = findViewById(R.id.line7);

        volume = findViewById(R.id.volumeControl);
        music = findViewById(R.id.musicButton);
        back = findViewById(R.id.backToMain);
        minute = findViewById(R.id.minuteInt);
        text = findViewById(R.id.textMessage);
        setAlarm = findViewById(R.id.alarm_button);
        vibNew = findViewById(R.id.vibration);
        loudNew = findViewById(R.id.moreLoud);
        plus = findViewById(R.id.createdNewAlarm);

        sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        newAlarm = new CreateNewAlarm();
        message += text.getText().toString();

        music.setOnClickListener(h ->{
            intent = new Intent("android.intent.action.RINGTONE_PICKER");
            intent.setType("audio/mpeg");
            startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI), 1);
        });






        back.setOnClickListener(click);

        minute.setMin(3);
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


        volume.setMin(0);
        volume.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        volume.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
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




        setAlarm.setOnClickListener( n -> {
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

                setAlarm.setTextSize(90);
                setAlarm.setText(sdf.format(calendar.getTime()));

                alarm = true;
                Toast.makeText(this, "Будильник установлен на " + sdf.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
            });

            materialTimePicker.show(getSupportFragmentManager(), "tag_picker");
        });


        plus.setOnClickListener(c -> {
            if (alarm) {

                if (CheckedMusic == null) CheckedMusic = MediaPlayer.create(getApplicationContext(), R.raw.music);

                // заполнение параметров будильника; после передачи по ключу они все равны нулл
                progressM = minute.getProgress();
                progress = volume.getProgress();
                newAlarm.time = calendar.getTimeInMillis();
                newAlarm.timeName= sdf.format(calendar.getTime());
                newAlarm.more = loudNew.isChecked();
                newAlarm.music = CheckedMusic;
                newAlarm.vib = vibNew.isChecked();
                newAlarm.vol = progress;
                newAlarm.textMessange = message;
                newAlarm.minute = progressM;

                alarmManager.setAlarmClock(alarmClockInfo, getAlarmActionPendingIntent());
                Intent intent1 = new Intent(NewOrChangedAlarm.this, MainActivity.class);
                startActivity(intent1);
            }
            else{
                Toast.makeText(this, "Вы не можете установить будильник без времени", Toast.LENGTH_SHORT).show();
            }
            if (Monday.isChecked()){
                AlarmDay(2, calendar, getAlarmActionPendingIntent(), alarmManager);}

            if (Tuesday.isChecked())
                AlarmDay(3, calendar, getAlarmActionPendingIntent(), alarmManager);
            if (Wednesday.isChecked())
                AlarmDay(4, calendar, getAlarmActionPendingIntent(), alarmManager);

            if (Thursday.isChecked())
                AlarmDay(5, calendar, getAlarmActionPendingIntent(), alarmManager);

            if (Friday.isChecked())
                AlarmDay(6, calendar, getAlarmActionPendingIntent(), alarmManager);

            if (Saturday.isChecked())
                AlarmDay(7, calendar, getAlarmActionPendingIntent(), alarmManager);
            if (Sunday.isChecked())
                AlarmDay(1, calendar, getAlarmActionPendingIntent(), alarmManager);

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

class  Click extends NewOrChangedAlarm implements View.OnClickListener{

    @SuppressLint({"ResourceAsColor", "UseCompatLoadingForDrawables", "NonConstantResourceId", "DefaultLocale"})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.backToMain:
                finish();
                break;
        }
    }

}




