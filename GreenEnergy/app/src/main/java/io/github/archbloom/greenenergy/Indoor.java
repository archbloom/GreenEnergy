package io.github.archbloom.greenenergy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * Created by archbloom on 5/3/16.
 */
public class Indoor extends Fragment  {

    View view;
    RecyclerView recyclerView;
    CardAdapter2 adapter;
    ArrayList<DeviceDetails> list = new ArrayList<>();
    Button saveProfile;

    public Indoor() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_indoor, container, false);
        setHasOptionsMenu(true);

        saveProfile = (Button) view.findViewById(R.id.saveProfile);
        recyclerView = (RecyclerView) view.findViewById(R.id.home_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(UniversalData.getBaseUrl())
                .build();

        MyAPI myAPI = retrofit.create(MyAPI.class);
        final Call<ArrayList<DeviceDetails>> listDevices = myAPI.getAllDevices();
        listDevices.enqueue(new Callback<ArrayList<DeviceDetails>>() {
            @Override
            public void onResponse(Response<ArrayList<DeviceDetails>> response, Retrofit retrofit) {
                if (response.isSuccess()) {
                    Log.d("ABC", "Success");
                    list = response.body();
                    adapter = new CardAdapter2(getContext(), response.body());
                    recyclerView.setAdapter(adapter);
                } else {
                    try {
                        Log.d("ABC", "Problem is " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Throwable t) {
                Log.d("ABC", "Error is " + t.getMessage());
            }
        });
        Log.d("ABC","got devices");
        saveProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.postProfile("Indoor",list);
                Toast.makeText(getContext(),"Indoor Profile Saved.",Toast.LENGTH_SHORT).show();
            }
        });
        return view;

    }
}
