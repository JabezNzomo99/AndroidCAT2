package com.jabezmagomere.keepfit.Models;

public class WorkOut{
    private String Date, Location, ExcerciseName, Id;
    private String reps,sets;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getExcerciseName() {
        return ExcerciseName;
    }

    public void setExcerciseName(String excerciseName) {
        ExcerciseName = excerciseName;
    }

    public String getReps() {
        return reps;
    }

    public void setReps(String reps) {
        this.reps = reps;
    }

    public String  getSets() {
        return sets;
    }

    public void setSets(String sets) {
        this.sets = sets;
    }
}
