package com.goodhappiness.bean;

import java.util.List;

/**
 * Created by 电脑 on 2016/5/16.
 */
public class FriendShip extends BaseBean {

    /**
     * friendshipList : [{"uid":27,"avatar":"http://7xsgec.com2.z0.glb.qiniucdn.com/FjNSe8OooMDvZJs3WCI_AY_6z79W","nickname":"腾龙万万觅","relation":3},{"uid":25,"avatar":"http://7xsgec.com2.z0.glb.qiniucdn.com/25-1461747395988.jpg","nickname":"波波呢是","relation":0}]
     * more : 2
     * total : 2
     */

    private int more;
    private int total;
    /**
     * uid : 27
     * avatar : http://7xsgec.com2.z0.glb.qiniucdn.com/FjNSe8OooMDvZJs3WCI_AY_6z79W
     * nickname : 腾龙万万觅
     * relation : 3
     */

    private List<PostUserInfoBean> list;

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

    public List<PostUserInfoBean> getFriendshipList() {
        return list;
    }

    public void setFriendshipList(List<PostUserInfoBean> friendshipList) {
        this.list = friendshipList;
    }

}
