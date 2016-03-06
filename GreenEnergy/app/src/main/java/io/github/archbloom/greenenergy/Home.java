package io.github.archbloom.greenenergy;

import android.content.BroadcastReceiver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringDef;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.Inflater;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by archbloom on 5/3/16.
 */
public class Home extends Fragment {
    ImageView weather_type;
    TextView current_temp,current_weather;
    TextClock current_time;
    View view;
    DeviceDetails deviceDetails;
    RecyclerView recyclerView;
    CardAdapter adapter;
    ArrayList<DeviceDetails> list = new ArrayList<>();
    public Home() {
        //
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setHasOptionsMenu(true);
        weather_type= (ImageView)view.findViewById(R.id.home_weather_image);
        current_time = (TextClock) view.findViewById(R.id.home_time);
        current_temp = (TextView)view.findViewById(R.id.home_temperature);
        current_weather = (TextView)view.findViewById(R.id.home_weather_type);


        recyclerView = (RecyclerView) view.findViewById(R.id.home_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(UniversalData.getWeather())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MyAPI myAPI = retrofit.create(MyAPI.class);
        Map <String,String> map = new HashMap<>();
        map.put("q","Pune,in");
        map.put("appid","44db6a862fba0b067b1930da0d769e98");
        Call<Weather> call = myAPI.getWeather(map);
        call.enqueue(new Callback<Weather>() {
            @Override
            public void onResponse(Response<Weather> response, Retrofit retrofit) {
                if(response.isSuccess())
                {
                    current_temp.setText(""+(response.body().getMain().getTemp()/10));
                    current_weather.setText(response.body().getWeather().get(0).getMain());
                    Picasso.with(getContext()).load("http://openweathermap.org/img/w/"+response.body().getWeather().get(0).getIcon()+".png")
                            .fit()
                            .into(weather_type);
                }
            }

            @Override
            public void onFailure(Throwable t) {

            }
        });
        Retrofit retrofit1 = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(UniversalData.getBaseUrl())
                .build();
        myAPI = retrofit1.create(MyAPI.class);
        Call<ArrayList<DeviceDetails>> listDevices = myAPI.getAllDevices();
        listDevices.enqueue(new Callback<ArrayList<DeviceDetails>>() {
            @Override
            public void onResponse(Response<ArrayList<DeviceDetails>> response, Retrofit retrofit) {
                if(response.isSuccess()) {
                    Log.d("ABC","Success");
                    adapter = new CardAdapter(getContext(),response.body());
                    recyclerView.setAdapter(adapter);
                }
                else {
                    try {
                        Log.d("ABC","Problem is "+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("ABC","Error is "+t.getMessage());
            }
        });
        return view;
    }

    public void onCreateOptionsMenu(Menu menu,MenuInflater inflater) {
        inflater.inflate(R.menu.actionbarmenu, menu);
        if(menu!=null){
            menu.findItem(R.id.add).setVisible(true);
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId())
        {
            case R.id.add:
                String[] arraySpinner = new String[]{"Select Device","H001","H002","H003","H004"};
                LayoutInflater li = LayoutInflater.from(getContext());
                View view = li.inflate(R.layout.dialog_ui,null);
                Spinner spinner = (Spinner)view.findViewById(R.id.spinner);
                final TextView t1,t2,t3,t4;
                final EditText editText;
                t1 = (TextView)view.findViewById(R.id.device_id);
                t2 = (TextView)view.findViewById(R.id.device_wattage);
                t3 = (TextView)view.findViewById(R.id.device_rating);
                t4 = (TextView)view.findViewById(R.id.device_type);
                editText = (EditText)view.findViewById(R.id.device_name);
                spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        String sel = (String) parent.getItemAtPosition(position);
                        if(!sel.equals("Select Device"))
                        {
                            Retrofit retrofit = new Retrofit.Builder()
                                    .baseUrl(UniversalData.getBaseUrl())
                                    .addConverterFactory(GsonConverterFactory.create())
                                    .build();
                            MyAPI myAPI = retrofit.create(MyAPI.class);
                            Map<String,String> map = new HashMap<String, String>();
                            map.put("Device_Id",sel);
                            Call<DeviceDetails> call = myAPI.getDevice(map);
                            call.enqueue(new Callback<DeviceDetails>() {
                                @Override
                                public void onResponse(Response<DeviceDetails> response, Retrofit retrofit) {
                                    if(response.isSuccess())
                                    {
                                        deviceDetails = response.body();
                                        t4.setText("Device Type: "+response.body().getName());
                                        t1.setText("Device Id: "+response.body().getDevice_Id());
                                        t2.setText("Wattage: "+response.body().getWattage());
                                        t3.setText("Star Rating: "+response.body().getRating()+"/5");
                                    }
                                }
                                @Override
                                public void onFailure(Throwable t) {

                                }
                            });
                        }
                    }
                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {

                    }
                });
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                        android.R.layout.simple_spinner_item, arraySpinner);
                spinner.setAdapter(adapter);
                new MaterialDialog.Builder(getContext())
                        .title("Add Device")
                        .customView(view,true)
                        .positiveText("Confirm")
                        .negativeText("Cancel")
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                String name = editText.getText().toString();
                                if(!name.equals(""))
                                {
                                    //Thermostat,BedRoomLight,Refrigerator
                                    deviceDetails.setName(name);
                                    Retrofit retrofit = new Retrofit.Builder()
                                            .baseUrl(UniversalData.getBaseUrl())
                                            .addConverterFactory(GsonConverterFactory.create())
                                            .build();
                                    MyAPI myAPI = retrofit.create(MyAPI.class);
                                    Call<com.squareup.okhttp.Response> call = myAPI.addDevice(deviceDetails);
                                    call.enqueue(new Callback<com.squareup.okhttp.Response>() {
                                        @Override
                                        public void onResponse(Response<com.squareup.okhttp.Response> response, Retrofit retrofit) {
                                            if(response.isSuccess()) {
                                                Log.d("ABC","success!");
                                            }
                                        }
                                        @Override
                                        public void onFailure(Throwable t) {
                                        }
                                    });
                                    dialog.dismiss();
                                }
                            }
                        })
                        .show();

                return true;

        }
        return true;
    }
}
