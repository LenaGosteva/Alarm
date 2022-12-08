package com.example.alarm;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    Adapter createNewAlarmAdapter;
    ArrayList<CreateNewAlarm> news;
    LinearLayoutManager linearLayoutManager;
    final static int REQUEST_L = 9876;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        super.onCreate(savedInstanceState);


        news = new ArrayList<CreateNewAlarm>(){};

        createNewAlarmAdapter = new Adapter(news);
        linearLayoutManager = new LinearLayoutManager(this);

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
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            switch (requestCode){
                case REQUEST_L:
                    CreateNewAlarm alarm = (CreateNewAlarm) intent.getSerializableExtra("NEW");
                    news.add(alarm);
                    binding.recyclerView.setLayoutManager(linearLayoutManager);
                    binding.recyclerView.setAdapter(createNewAlarmAdapter);
                    break;
            }

        }
    }


    public class Click implements View.OnClickListener{

        public void onClick(View view){
            switch (view.getId()){
                case R.id.settings:
                    Intent intentSettings = new Intent(MainActivity.this, Settings.class);
                    startActivity(intentSettings);
                    break;
                case R.id.plusMain:
                    intentNew = new Intent(MainActivity.this, NewOrChangedAlarm.class);
                    startActivityForResult(intentNew, REQUEST_L);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + view.getId());
            }
        }

    }

}