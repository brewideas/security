package com.infius.proximitysecurity.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Vehicle implements DataModel{

    String make;
    String model;

    public String getMake() {
        return make;
    }

    public void setMake(String make) {
        this.make = make;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getVehicleNo() {
        return vehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        this.vehicleNo = vehicleNo;
    }

    public String getParkingSlot() {
        return parkingSlot;
    }

    public void setParkingSlot(String parkingSlot) {
        this.parkingSlot = parkingSlot;
    }

    String vehicleNo;
    String parkingSlot;

    public Vehicle(String make, String model, String vehicleNo) {
        this.make = make;
        this.model = model;
        this.vehicleNo = vehicleNo;
    }

    public JSONObject toJsonObject() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("make", make);
            obj.put("model", model);
            obj.put("vehicleNo", vehicleNo);
            return obj;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
