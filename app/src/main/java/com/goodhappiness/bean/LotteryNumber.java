package com.goodhappiness.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 电脑 on 2016/4/6.
 */
public class LotteryNumber extends BaseBean implements Parcelable {

    private String Number;

    public LotteryNumber(String number) {
        Number = number;
    }

    public String getNumber() {
        return Number;
    }

    public void setNumber(String number) {
        Number = number;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.Number);
    }

    protected LotteryNumber(Parcel in) {
        this.Number = in.readString();
    }

    public static final Parcelable.Creator<LotteryNumber> CREATOR = new Parcelable.Creator<LotteryNumber>() {
        @Override
        public LotteryNumber createFromParcel(Parcel source) {
            return new LotteryNumber(source);
        }

        @Override
        public LotteryNumber[] newArray(int size) {
            return new LotteryNumber[size];
        }
    };
}
