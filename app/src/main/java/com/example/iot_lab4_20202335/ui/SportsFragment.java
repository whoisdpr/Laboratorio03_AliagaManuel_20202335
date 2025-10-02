package com.example.iot_lab4_20202335.ui;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.*;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.iot_lab4_20202335.R;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.*;

public class SportsFragment extends Fragment {

    private static final String API_KEY = "ec24b1c6dd8a4d528c1205500250305";
    private EditText inPlace;
    private Button btnSearch;
    private ProgressBar progress;
    private RecyclerView recycler;
    private SportsAdapter adapter;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater i, @Nullable ViewGroup c, @Nullable Bundle s) {
        return i.inflate(R.layout.fragment_sports, c, false);
    }

    @Override public void onViewCreated(@NonNull View v, @Nullable Bundle s) {
        inPlace  = v.findViewById(R.id.inPlace);
        btnSearch= v.findViewById(R.id.btnSearchSports);
        progress = v.findViewById(R.id.progressSports);
        recycler = v.findViewById(R.id.recyclerSports);

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new SportsAdapter();
        recycler.setAdapter(adapter);

        btnSearch.setOnClickListener(x -> {
            String q = inPlace.getText().toString().trim();
            if (TextUtils.isEmpty(q)) { inPlace.setError(getString(R.string.empty_search)); return; }
            new TaskSports().execute(q);
        });
    }

    private class TaskSports extends AsyncTask<String, Void, List<SportsAdapter.Item>> {
        @Override protected void onPreExecute(){ progress.setVisibility(View.VISIBLE); }
        @Override protected List<SportsAdapter.Item> doInBackground(String... p){
            String q = p[0];
            String urlS = "https://api.weatherapi.com/v1/sports.json?key="+API_KEY+"&q="+ Uri.encode(q);
            try {
                HttpURLConnection c = (HttpURLConnection) new URL(urlS).openConnection();
                c.setConnectTimeout(10000); c.setReadTimeout(10000); c.connect();
                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                StringBuilder sb=new StringBuilder(); String line; while((line=br.readLine())!=null) sb.append(line); br.close();

                JSONObject root = new JSONObject(sb.toString());
                JSONArray football = root.optJSONArray("football");
                List<SportsAdapter.Item> out = new ArrayList<>();
                if (football != null){
                    for (int i=0;i<football.length();i++){
                        JSONObject o = football.getJSONObject(i);
                        String match = o.optString("match");
                        String stadium = o.optString("stadium");
                        String country = o.optString("country");
                        String start = o.optString("start");
                        out.add(new SportsAdapter.Item(match, stadium, country, start));
                    }
                }
                return out;
            } catch (Exception e){ return Collections.emptyList(); }
        }
        @Override protected void onPostExecute(List<SportsAdapter.Item> res){
            progress.setVisibility(View.GONE);
            adapter.submit(res);
            if (res.isEmpty()) Toast.makeText(getContext(), R.string.no_data, Toast.LENGTH_SHORT).show();
        }
    }
}