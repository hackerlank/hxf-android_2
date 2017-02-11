package com.goodhappiness.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 电脑 on 2016/4/22.
 */
public class UserInfoResult extends BaseBean implements Serializable{

    private static final long serialVersionUID = 6352213230201387961L;
    private UserInfo userInfo;
    private int unreadCommentNum;
    private int unreadLikeNum;
    private List<Redbag> redBags;

    public List<Redbag> getRedBags() {
        return redBags;
    }

    public void setRedBags(List<Redbag> redBags) {
        this.redBags = redBags;
    }

    public int getUnreadCommentNum() {
        return unreadCommentNum;
    }

    public void setUnreadCommentNum(int unreadCommentNum) {
        this.unreadCommentNum = unreadCommentNum;
    }

    public int getUnreadLikeNum() {
        return unreadLikeNum;
    }

    public void setUnreadLikeNum(int unreadLikeNum) {
        this.unreadLikeNum = unreadLikeNum;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
}
