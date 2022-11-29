package com.example.alarm;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.alarm.databinding.ActivityMainBinding;

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    Calendar date;
    Intent intentNew;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        super.onCreate(savedInstanceState);

        Click click = new Click();
        binding.settings.setOnClickListener(click);
        binding.plusMain.setOnClickListener(click);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {

            date = Calendar.getInstance();
            binding.textt.setText(sdf.format(date.getTime()));
        }, 0, 1, TimeUnit.MILLISECONDS);


}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intentNew);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 3:
                    CreateNewAlarm alarm = (CreateNewAlarm) getIntent().getSerializableExtra("NEW");
                    Toast.makeText(this, alarm.textMessange, Toast.LENGTH_SHORT).show();
            }
        }
    }


    public class Click implements View.OnClickListener{

        @SuppressLint({"ResourceAsColor", "UseCompatLoadingForDrawables", "NonConstantResourceId", "DefaultLocale"})
        @Override
        public void onClick(View view){
            switch (view.getId()){
                case R.id.settings:
                    Intent intentSettings = new Intent(MainActivity.this, Settings.class);
                    startActivity(intentSettings);
                    break;

                case R.id.plusMain:

                    intentNew = new Intent(MainActivity.this, NewOrChangedAlarm.class);
                    startActivityForResult(intentNew, 3);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + view.getId());
            }
        }

    }

}