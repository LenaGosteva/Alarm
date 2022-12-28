package com.example.alarm;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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

        final CreateNewAlarm alarm = list.get(position);
        holder.time.setText(list.get(position).timeName);
        holder.OnOff.setChecked(list.get(position).on);
        holder.message.setText(list.get(position).textMessange);
        holder.days.setText(list.get(position).days);
        holder.OnOff.setOnClickListener(b ->{
            alarm.on = holder.OnOff.isChecked();
            Log.d("ONTRUEAD", String.valueOf(list.get(position).on));
            Log.d("ONTRUEAD", String.valueOf(list.get(position).id));


        });

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(activity, NewOrChangedAlarm.class);
            intent.putExtra("Cr", alarm);
            activity.startActivityForResult(intent, REQUEST_L);
        });
        holder.itemView.setOnLongClickListener(lon ->{
            int index = list.indexOf(alarm);
            if (index < 0) return false;
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage("Удалить будильник?");

            builder.setPositiveButton("ДА", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                    list.remove(alarm);
                    notifyItemRemoved(index);
                }
            });
            builder.setNegativeButton("НЕТ", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    dialog.dismiss();
                }
            });
            AlertDialog alert = builder.create();
            alert.show();
            return true;


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