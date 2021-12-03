package com.zybooks.termtrackerjesse.models;

public class Note {
    private int id;
    private String title;
    private String text;
    private int courseID;

    public Note(int id, String title, String text, int courseID) {
        this.id = id;
        this.title = title;
        this.text = text;
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

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                ", courseID=" + courseID +
                '}';
    }
}
