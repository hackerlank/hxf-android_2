package com.goodhappiness.bean;

import java.util.List;

/**
 * Created by 电脑 on 2016/4/21.
 */
public class AddressList extends BaseBean  {

    /**
     * list : [{"address_id":12,"username":"123","address":"123","mobile":"13798211177","is_default":0,"province_id":440300,"city_id":440300,"area_id":440300}]
     * more : 2
     * total : 1
     */

    private int more;
    private int total;
    /**
     * address_id : 12
     * username : 123
     * address : 123
     * mobile : 13798211177
     * is_default : 0
     * province_id : 440300
     * city_id : 440300
     * area_id : 440300
     */

    private List<Address> list;

    public int getMore() {
        return more;
    }

    public void setMore(int more) {
        this.more = more;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public List<Address> getList() {
        return list;
    }

    public void setList(List<Address> list) {
        this.list = list;
    }


}
