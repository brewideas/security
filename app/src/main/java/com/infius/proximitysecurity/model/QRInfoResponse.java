package com.infius.proximitysecurity.model;

public class QRInfoResponse implements DataModel {
    String message;
    String status;
    GuestVisit data;

    public String getMessage() {
        return message;
    }

    public String getStatus() {
        return status;
    }

    public GuestVisit getGuestsVisit() {
        return data;
    }
}
