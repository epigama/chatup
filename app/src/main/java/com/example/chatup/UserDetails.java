

package com.example.chatup;

public class UserDetails {
    public static String username = "";

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

    public static String chatWith = "";
    public static String UID = "";

}
