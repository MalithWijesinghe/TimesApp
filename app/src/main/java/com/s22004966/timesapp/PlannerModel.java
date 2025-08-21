package com.s22004966.timesapp;

public class PlannerModel {
    private int id;
    private String schedule;
    private String date;
    private String time;
    private String location;

    public PlannerModel(int id, String schedule, String date, String time, String location) {
        this.id = id;
        this.schedule = schedule;
        this.date = date;
        this.time = time;
        this.location = location;
    }

    public int getId() {
        return id;
    }

    public String getSchedule() {
        return schedule;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }
}
