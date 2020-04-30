package com.example.apiretrofit.Data;

import com.example.apiretrofit.Models.WeatherModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface PostHolder {

    @GET("data/2.5/forecast")
    Call<WeatherModel> get(@Query("lat") double lat, @Query("lon") double lon, @Query("appid") String apikey);
}
