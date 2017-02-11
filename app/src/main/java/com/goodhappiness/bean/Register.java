package com.goodhappiness.bean;

/**
 * Created by 电脑 on 2016/4/9.
 */
public class Register extends BaseBean{

    /**
     * sid : SESSION-de6d423e2f5aeefb98662553156a56e2-25
     * userInfo : {"nickname":"13728855025","uid":25,"IPAddress":null,"IP":"119.122.232.60","avatar":null,"mobile":"13728855025"}
     */

    private String sid;
    private String deviceIdentifier;
    private String chatToken;

    public String getDeviceIdentifier() {
        return deviceIdentifier;
    }

    public void setDeviceIdentifier(String deviceIdentifier) {
        this.deviceIdentifier = deviceIdentifier;
    }

    public String getChatToken() {
        return chatToken;
    }

    public void setChatToken(String chatToken) {
        this.chatToken = chatToken;
    }

    /**
     * nickname : 13728855025
     * uid : 25
     * IPAddress : null
     * IP : 119.122.232.60
     * avatar : null
     * mobile : 13728855025
     */

    private UserInfo userInfo;

    public String getSid() {
        return sid;
    }

    public void setSid(String sid) {
        this.sid = sid;
    }

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    @Override
    public String toString() {
        return "Register{" +
                "sid='" + sid + '\'' +
                ", userInfo=" + userInfo +
                '}';
    }
}
