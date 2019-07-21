package com.charlyge.android.trackmymovement.Database.Dao;

import android.location.Location;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.charlyge.android.trackmymovement.Database.Entities.MyLocations;

import java.util.List;

@Dao
public interface MovementDao {

    @Query("select * from MyLocations order by id desc limit 1")
    LiveData<List<MyLocations>> getLocations();

    @Insert
    void insetLocation(MyLocations myLocations);

}
