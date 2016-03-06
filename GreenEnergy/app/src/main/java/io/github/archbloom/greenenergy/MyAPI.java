package io.github.archbloom.greenenergy;

import com.squareup.okhttp.Response;

import java.util.ArrayList;
import java.util.Map;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.QueryMap;

/**
 * Created by archbloom on 5/3/16.
 */
public interface MyAPI {
    @GET("weather")
    Call<Weather> getWeather(@QueryMap Map<String,String> map);

    @GET("device")
    Call<DeviceDetails> getDevice(@QueryMap Map<String,String> map);

    @POST("device")
    Call<Response> addDevice(@Body DeviceDetails deviceDetails);

    @POST("profile")
    Call<Response> updateProfile(@Body ArrayList<DData> ddata,@QueryMap Map<String,String> map);

    @GET("device/")
    Call<ArrayList<DeviceDetails>> getAllDevices();

    @GET("device/toggle")
    Call<Response> toggle(@QueryMap Map<String,String>map);

    @GET("dashboard")
    Call<ArrayList<DeviceDetails>> getDashboard();

    @GET("device/details")
    Call<Integer[]> getLastMonthDetails(@QueryMap Map<String,String>map);

    @GET("profile")
    Call<DData> getProfile(@QueryMap Map<String,String> map);


}
