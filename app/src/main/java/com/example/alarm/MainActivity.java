package com.example.alarm;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import org.jetbrains.annotations.Nullable;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    TextView text;
    public static Calendar date;
    Button plusmain, settings;
    Intent intentNew;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        Click click = new Click();
        plusmain = findViewById(R.id.plusMain);
        settings = findViewById(R.id.settings);
        settings.setOnClickListener(click);
        plusmain.setOnClickListener(click);
        text = findViewById(R.id.textt);

        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        Executors.newSingleThreadScheduledExecutor().scheduleAtFixedRate(() -> {

            date = Calendar.getInstance();
            text.setText(sdf.format(date.getTime()));
        }, 0, 1, TimeUnit.MILLISECONDS);


}
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent intent) {
        super.onActivityResult(requestCode, resultCode, intentNew);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case 3:
                    CreateNewAlarm newA = (CreateNewAlarm) intent.getSerializableExtra("NEW");

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