package com.goodhappiness.bean;

import java.util.List;

/**
 * Created by 电脑 on 2016/4/9.
 */
public class CarList extends BaseBean{
    private List<Car> list;

    public List<Car> getCars() {
        return list;
    }

    public void setCars(List<Car> list) {
        this.list = list;
    }
}
