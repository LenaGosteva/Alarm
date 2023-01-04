package com.example.alarm;

import static java.util.Calendar.DAY_OF_MONTH;

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
import android.util.Log;
import android.view.View;
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
    public boolean canPlay;
    AudioManager audioManager;
    static MediaPlayer player;
    protected static int progressM = 2, progress;
    public String days = "";

    CreateNewAlarm newAlarm, alarmNew;
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
    Calendar calendar;
    ActivityNewBinding binding;
    public static Uri CheckedMusic = Settings.uriOfMusic;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        super.onCreate(savedInstanceState);


        player = MediaPlayer.create(getApplicationContext(), R.raw.music);
        newAlarm = (CreateNewAlarm) getIntent().getSerializableExtra("Cr");
        if (newAlarm == null) {
            newAlarm = new CreateNewAlarm(Settings.uriOfMusic);
        }
        final int id = newAlarm.id;
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, newAlarm.hours);
        calendar.set(Calendar.MINUTE, newAlarm.minutes);
        if (newAlarm.timeName.equals("Настроить время")) {
            binding.alarmButton.setTextSize(30);
        } else {
            binding.alarmButton.setTextSize(90);
        }

        for (CreateNewAlarm i : MainActivity.news) {
            if (id == i.id) {
                CheckedMusic = Uri.parse(i.music);
                break;
            }
        }
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        binding.musicButton.setOnClickListener(h -> {
            Intent intent_upload = new Intent();
            intent_upload.setType("audio/*");
            intent_upload.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(intent_upload, 1);
        });


        binding.today.setChecked(newAlarm.today);

        binding.textMessage.setText(newAlarm.textMessange);
        binding.nameOfCheckedMusic.setText(newAlarm.music);

        binding.line1.setChecked(newAlarm.monday);
        binding.line2.setChecked(newAlarm.tuesday);
        binding.line3.setChecked(newAlarm.wednesday);
        binding.line4.setChecked(newAlarm.thursday);
        binding.line5.setChecked(newAlarm.friday);
        binding.line6.setChecked(newAlarm.saturday);
        binding.line7.setChecked(newAlarm.sunday);

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
        foo();

        if (binding.today.isChecked()) {
            binding.repeat.setVisibility(View.GONE);
            binding.mtrlCalendarDaysOfWeek.setVisibility(View.GONE);
        }

        binding.today.setOnClickListener(v -> {
            if (binding.today.isChecked()) {
                binding.repeat.setVisibility(View.GONE);
                binding.mtrlCalendarDaysOfWeek.setVisibility(View.GONE);
                binding.line1.setChecked(false);
                binding.line2.setChecked(false);
                binding.line3.setChecked(false);
                binding.line4.setChecked(false);
                binding.line5.setChecked(false);
                binding.line6.setChecked(false);
                binding.line7.setChecked(false);

            } else {
                binding.repeat.setVisibility(View.VISIBLE);
                binding.mtrlCalendarDaysOfWeek.setVisibility(View.VISIBLE);
            }
        });

        binding.volumeControl.setMin(0);
        binding.volumeControl.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        binding.volumeControl.setProgress(newAlarm.vol);
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

        binding.vibration.setChecked(newAlarm.vib);
        binding.moreLoud.setChecked(newAlarm.more);
        binding.alarmButton.setTextSize(90);
        binding.alarmButton.setText(sdf.format(calendar.getTime()));
        canPlay = newAlarm.alarmCanPlay;


        binding.alarmButton.setOnClickListener(n -> {
            materialTimePicker = new MaterialTimePicker.Builder().setTimeFormat(TimeFormat.CLOCK_24H).
                    setHour(newAlarm.hours).setMinute(newAlarm.minutes).setTitleText("Выберите время для будильника").build();

            materialTimePicker.addOnPositiveButtonClickListener(view -> {

                calendar.set(Calendar.SECOND, 0);
                calendar.set(Calendar.MILLISECOND, 0);
                calendar.set(Calendar.MINUTE, materialTimePicker.getMinute());
                calendar.set(Calendar.HOUR_OF_DAY, materialTimePicker.getHour());

                newAlarm.hours = materialTimePicker.getHour();
                newAlarm.minutes = materialTimePicker.getMinute();

                binding.alarmButton.setTextSize(90);
                binding.alarmButton.setText(sdf.format(calendar.getTime()));
                newAlarm.alarm = true;

            });

            materialTimePicker.show(getSupportFragmentManager(), "tag_picker");
        });

        binding.line1.setOnClickListener(c -> {
            newAlarm.monday = binding.line1.isChecked();
            foo();
        });
        binding.line2.setOnClickListener(c -> {
            newAlarm.tuesday = binding.line2.isChecked();
            foo();
        });
        binding.line3.setOnClickListener(c -> {
            newAlarm.wednesday = binding.line3.isChecked();
            foo();
        });
        binding.line4.setOnClickListener(c -> {
            newAlarm.thursday = binding.line4.isChecked();
            foo();
        });
        binding.line5.setOnClickListener(c -> {
            newAlarm.friday = binding.line5.isChecked();
            foo();
        });
        binding.line6.setOnClickListener(c -> {
            newAlarm.saturday = binding.line6.isChecked();
            foo();
        });
        binding.line7.setOnClickListener(c -> {
            newAlarm.sunday = binding.line7.isChecked();
            foo();
        });


        binding.createdNewAlarm.setOnClickListener(c -> {
            if (newAlarm.alarm) {
                if (binding.line1.isChecked() && !days.contains("M ")) days += "M ";
                if (binding.line2.isChecked() && !days.contains("TU ")) days += "TU ";
                if (binding.line3.isChecked() && !days.contains("W ")) days += "W ";
                if (binding.line4.isChecked() && !days.contains("TH ")) days += "TH ";
                if (binding.line5.isChecked() && !days.contains("F ")) days += "F ";
                if (binding.line6.isChecked() && !days.contains("SA ")) days += "SA ";
                if (binding.line7.isChecked() && !days.contains("SU ")) days += "SU ";
                if (days.isEmpty()) days = binding.today.isChecked() ? "Today" : "Tomorrow";
                alarmNew = new CreateNewAlarm(binding.minuteInt.getProgress(), binding.volumeControl.getProgress(),
                        calendar.getTime().getHours(), calendar.getTime().getMinutes(),
                        binding.alarmButton.getText().toString(), days,
                        calendar.getTimeInMillis(), id,
                        binding.vibration.isChecked(), newAlarm.on,
                        binding.today.isChecked(),
                        newAlarm.monday, newAlarm.tuesday, newAlarm.wednesday, newAlarm.thursday, newAlarm.friday, newAlarm.saturday, newAlarm.sunday,
                        binding.moreLoud.isChecked(), CheckedMusic.toString(), binding.textMessage.getText().toString(), canPlay, newAlarm.alarm);


                if (!binding.line1.isChecked() && !binding.line2.isChecked()
                        && !binding.line3.isChecked() && !binding.line4.isChecked()
                        && !binding.line5.isChecked() && !binding.line6.isChecked()
                        && !binding.line7.isChecked()) {
                    if (!binding.today.isChecked()) {
                        setAlarm(alarmManager, id, calendar.getTimeInMillis() + 1000 * 60 * 60 * 24);
                    } else if (binding.today.isChecked() && calendar.getTimeInMillis() < System.currentTimeMillis()) {
                        newAlarm.alarm = true;
                        Toast.makeText(this, "Вы не можете установить будильник на сегодня на время " + sdf.format(calendar.getTime()) + ", т.к. это время уже прошло", Toast.LENGTH_SHORT).show();
                    } else if (binding.today.isChecked() && (calendar.getTimeInMillis() >= System.currentTimeMillis())) {
                        Log.e("DFGJ", NewOrChangedAlarm.CheckedMusic.toString());
                        setAlarm(alarmManager, id, calendar.getTimeInMillis());
                    }
                } else {
                    if (binding.line1.isChecked()) {
                        AlarmDay(2, getAlarmActionPendingIntentToRepeating(id), alarmManager);
                    }
                    if (binding.line2.isChecked()) {
                        AlarmDay(3, getAlarmActionPendingIntentToRepeating(id), alarmManager);
                    }
                    if (binding.line3.isChecked()) {
                        AlarmDay(4, getAlarmActionPendingIntentToRepeating(id), alarmManager);
                    }
                    if (binding.line4.isChecked()) {
                        AlarmDay(5, getAlarmActionPendingIntentToRepeating(id), alarmManager);
                    }
                    if (binding.line5.isChecked()) {
                        AlarmDay(6, getAlarmActionPendingIntentToRepeating(id), alarmManager);
                    }
                    if (binding.line6.isChecked()) {
                        AlarmDay(7, getAlarmActionPendingIntentToRepeating(id), alarmManager);
                    }
                    if (binding.line7.isChecked()) {
                        AlarmDay(1, getAlarmActionPendingIntentToRepeating(id), alarmManager);
                    }
                }

            } else {
                Toast.makeText(this, "Установите время", Toast.LENGTH_SHORT).show();
            }
        });


        binding.backToMain.setOnClickListener(f -> {
            finish();
        });

    }

    public void foo() {

        if (newAlarm.sunday || newAlarm.saturday || newAlarm.friday || newAlarm.thursday || newAlarm.wednesday || newAlarm.tuesday || newAlarm.monday) {
            binding.today.setVisibility(View.GONE);
            newAlarm.today = false;
        }else{
            binding.today.setVisibility(View.VISIBLE);
        }
    }


    public void AlarmDay(int week, PendingIntent pendingIntent, AlarmManager alarmManager) {
        if (week < Calendar.DAY_OF_WEEK && calendar.getTimeInMillis() < System.currentTimeMillis()) {
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(DAY_OF_MONTH) + 7, newAlarm.hours, newAlarm.minutes);
        } else {
            calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(DAY_OF_MONTH), newAlarm.hours, newAlarm.minutes);
            Toast.makeText(this, "Будильник установлен на " + sdf.format(calendar.getTime()), Toast.LENGTH_SHORT).show();

        }


        Intent alarmInfoIntent = new Intent(this, MainActivity.class);
        alarmInfoIntent.putExtra("NEW", alarmNew);
        alarmInfoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
        setResult(RESULT_OK, alarmInfoIntent);
        finish();
    }

    public void setAlarm(AlarmManager alarmManager, int id, long time) {
        Toast.makeText(this, "Будильник установлен на " + sdf.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
        Intent alarmInfoIntent = new Intent(this, MainActivity.class);
        alarmInfoIntent.putExtra("NEW", alarmNew);
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
                    binding.nameOfCheckedMusic.setText(String.format("%s", uri));
                    player = MediaPlayer.create(this, uri);

                    break;
            }
        } else {
            player = MediaPlayer.create(this, R.raw.music);
        }
    }


    PendingIntent getAlarmActionPendingIntent(int id) {
        Intent intent = new Intent(this, Alarm.class);
        intent.putExtra("Message", binding.textMessage.getText().toString());
        intent.putExtra("ALARM", id);
        intent.putExtra("loud", binding.moreLoud.isChecked());
        intent.putExtra("vibration", binding.vibration.isChecked());
        intent.putExtra("progress", progress);
        intent.putExtra("progressM", progressM);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    PendingIntent getAlarmActionPendingIntentToRepeating(int id) {
        Intent intent = new Intent(this, Alarm.class);
        intent.putExtra("Message", binding.textMessage.getText().toString());
        intent.putExtra("ALARM", id);
        intent.putExtra("loud", binding.moreLoud.isChecked());
        intent.putExtra("vibration", binding.vibration.isChecked());
        intent.putExtra("progress", progress);
        intent.putExtra("progressM", progressM);
        return PendingIntent.getActivity(this, id, intent, PendingIntent.FLAG_CANCEL_CURRENT);
    }

}




