package com.example.alarm;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.alarm.databinding.ActivityMainBinding;

import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    Calendar date;
    Intent intentNew;
    ArrayList<CreateNewAlarm> news;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        news = new ArrayList<CreateNewAlarm>(){};
        super.onCreate(savedInstanceState);

        Adapter createNewAlarmAdapter = new Adapter(this, news);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.recyclerView.setAdapter(createNewAlarmAdapter);


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
                    CreateNewAlarm alarm = (CreateNewAlarm) getIntent().getSerializableExtra("NEW");
                    news.add(alarm);
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