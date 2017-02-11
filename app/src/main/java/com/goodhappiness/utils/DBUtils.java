package com.goodhappiness.utils;

import android.util.Log;

import com.goodhappiness.bean.database.PUserInfo;
import com.goodhappiness.bean.database.SearchHistory;

import org.litepal.crud.DataSupport;

import java.util.List;

import io.rong.imlib.model.UserInfo;

/**
 * Created by 电脑 on 2016/7/21.
 */
public class DBUtils {
    //--------------------------------------SearchHistory--------------------------------------------
    public static List<SearchHistory> getSearchHistory() {
        return DataSupport.findAll(SearchHistory.class);
    }

    public static void insertSearchHistory(String item) {
        List<SearchHistory> list = getSearchHistory();
        if (list != null) {
            for(SearchHistory history:list){
                if(history.getItem().equals(item)){
                    return;
                }
            }
            if (list.size() >= 10) {
                DataSupport.findFirst(SearchHistory.class).delete();
            }

        }
        new SearchHistory(item).save();
    }
    //--------------------------------------SearchHistory--------------------------------------------

    //--------------------------------------User--------------------------------------------
    public static void setUserInfoToDataBase(UserInfo userInfo) {
        if (isUserInDataBase(userInfo.getUserId())) {
            List<PUserInfo> list = DataSupport.where("targetid = ?", userInfo.getUserId()).find(PUserInfo.class);
            for (PUserInfo info : list) {
                info.setName(userInfo.getName());
                info.setAvatar(userInfo.getPortraitUri().toString());
                info.update(info.getId());
            }
//            ContentValues values = new ContentValues();
//            values.put("name", userInfo.getName());
//            values.put("avatar", userInfo.getPortraitUri().toString());
//            DataSupport.update(PUserInfo.class, values, "targetid = ?", userInfo.getUserId());
        } else {
            PUserInfo pUserInfo = new PUserInfo(userInfo.getUserId(), userInfo.getName(), userInfo.getPortraitUri().toString());
            if (pUserInfo.save()) {
                Log.e("database", "userInfo save success! name:" + pUserInfo.getName());
            } else {
                Log.e("database", "userInfo save fail!");
            }
        }
    }

    public static PUserInfo getUserInDataBase(String id) {
        PUserInfo pUserInfo = null;
        if (isUserInDataBase(id)) {
            List<PUserInfo> list = DataSupport.where("targetid = ?", id).find(PUserInfo.class);
            for (PUserInfo info : list) {
                pUserInfo = info;
            }
        }
        return pUserInfo;
    }

    public static boolean isUserInDataBase(String id) {
        List<PUserInfo> list = DataSupport.where("targetid = ?", id).find(PUserInfo.class);
        if (list != null) {
            if (list.size() > 0) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
    //--------------------------------------User--------------------------------------------
}
