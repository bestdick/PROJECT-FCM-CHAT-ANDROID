package com.example.project_fcm;

public class _Bean_Notice {
    String type;
    String content;
    String senderID;
    String senderToken;
    String senderLocation;
    String senderAge;
    String senderGender;
    String time;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getSenderID() {
        return senderID;
    }

    public void setSenderID(String senderID) {
        this.senderID = senderID;
    }

    public String getSenderToken() {
        return senderToken;
    }

    public void setSenderToken(String senderToken) {
        this.senderToken = senderToken;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSenderLocation() {
        return senderLocation;
    }

    public void setSenderLocation(String senderLocation) {
        this.senderLocation = senderLocation;
    }

    public String getSenderAge() {
        return senderAge;
    }

    public void setSenderAge(String senderAge) {
        this.senderAge = senderAge;
    }

    public String getSenderGender() {
        return senderGender;
    }

    public void setSenderGender(String senderGender) {
        this.senderGender = senderGender;
    }

    public _Bean_Notice(String type, String content, String senderID, String senderToken, String senderLocation, String senderAge, String senderGender, String time) {
        this.type = type;
        this.content = content;
        this.senderID = senderID;
        this.senderToken = senderToken;
        this.senderLocation = senderLocation;
        this.senderAge = senderAge;
        this.senderGender = senderGender;
        this.time = time;
    }
}
