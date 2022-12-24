package com.example.alarm;

import static android.app.Activity.RESULT_OK;
import static com.example.alarm.Settings.uriOfMusic;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.core.content.SharedPreferencesKt;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateNewAlarm implements Serializable {
    public int minute;
    public int vol, hours, minutes;
    public String timeName = " ", days;
    public long time;
    public int id;
    public boolean vib;
    public boolean on;
    public boolean today;
    public boolean monday;
    public boolean tuesday;
    public boolean wednesday;
    public boolean thursday;
    public boolean friday,saturday, sunday;
    public boolean more;
    public String music;
    public String textMessange;
    public boolean alarmCanPlay;


    public CreateNewAlarm(int minute, int vol, int hours,
                          int minutes, String timeName,
                          String days,
                          long time, int id,
                          boolean vib, boolean on, boolean today,
                          boolean monday, boolean tuesday, boolean wednesday, boolean thursday, boolean friday,
                          boolean saturday, boolean sunday, boolean more, String music,
                          String textMessange, boolean alarmCanPlay) {
        this.minute = minute;
        this.vol = vol;
        this.hours = hours;
        this.minutes = minutes;
        this.timeName = timeName;
        this.days = days;
        this.time = time;
        this.id = id;
        this.vib = vib;
        this.on = on;
        this.today = today;
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
        this.more = more;
        this.music = music;
        this.textMessange = textMessange;
        this.alarmCanPlay = alarmCanPlay;
    }


    public CreateNewAlarm(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 12);
        c.set(Calendar.MINUTE, 30);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

        this.id = (int)System.currentTimeMillis();
        this.alarmCanPlay = false;
        this.days = "Tomorrow";
        this.saturday = false;
        this.thursday = false;
        this.monday = false;
        this.wednesday = false;
        this.sunday = false;
        this.friday = false;
        this.tuesday = false;
        this.on = true;
        this.music = uriOfMusic.toString();
        this.vol = 67;

        this.textMessange = "Hello";
        this.minute = 2;
        this.time = 2;
        this.timeName = sdf.format(c.getTime());
    }
    public CreateNewAlarm(Uri music){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 12);
        c.set(Calendar.MINUTE, 30);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());

        this.id = (int)System.currentTimeMillis();
        this.alarmCanPlay = false;
        this.days = "Tomorrow";
        this.saturday = false;
        this.thursday = false;
        this.monday = false;
        this.wednesday = false;
        this.sunday = false;
        this.friday = false;
        this.tuesday = false;
        this.on = true;
        this.music = music.toString();
        this.vol = 67;

        this.textMessange = "Hello";
        this.minute = 2;
        this.time = 2;
        this.timeName = sdf.format(c.getTime());
    }

}
