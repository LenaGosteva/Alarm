package com.example.alarm;

import java.io.Serializable;

public class CreateNewAlarm implements Serializable {

    public float minute;
    public int vol;
    public String time;
    public boolean vib;
    public boolean more;
    public int music;
    public String textMessange;

    CreateNewAlarm (float vol, float minute, boolean vib, boolean more){
        this.vol = NewOrChangedAlarm.progress;
        this.minute = NewOrChangedAlarm.progressM;
        this.vib = NewOrChangedAlarm.isValumeCanVibr;
        this.more = NewOrChangedAlarm.isValumeIncreasingGradually;
        this.textMessange = "";
        this.music = NewOrChangedAlarm.CheckedMusic;
    }
}
