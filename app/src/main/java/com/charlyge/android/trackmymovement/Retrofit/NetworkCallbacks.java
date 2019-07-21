package com.charlyge.android.trackmymovement.Retrofit;

import com.charlyge.android.trackmymovement.NetworkModels.ApiResponseClass;

import java.util.Map;

import retrofit2.http.GET;
import retrofit2.Call;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface NetworkCallbacks {

    @GET("{outputFormat}")
    Call<ApiResponseClass> getDistance(@Path("outputFormat") String format,
                                       @QueryMap Map<String, String> map);
}
