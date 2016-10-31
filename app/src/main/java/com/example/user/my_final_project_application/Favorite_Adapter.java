package com.example.user.my_final_project_application;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by User on 06/10/2016.
 */
public class Favorite_Adapter extends RecyclerView.Adapter<Favorite_Adapter.My_Holder> {

    My_Favorite_Place myFavoritePlace;
    DB_Place dbPlace;
    ArrayList<Place> places = new ArrayList<>();
    Context context;
    Map_Interface map_interface;



    public Favorite_Adapter(ArrayList<Place> places, Context context) {
        this.myFavoritePlace = (My_Favorite_Place) context;
        dbPlace = new DB_Place(context);
        this.places = places;
        this.context = context;
        map_interface = (Map_Interface) context;

    }


    public void add(Place place) {
        places.add(place);
        notifyDataSetChanged();
    }

    public void cl() {
        places.clear();
        notifyDataSetChanged();
    }


    @Override
    public My_Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.list_item,parent, false);
        My_Holder holder= new My_Holder(v);
        return holder;
    }

    @Override
    public void onBindViewHolder(My_Holder holder, int position) {
        Picasso.with(context).load(places.get(position).getIcon()).into(holder.icon);
        Place place = places.get(position);
        holder.bind(place);

    }

    @Override
    public int getItemCount() {
        return places.size();
    }


    public void DismissPlace(int pos) {
        places.remove(pos);
        this.notifyDataSetChanged();
    }
    public int Get_Place_By(int pos){
        places.get(pos);

        return pos;
    }


    ///////////////////////////////////////////My Holder class///////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    public class My_Holder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public SharedPreferences sp;
        private TextView name;
        private TextView address;
        private ImageView icon;
        private RelativeLayout list_favorite;
        private Place place;
        private Favorite_Adapter adapter;
        double lat,lan;
        Location place_click;



        public My_Holder(View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            address = (TextView) itemView.findViewById(R.id.address);
            icon = (ImageView) itemView.findViewById(R.id.icon);
            list_favorite = (RelativeLayout) itemView.findViewById(R.id.linearLayout);
            list_favorite.setOnClickListener(this);
            sp = PreferenceManager.getDefaultSharedPreferences(itemView.getContext());
            adapter= new Favorite_Adapter(places,context);
            place_click =new Location("");





        }


        public void bind(Place place) {
            this.place=place;
            name.setText(place.getName());
            address.setText(place.getAddress());

            place_click.setLongitude(place.getLat());
            place_click.setLatitude(place.getLan());
            place.setLat(place_click.getLatitude());
            place.setLan(place_click.getLongitude());

            Location myloc = new Location(" ");

            if (sp.getString("mylat", null) != null) {
                myloc.setLatitude(Double.parseDouble(sp.getString("mylat", null)));
                myloc.setLongitude(Double.parseDouble(sp.getString("mylan", null)));
            } else {

            }





        }

        @Override
        public void onClick(View view) {
            int pos =adapter.Get_Place_By(this.getAdapterPosition());
            place=places.get(pos);
            Toast.makeText(context,place_click.getLatitude()+"", Toast.LENGTH_SHORT).show();
            Toast.makeText(context, place_click.getLongitude()+"", Toast.LENGTH_SHORT).show();

            map_interface.Go_To_Map(place);

        }


    }
}
