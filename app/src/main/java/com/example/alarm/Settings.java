package com.example.alarm;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.PopupMenu;
import android.widget.Toast;

public class Settings extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }


    public void music(View view) {
        Intent intent = new Intent(Settings.this, Music.class);
        startActivity(intent);
    }
    public void back(View view) {
        Intent intent = new Intent(Settings.this, MainActivity.class);
        startActivity(intent);}
}