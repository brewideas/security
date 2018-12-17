package com.infius.proximitysecurity.model;

public class InvitationModel implements DataModel {
    String message;
    String status;
    String data;

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public String getData() {
        return data;
    }
}
