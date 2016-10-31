package com.example.user.my_final_project_application;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

public class MainActivity extends AppCompatActivity implements Map_Interface,AlertDialog.OnClickListener {

    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentTransaction transaction=fragmentManager.beginTransaction();
    public Place place;
    public static boolean isphone;
    private Map_Fragment map;
    private DB_Place helper;
    public boolean isCharging;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Search_Place search_place=new Search_Place();

        isphone=isphone();
        if (isphone){
            transaction.add(R.id.layout,search_place);
        }else {
            map=new Map_Fragment();
            transaction.add(R.id.search_container,search_place);
            transaction.add(R.id.map_container,map);

        }
        transaction.commit();


        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);

        Intent batteryStatus = this.registerReceiver(null, filter);

        // Are we charging / charged?

        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

        if (isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING) {
            Toast.makeText(MainActivity.this, "Battery  Charging!!", Toast.LENGTH_SHORT).show();
        }
        if (isCharging = status == BatteryManager.BATTERY_STATUS_DISCHARGING) {
            Toast.makeText(MainActivity.this, "Battery Not Charging!!!", Toast.LENGTH_SHORT).show();
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_settings:
                Intent in = new Intent(this, Activity_Setting.class);
                startActivity(in);
                break;

            case R.id.Delete:
                AlertDialog dialog=new AlertDialog.Builder(this).create();
                dialog.setTitle("Delete All Places?");
                dialog.setMessage("Are you shore to Delete all places?");
                dialog.setButton(DialogInterface.BUTTON_POSITIVE,"YES",this);
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE,"NO",this);
                dialog.show();
                break;

            case R.id.Favorite:
                Intent intent = new Intent(this, My_Favorite_Place.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void Go_To_Map(Place place) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("Place", place);
        if (isphone){
            map=new Map_Fragment();
            map.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.layout,map).addToBackStack("").commit();

        }
        else {
            map= (Map_Fragment) getSupportFragmentManager().findFragmentById(R.id.map_container);
            map.mark_on_map(place);
        }


    }
    private boolean isphone(){
        boolean isphone=true;
        View container=findViewById(R.id.layout);
        if (container==null){
            isphone=false;
        }
        return isphone;

    }


    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        switch (i){

            case DialogInterface.BUTTON_NEGATIVE:
                break;
            case DialogInterface.BUTTON_POSITIVE:
                helper=new DB_Place(this);
                helper.delete_all_places();
                break;
        }

    }
}
