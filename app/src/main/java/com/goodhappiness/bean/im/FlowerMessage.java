package com.goodhappiness.bean.im;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import io.rong.common.ParcelUtils;
import io.rong.imlib.MessageTag;
import io.rong.imlib.model.MessageContent;
import io.rong.imlib.model.UserInfo;

/**
 * Created by 电脑 on 2016/7/4.
 */
@MessageTag(value = "app:flower", flag = MessageTag.ISCOUNTED | MessageTag.ISPERSISTED)
public class FlowerMessage extends MessageContent {
    private int flowerCount;
    private String receiverId;

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    @Override
    public byte[] encode() {
        JSONObject jsonObj = new JSONObject();

        try {
            jsonObj.put("flowerCount", getFlowerCount());
            jsonObj.put("receiverId", getReceiverId());
            if (this.getJSONUserInfo() != null) {
                jsonObj.putOpt("user", this.getJSONUserInfo());
            }
        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }

        try {
            return jsonObj.toString().getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return null;
    }

    public FlowerMessage(byte[] data) {
        String jsonStr = null;

        try {
            jsonStr = new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e1) {

        }

        try {
            JSONObject jsonObj = new JSONObject(jsonStr);

            if (jsonObj.has("flowerCount")) {
                flowerCount = jsonObj.optInt("flowerCount");
            }
            if (jsonObj.has("receiverId")) {
                receiverId = jsonObj.optString("receiverId");
            }
            if (jsonObj.has("user")) {
                this.setUserInfo(this.parseJsonToUserInfo(jsonObj.getJSONObject("user")));
            }

        } catch (JSONException e) {
            Log.e("JSONException", e.getMessage());
        }

    }

    public static FlowerMessage obtain(int flowerCount,String receiverId) {
        FlowerMessage model = new FlowerMessage();
        model.setFlowerCount(flowerCount);
        model.setReceiverId(receiverId);
        return model;
    }

    protected FlowerMessage() {
    }

    public FlowerMessage(Parcel in) {
        this.setFlowerCount(ParcelUtils.readIntFromParcel(in));
        this.setReceiverId(ParcelUtils.readFromParcel(in));
        this.setUserInfo((UserInfo) ParcelUtils.readFromParcel(in, UserInfo.class));
        //这里可继续增加你消息的属性
    }

    public static final Parcelable.Creator<FlowerMessage> CREATOR = new Parcelable.Creator<FlowerMessage>() {
        @Override
        public FlowerMessage createFromParcel(Parcel source) {
            return new FlowerMessage(source);
        }

        @Override
        public FlowerMessage[] newArray(int size) {
            return new FlowerMessage[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        ParcelUtils.writeToParcel(dest, this.flowerCount);
        ParcelUtils.writeToParcel(dest, this.receiverId);
        ParcelUtils.writeToParcel(dest, this.getUserInfo());
    }


    public int getFlowerCount() {
        return flowerCount;
    }

    public void setFlowerCount(int flowerCount) {
        this.flowerCount = flowerCount;
    }

}
