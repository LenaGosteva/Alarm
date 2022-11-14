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

public class newA extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new);
    }

    public void music(View view) {
        Intent intent = new Intent(newA.this, Music.class);
        startActivity(intent);
    }

    public void back(View view) {
        Intent intent = new Intent(newA.this, Settings.class);
        startActivity(intent);
    }
}