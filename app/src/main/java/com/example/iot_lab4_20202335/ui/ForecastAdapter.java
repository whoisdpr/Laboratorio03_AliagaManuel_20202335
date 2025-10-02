package com.example.iot_lab4_20202335.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.iot_lab4_20202335.R;
import java.util.ArrayList;
import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.Holder> {

    public static class Item {
        public final String date;
        public final double tmax, tmin;
        public final String condition;
        public Item(String date, double tmax, double tmin, String condition){
            this.date=date; this.tmax=tmax; this.tmin=tmin; this.condition=condition;
        }
    }

    private final List<Item> data = new ArrayList<>();
    public void submit(List<Item> items){ data.clear(); if(items!=null) data.addAll(items); notifyDataSetChanged(); }

    @NonNull @Override public Holder onCreateViewHolder(@NonNull ViewGroup p, int v) {
        View view = LayoutInflater.from(p.getContext()).inflate(R.layout.item_forecast, p, false);
        return new Holder(view);
    }

    @Override public void onBindViewHolder(@NonNull Holder h, int pos){
        Item it = data.get(pos);
        h.tDate.setText(it.date);
        h.tTemps.setText(String.format("Máx %.1f°C • Mín %.1f°C", it.tmax, it.tmin));
        h.tCond.setText(it.condition);
    }

    @Override public int getItemCount(){ return data.size(); }

    static class Holder extends RecyclerView.ViewHolder{
        final TextView tDate, tTemps, tCond;
        Holder(@NonNull View v){
            super(v);
            tDate = v.findViewById(R.id.tDate);
            tTemps = v.findViewById(R.id.tTemps);
            tCond = v.findViewById(R.id.tCond);
        }
    }
}