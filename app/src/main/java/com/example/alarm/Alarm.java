package com.example.alarm;

import static com.example.alarm.newA.vibr;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;



public class Alarm extends AppCompatActivity {
    public static Ringtone ringtone;

    TextView textView;
    Calendar date;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);



        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                textView = findViewById(R.id.text);
                date = Calendar.getInstance();
                textView.setTextAlignment(View.TEXT_ALIGNMENT_GRAVITY);//вывод текста
                textView.setText(String.valueOf(date.get(Calendar.HOUR_OF_DAY))+":"+(date.get(Calendar.MINUTE)));
            }
        }, 0, 1, TimeUnit.MILLISECONDS);



        Uri notif = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        ringtone = RingtoneManager.getRingtone(this, notif);

        if (ringtone == null) {
            notif = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            ringtone = RingtoneManager.getRingtone(this, notif);
        }
        if (ringtone != null) {
            ringtone.play();
        }
        if(vibr){
            Intent intentVibrate = new Intent(getApplicationContext(),VibrateService.class);
            startService(intentVibrate);
        }

    }
    public void off(View view) {
        if (ringtone != null && ringtone.isPlaying()) {
            ringtone.stop();
        }
        super.onDestroy();
    }
//    protected static String beautiful(String s){
//        String stmin = "";
//        if (s.length() < 2){
//            stmin= "0" + stmin;
//        }
//        return stmin;
//    }

}