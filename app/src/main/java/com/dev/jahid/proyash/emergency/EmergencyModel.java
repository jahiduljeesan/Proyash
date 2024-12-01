package com.dev.jahid.proyash.emergency;

public class EmergencyModel {
    String name,title,phone,category;

    public EmergencyModel(String name, String title, String phone, String category) {
        this.name = name;
        this.title = title;
        this.phone = phone;
        this.category = category;
    }

    public EmergencyModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
