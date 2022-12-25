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
    public boolean alarm;
    public static boolean vibNew = false, loudNew = false;
    AudioManager audioManager;
    protected static int progressM = 2, progress;
    public String days = "";

    CreateNewAlarm newAlarm, alarmNew;
    SimpleDateFormat sdf;
    Calendar calendar = Calendar.getInstance();
    Intent intent;
    ActivityNewBinding binding;
    public static Uri CheckedMusic = Settings.uriOfMusic;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityNewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        newAlarm = (CreateNewAlarm) getIntent().getSerializableExtra("Cr");
        if (newAlarm == null) {
            newAlarm = new CreateNewAlarm(Settings.uriOfMusic);
        }
        super.onCreate(savedInstanceState);

        final int id = newAlarm.id;

        if (newAlarm.timeName.equals("Настроить время")) {
            binding.alarmButton.setTextSize(30);
        } else {
            binding.alarmButton.setTextSize(90);
        }
        binding.alarmButton.setText(newAlarm.timeName);

        for (CreateNewAlarm i : MainActivity.news) {
            if (id == i.id) {
                CheckedMusic = Uri.parse(i.music);
                break;
            }
        }
        sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

        binding.musicButton.setOnClickListener(h -> {
            intent = new Intent("android.intent.action.RINGTONE_PICKER");
            startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI), 1);
        });


        binding.backToMain.setOnClickListener(f -> {
            finish();
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

        alarm = newAlarm.alarmCanPlay;
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

                newAlarm.hours = materialTimePicker.getHour();
                newAlarm.minutes = materialTimePicker.getMinute();

                binding.alarmButton.setTextSize(90);
                binding.alarmButton.setText(sdf.format(calendar.getTime()));
                alarm = true;

            });

            materialTimePicker.show(getSupportFragmentManager(), "tag_picker");
        });

        binding.line1.setOnClickListener(c -> {
            newAlarm.monday = binding.line1.isChecked();
            if (!days.contains("M ")) days += "M ";
        });
        binding.line2.setOnClickListener(c -> {
            newAlarm.tuesday = binding.line2.isChecked();
            if (!days.contains("TU ")) days += "TU ";
        });
        binding.line3.setOnClickListener(c -> {
            newAlarm.wednesday = binding.line3.isChecked();
            if (!days.contains("W ")) days += "W ";
        });
        binding.line4.setOnClickListener(c -> {
            newAlarm.thursday = binding.line4.isChecked();
            if (!days.contains("TH ")) days += "TH ";

        });
        binding.line5.setOnClickListener(c -> {
            newAlarm.friday = binding.line5.isChecked();
            if (!days.contains("F ")) days += "F ";

        });
        binding.line6.setOnClickListener(c -> {
            newAlarm.saturday = binding.line6.isChecked();
            if (!days.contains("SA ")) days += "SA ";

        });
        binding.line7.setOnClickListener(c -> {
            newAlarm.sunday = binding.line7.isChecked();
            if (!days.contains("SU ")) days += "SU ";

        });
        binding.createdNewAlarm.setOnClickListener(c -> {
            if (alarm) {
                if (days.isEmpty()) days = binding.today.isChecked() ? "Today" : "Tomorrow";
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmNew = new CreateNewAlarm(binding.minuteInt.getProgress(), binding.volumeControl.getProgress(),
                        calendar.getTime().getHours(), calendar.getTime().getMinutes(), binding.alarmButton.getText().toString(),
                        days, calendar.getTimeInMillis(), id, binding.vibration.isChecked(), newAlarm.on, binding.today.isChecked(),
                        newAlarm.monday, newAlarm.tuesday, newAlarm.wednesday, newAlarm.thursday,
                        newAlarm.friday, newAlarm.saturday, newAlarm.sunday, binding.moreLoud.isChecked(),
                        CheckedMusic.toString(), binding.textMessage.getText().toString(), alarm);


                if (!binding.line1.isChecked() && !binding.line2.isChecked()
                        && !binding.line3.isChecked() && !binding.line4.isChecked()
                        && !binding.line5.isChecked() && !binding.line6.isChecked()
                        && !binding.line7.isChecked()) {
                    if (!binding.today.isChecked()) {
                        setAlarm(alarmManager, id, calendar.getTimeInMillis() + 1000 * 60 * 60 * 24);
                    } else if (binding.today.isChecked() && calendar.getTimeInMillis() < System.currentTimeMillis()) {
                        alarm = true;
                        Toast.makeText(this, "Вы не можете установить будильник на сегодня на время " +
                                sdf.format(calendar.getTime()) + ", т.к. это время уже прошло", Toast.LENGTH_SHORT).show();
                    } else if (binding.today.isChecked() && (calendar.getTimeInMillis() >= System.currentTimeMillis())) {

                        setAlarm(alarmManager, id, calendar.getTimeInMillis());
                    }
                }else{
                    if (binding.line1.isChecked()) {
                        AlarmDay(2, getAlarmActionPendingIntent(id), alarmManager);
                    }if (binding.line2.isChecked()) {
                        AlarmDay(3, getAlarmActionPendingIntent(id), alarmManager);
                    }if (binding.line3.isChecked()) {
                        AlarmDay(4, getAlarmActionPendingIntent(id), alarmManager);
                    }if (binding.line4.isChecked()) {
                        AlarmDay(5, getAlarmActionPendingIntent(id), alarmManager);
                    }if (binding.line5.isChecked()) {
                        AlarmDay(6, getAlarmActionPendingIntent(id), alarmManager);
                    }if (binding.line6.isChecked()) {
                        AlarmDay(7, getAlarmActionPendingIntent(id), alarmManager);
                    }if (binding.line7.isChecked()) {
                        Log.d("TIMENAMe", binding.alarmButton.getText().toString());
                        AlarmDay(1, getAlarmActionPendingIntent(id), alarmManager);
                    }
                }

            } else {
                Toast.makeText(this, "Вы не можете установить будильник без времени", Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void AlarmDay(int week, PendingIntent pendingIntent, AlarmManager alarmManager) {
        Calendar c = Calendar.getInstance();
        if (week < Calendar.DAY_OF_WEEK)
            c.set(Calendar.WEEK_OF_YEAR, Calendar.getInstance().get(Calendar.WEEK_OF_YEAR) + 1);
        c.set(Calendar.DAY_OF_WEEK, week);
        c.setTime(calendar.getTime());
        Toast.makeText(this, "Будильник установлен на " + sdf.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
        Intent alarmInfoIntent = new Intent(this, MainActivity.class);
        alarmInfoIntent.putExtra("NEW", alarmNew);
        alarmInfoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);
        setResult(RESULT_OK, alarmInfoIntent);
        finish();
    }

    public void setAlarm(AlarmManager alarmManager, int id, long time) {
        Toast.makeText(this, "Будильник установлен на " + sdf.format(calendar.getTime()), Toast.LENGTH_SHORT).show();
        Intent alarmInfoIntent = new Intent(this, MainActivity.class);
        alarmInfoIntent.putExtra("NEW", alarmNew);
        alarmInfoIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        AlarmManager.AlarmClockInfo alarmClockInfo = new AlarmManager.AlarmClockInfo(time,
                PendingIntent.getActivity(this, id, alarmInfoIntent, PendingIntent.FLAG_UPDATE_CURRENT));
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
        intent.putExtra("ALARM", id);
        intent.putExtra("loud", binding.moreLoud.isChecked());
        intent.putExtra("vibration", binding.vibration.isChecked());
        intent.putExtra("progress", progress);
        intent.putExtra("progressM", progressM);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        return PendingIntent.getActivity(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }


}




