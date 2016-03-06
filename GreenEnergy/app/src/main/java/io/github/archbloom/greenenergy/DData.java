package io.github.archbloom.greenenergy;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Map;
public class DData {
    @SerializedName("Name")
    @Expose
    private String Name;
    @SerializedName("Status")
    @Expose
    private int Status;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public DData(String name, int status) {
        Name = name;
        Status = status;
    }

    public DData() {
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }
}