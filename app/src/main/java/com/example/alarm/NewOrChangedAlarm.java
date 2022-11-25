package com.example.alarm;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
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
    public static EditText text;
    Button setAlarm;
    Button plus;
    @SuppressLint({"StaticFieldLeak", "UseSwitchCompatOrMaterialCode"})
    protected static Switch vibNew, loudNew;
    SeekBar volume, minute;
    protected boolean alarm;
    AudioManager audioManager;
    protected static int progressM = 2, progress;
    Button music;
    AlarmManager alarmManager;
    AlarmManager.AlarmClockInfo alarmClockInfo;
    int curValue;
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

        Monday = findViewById(R.id.line1);
        Tuesday = findViewById(R.id.line2);
        Wednesday = findViewById(R.id.line3);
        Thursday = findViewById(R.id.line4);
        Friday = findViewById(R.id.line5);
        Saturday = findViewById(R.id.line6);
        Sunday = findViewById(R.id.line7);

        Click click = new Click();
        music = findViewById(R.id.musicButton);
//        music.setOnClickListener(click);
        music.setOnClickListener(h ->{
            intent = new Intent("android.intent.action.RINGTONE_PICKER");
            intent.setType("audio/mpeg");
            startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI), 1);
        });


        sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

        newAlarm = new CreateNewAlarm();


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

        text = findViewById(R.id.textMessage);
        setAlarm = findViewById(R.id.alarm_button);
//        click.onClick(setAlarm);
        setAlarm.setOnClickListener(click);


        vibNew = findViewById(R.id.vibration);


        loudNew = findViewById(R.id.moreLoud);



        plus = findViewById(R.id.createdNewAlarm);
        plus.setOnClickListener(click);


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

                    CheckedMusic = MediaPlayer.create(NewOrChangedAlarm.this, uri);
                    nameOfMusic = findViewById(R.id.nameOfCheckedMusic);
                    nameOfMusic.setText(uri.getEncodedUserInfo());
                    break;
            }
        }
        else{
            Uri notificationUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            CheckedMusic = MediaPlayer.create(this, notificationUri);
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
            case R.id.alarm_button:
                MaterialTimePicker materialTimePicker = new MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .setHour(12)
                        .setMinute(0)
                        .setTitleText("Выберите время для будильника")
                        .build();

                materialTimePicker.addOnPositiveButtonClickListener(view -> {
                    calendar = Calendar.getInstance();
                    calendar.set(Calendar.SECOND, 0);
                    calendar.set(Calendar.MILLISECOND, 0);
                    calendar.set(Calendar.MINUTE, materialTimePicker.getMinute());
                    calendar.set(Calendar.HOUR_OF_DAY, materialTimePicker.getHour());
                    newAlarm.timeName = sdf.format(calendar.getTime());
                    alarmManager= (AlarmManager) getSystemService(Context.ALARM_SERVICE);

                    alarmClockInfo= new AlarmManager.AlarmClockInfo(calendar.getTimeInMillis(), getAlarmInfoPendingIntent());
                    alarm = true;
                    setAlarm.setTextSize(90);
                    setAlarm.setText(sdf.format(calendar.getTime()));
                    Toast.makeText(this, "Будильник установлен на " + sdf.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
                });

                materialTimePicker.show(getSupportFragmentManager(), "tag_picker");
                break;
            case R.id.backToMain:
                finish();
                break;
            case R.id.createdNewAlarm:
                if (alarm) {
                    progressM = minute.getProgress();
                    progress = volume.getProgress();
                    newAlarm.time = calendar.getTimeInMillis();
                    newAlarm.timeName= sdf.format(calendar.getTime());
                    newAlarm.more = loudNew.isChecked();
                    newAlarm.music = CheckedMusic;
                    newAlarm.vib = vibNew.isChecked();
                    newAlarm.vol = progress;
                    newAlarm.textMessange = text.getText().toString();
                    newAlarm.minute = progressM;

                    alarmManager.setAlarmClock(alarmClockInfo, getAlarmActionPendingIntent());
                    Intent intent1 = new Intent();
                    intent1.putExtra("NEW", newAlarm);
                    setResult(RESULT_OK, intent1);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Вы не можете установить будильник без времени", Toast.LENGTH_SHORT).show();
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
               break;
        }
    }

}




