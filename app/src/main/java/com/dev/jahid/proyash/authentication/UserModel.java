package com.dev.jahid.proyash.authentication;

public class UserModel {
    private String fullName;
    private boolean admin,member;

    public UserModel(String fullName, boolean admin, boolean member) {
        this.fullName = fullName;
        this.admin = admin;
        this.member = member;
    }

    public UserModel() {
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public boolean getAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    public boolean getMember() {
        return member;
    }

    public void setMember(boolean member) {
        this.member = member;
    }
}
