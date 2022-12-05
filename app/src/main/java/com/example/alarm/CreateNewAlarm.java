package com.example.alarm;

import android.net.Uri;

import java.io.Serializable;

public class CreateNewAlarm implements Serializable {

    public float minute;
    public int vol, id;
    public String timeName, days;
    public long time;
    public boolean vib;
    public boolean more;
    public Uri music;
    public String textMessange;


}
