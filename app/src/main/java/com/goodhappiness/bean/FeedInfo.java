package com.goodhappiness.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 电脑 on 2016/5/14.
 */
public class FeedInfo extends BaseBean implements Serializable {


    private static final long serialVersionUID = -7150341621588435252L;
    private PostUserInfoBean postUserInfo;
    private long feedId;
    private int feedType;
    private String postContent;
    private int commentNum;
    private int likeNum;
    private int created;
    private int isLike;
    private int chanelId;
    private List<String> tagArray;
    private boolean isDelete = false;
    private ArrayList<PostFilesBean> postFiles;

    public int getChanelId() {
        return chanelId;
    }

    public void setChanelId(int chanelId) {
        this.chanelId = chanelId;
    }

    public List<String> getTagArray() {
        return tagArray;
    }

    public void setTagArray(List<String> tagArray) {
        this.tagArray = tagArray;
    }

    public void setDelete(boolean delete) {
        isDelete = delete;
    }

    public boolean isDelete() {
        return isDelete;
    }

    public void setIsDelete(boolean isDelete) {
        this.isDelete = isDelete;
    }

    public PostUserInfoBean getPostUserInfo() {
        return postUserInfo;
    }

    public void setPostUserInfo(PostUserInfoBean postUserInfo) {
        this.postUserInfo = postUserInfo;
    }

    public long getFeedId() {
        return feedId;
    }

    public void setFeedId(long feedId) {
        this.feedId = feedId;
    }

    public int getFeedType() {
        return feedType;
    }

    public void setFeedType(int feedType) {
        this.feedType = feedType;
    }

    public String getPostContent() {
        return postContent;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public int getCommentNum() {
        return commentNum;
    }

    public void setCommentNum(int commentNum) {
        this.commentNum = commentNum;
    }

    public int getLikeNum() {
        return likeNum;
    }

    public void setLikeNum(int likeNum) {
        this.likeNum = likeNum;
    }

    public int getCreated() {
        return created;
    }

    public void setCreated(int created) {
        this.created = created;
    }

    public int getIsLike() {
        return isLike;
    }

    public void setIsLike(int isLike) {
        this.isLike = isLike;
    }

    public ArrayList<PostFilesBean> getPostFiles() {
        return postFiles;
    }

    public void setPostFiles(ArrayList<PostFilesBean> postFiles) {
        this.postFiles = postFiles;
    }

}