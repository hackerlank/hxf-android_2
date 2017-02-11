package com.goodhappiness.bean;

import java.io.Serializable;

/**
 * Created by 电脑 on 2016/4/9.
 */
public class UserInfo extends BaseBean implements Serializable{

    private static final long serialVersionUID = -324766493264726218L;
    private String nickname;
    private long uid;
    private String IPAddress;
    private String IP;
    private String avatar;
    private String mobile;
    private double money;
    private double happyCoin;
    private double generalCoin;
    private long followingNum;
    private long followerNum;
    private int relation;
    private int isBind;
    private int isCheck;

    public int getIsCheck() {
        return isCheck;
    }

    public void setIsCheck(int isCheck) {
        this.isCheck = isCheck;
    }

    public int getIsBind() {
        return isBind;
    }

    public void setIsBind(int isBind) {
        this.isBind = isBind;
    }

    public long getFollowingNum() {
        return followingNum;
    }

    public void setFollowingNum(long followingNum) {
        this.followingNum = followingNum;
    }

    public long getFollowerNum() {
        return followerNum;
    }

    public void setFollowerNum(long followerNum) {
        this.followerNum = followerNum;
    }

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public double getHappyCoin() {
        return happyCoin;
    }

    public void setHappyCoin(double happyCoin) {
        this.happyCoin = happyCoin;
    }

    public double getGeneralCoin() {
        return generalCoin;
    }

    public void setGeneralCoin(double generalCoin) {
        this.generalCoin = generalCoin;
    }

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

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    @Override
    public String toString() {
        return "UserInfo{" +
                "nickname='" + nickname + '\'' +
                ", uid=" + uid +
                ", IPAddress=" + IPAddress +
                ", IP='" + IP + '\'' +
                ", avatar=" + avatar +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
