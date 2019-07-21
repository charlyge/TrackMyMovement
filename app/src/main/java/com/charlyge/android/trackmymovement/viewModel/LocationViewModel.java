package com.charlyge.android.trackmymovement.viewModel;

import android.app.Application;

import com.charlyge.android.trackmymovement.Database.Entities.MyLocations;
import com.charlyge.android.trackmymovement.Repository.AppRepository;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


public class LocationViewModel extends AndroidViewModel {
      private AppRepository appRepository;

    public LocationViewModel(@NonNull Application application) {
        super(application);
        appRepository = new AppRepository(application);
    }

    public LiveData<List<MyLocations>> getLocationList() {
       return appRepository.getAllCordinates();
    }
}
