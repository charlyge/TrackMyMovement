package com.charlyge.android.trackmymovement.Repository;

import android.content.Context;

import androidx.lifecycle.LiveData;

import com.charlyge.android.trackmymovement.Database.AppDatabase;
import com.charlyge.android.trackmymovement.Database.AppExecutors;
import com.charlyge.android.trackmymovement.Database.Entities.MyLocations;

import java.util.List;

public class AppRepository {
    private static AppDatabase appDatabase;
    private static AppRepository appRepository;

    public AppRepository(Context context) {
        appDatabase = AppDatabase.getInstance(context);
    }

    public synchronized static AppRepository getInstance(Context context){
        if(appRepository==null){
            appRepository = new AppRepository(context);
        }
        return appRepository;
    }

    public void insertDistance(final MyLocations myLocations){
        AppExecutors.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                appDatabase.movementDao().insetLocation(myLocations);
            }
        });

    }



    public LiveData<List<MyLocations>> getAllCordinates(){
        return appDatabase.movementDao().getLocations();
    }
}
