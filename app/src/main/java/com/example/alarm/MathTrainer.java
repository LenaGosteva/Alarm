package com.example.alarm;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.alarm.databinding.MathTrainerBinding;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MathTrainer extends AppCompatActivity {
    private final Problem problem = new Problem();

    private @NonNull
    MathTrainerBinding binding;
    boolean fl = false;
    int counter = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        fl = false;
        super.onCreate(savedInstanceState);
        binding = MathTrainerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        gener();
        click click = new click();
        binding.next.setOnClickListener(click);
        binding.text.setOnClickListener(click);
        binding.text1.setOnClickListener(click);
        binding.text2.setOnClickListener(click);
    }

    @SuppressLint("DefaultLocale")
    private void gener(){

        int pos = problem.getRandom(1, 4);
        binding.problem.setText(problem.getProblem());
        float a = problem.getNotResult();
        float b = problem.getNotResult();
        if (a != problem.getResult() && a == b){
            b = a + problem.getRandom(-10, 0);
            if (b == problem.getResult()) b = b + problem.getRandom(12, 15);
        }
        if (a == problem.getResult() && a == b){
            a = a + problem.getRandom(-10, -1);
            b = b + problem.getRandom(12, 15);
        }

        switch (pos){
            case 1:
                binding.text1.setText(String.format("%.2f", problem.getResult()));
                binding.text.setText(String.format("%.2f", a));
                binding.text2.setText(String.format("%.2f", b));
                break;
            case 2:

                binding.text2.setText(String.format("%.2f", problem.getResult()));
                binding.text1.setText(String.format("%.2f", b));
                binding.text.setText(String.format("%.2f", a));
                break;
            case 3:
                binding.text.setText(String.format("%.2f", problem.getResult()));
                binding.text1.setText(String.format("%.2f", a));
                binding.text2.setText(String.format("%.2f", b));
                break;
        }
    }
    class click implements View.OnClickListener{

        @SuppressLint({"ResourceAsColor", "UseCompatLoadingForDrawables", "NonConstantResourceId", "DefaultLocale"})
        @Override
        public void onClick(View view){
            switch (view.getId()){
                case R.id.next:

                    if(fl) {
                        binding.text.setBackground(getDrawable(R.drawable.buttons));
                        binding.text1.setBackground(getDrawable(R.drawable.buttons));
                        binding.text2.setBackground(getDrawable(R.drawable.buttons));
                        gener();
                        counter = 3;
                    }
                    else{
                        Toast.makeText(MathTrainer.this, "Вы не можете нажать эту кнопку, введите ответ!", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.text1:
                case R.id.text:
                case R.id.text2:
                    String text =  ((TextView)view).getText().toString();
                    if(text.equals(String.format("%.2f",problem.getResult()))){
                        view.setBackground(getDrawable(R.drawable.buttons_true));
                        fl = true;
                    }else{
                        view.setBackground(getDrawable(R.drawable.buttons_false));
                        fl = false;

                        counter--;
                        if (counter>0){
                            Toast.makeText(MathTrainer.this, "У Вас осталось "+ counter+" попыток", Toast.LENGTH_SHORT).show();
                        }
                        if (counter==0){
                            Toast.makeText(MathTrainer.this, "У Вас кончились жизни \n Попробуйте другой пример", Toast.LENGTH_SHORT).show();
                            gener();
                        }
                        final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                        executorService.schedule(() -> {
                            binding.text.setBackground(getDrawable(R.drawable.buttons));
                            binding.text1.setBackground(getDrawable(R.drawable.buttons));
                            binding.text2.setBackground(getDrawable(R.drawable.buttons));
                        }, 1, TimeUnit.SECONDS);
                    }


                    break;
            }
        }
    }
}