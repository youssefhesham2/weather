package com.example.apiretrofit.Ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apiretrofit.Data.RetrofitClient;

import com.example.apiretrofit.Models.Weather;
import com.example.apiretrofit.Models.WeatherModel;
import com.example.apiretrofit.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    postAdpter adpter;
    ImageView imageeviningormorning, sunormoon;
    TextView degretoday, cityname, weatherstatus;
    Calendar calnder = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initalrecycler();
        IntialView();
        intiloc();

    }

    private void intiloc() {
         FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {

            @Override
            public void onSuccess(Location location) {
                if (location != null)
                {

                    getdata(location.getLatitude(), location.getLongitude());

                }

                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());

                try {
                    addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
                    String city = addresses.get(0).getLocality();
                    String country = addresses.get(0).getCountryName();
                    cityname.setText(city+","+country);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        });



    }

    private void IntialView() {
        imageeviningormorning=findViewById(R.id.imageeviningormorning);
        sunormoon=findViewById(R.id.sunormoon);
        degretoday=findViewById(R.id.degre);
        cityname=findViewById(R.id.cityname);
        weatherstatus=findViewById(R.id.weatherstatus);
        if (calnder.get(Calendar.HOUR_OF_DAY)>16)
        {
            imageeviningormorning.setImageResource(R.drawable.ggg);
            sunormoon.setImageResource(R.drawable.ic_brightness_3_black_24dp);
        }
    }

    private void getdata(double lat,double log) {

        RetrofitClient.getInstance().get(lat,log).enqueue(new Callback<WeatherModel>() {
            @Override
            public void onResponse(Call<WeatherModel> call, Response<WeatherModel> response) {
                if(response.isSuccessful() && response.code()==200){
                    List<com.example.apiretrofit.Models.List> list = new ArrayList<com.example.apiretrofit.Models.List>();
                    com.example.apiretrofit.Models.List l=response.body().getList().get(0);
                    {
                        list.add(response.body().getList().get(8));
                        list.add(response.body().getList().get(16));
                        list.add(response.body().getList().get(24));
                        list.add(response.body().getList().get(32));

                        list.add(response.body().getList().get(12));
                        list.add(response.body().getList().get(20));
                        list.add(response.body().getList().get(28));
                        list.add(response.body().getList().get(36));

                    }
                    if(list !=null)
                    {
                        adpter=new postAdpter(list);
                        recyclerView.setAdapter(adpter);
                        com.example.apiretrofit.Models.List objoflist=response.body().getList().get(0);
                       int temp=(int) (objoflist.getMain().getTemp()-273.15);
                       String weatherstatuss=objoflist.getWeather().get(0).getDescription();
                       degretoday.setText(String.valueOf(temp)+"°C");
                       weatherstatus.setText(weatherstatuss);

                    }
                }
                else {
                Toast.makeText(getApplicationContext(),response.message(),Toast.LENGTH_SHORT).show();
                }
            }

            @SuppressLint("ResourceAsColor")
            @Override
            public void onFailure(Call<WeatherModel> call, Throwable t) {

                Toast.makeText(getApplicationContext(),t.getMessage(),Toast.LENGTH_SHORT).show();
                sunormoon.setVisibility(View.INVISIBLE);
                weatherstatus.setVisibility(View.INVISIBLE);
                degretoday.setVisibility(View.INVISIBLE);
                imageeviningormorning.setVisibility(View.INVISIBLE);
                cityname.setText("No INTERNET CCONNECTED");
                cityname.setTextColor(R.color.cardview_dark_background);
            }
        });
    }

    private void initalrecycler() {
        recyclerView=findViewById(R.id.recycler);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getApplicationContext(),RecyclerView.VERTICAL,false);
        recyclerView.setLayoutManager(layoutManager);
    }

    class postAdpter extends RecyclerView.Adapter<postAdpter.postVH>{
        List<com.example.apiretrofit.Models.List> models;

        public postAdpter(List<com.example.apiretrofit.Models.List> models) {
            this.models = models;
        }

        @NonNull
        @Override
        public postVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
           View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.item,parent,false);
            return new postVH(view);
        }

        @Override
        public void onBindViewHolder(@NonNull postVH holder, int position) {
                com.example.apiretrofit.Models.List model = models.get(position);
                com.example.apiretrofit.Models.List model2 = models.get(position+4);


            String dayname = model.getDtTxt();
            SimpleDateFormat formatterdate = new SimpleDateFormat("yyyy-mm-dd hh:mm:ss");
            SimpleDateFormat formterdaynam = new SimpleDateFormat("EEE");
            int time =calnder.get(Calendar.HOUR_OF_DAY);
            try {

                calnder.setTime(formatterdate.parse(dayname));
                calnder.add(Calendar.DATE, 3);
                String daynamee=formterdaynam.format(calnder.getTime());
                holder.dayname.setText(daynamee);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            if(time<12)
            {
                int degremax=(int)(((model.getMain().getTempMax()))-273.5);
                int degremin=(int)((model2.getMain().getTempMax())-273.5);
                holder.degree.setText(degremax+"°/"+degremin+"°C");

            }
            if(time>12)
            {
                int degremax=(int)(((model2.getMain().getTempMax()))-273.5);
                int degremin=(int)((model.getMain().getTempMax())-273.5);
                holder.degree.setText(degremax+"°/"+degremin+"°C");
            }
            int degremin=(int)((model.getMain().getTempMin())-273.5);
            String describ=model.getWeather().get(0).getMain();
            switch (describ){
                case "Clear":
                    if(time<12)
                    {
                        holder.weatherimage.setImageResource(R.drawable.sunny);

                    }
                    if(time>12)
                    {
                        holder.weatherimage.setImageResource(R.drawable.ic_brightness_3_black_24dp);
                    }
                    break;
                case "Clouds":
                    holder.weatherimage.setImageResource(R.drawable.cloud);
                    holder.weatherimage.setRotation(1);

                    break;
                case "Rain":
                    holder.weatherimage.setImageResource(R.drawable.rain);
                    holder.weatherimage.setRotation(1);
                    break;


            }
            }


        @Override
        public int getItemCount() {
            return 4;
        }

        class postVH extends RecyclerView.ViewHolder{
            TextView dayname,degree;
            ImageView weatherimage;

            public postVH(@NonNull View itemView) {
                super(itemView);
                dayname=itemView.findViewById(R.id.dayname);
                degree=itemView.findViewById(R.id.degree);
                weatherimage=itemView.findViewById(R.id.imageweather);

            }
        }
    }
}
