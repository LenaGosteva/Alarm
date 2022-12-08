package com.example.alarm;

import android.net.Uri;

import java.io.Serializable;

public class CreateNewAlarm implements Serializable {

    public float minute;
    public int vol, id;
    public String timeName = " ", days;
    public long time;
    public boolean vib;
    public boolean more;
    transient public Uri music;
    public String textMessange;

    public CreateNewAlarm (float minute, int id, int vol, String textMessange,
                           String timeName, String days, long time, Uri music, boolean more, boolean vib){
        this.id = id;
        this.days = days;
        this.music = music;
        this.vol = vol;
        this.textMessange = textMessange;
        this.timeName = timeName;
        this.minute = minute;
        this.time = time;
        this.more = more;
        this.vib = vib;
    }

}
