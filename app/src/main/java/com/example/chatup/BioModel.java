package com.example.chatup;

public class BioModel {
    String name;
    String bio;

    public BioModel() {
    }

    public BioModel(String name, String bio) {
        this.name = name;
        this.bio = bio;
    }

    public String getName() {
        return name;
    }

    public String getBio() {
        return bio;
    }
}
