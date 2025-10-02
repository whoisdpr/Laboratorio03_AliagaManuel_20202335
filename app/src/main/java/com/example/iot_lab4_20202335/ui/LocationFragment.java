package com.example.iot_lab4_20202335.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.iot_lab4_20202335.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LocationFragment extends Fragment {

    private static final String API_KEY = "30a2c3275bc3456ba38160745250305";

    private EditText inQuery;
    private Button btnSearch;
    private RecyclerView recycler;
    private ProgressBar progress;
    private LocationAdapter adapter;

    @Nullable @Override
    public View onCreateView(@NonNull LayoutInflater i, @Nullable ViewGroup c, @Nullable Bundle s) {
        return i.inflate(R.layout.fragment_location, c, false);
    }

    @Override public void onViewCreated(@NonNull View v, @Nullable Bundle s) {
        inQuery   = v.findViewById(R.id.inQuery);
        btnSearch = v.findViewById(R.id.btnSearch);
        recycler  = v.findViewById(R.id.recycler);
        progress  = v.findViewById(R.id.progress);

        recycler.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new LocationAdapter(item -> {
            Bundle args = new Bundle();
            args.putInt("locationId", item.id);
            args.putString("locationName", item.displayTitle());
            Navigation.findNavController(v).navigate(R.id.action_location_to_forecast, args);
        });
        recycler.setAdapter(adapter);

        btnSearch.setOnClickListener(x -> {
            if (!isOnline(requireContext())) {
                showNoInternetDialog();
                return;
            }
            String q = inQuery.getText().toString().trim();
            if (TextUtils.isEmpty(q)) {
                inQuery.setError(getString(R.string.empty_search));
                return;
            }
            new TaskSearch().execute(q);
        });
    }

    private boolean isOnline(Context ctx) {
        ConnectivityManager cm = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) return false;

        Network n = cm.getActiveNetwork();
        if (n == null) return false;

        NetworkCapabilities caps = cm.getNetworkCapabilities(n);
        return caps != null && caps.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
    }

    private void showNoInternetDialog() {
        new AlertDialog.Builder(requireContext())
                .setTitle("Sin conexión")
                .setMessage("No hay acceso a Internet. Ve a Configuración para habilitar datos o Wi-Fi.")
                .setPositiveButton("Configuración", new DialogInterface.OnClickListener() {
                    @Override public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Settings.ACTION_WIRELESS_SETTINGS));
                    }
                })
                .setNegativeButton("Cancelar", null)
                .show();
    }

    private class TaskSearch extends AsyncTask<String, Void, List<LocationItem>> {
        @Override protected void onPreExecute() {
            progress.setVisibility(View.VISIBLE);
            btnSearch.setEnabled(false);
        }

        @Override protected List<LocationItem> doInBackground(String... p) {
            String q = p[0];
            String urlS = "https://api.weatherapi.com/v1/search.json?key=" + API_KEY + "&q=" + Uri.encode(q);
            HttpURLConnection c = null;
            BufferedReader br = null;
            try {
                c = (HttpURLConnection) new URL(urlS).openConnection();
                c.setConnectTimeout(12000);
                c.setReadTimeout(12000);
                c.connect();

                br = new BufferedReader(new InputStreamReader(c.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) sb.append(line);

                JSONArray arr = new JSONArray(sb.toString());
                List<LocationItem> out = new ArrayList<>();
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject o = arr.getJSONObject(i);
                    int id = o.optInt("id");
                    String name = o.optString("name");
                    String region = o.optString("region");
                    String country = o.optString("country");
                    double lat = o.optDouble("lat");
                    double lon = o.optDouble("lon");
                    out.add(new LocationItem(id, name, region, country, lat, lon));
                }
                return out;
            } catch (Exception e) {
                return Collections.emptyList();
            } finally {
                try { if (br != null) br.close(); } catch (Exception ignore) {}
                if (c != null) c.disconnect();
            }
        }

        @Override protected void onPostExecute(List<LocationItem> res) {
            progress.setVisibility(View.GONE);
            btnSearch.setEnabled(true);
            adapter.submit(res);
            if (res.isEmpty()) {
                Toast.makeText(getContext(), R.string.no_data, Toast.LENGTH_SHORT).show();
            }
        }
    }
}