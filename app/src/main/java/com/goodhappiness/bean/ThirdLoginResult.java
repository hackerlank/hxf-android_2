package com.goodhappiness.bean;

/**
 * Created by 电脑 on 2016/8/25.
 */
public class ThirdLoginResult extends BaseBean {

    /**
     * isBind : 1
     * sid : SESSION:21CF01AAA8672A3C8E096DD1995A8791E68C769A
     * userInfo : {"uid":104080,"nickname":"向天空飞翔","IPAddress":"","IP":"14.20.98.114","avatar":"123123","mobile":"13798211114"}
     * deviceIdentifier : 12d8f865078d7af7427ec9b67a7b2e37
     * chatToken : qNsX9h/VbQ8G6tIjenGwQJI1AXYxYQMfjjpoa9pccPAWMTVVc6uheIhADXg+bcL/asIGGRpC7P2+T5iws9e2Mw==
     */

    private int isBind;
    private String sid;
    /**
     * uid : 104080
     * nickname : 向天空飞翔
     * IPAddress :
     * IP : 14.20.98.114
     * avatar : 123123
     * mobile : 13798211114
     */

    private UserInfo userInfo;
    private String deviceIdentifier;
    private String chatToken;

    public int getIsBind() {
        return isBind;
    }

    public void setIsBind(int isBind) {
        this.isBind = isBind;
    }

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

    @Override
    public String toString() {
        return "ThirdLoginResult{" +
                "isBind=" + isBind +
                ", sid='" + sid + '\'' +
                ", userInfo=" + userInfo +
                ", deviceIdentifier='" + deviceIdentifier + '\'' +
                ", chatToken='" + chatToken + '\'' +
                '}';
    }
}
