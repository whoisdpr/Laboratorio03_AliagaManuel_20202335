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

public class ForecastFragment extends Fragment {

    private static final String API_KEY = "ec24b1c6dd8a4d528c1205500250305";
    private EditText inLocationId, inDays;
    private Button btnSearch;
    private ProgressBar progress;
    private RecyclerView recycler;
    private ForecastAdapter adapter;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater i, @Nullable ViewGroup c, @Nullable Bundle s) {
        return i.inflate(R.layout.fragment_forecast, c, false);
    }

    @Override public void onViewCreated(@NonNull View v, @Nullable Bundle s) {
        inLocationId = v.findViewById(R.id.inLocationId);
        inDays       = v.findViewById(R.id.inDays);
        btnSearch    = v.findViewById(R.id.btnSearchForecast);
        progress     = v.findViewById(R.id.progressForecast);
        recycler     = v.findViewById(R.id.recyclerForecast);

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ForecastAdapter();
        recycler.setAdapter(adapter);

        Bundle args = getArguments();
        if (args != null && args.containsKey("locationId")) {
            inLocationId.setText(String.valueOf(args.getInt("locationId")));
        }
        if (TextUtils.isEmpty(inDays.getText())) inDays.setText("7");

        btnSearch.setOnClickListener(x -> {
            String idS = inLocationId.getText().toString().trim();
            String daysS = inDays.getText().toString().trim();
            if (TextUtils.isEmpty(idS)) { inLocationId.setError("Ingresa id"); return; }
            if (TextUtils.isEmpty(daysS)) { inDays.setError("Ingresa días"); return; }
            new TaskForecast().execute(idS, daysS);
        });
    }

    private class TaskForecast extends AsyncTask<String, Void, List<ForecastAdapter.Item>> {
        @Override protected void onPreExecute(){ progress.setVisibility(View.VISIBLE); }
        @Override protected List<ForecastAdapter.Item> doInBackground(String... p){
            String locId = p[0];
            String days  = p[1];
            String urlS = "https://api.weatherapi.com/v1/forecast.json?key="+API_KEY+
                    "&q=id:"+ Uri.encode(locId) + "&days=" + Uri.encode(days);
            try {
                HttpURLConnection c = (HttpURLConnection) new URL(urlS).openConnection();
                c.setConnectTimeout(10000); c.setReadTimeout(10000); c.connect();
                BufferedReader br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                StringBuilder sb=new StringBuilder(); String line; while((line=br.readLine())!=null) sb.append(line); br.close();

                JSONObject root = new JSONObject(sb.toString());
                JSONObject forecast = root.getJSONObject("forecast");
                JSONArray daysArr = forecast.getJSONArray("forecastday");
                List<ForecastAdapter.Item> out = new ArrayList<>();
                for (int i=0;i<daysArr.length();i++){
                    JSONObject d = daysArr.getJSONObject(i);
                    String date = d.optString("date");
                    JSONObject day = d.optJSONObject("day");
                    double tmax = day!=null ? day.optDouble("maxtemp_c") : Double.NaN;
                    double tmin = day!=null ? day.optDouble("mintemp_c") : Double.NaN;
                    String cond = "";
                    if (day!=null && day.has("condition")) {
                        cond = day.getJSONObject("condition").optString("text");
                    }
                    out.add(new ForecastAdapter.Item(date, tmax, tmin, cond));
                }
                return out;
            } catch (Exception e){ return Collections.emptyList(); }
        }
        @Override protected void onPostExecute(List<ForecastAdapter.Item> res){
            progress.setVisibility(View.GONE);
            adapter.submit(res);
            if (res.isEmpty()) Toast.makeText(getContext(), R.string.no_data, Toast.LENGTH_SHORT).show();
        }
    }
}