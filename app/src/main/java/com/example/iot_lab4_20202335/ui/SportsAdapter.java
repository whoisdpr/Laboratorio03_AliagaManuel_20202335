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

public class SportsAdapter extends RecyclerView.Adapter<SportsAdapter.Holder> {

    public static class Item {
        public final String match, stadium, country, start;
        public Item(String match, String stadium, String country, String start){
            this.match=match; this.stadium=stadium; this.country=country; this.start=start;
        }
    }

    private final List<Item> data = new ArrayList<>();
    public void submit(List<Item> items){ data.clear(); if(items!=null) data.addAll(items); notifyDataSetChanged(); }

    @NonNull @Override public Holder onCreateViewHolder(@NonNull ViewGroup p, int v) {
        View view = LayoutInflater.from(p.getContext()).inflate(R.layout.item_sport, p, false);
        return new Holder(view);
    }

    @Override public void onBindViewHolder(@NonNull Holder h, int pos){
        Item it = data.get(pos);
        h.tMatch.setText(it.match);
        h.tPlace.setText(it.stadium + " • " + it.country);
        h.tStart.setText(it.start);
    }

    @Override public int getItemCount(){ return data.size(); }

    static class Holder extends RecyclerView.ViewHolder{
        final TextView tMatch, tPlace, tStart;
        Holder(@NonNull View v){
            super(v);
            tMatch = v.findViewById(R.id.tMatch);
            tPlace = v.findViewById(R.id.tPlace);
            tStart = v.findViewById(R.id.tStart);
        }
    }
}