package com.example.apiretrofit.Data;

import com.example.apiretrofit.Models.WeatherModel;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
  public final static String base_url="http://api.openweathermap.org/";
  static RetrofitClient retrofitClient;
  PostHolder postHolder;

    private RetrofitClient() {
        Retrofit retrofit=new Retrofit.Builder().baseUrl(base_url).addConverterFactory(GsonConverterFactory.create()).build();

        postHolder=retrofit.create(PostHolder.class);

    }

    // make singlture
    // عملت سنجلتور عشان اضمن ان الكلاس هيتاخد منه obj واحد طوال فترة البرنامج
    public static RetrofitClient getInstance(){
        if(retrofitClient==null)
        {
            retrofitClient=new RetrofitClient();
        }
        return retrofitClient;
    }

    public Call<WeatherModel> get(double lat, double lon)
    {
        return postHolder.get(lat,lon,"2f593663096f7b4f8d38a3719b1ef27f");
    }


}
