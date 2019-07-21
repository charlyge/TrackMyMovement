package com.charlyge.android.trackmymovement;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.charlyge.android.trackmymovement.Database.Entities.MyLocations;
import com.charlyge.android.trackmymovement.Repository.AppRepository;
import com.charlyge.android.trackmymovement.Retrofit.NetworkCallbacks;
import com.charlyge.android.trackmymovement.Retrofit.NetworkService;
import com.charlyge.android.trackmymovement.NetworkModels.ApiResponseClass;
import com.charlyge.android.trackmymovement.viewModel.LocationViewModel;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private Button start_button;
    private int REQUEST_LOCATION_PERMISSION = 45;
    private FusedLocationProviderClient mFusedLocationClient;
    private ImageView mAndroidImageView;
    private AnimatorSet mRotateAnim;
    private boolean isTrackingEnabled = true;
    private boolean hasStartCordinatedInserted=false;
    private  TextView distanceCovered;
    private LocationCallback mLocationCallback;
    private double longitudeStart;
    private double latitudeStart;
    private double longitudeEnd;
    private double latitudeEnd;
    private double longitude =0;
    private double latitude = 0;
    private String url = "";
    private AppRepository repository;
    private TextView last_distance_covered;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        start_button = findViewById(R.id.start_button);
        mAndroidImageView = findViewById(R.id.imageView);
         last_distance_covered =  findViewById(R.id.tv_last_distance_covered);
         distanceCovered = findViewById(R.id.tv_distance_covered);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager != null ? connectivityManager.getActiveNetworkInfo() : null;
        if (networkInfo == null) {
            Toast.makeText(this, "Enable Your Gsm Network for accurate Result", Toast.LENGTH_SHORT).show();
        }
        mRotateAnim = (AnimatorSet) AnimatorInflater.loadAnimator
                (this, R.animator.rotate);
        mRotateAnim.setTarget(mAndroidImageView);
        repository = AppRepository.getInstance(this);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        ViewModelProviders.of(this).get(LocationViewModel.class).getLocationList().observe(this, new Observer<List<MyLocations>>() {
            @Override
            public void onChanged(List<MyLocations> myLocations) {
                if(myLocations.size()>0){
                   last_distance_covered.setText("Your last Distance covered is : " + myLocations.get(0).getDisanceCovered());
                }

            }
        });
        start_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (isTrackingEnabled) {
                    mRotateAnim.start();
                    startTracking();
                    start_button.setBackgroundColor(getResources().getColor(R.color.red));
                    start_button.setText("Stop Tracking");
                } else {
                    stopTracking();
                    start_button.setBackgroundColor(getResources().getColor(R.color.green));
                    start_button.setText("Start Tracking");
                }
            }
        });
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                 longitude = locationResult.getLastLocation().getLongitude();
                 latitude = locationResult.getLastLocation().getLatitude();

                        if(!hasStartCordinatedInserted && longitude!=0){
                            longitudeStart = longitude;
                            latitudeStart= latitude;
                            hasStartCordinatedInserted =true;
                        }
               // distanceCovered.setText("distance covered is : " + distance);
            }
        };
        IsGpsEnabled();
    }

    private void stopTracking() {
        isTrackingEnabled = true;
        mRotateAnim.end();
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
        longitudeEnd = longitude;
        latitudeEnd = latitude;
     //   insertIntoDb
               // getDistanceCovered
        fetchDistance();
    }

    private void startTracking() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]
                            {Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_LOCATION_PERMISSION);
        } else {
            isTrackingEnabled = false;
            Toast.makeText(this, "Permisssion Granted", Toast.LENGTH_SHORT).show();

            mFusedLocationClient.requestLocationUpdates
                    (getLocationRequest(), mLocationCallback,
                            null /* Looper */);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_LOCATION_PERMISSION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startTracking();
        } else {
            Toast.makeText(this, "permission Denied", Toast.LENGTH_SHORT).show();
        }

    }


    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    private void fetchDistance() {
        distanceCovered.setText("Fetching distance covered please wait.....");
            NetworkCallbacks networkCallbacks = NetworkService.getInstance().getNetworkCallbacks();
            Map<String, String> myParams = new HashMap<String, String>();
            myParams.put("origins", +latitudeStart + "," + longitudeStart);
            myParams.put("destinations", + latitudeEnd + "," + longitudeEnd);
            myParams.put("key", getResources().getString(R.string.ApiKey));
            Call<ApiResponseClass> call = networkCallbacks.getDistance("json", myParams);
            call.enqueue(new Callback<ApiResponseClass>() {
                @Override
                public void onResponse(Call<ApiResponseClass> call, Response<ApiResponseClass> response) {
                    if(response.body() == null){
                        UseOfflineLocation();
                        return;
                    }
                    String distance = response.body().getRows().get(0).getElements().get(0).getDistance().getText();
                    MyLocations myLocations = new MyLocations(longitudeStart,latitudeStart,longitudeEnd,latitudeEnd,distance,new Date());
                    repository.insertDistance(myLocations);
                    hasStartCordinatedInserted = false;
                    distanceCovered.setText("You Covered " + distance);
                }

                @Override
                public void onFailure(Call<ApiResponseClass> call, Throwable t) {
                    UseOfflineLocation();
                }
            });

    }

    private void UseOfflineLocation() {
        Location crntLocation=new Location("crntLocation");
        crntLocation.setLatitude(latitudeStart);
        crntLocation.setLongitude(longitudeStart);

        Location newLocation=new Location("newlocation");
        newLocation.setLatitude(latitudeEnd);
        newLocation.setLongitude(longitudeEnd);
        float distance = crntLocation.distanceTo(newLocation) / 1000; // in km
        distanceCovered.setText("distance covered: " + distance);
        MyLocations myLocations = new MyLocations(longitudeStart,latitudeStart,longitudeEnd,latitudeEnd,String.valueOf(distance),new Date());
        repository.insertDistance(myLocations);
        hasStartCordinatedInserted = false;
    }

    public boolean IsGpsEnabled() {
        final LocationManager manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            return false;

        }
        return true;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your GPS seems to be disabled, Enable Gps to continue?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int id) {
                        startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }
}

