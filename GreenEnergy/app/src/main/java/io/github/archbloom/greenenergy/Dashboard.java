package io.github.archbloom.greenenergy;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;


/**
 * A simple {@link Fragment} subclass.
 */
public class Dashboard extends Fragment {

    View view;
    HorizontalBarChart barChart;
    ArrayList<BarEntry> entries = new ArrayList<>();
    ArrayList<String> labels = new ArrayList<String>();
    ArrayList<DeviceDetails> list = new ArrayList<>();


    public Dashboard() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        barChart = (HorizontalBarChart)view.findViewById(R.id.dashboard_chart);
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(UniversalData.getBaseUrl())
                .build();
        MyAPI myAPI = retrofit.create(MyAPI.class);
        Call<ArrayList<DeviceDetails>> call = myAPI.getDashboard();
        call.enqueue(new Callback<ArrayList<DeviceDetails>>() {
            @Override
            public void onResponse(Response<ArrayList<DeviceDetails>> response, Retrofit retrofit) {
                if(response.isSuccess()) {
                    list = response.body();
                    Log.d("ABC","size is "+list.size());
                    for(int i=0;i<list.size();i++)
                    {
                        Log.d("ABC","i is "+i);
                        long seconds = list.get(i).getTotal_Uptime().divide(new BigInteger("1000000000")).longValue();
                        float energy_consumed = list.get(i).getWattage()*(seconds/60);
                        entries.add(new BarEntry(energy_consumed,i));
                        labels.add(list.get(i).getName());
                    }
                    BarDataSet dataset = new BarDataSet(entries, "Energy consumption");
                    BarData data = new BarData(labels, dataset);
                    barChart.setData(data);
                    barChart.setDescription("");
                    // set the data and list of lables into chart
                }
                else {
                    try {
                        Log.d("ABC","Error : "+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Throwable t) {
                Log.d("ABC","Failure : "+t.getMessage());
            }
        });
        return view;
    }

}
