package com.example.chatup.Models;

public class UserDetails {
    public static String username = "";
    public static String bio = "";
    public static String chatWith = "";
    public static String chatWithNumber = "";
    public static String UID = "";
    public static String phoneNum = "";
    public static String uri = "";

    public static String getUri() {
        return uri;
    }

    public static void setUri(String uri) {
        UserDetails.uri = uri;
    }

    public static int darkSwitch = 0; //0 = Light, 1 = Dark

    public static int getDarkSwitch() {
        return darkSwitch;
    }

    public static void setDarkSwitch(int darkSwitch) {
        UserDetails.darkSwitch = darkSwitch;
    }

    public static String getPhoneNum() {
        return phoneNum;
    }

    public static void setPhoneNum(String phoneNum) {
        UserDetails.phoneNum = phoneNum;
    }

    public static String getBio() {
        return bio;
    }

    public static void setBio(String bio) {
        UserDetails.bio = bio;
    }

    public static String getUsername() {
        return username;
    }

    public static void setUsername(String username) {
        UserDetails.username = username;
    }

    public static String getChatWith() {
        return chatWith;
    }

    public static void setChatWith(String chatWith) {
        UserDetails.chatWith = chatWith;
    }

    public static String getUID() {
        return UID;
    }

    public static void setUID(String UID) {
        UserDetails.UID = UID;
    }

    public static String getChatWithNumber() {
        return chatWithNumber;
    }

    public static void setChatWithNumber(String chatWithNumber) {
        UserDetails.chatWithNumber = chatWithNumber;
    }


}
