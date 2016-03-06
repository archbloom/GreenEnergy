package io.github.archbloom.greenenergy;

import android.content.Context;
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
import android.widget.Toast;

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
public class CardAdapter2 extends RecyclerView.Adapter<CardAdapter2.ListCard> {

    Context context;
    List<DeviceDetails> list = new ArrayList<>();

    BigInteger TotalTime = new BigInteger("0");

    Map<String,Integer> mapPost;
    Integer state = 2;

    DData dData;
    String n;
    int s;

    public CardAdapter2() {
    }

    public CardAdapter2(Context context, List<DeviceDetails> list) {
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
    public void onBindViewHolder(final ListCard holder, final int position) {
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
        holder.toggleButton.setTag(position);
        holder.toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked)
                {
                    list.get((int)holder.toggleButton.getTag()).setState(1);
                }
                else
                {
                    list.get((int)holder.toggleButton.getTag()).setState(0);
                }

            }
        });
        holder.textView3.setText(""+list.get(position).getWattage()+"W");
        holder.ratingBar.setRating(list.get(position).getRating());
        if(list.get(position).getState()==1) {
            holder.toggleButton.setChecked(true);
            list.get(position).setState(1);
        }else{
            holder.toggleButton.setChecked(false);
            list.get(position).setState(0);
        }
    }

    public void postProfile(String prof,ArrayList<DeviceDetails> lll){
        dData = new DData(n,s);
        ArrayList<DData> arrayList = new ArrayList<>();

        Retrofit retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(UniversalData.getBaseUrl())
                .build();

        Map <String,String> maap = new HashMap<>();
        maap.put("Name",prof);

        Log.d("ABC","List size : "+list.size());
        MyAPI myAPI = retrofit.create(MyAPI.class);
        for(int i=0;i<list.size();i++){
            arrayList.add(new DData(lll.get(i).getName(),lll.get(i).getState()));
        }
        Call<com.squareup.okhttp.Response> call = myAPI.updateProfile(arrayList,maap);
        call.enqueue(new Callback<com.squareup.okhttp.Response>() {
            @Override
            public void onResponse(retrofit.Response<Response> response, Retrofit retrofit) {
                Log.d("ABC","Success : "+response.code());
            }

            @Override
            public void onFailure(Throwable t) {
                Log.d("ABC","Fail : "+t.getMessage());
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
