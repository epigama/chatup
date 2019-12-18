package com.example.chatup.Models;

import com.stfalcon.chatkit.commons.models.IUser;

/*
 * Created by troy379 on 04.04.17.
 */
public class User implements IUser {

    private String id;
    private String name;
    private String avatar;
    private String imageURL;
    private boolean online;
    private String status;


    public User(String id, String name, String avatar, boolean online) {
        this.id = id;
        this.name = name;
        this.avatar = avatar;
        this.imageURL=imageURL;
        this.online = online;
        this.status=status;
    }

    public User() {
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getAvatar() {
        return avatar;
    }

    public boolean isOnline() {
        return online;
    }

    public String getImageURL(){
        return imageURL;
    }
    public String getStatus(){
        return status;
    }


}
