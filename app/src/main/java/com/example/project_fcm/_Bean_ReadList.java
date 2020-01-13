package com.example.project_fcm;

public class _Bean_ReadList {
    String token; // global token

    String sender;
    String s_location;
    String s_age;
    String s_gender;

    String receiver;
    String r_location;
    String r_age;
    String r_gender;

    String message;
    String instant_sender;
    String isRead;
    String time;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getS_location() {
        return s_location;
    }

    public void setS_location(String s_location) {
        this.s_location = s_location;
    }

    public String getS_age() {
        return s_age;
    }

    public void setS_age(String s_age) {
        this.s_age = s_age;
    }

    public String getS_gender() {
        return s_gender;
    }

    public void setS_gender(String s_gender) {
        this.s_gender = s_gender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public String getR_location() {
        return r_location;
    }

    public void setR_location(String r_location) {
        this.r_location = r_location;
    }

    public String getR_age() {
        return r_age;
    }

    public void setR_age(String r_age) {
        this.r_age = r_age;
    }

    public String getR_gender() {
        return r_gender;
    }

    public void setR_gender(String r_gender) {
        this.r_gender = r_gender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getInstant_sender() {
        return instant_sender;
    }

    public void setInstant_sender(String instant_sender) {
        this.instant_sender = instant_sender;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public _Bean_ReadList(String token, String sender, String s_location, String s_age, String s_gender, String receiver, String r_location, String r_age, String r_gender, String message, String instant_sender, String isRead, String time) {
        this.token = token;
        this.sender = sender;
        this.s_location = s_location;
        this.s_age = s_age;
        this.s_gender = s_gender;
        this.receiver = receiver;
        this.r_location = r_location;
        this.r_age = r_age;
        this.r_gender = r_gender;
        this.message = message;
        this.instant_sender = instant_sender;
        this.isRead = isRead;
        this.time = time;
    }
}
