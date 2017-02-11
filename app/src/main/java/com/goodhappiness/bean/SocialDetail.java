package com.goodhappiness.bean;

import java.util.List;

/**
 * Created by 电脑 on 2016/5/12.
 */
public class SocialDetail extends BaseBean {


    private FeedInfo feedInfo;

    private CommentBean comment;

    private LikeBean like;

    public FeedInfo getFeedInfo() {
        return feedInfo;
    }

    public void setFeedInfo(FeedInfo feedInfo) {
        this.feedInfo = feedInfo;
    }

    public CommentBean getComment() {
        return comment;
    }

    public void setComment(CommentBean comment) {
        this.comment = comment;
    }

    public LikeBean getLike() {
        return like;
    }

    public void setLike(LikeBean like) {
        this.like = like;
    }

    public static class FeedInfoBean {
        /**
         * uid : 27
         * avatar : http://7xsgec.com2.z0.glb.qiniucdn.com/FjNSe8OooMDvZJs3WCI_AY_6z79W
         * nickname : 腾龙万万觅
         * followingNum : 0
         * followerNum : 0
         */

        private PostUserInfoBean postUserInfo;
        private int feedId;
        private int feedType;
        private String postContent;
        private int commentNum;
        private int likeNum;
        private int created;
        private String isLike;
        /**
         * fileId : FsfQpdLFJ_lZgXbB1X8YVDvXWHjn
         * fileUrl : http://7xsgec.com2.z0.glb.qiniucdn.com/FsfQpdLFJ_lZgXbB1X8YVDvXWHjn
         * fileType : 1
         */

        private List<PostFilesBean> postFiles;

        public PostUserInfoBean getPostUserInfo() {
            return postUserInfo;
        }

        public void setPostUserInfo(PostUserInfoBean postUserInfo) {
            this.postUserInfo = postUserInfo;
        }

        public int getFeedId() {
            return feedId;
        }

        public void setFeedId(int feedId) {
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

        public String getIsLike() {
            return isLike;
        }

        public void setIsLike(String isLike) {
            this.isLike = isLike;
        }

        public List<PostFilesBean> getPostFiles() {
            return postFiles;
        }

        public void setPostFiles(List<PostFilesBean> postFiles) {
            this.postFiles = postFiles;
        }

        public static class PostUserInfoBean {
            private int uid;
            private String avatar;
            private String nickname;
            private int followingNum;
            private int followerNum;

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
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

        public static class PostFilesBean {
            private String fileId;
            private String fileUrl;
            private int fileType;

            public String getFileId() {
                return fileId;
            }

            public void setFileId(String fileId) {
                this.fileId = fileId;
            }

            public String getFileUrl() {
                return fileUrl;
            }

            public void setFileUrl(String fileUrl) {
                this.fileUrl = fileUrl;
            }

            public int getFileType() {
                return fileType;
            }

            public void setFileType(int fileType) {
                this.fileType = fileType;
            }
        }
    }

    public static class CommentBean {
        private int more;
        private int total;
        /**
         * commentId : 5
         * feedId : 3
         * content : 顾家家居
         * replyId : 0
         * created : 1463200308
         * fromUserInfo : {"uid":25,"avatar":"http://7xsgec.com2.z0.glb.qiniucdn.com/25-1461747395988.jpg","nickname":"波波呢是"}
         * toUserInfo : {}
         */

        private List<Comment> commentList;

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

        public List<Comment> getCommentList() {
            return commentList;
        }

        public void setCommentList(List<Comment> commentList) {
            this.commentList = commentList;
        }

    }

    public static class LikeBean {
        private int more;
        private int total;
        /**
         * uid : 25
         * avatar : http://7xsgec.com2.z0.glb.qiniucdn.com/25-1461747395988.jpg
         * nickname : 波波呢是
         * relation : 0
         */

        private List<Like> likeList;

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

        public List<Like> getLikeList() {
            return likeList;
        }

        public void setLikeList(List<Like> likeList) {
            this.likeList = likeList;
        }

    }
}
