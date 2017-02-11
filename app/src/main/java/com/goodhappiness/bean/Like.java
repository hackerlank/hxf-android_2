package com.goodhappiness.bean;

/**
 * Created by 电脑 on 2016/5/14.
 */
public class Like extends BaseBean{


    private long uid;
    private String avatar;
    private String nickname;
    private int relation;
    private long feedId;
    private String fileUrl;
    private long created;

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public long getFeedId() {
        return feedId;
    }

    public void setFeedId(long feedId) {
        this.feedId = feedId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
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

    public int getRelation() {
        return relation;
    }

    public void setRelation(int relation) {
        this.relation = relation;
    }
}
