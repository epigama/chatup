package com.example.chatup;

public class DatabaseModel {
    String bio;
    String name;
    String phone;
     DatabaseModel(String Bio, String Name, String Phone){
         this.bio=Bio;
         this.name=Name;
         this.phone=Phone;
     }
     //Empty constructor needed by firebase
     DatabaseModel(){}

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
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
}

