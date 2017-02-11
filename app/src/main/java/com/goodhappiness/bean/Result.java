package com.goodhappiness.bean;

/**
 * Created by 电脑 on 2016/4/8.
 */
public class Result<BaseBean> {
    private String status;
    private int code;
    private BaseBean data;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public BaseBean getData() {
        return data;
    }

    public void setData(BaseBean data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "status='" + status + '\'' +
                ", code=" + code +
                ", data=" + data +
                '}';
    }
}
