package com.goodhappiness.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 电脑 on 2016/5/9.
 */
public class FeedInfoList extends BaseBean implements Serializable{


    private static final long serialVersionUID = -4897955834264605344L;
    private int more;
    private int total;
    private List<FeedInfo> list;
    private List<Banner> banner;
    private UserInfo userInfo;
    private List<Channel> channel;

    public List<Banner> getBanner() {
        return banner;
    }

    public void setBanner(List<Banner> banner) {
        this.banner = banner;
    }

    public List<Channel> getChannel() {
        return channel;
    }

    public void setChannel(List<Channel> channel) {
        this.channel = channel;
    }

    public List<Banner> getBanners() {
        return banner;
    }

    public void setBanners(List<Banner> banners) {
        this.banner = banners;
    }

    public int getMore() {
        return more;
    }

    public void setMore(int more) {
        this.more = more;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<FeedInfo> getList() {
        return list;
    }

    public void setList(List<FeedInfo> list) {
        this.list = list;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
