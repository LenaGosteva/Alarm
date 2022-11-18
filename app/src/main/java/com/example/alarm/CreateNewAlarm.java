package com.example.alarm;

import java.io.Serializable;

public class CreateNewAlarm implements Serializable {

    private float minute;
    public float vol;
    public String time;
    public boolean vib;
    public boolean more;

    CreateNewAlarm (float vol, float minute, boolean vib, boolean more){
        this.vol = Settings.progress;
        this.minute = Settings.progressM;
        this.vib = Settings.isValumeCanVibr;
        this.more = Settings.isValumeIncreasingGradually;
    }
}
