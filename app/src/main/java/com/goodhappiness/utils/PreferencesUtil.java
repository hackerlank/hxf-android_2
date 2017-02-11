package com.goodhappiness.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.bean.Register;
import com.goodhappiness.bean.ThirdLoginResult;
import com.goodhappiness.bean.UserInfo;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.dao.MyConnectionStatusListener;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;

import io.rong.imkit.RongIM;

/**
 * SharePreference工具类
 *
 * @author lanyan
 */
public class PreferencesUtil {
    private static String PreferenceName = "Constant";

    public static String getSid(){
        if(TextUtils.isEmpty(PreferencesUtil.getStringPreferences(GoodHappinessApplication.getContext(),FieldFinals.SID))){
            return "";
        }
        return PreferencesUtil.getStringPreferences(GoodHappinessApplication.getContext(),FieldFinals.SID);
    }
    public static String getDid(){
        return PreferencesUtil.getStringPreferences(GoodHappinessApplication.getContext(),FieldFinals.DEVICE_IDENTIFIER);
    }
    public static long getUid() {
        return PreferencesUtil.getLongPreferences(GoodHappinessApplication.getContext(),FieldFinals.UID,0);
    }
    public static String getNickName() {
        return PreferencesUtil.getStringPreferences(GoodHappinessApplication.getContext(),FieldFinals.NICKNAME);
    }
    public static void saveUserInfo(Context context, UserInfo userInfo) {
        setPreferences(context, FieldFinals.UID, userInfo.getUid());
        setPreferences(context, FieldFinals.IS_BIND, userInfo.getIsBind());
        setPreferences(context, FieldFinals.MOBILE, userInfo.getMobile());
        setPreferences(context, FieldFinals.AVATAR, userInfo.getAvatar());
        setPreferences(context, FieldFinals.NICKNAME, userInfo.getNickname());
        setPreferences(context, FieldFinals.MONEY, userInfo.getMoney());
        setPreferences(context, FieldFinals.HAPPY_COIN, userInfo.getHappyCoin());
        setPreferences(context, FieldFinals.GENERAL_COIN, userInfo.getGeneralCoin());
    }

    public static void saveUserInfo(Context context, Register register) {
        setPreferences(context, FieldFinals.SID, register.getSid());
        setPreferences(context, FieldFinals.CHAT_TOKEN, register.getChatToken());
        setPreferences(context, FieldFinals.NICKNAME, register.getUserInfo().getNickname());
        setPreferences(context, FieldFinals.UID, register.getUserInfo().getUid());
        setPreferences(context, FieldFinals.IP_ADDRESS, register.getUserInfo().getIPAddress());
        setPreferences(context, FieldFinals.IP, register.getUserInfo().getIP());
        setPreferences(context, FieldFinals.AVATAR, register.getUserInfo().getAvatar());
        setPreferences(context, FieldFinals.MOBILE, register.getUserInfo().getMobile());
        if (!TextUtils.isEmpty(register.getChatToken())) {
            RongIMUtils.connect(context, register.getChatToken());
            RongIM.getInstance().setCurrentUserInfo(new io.rong.imlib.model.UserInfo(String.valueOf(register.getUserInfo().getUid()), String.valueOf(register.getUserInfo().getNickname()), Uri.parse(register.getUserInfo().getAvatar())));
            RongIM.getInstance().setMessageAttachedUserInfo(true);
            RongIM.getInstance().getRongIMClient().setConnectionStatusListener(new MyConnectionStatusListener(GoodHappinessApplication.getContext()));
        }
    }
    public static void saveUserInfo(Context context, ThirdLoginResult register) {
        setPreferences(context, FieldFinals.SID, register.getSid());
        setPreferences(context, FieldFinals.CHAT_TOKEN, register.getChatToken());
        setPreferences(context, FieldFinals.NICKNAME, register.getUserInfo().getNickname());
        setPreferences(context, FieldFinals.UID, register.getUserInfo().getUid());
        setPreferences(context, FieldFinals.IP_ADDRESS, register.getUserInfo().getIPAddress());
        setPreferences(context, FieldFinals.IP, register.getUserInfo().getIP());
        setPreferences(context, FieldFinals.AVATAR, register.getUserInfo().getAvatar());
        setPreferences(context, FieldFinals.MOBILE, register.getUserInfo().getMobile());
        if (!TextUtils.isEmpty(register.getChatToken())) {
            RongIMUtils.connect(context, register.getChatToken());
            RongIM.getInstance().setCurrentUserInfo(new io.rong.imlib.model.UserInfo(String.valueOf(register.getUserInfo().getUid()), String.valueOf(register.getUserInfo().getNickname()), Uri.parse(register.getUserInfo().getAvatar())));
            RongIM.getInstance().setMessageAttachedUserInfo(true);
            RongIM.getInstance().getRongIMClient().setConnectionStatusListener(new MyConnectionStatusListener(GoodHappinessApplication.getContext()));
        }
    }

