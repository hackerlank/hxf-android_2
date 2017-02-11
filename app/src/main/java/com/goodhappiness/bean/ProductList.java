package com.goodhappiness.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 电脑 on 2016/11/7.
 */
public class ProductList implements Serializable{

    private static final long serialVersionUID = 8252478881788872629L;
    /**
     * productId : 58
     * productImage : ["http://img.mypuduo.com/1466756420"]
     * productName : 小米红米3 2GB+16GB 全网通 标准版
     * description : 颜色随机
     * is_full_buy : 1
     * detailUrl : http://wx.hxfapp.com/V2/shop/detail?productId=58
     * happyCoin : 0
     * generalCoin : 9
     */

    private int productId;
    private String productName;
    private String description;
    private int is_full_buy;
    private String detailUrl;
    private int happyCoin;
    private int generalCoin;
    private List<String> productImage;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getIs_full_buy() {
        return is_full_buy;
    }

    public void setIs_full_buy(int is_full_buy) {
        this.is_full_buy = is_full_buy;
    }

    public String getDetailUrl() {
        return detailUrl;
    }

    public void setDetailUrl(String detailUrl) {
        this.detailUrl = detailUrl;
    }

    public int getHappyCoin() {
        return happyCoin;
    }

    public void setHappyCoin(int happyCoin) {
        this.happyCoin = happyCoin;
    }

    public int getGeneralCoin() {
        return generalCoin;
    }

    public void setGeneralCoin(int generalCoin) {
        this.generalCoin = generalCoin;
    }

    public List<String> getProductImage() {
        return productImage;
    }

    public void setProductImage(List<String> productImage) {
        this.productImage = productImage;
    }
}
