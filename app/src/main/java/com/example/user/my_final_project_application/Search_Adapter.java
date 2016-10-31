package com.example.user.my_final_project_application;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by User on 18/09/2016.
 */
public class Search_Adapter extends RecyclerView.Adapter<Search_Adapter.MyHolder> {
    public SharedPreferences sp;
    private ArrayList<Place> data = new ArrayList<>();
    private Context context;
    private Map_Interface map_interface;
    public DB_Place helpr;



    public Search_Adapter(ArrayList<Place> data, Context context) {
        this.data = data;
        this.context = context;
        map_interface = (Map_Interface) context;

    }




    public void add(Place place) {
        data.add(place);
        this.notifyDataSetChanged();
    }

    public void cl (){
        data.clear();
        notifyDataSetChanged();
    }


    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.list_item, null);
        MyHolder holder=new MyHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {

        Picasso.with(context).load(data.get(position).getIcon()).into(holder.icon);
        Place place = data.get(position);

        holder.bind(place);

    }

    @Override
    public int getItemCount() {
        return data.size();

    }

    public int Get_Place_By(int pos){
        data.get(pos);

        return pos;
    }




    /////////////////////////=Holder Class=////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////


    public class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener,AlertDialog.OnClickListener {

        private SharedPreferences sp;
        private TextView name;
        private TextView address;
        private RelativeLayout layout;
        public double lat, lan;
        private ImageView icon;
        private Place place;
        private Search_Adapter adapter;


        public MyHolder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            address = (TextView) itemView.findViewById(R.id.address);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            layout = (RelativeLayout) itemView.findViewById(R.id.linearLayout);
            layout.setOnClickListener(this);
            layout.setOnLongClickListener(this);
            sp=PreferenceManager.getDefaultSharedPreferences(context);
            adapter=new Search_Adapter(data,context);
        }

        public void bind(Place place) {
            this.place = place;
            name.setText(place.getName());
            address.setText(place.getAddress());
            lat = place.getLat();
            lan = place.getLan();


        }

        @Override
        public void onClick(View view) {
            int pos =adapter.Get_Place_By(this.getAdapterPosition());
            place=data.get(pos);
            Toast.makeText(context, place.getLat()+"Lat", Toast.LENGTH_SHORT).show();
            Toast.makeText(context, place.getLan()+"Lan", Toast.LENGTH_SHORT).show();
            map_interface.Go_To_Map(place);

        }

        @Override
        public boolean onLongClick(View view) {


            AlertDialog dialog = new AlertDialog.Builder(context).create();
            dialog.setTitle("Options");
            dialog.setMessage("For Share Press on SHARE"+"\n"+"For Adding To Favorite Press on Favorite");
            dialog.setButton(DialogInterface.BUTTON_NEUTRAL,"SHARE", MyHolder.this);
            dialog.setButton(DialogInterface.BUTTON_NEGATIVE,"FAVORITES", MyHolder.this);
            dialog.show();

            return true;


        }

        @Override
        public void onClick(DialogInterface dialogInterface, int i) {
            switch (i){
                case DialogInterface.BUTTON_NEUTRAL:
                    String text="Hello Come To visit In Places App";
                    String sharebody= text+"\n"+ place.toString();
                    Intent sharingplace=new Intent(Intent.ACTION_SEND);
                    sharingplace.setType("text/plain");
                    sharingplace.putExtra(Intent.EXTRA_SUBJECT,"A Place from MyPlaces App") ;
                    sharingplace.putExtra(Intent.EXTRA_TEXT,sharebody);
                    context.startActivity(sharingplace);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    helpr=new DB_Place(context);
;                   helpr.insert_Place(place);
                    Toast.makeText(context, "Place Add Secussed", Toast.LENGTH_SHORT).show();
                    Intent in=new Intent(context,My_Favorite_Place.class);
                    in.putExtra("Place",place);
                    context.startActivity(in);
                    break;

            }
        }

    }



}
