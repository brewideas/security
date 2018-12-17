package com.infius.proximitysecurity.model;

import java.util.ArrayList;

public class Visit implements DataModel{

    String visitId;
    String guestType;
    String actualInTime;
    String actualOutTime;
    String expectedIn;
    String expectedOut;
    ArrayList<Vehicle> vehicles;
    ArrayList<Guest> otherGuests;
}
