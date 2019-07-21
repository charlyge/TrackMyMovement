package com.charlyge.android.trackmymovement.Database;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.charlyge.android.trackmymovement.Database.Dao.MovementDao;
import com.charlyge.android.trackmymovement.Database.Entities.MyLocations;
import com.charlyge.android.trackmymovement.DateConverter;

@Database(entities = {MyLocations.class},version = 1,exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase appDatabase;
    private static String DATABASE_NAME = "LocationsDb";
    private static Object LOCK = new Object();


    public static AppDatabase getInstance(Context context) {

        if (appDatabase == null) {
            synchronized (LOCK) {
                appDatabase = Room.databaseBuilder(context.getApplicationContext(),
                        AppDatabase.class,
                        AppDatabase.DATABASE_NAME).build();

            }

        }
        return appDatabase;
    }

  public abstract MovementDao movementDao();
}
