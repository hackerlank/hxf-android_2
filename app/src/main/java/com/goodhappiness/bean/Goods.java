package com.goodhappiness.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 电脑 on 2016/4/9.
 */
public class Goods extends BaseBean implements Serializable, Parcelable {


    private static final long serialVersionUID = 7161225883835808001L;
    private int gid;
    private int num;
    private String name;
    private String desc;
    private int price;
    private int buyUnit;
    private int goodsType;
    private List<String> pic;
    private List<String> picNew;
    private List<String> picDetails;

    public List<String> getPicNew() {
        return picNew;
    }

    public void setPicNew(List<String> picNew) {
        this.picNew = picNew;
    }

    public List<String> getPicDetails() {
        return picDetails;
    }

    public void setPicDetails(List<String> picDetails) {
        this.picDetails = picDetails;
    }

    public int getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(int goodsType) {
        this.goodsType = goodsType;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public int getGid() {
        return gid;
    }

    public void setGid(int gid) {
        this.gid = gid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getBuyUnit() {
        return buyUnit;
    }

    public void setBuyUnit(int buyUnit) {
        this.buyUnit = buyUnit;
    }

    public List<String> getPic() {
        return pic;
    }

    public void setPic(List<String> pic) {
        this.pic = pic;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.gid);
        dest.writeInt(this.num);
        dest.writeString(this.name);
        dest.writeString(this.desc);
        dest.writeInt(this.price);
        dest.writeInt(this.buyUnit);
        dest.writeInt(this.goodsType);
        dest.writeStringList(this.pic);
        dest.writeStringList(this.picNew);
        dest.writeStringList(this.picDetails);
    }

    public Goods() {
    }

    protected Goods(Parcel in) {
        this.gid = in.readInt();
        this.num = in.readInt();
        this.name = in.readString();
        this.desc = in.readString();
        this.price = in.readInt();
        this.buyUnit = in.readInt();
        this.goodsType = in.readInt();
        this.pic = in.createStringArrayList();
        this.picNew = in.createStringArrayList();
        this.picDetails = in.createStringArrayList();
    }

    public static final Parcelable.Creator<Goods> CREATOR = new Parcelable.Creator<Goods>() {
        @Override
        public Goods createFromParcel(Parcel source) {
            return new Goods(source);
        }

        @Override
        public Goods[] newArray(int size) {
            return new Goods[size];
        }
    };
}
