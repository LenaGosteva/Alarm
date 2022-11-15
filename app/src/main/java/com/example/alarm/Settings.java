package com.example.alarm;

import static java.lang.Boolean.getBoolean;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.Toast;

public class Settings extends AppCompatActivity {
    Switch vib;
    Button save;
    SharedPreferences mSharedPref;
    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        save = findViewById(R.id.save);
        setContentView(R.layout.activity_settings);
        vib = findViewById(R.id.vibrationS);


        vib.setChecked(getBoolean("vibr"));
        vib.setOnClickListener(t -> {
            newA.vibr = true;
        });

    }

    protected boolean bool() {
        if(getIntent().getBooleanExtra("vibration", newA.vibr)) return true;
        return false;
    }

    public void music(View view) {
        Intent intent = new Intent(Settings.this, Music.class);
        startActivity(intent);
    }
    public void back(View view) {
        Intent intent = new Intent(Settings.this, MainActivity.class);
        startActivity(intent);}

    public void save(View view) {
        mSharedPref = getPreferences(MODE_PRIVATE);
            SharedPreferences.Editor mEditor = mSharedPref.edit();
            mEditor.putBoolean("vibr", newA.vibr);
            mEditor.commit();
//
//            intent = new Intent(Settings.this, newA.class);
//            intent.putExtra("vibration", newA.vibr);
            Intent intentA = new Intent(Settings.this, MainActivity.class);
            startActivity(intentA);
    }
    @Override
    protected void onPause() {

        mSharedPref = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPref.edit();
        mEditor.putBoolean("vibr", newA.vibr);
        mEditor.apply();
        super.onPause();
    }

}