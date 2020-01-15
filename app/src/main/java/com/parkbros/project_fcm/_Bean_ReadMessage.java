package com.parkbros.project_fcm;

public class _Bean_ReadMessage {
    String sender_id;
    String receiver_id;

    String sender_token;
    String receiver_token;

    String instant_sender;
    String instant_sender_token;

    String sender_location;
    String receiver_location;

    String sender_age;
    String receiver_age;

    String sender_gender;
    String receiver_gender;

    String isRead;

    String message;
    String time;

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getSender_token() {
        return sender_token;
    }

    public void setSender_token(String sender_token) {
        this.sender_token = sender_token;
    }

    public String getReceiver_token() {
        return receiver_token;
    }

    public void setReceiver_token(String receiver_token) {
        this.receiver_token = receiver_token;
    }

    public String getInstant_sender() {
        return instant_sender;
    }

    public void setInstant_sender(String instant_sender) {
        this.instant_sender = instant_sender;
    }

    public String getInstant_sender_token() {
        return instant_sender_token;
    }

    public void setInstant_sender_token(String instant_sender_token) {
        this.instant_sender_token = instant_sender_token;
    }

    public String getSender_location() {
        return sender_location;
    }

    public void setSender_location(String sender_location) {
        this.sender_location = sender_location;
    }

    public String getReceiver_location() {
        return receiver_location;
    }

    public void setReceiver_location(String receiver_location) {
        this.receiver_location = receiver_location;
    }

    public String getSender_age() {
        return sender_age;
    }

    public void setSender_age(String sender_age) {
        this.sender_age = sender_age;
    }

    public String getReceiver_age() {
        return receiver_age;
    }

    public void setReceiver_age(String receiver_age) {
        this.receiver_age = receiver_age;
    }

    public String getSender_gender() {
        return sender_gender;
    }

    public void setSender_gender(String sender_gender) {
        this.sender_gender = sender_gender;
    }

    public String getReceiver_gender() {
        return receiver_gender;
    }

    public void setReceiver_gender(String receiver_gender) {
        this.receiver_gender = receiver_gender;
    }

    public String getIsRead() {
        return isRead;
    }

    public void setIsRead(String isRead) {
        this.isRead = isRead;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public _Bean_ReadMessage(String sender_id, String receiver_id, String sender_token, String receiver_token, String instant_sender, String instant_sender_token, String sender_location, String receiver_location, String sender_age, String receiver_age, String sender_gender, String receiver_gender, String isRead, String message, String time) {
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.sender_token = sender_token;
        this.receiver_token = receiver_token;
        this.instant_sender = instant_sender;
        this.instant_sender_token = instant_sender_token;
        this.sender_location = sender_location;
        this.receiver_location = receiver_location;
        this.sender_age = sender_age;
        this.receiver_age = receiver_age;
        this.sender_gender = sender_gender;
        this.receiver_gender = receiver_gender;
        this.isRead = isRead;
        this.message = message;
        this.time = time;
    }
}
