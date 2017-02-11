package com.goodhappiness.bean;

/**
 * Created by 电脑 on 2016/5/14.
 */
public class Comment extends BaseBean {


    private long commentId;
    private long feedId;
    private String content;
    private long replyId;
    private long created;
    private String fileUrl;
    private PostUserInfoBean fromUserInfo;
    private PostUserInfoBean toUserInfo;

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public long getCommentId() {
        return commentId;
    }

    public void setCommentId(long commentId) {
        this.commentId = commentId;
    }

    public long getFeedId() {
        return feedId;
    }

    public void setFeedId(long feedId) {
        this.feedId = feedId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getReplyId() {
        return replyId;
    }

    public void setReplyId(long replyId) {
        this.replyId = replyId;
    }

    public long getCreated() {
        return created;
    }

    public void setCreated(long created) {
        this.created = created;
    }

    public PostUserInfoBean getFromUserInfo() {
        return fromUserInfo;
    }

    public void setFromUserInfo(PostUserInfoBean fromUserInfo) {
        this.fromUserInfo = fromUserInfo;
    }

    public PostUserInfoBean getToUserInfo() {
        return toUserInfo;
    }

    public void setToUserInfo(PostUserInfoBean toUserInfo) {
        this.toUserInfo = toUserInfo;
    }

}

