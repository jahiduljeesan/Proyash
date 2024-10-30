package com.dev.jahid.proyash.database;

public class PostModel {
    private String id,postPersonName,postGroup,postPatientType,postLocation,postDescription,phoneNumber1,phoneNumber2,date,userEmail;

    public PostModel(String id, String postPersonName, String postGroup, String postPatientType, String postLocation, String postDescription, String phoneNumber1, String phoneNumber2, String date, String userEmail) {
        this.id = id;
        this.postPersonName = postPersonName;
        this.postGroup = postGroup;
        this.postPatientType = postPatientType;
        this.postLocation = postLocation;
        this.postDescription = postDescription;
        this.phoneNumber1 = phoneNumber1;
        this.phoneNumber2 = phoneNumber2;
        this.date = date;
        this.userEmail = userEmail;
    }

    public String getPostPersonName() {
        return postPersonName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setPostPersonName(String postPersonName) {
        this.postPersonName = postPersonName;
    }

    public String getPostGroup() {
        return postGroup;
    }

    public void setPostGroup(String postGroup) {
        this.postGroup = postGroup;
    }

    public String getPostPatientType() {
        return postPatientType;
    }

    public void setPostPatientType(String postPatientType) {
        this.postPatientType = postPatientType;
    }

    public String getPostLocation() {
        return postLocation;
    }

    public void setPostLocation(String postLocation) {
        this.postLocation = postLocation;
    }

    public String getPostDescription() {
        return postDescription;
    }

    public void setPostDescription(String postDescription) {
        this.postDescription = postDescription;
    }

    public String getPhoneNumber1() {
        return phoneNumber1;
    }

    public void setPhoneNumber1(String phoneNumber1) {
        this.phoneNumber1 = phoneNumber1;
    }

    public String getPhoneNumber2() {
        return phoneNumber2;
    }

    public void setPhoneNumber2(String phoneNumber2) {
        this.phoneNumber2 = phoneNumber2;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public PostModel() {
    }

}
