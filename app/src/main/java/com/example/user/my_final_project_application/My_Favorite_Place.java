package com.example.user.my_final_project_application;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class My_Favorite_Place extends AppCompatActivity implements Map_Interface {

    private DB_Place dbPlace;
    private Favorite_Adapter myAdapter;
    private RecyclerView recyclerView;
    private SharedPreferences sp;
    private ArrayList<Place> places=new ArrayList<>();
    private Map_Fragment map;
    private Cursor c;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my__favorite__place);

        //Recycler

        recyclerView = (RecyclerView) findViewById(R.id.list_favorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        //Data
        dbPlace = new DB_Place(this);
        places=dbPlace.getAllPlaces();

        //Adapter
        myAdapter = new Favorite_Adapter(places,this);
        recyclerView.setAdapter(myAdapter);



        Intent intent=getIntent();
        intent.getParcelableExtra("Place");

        ItemTouchHelper.Callback callback=new Swipe_Helper(myAdapter,this);
        ItemTouchHelper helper=new ItemTouchHelper(callback);
        helper.attachToRecyclerView(recyclerView);

    }

    @Override
    public void Go_To_Map(Place place) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("Place",place);
            map=new Map_Fragment();
            map.setArguments(bundle);
            getSupportFragmentManager().beginTransaction().replace(R.id.Favorite,map).addToBackStack("").commit();


    }





}

