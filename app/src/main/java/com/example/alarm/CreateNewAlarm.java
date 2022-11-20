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

    CreateNewAlarm (){
        this.vol = NewOrChangedAlarm.progress;
        this.minute = NewOrChangedAlarm.progressM;
        this.vib = true;
        this.more = NewOrChangedAlarm.isValumeIncreasingGradually;
        this.textMessange = "";
        this.music = NewOrChangedAlarm.CheckedMusic;
    }

}
