package com.example.alarm;

import android.widget.SeekBar;

public class CreateNewAlarm {

    private float minute;
    public float vol;
    public String time;
    public boolean vib;
    public boolean more;

    CreateNewAlarm (float vol, float minute, boolean vib, boolean more){
        this.vol = Settings.progress;
        this.minute = Settings.progressM;
        this.vib = Settings.vibr;
        this.more = Settings.loudB;
    }
}
