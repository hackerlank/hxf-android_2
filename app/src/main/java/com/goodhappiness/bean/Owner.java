package com.goodhappiness.bean;

import java.io.Serializable;

/**
 * Created by 电脑 on 2016/4/18.
 */
public class Owner implements Serializable{

    private static final long serialVersionUID = 8973758550841637036L;
    private String nickname;
    private long uid;
    private String IPAddress;
    private String IP;
    private String avatar;
    private boolean mobile;

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getIPAddress() {
        return IPAddress;
    }

    public void setIPAddress(String IPAddress) {
        this.IPAddress = IPAddress;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public boolean isMobile() {
        return mobile;
    }

    public void setMobile(boolean mobile) {
        this.mobile = mobile;
    }
}
