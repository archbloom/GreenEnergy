package io.github.archbloom.greenenergy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.Map;

public class DeviceDetails {

    @SerializedName("Device_Id")
    @Expose
    private String Device_Id;
    @SerializedName("Name")
    @Expose
    private String Name;
    @SerializedName("Wattage")
    @Expose
    private int Wattage;
    @SerializedName("Rating")
    @Expose
    private int Rating;
    @SerializedName("State")
    @Expose
    private int State;
    @SerializedName("Total_Uptime")
    @Expose
    private BigInteger Total_Uptime;
    @SerializedName("Current_Timestamp")
    @Expose
    private String Current_Timestamp;

    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * No args constructor for use in serialization
     *
     */
    public DeviceDetails() {
    }

    /**
     *
     * @param Name
     * @param State
     * @param Rating
     * @param Device_Id
     * @param Current_Timestamp
     * @param Wattage
     * @param Total_Uptime
     */
    public DeviceDetails(String Device_Id, String Name, int Wattage, int Rating, int State, BigInteger Total_Uptime, String Current_Timestamp) {
        this.Device_Id = Device_Id;
        this.Name = Name;
        this.Wattage = Wattage;
        this.Rating = Rating;
        this.State = State;
        this.Total_Uptime = Total_Uptime;
        this.Current_Timestamp = Current_Timestamp;
    }

    /**
     *
     * @return
     * The Device_Id
     */
    public String getDevice_Id() {
        return Device_Id;
    }

    /**
     *
     * @param Device_Id
     * The Device_Id
     */
    public void setDevice_Id(String Device_Id) {
        this.Device_Id = Device_Id;
    }

    /**
     *
     * @return
     * The Name
     */
    public String getName() {
        return Name;
    }

    /**
     *
     * @param Name
     * The Name
     */
    public void setName(String Name) {
        this.Name = Name;
    }

    /**
     *
     * @return
     * The Wattage
     */
    public int getWattage() {
        return Wattage;
    }

    /**
     *
     * @param Wattage
     * The Wattage
     */
    public void setWattage(int Wattage) {
        this.Wattage = Wattage;
    }

    /**
     *
     * @return
     * The Rating
     */
    public int getRating() {
        return Rating;
    }

    /**
     *
     * @param Rating
     * The Rating
     */
    public void setRating(int Rating) {
        this.Rating = Rating;
    }

    /**
     *
     * @return
     * The State
     */
    public int getState() {
        return State;
    }

    /**
     *
     * @param State
     * The State
     */
    public void setState(int State) {
        this.State = State;
    }

    /**
     *
     * @return
     * The Total_Uptime
     */
    public BigInteger getTotal_Uptime() {
        return Total_Uptime;
    }

    /**
     *
     * @param Total_Uptime
     * The Total_Uptime
     */
    public void setTotal_Uptime(BigInteger Total_Uptime) {
        this.Total_Uptime = Total_Uptime;
    }

    /**
     *
     * @return
     * The Current_Timestamp
     */
    public String getCurrent_Timestamp() {
        return Current_Timestamp;
    }

    /**
     *
     * @param Current_Timestamp
     * The Current_Timestamp
     */
    public void setCurrent_Timestamp(String Current_Timestamp) {
        this.Current_Timestamp = Current_Timestamp;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}