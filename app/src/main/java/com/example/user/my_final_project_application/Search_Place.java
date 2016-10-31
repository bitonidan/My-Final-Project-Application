package com.example.user.my_final_project_application;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Search_Place extends Fragment implements View.OnClickListener, android.location.LocationListener {

    public ArrayList places = new ArrayList<>();
    private String searchvalue;
    private EditText address;
    private RecyclerView list_search;
    private Search_Adapter adapter;
    private Search_Adapter adapter2;
    private Location mylocation;
    private SharedPreferences sp;
    private LocationManager lc;
    private String last_json;
    private String icon;
    private int Radiuos;
    private Cursor cursor;
    private double lat, lan;


    //CTOR
    public Search_Place() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_a, container,false);
        address = (EditText) v.findViewById(R.id.search_address);
        v.findViewById(R.id.but_Near_by).setOnClickListener(this);
        v.findViewById(R.id.butSearch).setOnClickListener(this);
        list_search = (RecyclerView) v.findViewById(R.id.list_search);
        list_search.setOnClickListener(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        list_search.setLayoutManager(linearLayoutManager);
        adapter = new Search_Adapter(places, getContext());
        adapter2=new Search_Adapter(places,getContext());
        list_search.setAdapter(adapter2);
        adapter2.onAttachedToRecyclerView(list_search);
        sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        last_json = sp.getString("res", " ");


        if (Radiuos != 4000) {
            Radiuos = sp.getInt("Rad", 0);
            Toast.makeText(getContext(),  "Radios is:"+Radiuos, Toast.LENGTH_SHORT).show();
        } else {
            Radiuos = 4000;
        }
        Location_con();

        return v;
    }




    @Override
    public void onClick(View view) {



        switch (view.getId()) {
            case R.id.butSearch:
                Get_Search search = new Get_Search(adapter2);
                searchvalue = address.getText().toString().replace(" ", "%20").trim();
                if (searchvalue.isEmpty()) {
                    Toast.makeText(getContext(), "You Must To enter the Field ", Toast.LENGTH_SHORT).show();
                    adapter2.cl();
                    return;
                } else {
                    adapter2.cl();
                    search.execute("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + mylocation.getLatitude() + "," + mylocation.getLongitude() + "&keyword=" + searchvalue + "&radius=" + Radiuos + "&key=AIzaSyDz6NyR4T0PUHxk_0DMXHT0wC4cITOXoxM");
                }

                break;

            case R.id.but_Near_by:
                adapter2.cl();
                Get_Search search1 = new Get_Search(adapter2);
                search1.execute("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=" + mylocation.getLatitude() + "," + mylocation.getLongitude() + "&radius=" + Radiuos + "&key=AIzaSyDz6NyR4T0PUHxk_0DMXHT0wC4cITOXoxM");
                break;

        }


    }

    @Override
    public void onLocationChanged(Location location) {
        mylocation = location;
        mylocation.setLatitude(location.getLatitude());
        mylocation.setLongitude(location.getLongitude());
        sp.edit().putString("mylat", String.valueOf(mylocation.getLatitude())).apply();
        sp.edit().putString("mylan", String.valueOf(mylocation.getLongitude())).apply();
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }


    class Get_Search extends AsyncTask<String, Void, String> {
        private Search_Adapter adapter;
        private Search_Adapter adapter2;
        private ProgressDialog dialog;

        public Get_Search(Search_Adapter adapter) {
            this.adapter = adapter;
            this.adapter2=adapter;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getContext());
            dialog.setTitle("Please Wait");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... strings) {
            HttpURLConnection con = null;
            BufferedReader reader = null;
            try {
                URL url = new URL(strings[0]);
                con = (HttpURLConnection) url.openConnection();
                if (con.getResponseCode() != HttpURLConnection.HTTP_OK) {

                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                String result = "", line;
                line = reader.readLine();
                while (line != null) {
                    result += line;
                    line = reader.readLine();
                }
                Log.e("res:", result);
                sp.edit().putString("res", result.toString()).apply();
                return result;


            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (con != null) {
                    con.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (result == null) {
                Toast.makeText(getContext(), "Erorr", Toast.LENGTH_SHORT).show();
            } else {
                try {
                    JSONObject root = new JSONObject(result);
                    JSONArray arr = root.getJSONArray("results");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject place = arr.getJSONObject(i);
                        String name = place.getString("name");
                        Log.e("test", "test: " + result);
                        String address = place.getString("vicinity");
                        JSONObject geometry = place.getJSONObject("geometry");
                        JSONObject location = geometry.getJSONObject("location");
                        double lat = location.getDouble("lat");
                        double lng = location.getDouble("lng");
                        if (place.has("photos")) {
                            icon = ("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + place.getJSONArray("photos").getJSONObject(0).getString("photo_reference") + "&key=AIzaSyDz6NyR4T0PUHxk_0DMXHT0wC4cITOXoxM");

                        } else {
                            icon = null;
                        }
                        Place prepare_place = new Place(name, address, lat, lng, icon);
                        places.add(prepare_place);
                        adapter.add(prepare_place);
                        adapter2.add(prepare_place);




                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            dialog.dismiss();
            super.onPostExecute(result);


        }

    }

    public void Location_con() {
        Location netloc = new Location("");
        Location GPSloc = new Location("");

        if (ActivityCompat.checkSelfPermission(getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            lc = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

            if (lc.isProviderEnabled(LocationManager.GPS_PROVIDER) == false && lc.isProviderEnabled(LocationManager.GPS_PROVIDER) == false) {

                Toast.makeText(getContext(), "GPS and network false", Toast.LENGTH_SHORT).show();
            } else {
                lc.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                lc.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (netloc != null) {
                    lc.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 10000, 10, this);
                }
                if (GPSloc != null) {
                    lc.requestLocationUpdates(LocationManager.GPS_PROVIDER, 10000, 10, this);
                }
                Toast.makeText(getContext(), "GPS and network true", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void Show_last_search() {

        if (last_json == null) {
            Toast.makeText(getContext(), "Erorr", Toast.LENGTH_SHORT).show();
        } else {
            try {
                JSONObject root = new JSONObject(last_json);
                JSONArray arr = root.getJSONArray("results");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject place = arr.getJSONObject(i);
                    String name = place.getString("name");
                    Log.e("test", "test: " + last_json);
                    String address = place.getString("vicinity");
                    JSONObject geometry = place.getJSONObject("geometry");
                    JSONObject location = geometry.getJSONObject("location");
                    if (place.has("photos")) {
                        icon = ("https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + place.getJSONArray("photos").getJSONObject(0).getString("photo_reference") + "&key=AIzaSyDz6NyR4T0PUHxk_0DMXHT0wC4cITOXoxM");

                    } else {
                        icon = null;
                    }
                    double lat = location.getDouble("lat");
                    double lng = location.getDouble("lng");
                    Place prepare_place = new Place(name, address, lat, lng, icon);
                    places.add(prepare_place);
                    adapter.add(prepare_place);
                    adapter2.add(prepare_place);

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter2.cl();
        Show_last_search();




    }
}
