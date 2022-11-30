package com.example.alarm;

import android.media.MediaPlayer;

import java.io.Serializable;

public class CreateNewAlarm implements Serializable {

    public float minute;
    public int vol;
    public String timeName, days;
    public long time;
    public boolean vib;
    public boolean more;
    public MediaPlayer music;
    public String textMessange;


}
