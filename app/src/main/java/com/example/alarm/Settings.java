package com.example.alarm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.example.alarm.databinding.ActivitySettingsBinding;

public class Settings extends AppCompatActivity {
    ActivitySettingsBinding binding;
    public boolean th = false;
    public String music;
    public static Uri uriOfMusic = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPreferences.Editor ed = getSharedPreferences("test", Context.MODE_PRIVATE).edit();
        SharedPreferences preferences = getSharedPreferences("test", Context.MODE_PRIVATE);

        uriOfMusic = Uri.parse(preferences.getString("NAME_OF_MUSIC", uriOfMusic.toString()));
        binding.nameOfCheckedMusic.setText(uriOfMusic.getPath());
        Log.e("DFGHJK1", String.valueOf(preferences.getBoolean("theme", false)));

        binding.theme.setChecked(preferences.getBoolean("theme", false));
        binding.save.setOnClickListener(save -> {
            ed.putString("NAME_OF_MUSIC", music);
            ed.apply();
            Toast.makeText(this, "Настройки по умолчанию сохранены", Toast.LENGTH_SHORT).show();

        });

        binding.theme.setOnClickListener(b ->{
            AppCompatDelegate.setDefaultNightMode((binding.theme.isChecked())?AppCompatDelegate.MODE_NIGHT_YES:AppCompatDelegate.MODE_NIGHT_NO);
//            th = binding.theme.isChecked()?true:false;
            Log.d("DFGHJK", String.valueOf(binding.theme.isChecked()));
            ed.putBoolean("theme",th = binding.theme.isChecked());
            ed.apply();

        });

        binding.ringtone.setOnClickListener(v ->{
            startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI), 1);
        });

    }

    public void back(View view) {
        Log.d("MUSICINS", uriOfMusic.toString());
        startActivity(new Intent(this, MainActivity.class));

        }
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    uriOfMusic = intent.getData();
                    binding.nameOfCheckedMusic.setText(intent.getData().getPath());
                    music =  uriOfMusic.toString();
                    break;
            }
        } else {
            uriOfMusic = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALL);
        }
    }
}