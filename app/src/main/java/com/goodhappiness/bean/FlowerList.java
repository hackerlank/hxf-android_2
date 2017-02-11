package com.goodhappiness.bean;

import java.util.List;

/**
 * Created by 电脑 on 2016/7/19.
 */
public class FlowerList extends BaseBean{

    /**
     * list : [{"nickname":"this is a joke","avatar":"http://7xsgec.com2.z0.glb.qiniucdn.com/FgUmqLcfJ-0M1WUiPOJMmUUIMX4F","uid":100021,"num":1,"created":1468580464},{"nickname":"this is a joke","avatar":"http://7xsgec.com2.z0.glb.qiniucdn.com/FgUmqLcfJ-0M1WUiPOJMmUUIMX4F","uid":100021,"num":1,"created":1468580273},{"nickname":"this is a joke","avatar":"http://7xsgec.com2.z0.glb.qiniucdn.com/FgUmqLcfJ-0M1WUiPOJMmUUIMX4F","uid":100021,"num":1,"created":1468579616},{"nickname":"this is a joke","avatar":"http://7xsgec.com2.z0.glb.qiniucdn.com/FgUmqLcfJ-0M1WUiPOJMmUUIMX4F","uid":100021,"num":1,"created":1468578995}]
     * more : 2
     * total : 4
     */

    private int more;
    private int total;
    /**
     * nickname : this is a joke
     * avatar : http://7xsgec.com2.z0.glb.qiniucdn.com/FgUmqLcfJ-0M1WUiPOJMmUUIMX4F
     * uid : 100021
     * num : 1
     * created : 1468580464
     */

    private List<ListBean> list;

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

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public static class ListBean {
        private String nickname;
        private String avatar;
        private long uid;
        private int num;
        private long created;

        public String getNickname() {
            return nickname;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public String getAvatar() {
            return avatar;
        }

        public void setAvatar(String avatar) {
            this.avatar = avatar;
        }

        public long getUid() {
            return uid;
        }

        public void setUid(long uid) {
            this.uid = uid;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public long getCreated() {
            return created;
        }

        public void setCreated(long created) {
            this.created = created;
        }
    }
}
