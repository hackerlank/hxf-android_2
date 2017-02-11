package com.goodhappiness.bean.database;

import org.litepal.crud.DataSupport;

/**
 * Created by 电脑 on 2016/7/8.
 */
public class PUserInfo extends DataSupport {
    private long id;
    private String targetId;
    private String name;
    private String avatar;

    public PUserInfo() {
    }

    public PUserInfo(String targetId, String name, String avatar) {
        this.targetId = targetId;
        this.name = name;
        this.avatar = avatar;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public String toString() {
        return "PUserInfo{" +
                "id=" + id +
                ", targetId='" + targetId + '\'' +
                ", name='" + name + '\'' +
                ", avatar='" + avatar + '\'' +
                '}';
    }
}
