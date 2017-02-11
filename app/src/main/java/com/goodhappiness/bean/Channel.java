package com.goodhappiness.bean;

import java.io.Serializable;

/**
 * Created by 电脑 on 2016/9/18.
 */
public class Channel implements Serializable{

    private static final long serialVersionUID = 4593791022915604333L;
    /**
     * channelName : 生活
     * imageUrl :
     */

    private int channelId;
    private String channelName;
    private String imageUrl;

    public Channel(int channelId, String channelName, String imageUrl) {
        this.channelId = channelId;
        this.channelName = channelName;
        this.imageUrl = imageUrl;
    }

    public int getChannelId() {
        return channelId;
    }

    public void setChannelId(int channelId) {
        this.channelId = channelId;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
