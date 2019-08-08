package com.example.chatup;

public class DatabaseModel {
    String name;
    String bio;
    String phone;

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }

    public String getPhone() {
        return phone;
    }

    public DatabaseModel(String bio, String name, String phone) {
        this.name = name;
        this.bio = bio;
        this.phone = phone;
    }

    public DatabaseModel() {
    }
}
