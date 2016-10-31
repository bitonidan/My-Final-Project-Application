package com.example.user.my_final_project_application;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by User on 18/09/2016.
 */
public class Place implements Parcelable {

    private String name,address;
    private double lat,lan;
    private long id;
    private String icon;

    public Place(String name, String address, double lat, double lan,String icon, long id) {
        this.name = name;
        this.address = address;
        this.lat = lat;
        this.lan = lan;
        this.icon=icon;
        this.id = id;
    }

    public Place(String name,String address, double lat, double lan,String icon) {
        this.name = name;
        this.address=address;
        this.lat = lat;
        this.lan = lan;
        this.icon=icon;
    }

    public Place(Parcel parcel) {
        id=parcel.readLong();
        name=parcel.readString();
        address=parcel.readString();
        lat=parcel.readDouble();
        lan=parcel.readDouble();
        icon=parcel.readString();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLan() {
        return lan;
    }

    public void setLan(double lan) {
        this.lan = lan;
    }

    public long getId() {
        return id;
    }

    public String getIcon() {
        return icon;
    }

    public static final Creator<Place> CREATOR=new Creator<Place>() {
    @Override
    public Place createFromParcel(Parcel parcel) {
        return new Place(parcel);
    }

    @Override
    public Place[] newArray(int i) {
        return new Place[i];
    }
};

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {

        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeDouble(lan);
        parcel.writeDouble(lat);
        parcel.writeLong(id);
        parcel.writeString(icon);

    }
}
