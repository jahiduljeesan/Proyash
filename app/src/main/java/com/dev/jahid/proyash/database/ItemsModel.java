package com.dev.jahid.proyash.database;

public class ItemsModel {
    private String name,phone,gender,union,village,group,username;

    public ItemsModel(String name, String phone, String gender, String union, String village, String group, String username) {
        this.name = name;
        this.phone = phone;
        this.gender = gender;
        this.union = union;
        this.village = village;
        this.group = group;
        this.username = username;
    }

    public ItemsModel() {

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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUnion() {
        return union;
    }

    public void setUnion(String union) {
        this.union = union;
    }

    public String getVillage() {
        return village;
    }

    public void setVillage(String village) {
        this.village = village;
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
