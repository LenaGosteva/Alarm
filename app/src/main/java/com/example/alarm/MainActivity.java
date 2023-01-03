package com.example.alarm;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.alarm.databinding.ActivityMainBinding;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ActivityMainBinding binding;
    SharedPreferences prefs;
    Intent intentNew;
    Adapter createNewAlarmAdapter;
    public static ArrayList<CreateNewAlarm> news = new ArrayList<CreateNewAlarm>() {};
    LinearLayoutManager linearLayoutManager;
    final static int REQUEST_L = 9876;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        prefs = getSharedPreferences("test", Context.MODE_PRIVATE);
        super.onCreate(savedInstanceState);
        createNewAlarmAdapter = new Adapter(MainActivity.this, news);
        linearLayoutManager = new LinearLayoutManager(this);
        binding.recyclerView.setLayoutManager(linearLayoutManager);
        binding.recyclerView.setAdapter(createNewAlarmAdapter);

        Log.d("MUSICINSTOM", Settings.uriOfMusic.toString());

        Click click = new Click();
        binding.settings.setOnClickListener(click);
        binding.plusMain.setOnClickListener(click);



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_L:
                    CreateNewAlarm alarm = (CreateNewAlarm) intent.getSerializableExtra("NEW");
                    int foundedAlarmIndex = -1;
                    for (int i = 0; i < news.size(); i++) {
                        if (news.get(i).id == alarm.id) {
                            foundedAlarmIndex = i;
                            break;
                        }
                    }
                    if (foundedAlarmIndex == -1) {
                        news.add(alarm);
                        foundedAlarmIndex = news.size() - 1;
                    } else {
                        news.set(foundedAlarmIndex, alarm);
                    }
                    binding.recyclerView.getAdapter().notifyItemChanged(foundedAlarmIndex);
                    break;
            }

        }
    }


    public class Click implements View.OnClickListener {

        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.settings:
                    Intent intentSettings = new Intent(MainActivity.this, Settings.class);
                    startActivity(intentSettings);
                    break;
                case R.id.plusMain:
                    intentNew = new Intent(MainActivity.this, NewOrChangedAlarm.class);
                    CreateNewAlarm c = new CreateNewAlarm(Settings.uriOfMusic);
                    Log.d("MUSICINSTOM", c.music);
                    intentNew.putExtra("Cr", c);
                    startActivityForResult(intentNew, REQUEST_L);
                    break;
                default:
                    throw new IllegalStateException("Unexpected value: " + view.getId());
            }
        }

    }

}