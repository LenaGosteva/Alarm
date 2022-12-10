package com.example.alarm;

import android.media.RingtoneManager;
import android.net.Uri;

import java.io.Serializable;

public class CreateNewAlarm implements Serializable {

    public int minute;
    public int vol, hours, minutes;
    public String timeName = " ", days;
    public long time, id;
    public boolean vib;
    public boolean more;
    transient public Uri music = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALL);
    public String textMessange;

    public CreateNewAlarm (String timeName, int minute, long id, int vol, String textMessange,
                           String days, long time, Uri music, boolean more, boolean vib, int hours, int minutes){
        this.id = id;
        this.days = days;
        this.music = music;
        this.vol = vol;
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
        this.id = System.currentTimeMillis();
        this.days = " ";
        this.music = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALL);
        this.vol = 67;
        this.textMessange = "Hello";
        this.minute = 2;
        this.time = 123456;
        this.timeName = "";
    }

}
