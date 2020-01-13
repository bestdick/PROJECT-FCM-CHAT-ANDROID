package com.example.project_fcm;

public class _Bean_DistanceList {
    String devID;
    String devToken;
    String location;
    String age;
    String gender;
    String lastLogin;
    String distance;

    public String getDevID() {
        return devID;
    }

    public void setDevID(String devID) {
        this.devID = devID;
    }

    public String getDevToken() {
        return devToken;
    }

    public void setDevToken(String devToken) {
        this.devToken = devToken;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(String lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public _Bean_DistanceList(String devID, String devToken, String location, String age, String gender, String lastLogin, String distance) {
        this.devID = devID;
        this.devToken = devToken;
        this.location = location;
        this.age = age;
        this.gender = gender;
        this.lastLogin = lastLogin;
        this.distance = distance;
    }
}
