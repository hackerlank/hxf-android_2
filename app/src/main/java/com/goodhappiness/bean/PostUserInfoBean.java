package com.goodhappiness.bean;

import java.io.Serializable;

/**
 * Created by 电脑 on 2016/5/14.
 */
public class PostUserInfoBean extends BaseBean implements Serializable {


    private static final long serialVersionUID = -7779419735026176085L;
    private long uid;
    private String avatar;
    private String nickname;
    private int followingNum;
    private int followerNum;
    private int relation;

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }

    public long getUid() {
        return uid;
    }

    public void setUid(long uid) {
        this.uid = uid;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getFollowingNum() {
        return followingNum;
    }

    public void setFollowingNum(int followingNum) {
        this.followingNum = followingNum;
    }

    public int getFollowerNum() {
        return followerNum;
    }

    public void setFollowerNum(int followerNum) {
        this.followerNum = followerNum;
    }

}