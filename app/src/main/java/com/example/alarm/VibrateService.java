package com.example.alarm;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.os.Vibrator;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class VibrateService extends Service {
    public static Vibrator vibrator;


    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                vibrator.vibrate(1000);
            }

        } ,1, 2, TimeUnit.SECONDS);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();

    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}