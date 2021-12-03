package com.zybooks.termtrackerjesse.models;

public class Instructor {
    private int id;
    private String name;
    private String phone;
    private String email;
    private int courseID;

    public Instructor(int id, String name, String phone, String email, int courseID) {
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.courseID = courseID;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getCourseID() {
        return courseID;
    }

    public void setCourseID(int courseID) {
        this.courseID = courseID;
    }

    @Override
    public String toString() {
        return "Instructor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", courseID=" + courseID +
                '}';
    }
}
