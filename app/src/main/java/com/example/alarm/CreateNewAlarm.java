package com.example.alarm;

import android.media.RingtoneManager;
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
                           String days, long time, Uri music, boolean more, boolean vib){
        this.id = id;
        this.days = days;
        this.music = music;
        this.vol = vol;
        this.textMessange = textMessange;
        this.minute = minute;
        this.time = time;
        this.more = more;
        this.vib = vib;
    }
    public CreateNewAlarm(){
        this.id = 1244555;
        this.days = "TH FR";
        this.music = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
        this.vol = 67;
        this.textMessange = "Hello";
        this.minute = 123112;
        this.time = 123456;
        this.timeName = "11:02";
    }

}
