package com.jabezmagomere.keepfit.Models;

public class Gym {
    private String GymName, Rating, Phone, Open, Close;

    public String getGymName() {
        return GymName;
    }

    public void setGymName(String gymName) {
        GymName = gymName;
    }

    public Gym(String gymName, String rating, String phone, String open, String close) {
        GymName = gymName;
        Rating = rating;
        Phone = phone;
        Open = open;
        Close = close;
    }

    public String getRating() {
        return Rating;

    }

    public void setRating(String rating) {
        Rating = rating;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getOpen() {
        return Open;
    }

    public void setOpen(String open) {
        Open = open;
    }

    public String getClose() {
        return Close;
    }

    public void setClose(String close) {
        Close = close;
    }
}
