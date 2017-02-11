package com.goodhappiness.bean;

/**
 * Created by 电脑 on 2016/8/23.
 */
public class QQAuth {

    /**
     * ret : 0
     * pay_token : 7BB4FDC123233802D0F7E61346CD67EB
     * pf : desktop_m_qq-10000144-android-2002-
     * query_authority_cost : 1295
     * authority_cost : 0
     * openid : 7E6203FF0C936898D2A9497A5F8430E2
     * expires_in : 7776000
     * pfkey : 40ec23e26c3ba0afd5826b285358e840
     * msg :
     * access_token : 2BCAFD8CF24E9A07BAD1BFD7F7865987
     * login_cost : 856
     */

    private int ret;
    private String pay_token;
    private String pf;
    private int query_authority_cost;
    private int authority_cost;
    private String openid;
    private int expires_in;
    private String pfkey;
    private String msg;
    private String access_token;
    private int login_cost;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getPay_token() {
        return pay_token;
    }

    public void setPay_token(String pay_token) {
        this.pay_token = pay_token;
    }

    public String getPf() {
        return pf;
    }

    public void setPf(String pf) {
        this.pf = pf;
    }

    public int getQuery_authority_cost() {
        return query_authority_cost;
    }

    public void setQuery_authority_cost(int query_authority_cost) {
        this.query_authority_cost = query_authority_cost;
    }

    public int getAuthority_cost() {
        return authority_cost;
    }

    public void setAuthority_cost(int authority_cost) {
        this.authority_cost = authority_cost;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public int getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(int expires_in) {
        this.expires_in = expires_in;
    }

    public String getPfkey() {
        return pfkey;
    }

    public void setPfkey(String pfkey) {
        this.pfkey = pfkey;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public int getLogin_cost() {
        return login_cost;
    }

    public void setLogin_cost(int login_cost) {
        this.login_cost = login_cost;
    }
}
