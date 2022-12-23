package com.example.alarm;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alarm.databinding.ActivitySettingsBinding;

public class Settings extends AppCompatActivity {
    ActivitySettingsBinding binding;
    public static Uri uriOfMusic = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALL);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.save.setOnClickListener(save -> {

            SharedPreferences.Editor ed = getSharedPreferences("test", Context.MODE_PRIVATE).edit();

                ed.apply();
                Toast.makeText(this, "Настройки по умолчанию сохранены", Toast.LENGTH_SHORT).show();

        });

        binding.theme.setOnClickListener(b ->{
//            setTheme(binding.theme.isChecked() ? R.style.Theme_Alarm: R.style.Theme_Alarm_Night);
        });

        binding.ringtone.setOnClickListener(v ->{
            Intent intent = new Intent("android.intent.action.RINGTONE_PICKER");
            startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Audio.Media.EXTERNAL_CONTENT_URI), 1);
        });

    }

    public void back(View view) {finish();}
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 1:
                    Uri uri = intent.getData();
                    uriOfMusic = uri;
                    binding.nameOfCheckedMusic.setText(uri.getPath());
                    break;
            }
        } else {
            uriOfMusic = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALL);
        }
    }
}