package com.goodhappiness.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 电脑 on 2016/4/11.
 */
public class SubmitOrder extends BaseBean implements Serializable, Parcelable {


    private static final long serialVersionUID = 7682060728630595255L;
    /**
     * order : [{"cid":"570b66783feed","created_at":1460364920,"info":{"existingTimes":0,"goods":{"gid":1,"name":"幸福劵 5张","desc":"幸福劵 5张","price":5000,"buyUnit":1,"pic":[]},"period":1,"status":1},"num":7,"renew":0},{"cid":"570b667922a0c","created_at":1460364921,"info":{"existingTimes":0,"goods":{"gid":2,"name":"幸福劵 10张","desc":"幸福劵 10张","price":10000,"buyUnit":2,"pic":[]},"period":3,"status":1},"num":5,"renew":0},{"cid":"570b667a78830","created_at":1460364922,"info":{"existingTimes":0,"goods":{"gid":3,"name":"幸福劵 20张","desc":"幸福劵 20张","price":20000,"buyUnit":1,"pic":[]},"period":5,"status":1},"num":5,"renew":0},{"cid":"570b22d951947","created_at":1460347609,"info":{"existingTimes":0,"goods":{"gid":4,"name":"幸福劵 40张","desc":"幸福劵 40张","price":40000,"buyUnit":2,"pic":[]},"period":7,"status":1},"num":5,"renew":0}]
     * fees : 22
     * total : 4
     */

    private int fees;
    private int total;
    private int discountPrice;
    private List<Car> order;
    private List<Redbag> avaliRedBag;
    private List<Redbag> notAvaliRedBag;

    public int getDiscountPrice() {
        return discountPrice;
    }

    public void setDiscountPrice(int discountPrice) {
        this.discountPrice = discountPrice;
    }

    public List<Car> getOrder() {
        return order;
    }

    public void setOrder(List<Car> order) {
        this.order = order;
    }

    public int getFees() {
        return fees;
    }

    public void setFees(int fees) {
        this.fees = fees;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Redbag> getAvaliRedBag() {
        return avaliRedBag;
    }

    public void setAvaliRedBag(List<Redbag> avaliRedBag) {
        this.avaliRedBag = avaliRedBag;
    }

    public List<Redbag> getNotAvaliRedBag() {
        return notAvaliRedBag;
    }

    public void setNotAvaliRedBag(List<Redbag> notAvaliRedBag) {
        this.notAvaliRedBag = notAvaliRedBag;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.fees);
        dest.writeInt(this.total);
        dest.writeInt(this.discountPrice);
        dest.writeTypedList(this.order);
        dest.writeTypedList(this.avaliRedBag);
        dest.writeTypedList(this.notAvaliRedBag);
    }

    public SubmitOrder() {
    }

    protected SubmitOrder(Parcel in) {
        this.fees = in.readInt();
        this.total = in.readInt();
        this.discountPrice = in.readInt();
        this.order = in.createTypedArrayList(Car.CREATOR);
        this.avaliRedBag = in.createTypedArrayList(Redbag.CREATOR);
        this.notAvaliRedBag = in.createTypedArrayList(Redbag.CREATOR);
    }

    public static final Parcelable.Creator<SubmitOrder> CREATOR = new Parcelable.Creator<SubmitOrder>() {
        @Override
        public SubmitOrder createFromParcel(Parcel source) {
            return new SubmitOrder(source);
        }

        @Override
        public SubmitOrder[] newArray(int size) {
            return new SubmitOrder[size];
        }
    };
}
