package com.example.alarm;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.CreateNewAlarmViewHolder>{
    ArrayList<CreateNewAlarm> list;
    TextView time, days, message;
    Switch OnOff;
    public Adapter(MainActivity mainActivity, ArrayList<CreateNewAlarm> news) {
        this.list = news;
    }

    @NonNull
    @Override
    public Adapter.CreateNewAlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item,  parent, false);
        CreateNewAlarmViewHolder c = new CreateNewAlarmViewHolder(view);
        return c;
    }

    @Override
    public void onBindViewHolder(@NonNull Adapter.CreateNewAlarmViewHolder holder, int position) {
        time.setText(list.get(position).timeName);
        message.setText(list.get(position).textMessange);
        days.setText(list.get(position).days);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    public class CreateNewAlarmViewHolder extends RecyclerView.ViewHolder{
        public CreateNewAlarmViewHolder(@NonNull View itemView) {
            super(itemView);
            time = itemView.findViewById(R.id.time);
            message = itemView.findViewById(R.id.textM);
            days = itemView.findViewById(R.id.days);



        }
    }
}