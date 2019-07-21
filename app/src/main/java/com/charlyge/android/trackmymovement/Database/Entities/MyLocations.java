package com.charlyge.android.trackmymovement.Database.Entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity
public class MyLocations {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private double longitudeStart;
    private double latitudeStart;
    private double longitudeEnd;
    private double latitudeEnd;
    private String disanceCovered;
    private Date date;


    public MyLocations(int id, double longitudeStart, double latitudeStart, double longitudeEnd, double latitudeEnd, String disanceCovered, Date date) {
        this.id = id;
        this.longitudeStart = longitudeStart;
        this.latitudeStart = latitudeStart;
        this.longitudeEnd = longitudeEnd;
        this.latitudeEnd = latitudeEnd;
        this.date = date;
        this.disanceCovered = disanceCovered;
    }


    @Ignore
    public MyLocations(double longitudeStart, double latitudeStart, double longitudeEnd, double latitudeEnd, String disanceCovered, Date date) {
        this.longitudeStart = longitudeStart;
        this.latitudeStart = latitudeStart;
        this.longitudeEnd = longitudeEnd;
        this.latitudeEnd = latitudeEnd;
        this.date = date;
        this.disanceCovered= disanceCovered;
    }

    public int getId() {
        return id;
    }


    public String getDisanceCovered() {
        return disanceCovered;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getLongitudeStart() {
        return longitudeStart;
    }

    public Date getDate() {
        return date;
    }

    public double getLatitudeStart() {
        return latitudeStart;
    }

    public double getLongitudeEnd() {
        return longitudeEnd;
    }

    public double getLatitudeEnd() {
        return latitudeEnd;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
