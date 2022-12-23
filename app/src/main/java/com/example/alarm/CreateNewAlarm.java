package com.example.alarm;

import static com.example.alarm.Settings.uriOfMusic;

import android.media.RingtoneManager;
import android.net.Uri;

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
    transient public String music;
    public String textMessange;

    public CreateNewAlarm(int minute, int vol, int hours,
                          int minutes, String timeName,
                          String days,
                          long time, int id,
                          boolean vib, boolean on, boolean today,
                          boolean monday, boolean tuesday, boolean wednesday, boolean thursday, boolean friday,
                          boolean saturday, boolean sunday, boolean more, String music,
                          String textMessange) {
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
        this.music = String.valueOf(music);
        this.textMessange = textMessange;
    }

    public CreateNewAlarm (String timeName, int minute, int id, int vol, String textMessange,
                           String days, long time, String music, boolean more, boolean vib, int hours, int minutes){
        this.id = id;
        this.days = days;
        this.on = true;
        this.today = false;
        this.music = music;
        this.vol = vol;
        this.saturday = false;
        this.thursday = false;
        this.monday = true;
        this.wednesday = false;
        this.sunday = false;
        this.friday = false;
        this.tuesday = false;
        this.timeName = timeName;
        this.textMessange = textMessange;
        this.minute = minute;
        this.time = time;
        this.more = more;
        this.vib = vib;
        this.hours = hours;
        this.minutes = minutes;

    }
    public CreateNewAlarm(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY, 12);
        c.set(Calendar.MINUTE, 30);
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        this.id = (int)System.currentTimeMillis();
        this.days = "Tomorrow";
        this.saturday = false;
        this.thursday = false;
        this.monday = false;
        this.wednesday = false;
        this.sunday = false;
        this.friday = false;
        this.tuesday = false;
        this.on = true;
        this.music = String.valueOf(uriOfMusic);
        this.vol = 67;

        this.textMessange = "Hello";
        this.minute = 2;
        this.time = 2;
        this.timeName = sdf.format(c.getTime());
    }
}
