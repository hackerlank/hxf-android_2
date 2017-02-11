package com.goodhappiness.bean;

import java.io.Serializable;

/**
 * Created by 电脑 on 2016/4/13.
 */
public class Address extends BaseBean implements Serializable {

    private static final long serialVersionUID = 1593294938060706211L;
    private long addressId;
    private String username;
    private String address;
    private String mobile;
    private int is_default;
    private String province_id;
    private String city_id;
    private String area_id;
    private String addressHead;

    public String getAddressHead() {
        return addressHead;
    }

    public void setAddressHead(String addressHead) {
        this.addressHead = addressHead;
    }

    public long getAddressId() {
        return addressId;
    }

    public void setAddressId(long addressId) {
        this.addressId = addressId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public int getIs_default() {
        return is_default;
    }

    public void setIs_default(int is_default) {
        this.is_default = is_default;
    }

    public String getProvince_id() {
        return province_id;
    }

    public void setProvince_id(String province_id) {
        this.province_id = province_id;
    }

    public String getCity_id() {
        return city_id;
    }

    public void setCity_id(String city_id) {
        this.city_id = city_id;
    }

    public String getArea_id() {
        return area_id;
    }

    public void setArea_id(String area_id) {
        this.area_id = area_id;
    }

    @Override
    public String toString() {
        return "Address{" +
                "addressId=" + addressId +
                ", username='" + username + '\'' +
                ", address='" + address + '\'' +
                ", mobile='" + mobile + '\'' +
                ", is_default=" + is_default +
                ", province_id=" + province_id +
                ", city_id=" + city_id +
                ", area_id=" + area_id +
                '}';
    }
}
