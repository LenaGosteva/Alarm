package com.example.alarm;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
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
    Context context;

    public Adapter(Context context, ArrayList<CreateNewAlarm> news) {
        this.context = context;
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
    public void onBindViewHolder(Adapter.CreateNewAlarmViewHolder holder,
                                 @SuppressLint("RecyclerView") int position) {
        holder.time.setText(list.get(position).timeName);
        holder.message.setText(list.get(position).textMessange);
        holder.days.setText(list.get(position).days);
        holder.OnOff.isChecked();

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                notifyDataSetChanged();
                Intent intent = new Intent(context, NewOrChangedAlarm.class);
                intent.putExtra("Cr", list.get(position));
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class CreateNewAlarmViewHolder extends RecyclerView.ViewHolder{
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

    public class Click implements View.OnClickListener{

        @Override
        public void onClick(View v) {

        }
    }
}