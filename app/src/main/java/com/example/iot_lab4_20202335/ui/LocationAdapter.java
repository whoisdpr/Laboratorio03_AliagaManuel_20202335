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

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.Holder> {

    public interface OnLocationClick { void onSelect(LocationItem item); }

    private final List<LocationItem> data = new ArrayList<>();
    private final OnLocationClick onClick;

    public LocationAdapter(OnLocationClick onClick) { this.onClick = onClick; }

    public void submit(List<LocationItem> items) {
        data.clear();
        if (items != null) data.addAll(items);
        notifyDataSetChanged();
    }

    @NonNull @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_location, parent, false);
        return new Holder(v);
    }

    @Override public void onBindViewHolder(@NonNull Holder h, int pos) {
        LocationItem it = data.get(pos);
        h.tTitle.setText(it.displayTitle());
        h.tSubtitle.setText(it.displaySubtitle());
        h.tCoords.setText(String.format("Lat %.4f • Lon %.4f", it.lat, it.lon));
        h.itemView.setOnClickListener(v -> { if (onClick != null) onClick.onSelect(it); });
    }

    @Override public int getItemCount() { return data.size(); }

    static class Holder extends RecyclerView.ViewHolder {
        final TextView tTitle, tSubtitle, tCoords;
        Holder(@NonNull View v) {
            super(v);
            tTitle    = v.findViewById(R.id.tTitle);
            tSubtitle = v.findViewById(R.id.tSubtitle);
            tCoords   = v.findViewById(R.id.tCoords);
        }
    }
}