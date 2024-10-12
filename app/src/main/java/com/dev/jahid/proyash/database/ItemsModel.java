package com.dev.jahid.proyash.database;

public class ItemsModel {
    private String name,phone,union,village,group;

    public ItemsModel(String name, String phone, String union, String village, String group) {
        this.name = name;
        this.phone = phone;
        this.union = union;
        this.village = village;
        this.group = group;
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
}
