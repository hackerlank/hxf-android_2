package com.goodhappiness.bean;

import java.io.Serializable;

/**
 * Created by 电脑 on 2016/6/28.
 */
public class FriendShipChangeBean implements Serializable{

    private static final long serialVersionUID = 6169725666480863402L;
    private String action;
    private FeedInfo feedInfo;

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public FeedInfo getFeedInfo() {
        return feedInfo;
    }

    public void setFeedInfo(FeedInfo feedInfo) {
        this.feedInfo = feedInfo;
    }
}
