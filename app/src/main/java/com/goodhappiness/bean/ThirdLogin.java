package com.goodhappiness.bean;

import java.io.Serializable;

/**
 * Created by 电脑 on 2016/8/25.
 */
public class ThirdLogin implements Serializable{

    private static final long serialVersionUID = -5610399853929744353L;
    private String action;
    private String openid;
    private String token;
    private String username;
    private String avatar;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "ThirdLogin{" +
                "action='" + action + '\'' +
                ", openid='" + openid + '\'' +
                ", token='" + token + '\'' +
                ", username='" + username + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
