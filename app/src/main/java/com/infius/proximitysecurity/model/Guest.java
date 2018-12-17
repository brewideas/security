package com.infius.proximitysecurity.model;

import org.json.JSONException;
import org.json.JSONObject;

public class Guest implements DataModel{

    String name;
    String mobile;
    String gender;
    int age;

    public Guest(String name, String mobile, String gender, int age) {
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.mobile = mobile;
    }

    public JSONObject toJsonObject() {
        try {
            JSONObject obj = new JSONObject();
            obj.put("name", name);
            obj.put("mobile", mobile);
            obj.put("age", age);
            obj.put("gender", gender);
            return obj;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getMobile() {
        return mobile;
    }

    public String getName() {
        return name;
    }
}
