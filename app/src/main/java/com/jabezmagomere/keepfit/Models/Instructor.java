package com.jabezmagomere.keepfit.Models;

public class Instructor {
    private String FirstName,LastName,PhoneNumber,Email,Gender, PhotoURL;
    private Integer GymId;

    public Instructor(String firstName, String lastName, String phoneNumber, String email, String gender, String photoURL, Integer gymId) {
        FirstName = firstName;
        LastName = lastName;
        PhoneNumber = phoneNumber;
        Email = email;
        Gender = gender;
        PhotoURL = photoURL;
        GymId = gymId;
    }

    public String getFirstName() {
        return FirstName;
    }

    public void setFirstName(String firstName) {
        FirstName = firstName;
    }

    public String getLastName() {
        return LastName;
    }

    public void setLastName(String lastName) {
        LastName = lastName;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        PhoneNumber = phoneNumber;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getPhotoURL() {
        return PhotoURL;
    }

    public void setPhotoURL(String photoURL) {
        PhotoURL = photoURL;
    }

    public Integer getGymId() {
        return GymId;
    }

    public void setGymId(Integer gymId) {
        GymId = gymId;
    }
}
