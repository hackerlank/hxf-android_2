package com.goodhappiness.bean;

import java.util.List;

/**
 * Created by 电脑 on 2016/4/23.
 */
public class DuoQuanRecord extends BaseBean {

    /**
     * list : [{"user":{"nickname":"ccxcxz","uid":27,"IPAddress":"","IP":"113.87.212.152","avatar":"","mobile":""},"time":"2016-04-23 14:18:38:000","num":984},{"user":{"nickname":"ccxcxz","uid":27,"IPAddress":"","IP":"113.87.212.152","avatar":"","mobile":""},"time":"2016-04-23 12:18:22:000","num":16}]
     * more : 2
     * total : 2
     */

    private int more;
    private int total;
    /**
     * user : {"nickname":"ccxcxz","uid":27,"IPAddress":"","IP":"113.87.212.152","avatar":"","mobile":""}
     * time : 2016-04-23 14:18:38:000
     * num : 984
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
        /**
         * nickname : ccxcxz
         * uid : 27
         * IPAddress :
         * IP : 113.87.212.152
         * avatar :
         * mobile :
         */

        private UserBean user;
        private String time;
        private int num;

        public UserBean getUser() {
            return user;
        }

        public void setUser(UserBean user) {
            this.user = user;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public int getNum() {
            return num;
        }

        public void setNum(int num) {
            this.num = num;
        }

        public static class UserBean {
            private String nickname;
            private int uid;
            private String IPAddress;
            private String IP;
            private String avatar;
            private String mobile;

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public int getUid() {
                return uid;
            }

            public void setUid(int uid) {
                this.uid = uid;
            }

            public String getIPAddress() {
                return IPAddress;
            }

            public void setIPAddress(String IPAddress) {
                this.IPAddress = IPAddress;
            }

            public String getIP() {
                return IP;
            }

            public void setIP(String IP) {
                this.IP = IP;
            }

            public String getAvatar() {
                return avatar;
            }

            public void setAvatar(String avatar) {
                this.avatar = avatar;
            }

            public String getMobile() {
                return mobile;
            }

            public void setMobile(String mobile) {
                this.mobile = mobile;
            }
        }
    }
}
