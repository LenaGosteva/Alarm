package com.example.alarm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.CreateNewAlarmViewHolder> {
    ArrayList<CreateNewAlarm> list;
    Activity activity;
    final static int REQUEST_L = 9876;


    public Adapter(Activity activity, ArrayList<CreateNewAlarm> news) {
        this.activity = activity;
        this.list = news;
    }

    @NonNull
    @Override
    public Adapter.CreateNewAlarmViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        CreateNewAlarmViewHolder c = new CreateNewAlarmViewHolder(view);
        return c;
    }

    @Override
    public void onBindViewHolder(Adapter.CreateNewAlarmViewHolder holder,
                                 @SuppressLint("RecyclerView") int position) {
        Log.d("ONTRUEAD", String.valueOf(list.get(position).timeName));

        holder.time.setText(list.get(position).timeName);
        holder.OnOff.setChecked(list.get(position).on);
        holder.message.setText(list.get(position).textMessange);
        holder.days.setText(list.get(position).days);
        holder.OnOff.setOnClickListener(b ->{
            list.get(position).on = holder.OnOff.isChecked();
            Log.d("ONTRUEAD", String.valueOf(list.get(position).on));
            Log.d("ONTRUEAD", String.valueOf(list.get(position).id));


        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(activity, NewOrChangedAlarm.class);
            intent.putExtra("Cr", list.get(position));
            activity.startActivityForResult(intent, REQUEST_L);
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CreateNewAlarmViewHolder extends RecyclerView.ViewHolder {
        TextView time, days, message;
        Switch OnOff;

        public CreateNewAlarmViewHolder(View itemView) {
            super(itemView);
            OnOff = itemView.findViewById(R.id.OnOff);
            time = itemView.findViewById(R.id.time);
            message = itemView.findViewById(R.id.textM);
            days = itemView.findViewById(R.id.days);

        }
    }

    public class Click implements View.OnClickListener {

        @Override
        public void onClick(View v) {

        }
    }
}