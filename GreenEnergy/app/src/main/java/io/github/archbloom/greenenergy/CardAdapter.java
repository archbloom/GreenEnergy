package io.github.archbloom.greenenergy;

import android.bluetooth.BluetoothClass;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.squareup.okhttp.Response;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;

/**
 * Created by archbloom on 5/3/16.
 */
public class CardAdapter extends RecyclerView.Adapter<CardAdapter.ListCard> {

    Context context;
    List<DeviceDetails> list = new ArrayList<>();

    BigInteger TotalTime = new BigInteger("0");

    Intent intent;
    String name,uptime;

    public CardAdapter() {
    }

    public CardAdapter(Context context, List<DeviceDetails> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public ListCard onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view,null);
        ListCard listCard = new ListCard(view);
        return listCard;
    }

    @Override
    public void onBindViewHolder(ListCard holder, final int position) {
        holder.textView2.setText(list.get(position).getName());
        TotalTime = TotalTime.add(list.get(position).getTotal_Uptime());
        if(list.get(position).getDevice_Id().equals("H001")) {
            holder.image.setImageResource(R.drawable.thermostat);
        }else if(list.get(position).getDevice_Id().equals("H002")) {
            holder.image.setImageResource(R.drawable.bulb);
        }else if(list.get(position).getDevice_Id().equals("H004")){
            holder.image.setImageResource(R.drawable.refrigerator);
        }else if(list.get(position).getDevice_Id().equals("H003")){
            holder.image.setImageResource(R.drawable.mwave);
        }
        holder.textView3.setText(""+list.get(position).getWattage()+"W");
        holder.ratingBar.setRating(list.get(position).getRating());
        if(list.get(position).getState()==1) {
            holder.toggleButton.setChecked(true);
        }else{
            holder.toggleButton.setChecked(false);
        }

        View.OnClickListener onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent = new Intent("io.github.archbloom.greenenergy.ANALYTICSACTIVITY");
                intent.putExtra("Total",TotalTime);
                intent.putExtra("Name",list.get(position).getName());
                intent.putExtra("Percent",list.get(position).getTotal_Uptime());
                context.startActivity(intent);
            }
        };
        holder.image.setOnClickListener(onClickListener);
        holder.textView2.setOnClickListener(onClickListener);
        holder.textView3.setOnClickListener(onClickListener);
        holder.ratingBar.setOnClickListener(onClickListener);

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(UniversalData.getBaseUrl())
                .build();
        final MyAPI myAPI = retrofit.create(MyAPI.class);
        final Map<String,String> map = new HashMap<>();
        map.put("Device_Name",list.get(position).getName());
        holder.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    map.put("Status","1");
                    Call<Response> call = myAPI.toggle(map);
                    call.enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(retrofit.Response<Response> response, Retrofit retrofit) {
                             if(response.isSuccess())
                             {
                                 Log.d("ABC","On");
                             }
                        }
                        @Override
                        public void onFailure(Throwable t) {

                        }
                    });
                }
                else
                {
                    map.put("Status","0");
                    Call<Response> call = myAPI.toggle(map);
                    call.enqueue(new Callback<Response>() {
                        @Override
                        public void onResponse(retrofit.Response<Response> response, Retrofit retrofit) {
                            if(response.isSuccess())
                            {
                                Log.d("ABC","Off");
                            }
                        }

                        @Override
                        public void onFailure(Throwable t) {

                        }
                    });
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return (null!= list ? list.size():0);
    }

    public class ListCard extends RecyclerView.ViewHolder  {

        ImageView image;
        TextView textView2,textView3;
        Switch toggleButton ;
        RatingBar ratingBar;
        public ListCard(View itemView) {
            super(itemView);
            image =(ImageView) itemView.findViewById(R.id.card_imageview);
            textView2 = (TextView) itemView.findViewById(R.id.card_textview2);
            textView3 = (TextView) itemView.findViewById(R.id.card_textview3);
            toggleButton = (Switch) itemView.findViewById(R.id.switch1);
            ratingBar = (RatingBar)itemView.findViewById(R.id.ratingBar);
            ratingBar.setMax(5);
            ratingBar.setEnabled(false);
        }
    }
}
