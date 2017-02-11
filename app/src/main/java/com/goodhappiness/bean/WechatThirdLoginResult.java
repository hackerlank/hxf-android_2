package com.goodhappiness.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 电脑 on 2016/8/25.
 */
public class WechatThirdLoginResult implements Serializable{


    private static final long serialVersionUID = -8074112722420427977L;
    /**
     * openid : ofUw6v8nkIVnkgs8_JDFruESYM6U
     * nickname : yyyyyy.
     * sex : 1
     * language : zh_CN
     * city :
     * province :
     * country : CN
     * headimgurl : http://wx.qlogo.cn/mmopen/PiajxSqBRaEJzBqgjftHU0SceRc9AleAGIjojnJib2IkHH7Sxqx9VhuWV9d1J7wzfiazhGRXGZcU0UoZ6ejRicPDdA/0
     * privilege : []
     * unionid : oTEXlwOEeNFTLMNVruzhdwGYesxY
     */

    private String openid;
    private String nickname;
    private int sex;
    private String language;
    private String city;
    private String province;
    private String country;
    private String headimgurl;
    private String unionid;
    private List<?> privilege;

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getSex() {
        return sex;
    }

    public void setSex(int sex) {
        this.sex = sex;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public List<?> getPrivilege() {
        return privilege;
    }

    public void setPrivilege(List<?> privilege) {
        this.privilege = privilege;
    }
}
