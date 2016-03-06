package io.github.archbloom.greenenergy;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.io.IOException;
import java.math.BigInteger;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class AnalyticsActivity extends AppCompatActivity {

    BarChart barChart;
    ArrayList<BarEntry> entries = new ArrayList<>();
    ArrayList<String> labels = new ArrayList<String>();
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_analytics);

        barChart = (BarChart)findViewById(R.id.analytics_chart);
        textView = (TextView)findViewById(R.id.analytics_percent_consumption);

        Map<String,String> map = new HashMap<>();
        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(UniversalData.getBaseUrl())
                .build();
        getSupportActionBar().setTitle("Analytics");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#303F9F")));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        MyAPI myAPI = retrofit.create(MyAPI.class);
        Bundle bundle = getIntent().getExtras();
        if(bundle!=null)
        {
            map.put("Name",bundle.getString("Name"));
            long total = ((BigInteger) bundle.get("Total")).longValue();
            long part = ((BigInteger) bundle.get("Percent")).longValue();

            Log.d("ABC","Total rec"+total);
            Log.d("ABC","Percentage is  rec"+ ((float)part/total)*100);

            float percentage = ((float)part/total)*100;

            NumberFormat formatter = new DecimalFormat("#0.00");
            String percent=formatter.format(percentage);

            textView.setText(percent+"%");

        }
        Call<Integer[]> call = myAPI.getLastMonthDetails(map);
        call.enqueue(new Callback<Integer[]>() {
            @Override
            public void onResponse(Response<Integer[]> response, Retrofit retrofit) {
                if(response.isSuccess()){
                    Log.d("ABC","Responce length : "+response.body().length);
                    for (int i=0;i<response.body().length;i++) {
                        entries.add(new BarEntry(response.body()[i],i));
                        labels.add(""+(i+1));
                    }
                    BarDataSet dataset = new BarDataSet(entries, "Energy consumption");
                    BarData data = new BarData(labels, dataset);
                    barChart.setData(data);
                    barChart.setDescription("");
                }
                else {
                    try {
                        Log.d("ABC","error "+response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Throwable t) {
                Log.d("ABC","onFailure");
            }
        });

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
