package com.zybooks.termtrackerjesse.models;

public class Assessment {
    private int id;
    private String title;
    private String date;
    private String type;
    private int courseID;

    public Assessment(int id, String title, String date, String type, int courseID) {
        this.id = id;
        this.title = title;
        this.date = date;
        this.type = type;
        this.courseID = courseID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    @Override
    public String toString() {
        return "Assessment{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", date='" + date + '\'' +
                ", type='" + type + '\'' +
                ", courseID=" + courseID +
                '}';
    }
}
