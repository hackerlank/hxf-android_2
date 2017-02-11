package com.goodhappiness.bean.database;

import org.litepal.crud.DataSupport;

/**
 * Created by 电脑 on 2016/9/5.
 */
public class SearchHistory extends DataSupport {
    private int id;
    private String item;

    public SearchHistory(String item) {
        this.item = item;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "SearchHistory{" +
                "id=" + id +
                ", item='" + item + '\'' +
                '}';
    }
}
