package com.infius.proximitysecurity.model;

import java.util.ArrayList;

public class PrimaryGuest implements DataModel{

    String visitorId;
    String name;
    String mobile;
    String email;
    int age;
    String gender;

    public String getVisitorId() {
        return visitorId;
    }

    public String getName() {
        return name;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public int getAge() {
        return age;
    }

    public String getGender() {
        return gender;
    }

    public String getGuestPic() {
        return guestPic;
    }

    public String getGuestThumbnailPic() {
        return guestThumbnailPic;
    }

    public ArrayList<Visit> getVisits() {
        return visits;
    }

    String guestPic;
    String guestThumbnailPic;
    ArrayList<Visit> visits;
}