    public static void clearUserInfo(Context context) {
        setPreferences(context, FieldFinals.SID, "");
        setPreferences(context, FieldFinals.CHAT_TOKEN, "");
        setPreferences(context, FieldFinals.NICKNAME, "");
        setPreferences(context, FieldFinals.UID, 0L);
        setPreferences(context, FieldFinals.IP_ADDRESS, null);
        setPreferences(context, FieldFinals.IP, "");
        setPreferences(context, FieldFinals.AVATAR, null);
        setPreferences(context, FieldFinals.MOBILE, "");
        setPreferences(context, FieldFinals.IMAGE_FILE_PATH, "");
        setPreferences(context, FieldFinals.COOKIE, "");
        PreferencesUtil.setPreferences(context, FieldFinals.FOCUS_REFRESH, true);
        PreferencesUtil.setPreferences(context, FieldFinals.WORLD_REFRESH, true);
        PreferencesUtil.setPreferences(context, FieldFinals.FIRST_SET_IM_CONFIG, true);
        RongIM.getInstance().logout();
//        DbCookieStore dbCookieStore = DbCookieStore.INSTANCE;
//        List<URI> uris = dbCookieStore.getURIs();
//        for(URI i:uris){
//            i.getAuthority();
//        }
//        dbCookieStore.removeAll();
    }

    public static void clearHeadImgPathInfo(Context context) {
        setPreferences(context, FieldFinals.IMAGE_FILE_PATH, "");
    }

    /**
     * 获取SharePreference中的String类型值
     */
    public static String getStringPreferences(Context context, String name) {
        SharedPreferences sp = GoodHappinessApplication.getContext().getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);

        // 获取数据
        return sp.getString(name, "");
    }

    /**
     * 将String信息存入Preferences
     *
     * @param context
     * @param name
     * @param value
     */
    public static void setPreferences(Context context, String name, String value) {
        SharedPreferences sp = context.getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);

        // 存入数据
        Editor editor = sp.edit();
        editor.putString(name, value);
        editor.commit();
    }

    /**
     * 获取SharePreference中的值
     */
    public static boolean getBooleanPreferences(Context context, String name,
                                                boolean defValue) {
        SharedPreferences sp = context.getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);

        // 获取数据
        return sp.getBoolean(name, defValue);
    }

    /**
     * 将boolean信息存入Preferences
     *
     * @param context
     * @param name
     * @param value
     */
    public static void setPreferences(Context context, String name,
                                      boolean value) {
        SharedPreferences sp = context.getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);

        // 存入数据
        Editor editor = sp.edit();
        editor.putBoolean(name, value);
        editor.commit();
    }

    /**
     * 获取SharePreference中的int类型值
     */
    public static int getIntPreferences(Context context, String name,
                                        int defValue) {
        SharedPreferences sp = context.getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);

        // 获取数据
        return sp.getInt(name, defValue);
    }

    public static long getLongPreferences(Context context, String name,
                                          long defValue) {
        SharedPreferences sp = context.getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);

        // 获取数据
        return sp.getLong(name, defValue);
    }

    /**
     * 将int信息存入Preferences
     *
     * @param context
     * @param name
     * @param value
     */
    public static void setPreferences(Context context, String name, int value) {
        SharedPreferences sp = context.getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);

        // 存入数据
        Editor editor = sp.edit();
        editor.putInt(name, value);
        editor.commit();
    }

    /**
     * 获取SharePreference中的值
     */
    public static float getFloatPreferences(Context context, String name,
                                            float defValue) {
        SharedPreferences sp = context.getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);

        // 获取数据
        return sp.getFloat(name, defValue);
    }

    /**
     * 将float信息存入Preferences
     *
     * @param context
     * @param name
     * @param value
     */
    public static void setPreferences(Context context, String name, float value) {
        SharedPreferences sp = context.getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);

        // 存入数据
        Editor editor = sp.edit();
        editor.putFloat(name, value);
        editor.commit();
    }

    public static void setPreferences(Context context, String name, long value) {
        SharedPreferences sp = context.getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);

        // 存入数据
        Editor editor = sp.edit();
        editor.putLong(name, value);
        editor.commit();
    }

    /**
     * 将Object信息Base64后存入Preferences
     *
     * @param context
     * @param name
     * @param obj
     */

    public static <T> void setPreferences(Context context, String name, T obj) {
        SharedPreferences sp = context.getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);
        Editor editor = sp.edit();
        if (obj == null) {
            editor.putString(name, null);
            editor.commit();
            return;
        }

        try {
            ByteArrayOutputStream toByte = new ByteArrayOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(toByte);
            oos.writeObject(obj);

            // 对byte[]进行Base64编码
            String obj64 = new String(Base64.encode(toByte.toByteArray(),
                    Base64.DEFAULT));

            editor.putString(name, obj64);
            editor.commit();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取SharePreference中值，Base64解码之后传出
     *
     * @param context
     * @param name
     */
    @SuppressWarnings("unchecked")
    public static <T> T getPreferences(Context context, String name) {
        if(context==null){
            return null;
        }
        SharedPreferences sp = context.getSharedPreferences(PreferenceName,
                Context.MODE_PRIVATE);

        try {
            String obj64 = sp.getString(name, "");
            if (TextUtils.isEmpty(obj64)) {
                return null;
            }
            byte[] base64Bytes = Base64.decode(obj64, Base64.DEFAULT);
            ByteArrayInputStream bais = new ByteArrayInputStream(base64Bytes);
            ObjectInputStream ois = new ObjectInputStream(bais);
            return (T) ois.readObject();
        } catch (StreamCorruptedException e) {
            e.printStackTrace();
        } catch (OptionalDataException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }


}
