package com.goodhappiness.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by 电脑 on 2016/4/9.
 */
public class Car extends BaseBean implements Serializable, Parcelable {

    private static final long serialVersionUID = -8079004401630436202L;
    /**
     * cid : 5708fcd78a42d
     * created_at : 1460206807
     * info : {"existingTimes":0,"goods":{"gid":1,"name":"幸福劵 10张","desc":"123123","price":1000,"buyUnit":1,"pic":[]},"period":"1","status":1}
     * num : 5
     * renew : 0
     */
    private boolean is_select = false;
    private boolean is_setEditChangeListener = false;
    private String cid;
    private int created_at;
    private Info info;
    /**
     * existingTimes : 0
     * goods : {"gid":1,"name":"幸福劵 10张","desc":"123123","price":1000,"buyUnit":1,"pic":[]}
     * period : 1
     * status : 1
     */

    private int num;
    private int renew;
    private String mention;
    private String tips;

    public String getTips() {
        return tips;
    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public String getMention() {
        return mention;
    }

    public void setMention(String mention) {
        this.mention = mention;
    }

    public boolean is_setEditChangeListener() {
        return is_setEditChangeListener;
    }

    public void setIs_setEditChangeListener(boolean is_setEditChangeListener) {
        this.is_setEditChangeListener = is_setEditChangeListener;
    }

    public boolean is_select() {
        return is_select;
    }

    public void setIs_select(boolean is_select) {
        this.is_select = is_select;
    }

    public Info getInfo() {
        return info;
    }

    public void setInfo(Info info) {
        this.info = info;
    }

    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public int getCreated_at() {
        return created_at;
    }

    public void setCreated_at(int created_at) {
        this.created_at = created_at;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getRenew() {
        return renew;
    }

    public void setRenew(int renew) {
        this.renew = renew;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeByte(is_select ? (byte) 1 : (byte) 0);
        dest.writeByte(is_setEditChangeListener ? (byte) 1 : (byte) 0);
        dest.writeString(this.cid);
        dest.writeInt(this.created_at);
        dest.writeParcelable(this.info, flags);
        dest.writeInt(this.num);
        dest.writeInt(this.renew);
    }

    public Car() {
    }

    protected Car(Parcel in) {
        this.is_select = in.readByte() != 0;
        this.is_setEditChangeListener = in.readByte() != 0;
        this.cid = in.readString();
        this.created_at = in.readInt();
        this.info = in.readParcelable(Info.class.getClassLoader());
        this.num = in.readInt();
        this.renew = in.readInt();
    }

    public static final Parcelable.Creator<Car> CREATOR = new Parcelable.Creator<Car>() {
        @Override
        public Car createFromParcel(Parcel source) {
            return new Car(source);
        }

        @Override
        public Car[] newArray(int size) {
            return new Car[size];
        }
    };
}
