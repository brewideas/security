package com.infius.proximitysecurity.model;

public class GuestVisit implements DataModel{
    String visitorId;
    String name;
    String mobile;
    String email;

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

    public String getExpectedIn() {
        return expectedIn;
    }

    public String getExpectedOut() {
        return expectedOut;
    }

    public String getActualInTime() {
        return actualInTime;
    }

    public String getActualOutTime() {
        return actualOutTime;
    }

    int age;
    String gender;
    String guestPic;
    String expectedIn;
    String expectedOut;
    String actualInTime;
    String actualOutTime;
}
