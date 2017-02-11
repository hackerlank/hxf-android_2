package com.goodhappiness.bean;

import java.util.List;

/**
 * Created by 电脑 on 2016/5/16.
 */
public class CommentList extends BaseBean {

    /**
     * commentList : [{"commentId":37,"feedId":6,"content":"哈哈哈","replyId":0,"created":1463368915,"fromUserInfo":{"uid":27,"avatar":"http://7xsgec.com2.z0.glb.qiniucdn.com/FjNSe8OooMDvZJs3WCI_AY_6z79W","nickname":"腾龙万万觅"},"toUserInfo":{}},{"commentId":36,"feedId":6,"content":"DsaDSAdsdsaDSAJkdhjskaJDKSJA;lfjdsal;JDLSA;Jd;lsaJ","replyId":0,"created":1463366588,"fromUserInfo":{"uid":27,"avatar":"http://7xsgec.com2.z0.glb.qiniucdn.com/FjNSe8OooMDvZJs3WCI_AY_6z79W","nickname":"腾龙万万觅"},"toUserInfo":{}},{"commentId":35,"feedId":6,"content":"Fdsafdsa;folds;jack;dsjaklfjkdlsajlkfdjsalkjfdlsajfkldjsalk;fuel;said;flash floods;dash;lfdjsal;Joel;Sahel;Dana;lfjdsal;Jeff;salad;lads and;leash;lfjdsal;lfjdsal;sajfdklsajfldsajlfdjasljfdsajfldsjalfjdsaljfldsajfldsjalfjdslajfldsjalfjdsklajflkdsajlkfdjsalkjfdslajflkdsajlfjdslajfldsjalfdjslajfldsjalfjdslajfldjsaljfdlsajfldjsaljfdslajfldsajf","replyId":0,"created":1463365709,"fromUserInfo":{"uid":27,"avatar":"http://7xsgec.com2.z0.glb.qiniucdn.com/FjNSe8OooMDvZJs3WCI_AY_6z79W","nickname":"腾龙万万觅"},"toUserInfo":{}},{"commentId":34,"feedId":6,"content":"Aaaaa ","replyId":0,"created":1463364061,"fromUserInfo":{"uid":27,"avatar":"http://7xsgec.com2.z0.glb.qiniucdn.com/FjNSe8OooMDvZJs3WCI_AY_6z79W","nickname":"腾龙万万觅"},"toUserInfo":{}},{"commentId":33,"feedId":6,"content":"Fdsafdsa","replyId":0,"created":1463364052,"fromUserInfo":{"uid":27,"avatar":"http://7xsgec.com2.z0.glb.qiniucdn.com/FjNSe8OooMDvZJs3WCI_AY_6z79W","nickname":"腾龙万万觅"},"toUserInfo":{}},{"commentId":32,"feedId":6,"content":"Fdsafdsa","replyId":0,"created":1463364038,"fromUserInfo":{"uid":27,"avatar":"http://7xsgec.com2.z0.glb.qiniucdn.com/FjNSe8OooMDvZJs3WCI_AY_6z79W","nickname":"腾龙万万觅"},"toUserInfo":{}},{"commentId":31,"feedId":6,"content":"😰😰","replyId":0,"created":1463363661,"fromUserInfo":{"uid":27,"avatar":"http://7xsgec.com2.z0.glb.qiniucdn.com/FjNSe8OooMDvZJs3WCI_AY_6z79W","nickname":"腾龙万万觅"},"toUserInfo":{}},{"commentId":21,"feedId":4,"content":"Fdsafdsafdsa","replyId":0,"created":1463224196,"fromUserInfo":{"uid":27,"avatar":"http://7xsgec.com2.z0.glb.qiniucdn.com/FjNSe8OooMDvZJs3WCI_AY_6z79W","nickname":"腾龙万万觅"},"toUserInfo":{}},{"commentId":3,"feedId":5,"content":"哈哈哈哈","replyId":0,"created":1463213082,"fromUserInfo":{"uid":27,"avatar":"http://7xsgec.com2.z0.glb.qiniucdn.com/FjNSe8OooMDvZJs3WCI_AY_6z79W","nickname":"腾龙万万觅"},"toUserInfo":{}}]
     * more : 2
     * total : 9
     */

    private int more;
    private int total;
    private List<Comment> commentList;

    public List<Comment> getCommentList() {
        return commentList;
    }

    public void setCommentList(List<Comment> commentList) {
        this.commentList = commentList;
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
}
