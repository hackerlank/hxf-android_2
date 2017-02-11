package com.goodhappiness.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by 电脑 on 2016/4/12.
 */
public class WechatRequest extends BaseBean{

    /**
     * use_bank : true
     * params : {"appid":"wx426b3015555a46be","partnerid":"1225312702","prepayid":"wx2016041217474866d6399b340452597124","package":"Sign=WXPay","nonce_str":"WToFDnVPVMjjiDNX","timestamp":"1460454468268","sign":"BA7A485BEAA1C71C22E84D1D21FF17A1"}
     * url :
     */

    private boolean useBank;
    /**
     * appid : wx426b3015555a46be
     * partnerid : 1225312702
     * prepayid : wx2016041217474866d6399b340452597124
     * package : Sign=WXPay
     * nonce_str : WToFDnVPVMjjiDNX
     * timestamp : 1460454468268
     * sign : BA7A485BEAA1C71C22E84D1D21FF17A1
     */

    private ParamsBean params;
    private String url;
    private String sn;

    @Override
    public String toString() {
        return "WechatRequest{" +
                "use_bank=" + useBank +
                ", params=" + params +
                ", url='" + url + '\'' +
                ", sn='" + sn + '\'' +
                '}';
    }

    public String getSn() {
        return sn;
    }

    public void setSn(String sn) {
        this.sn = sn;
    }

    public boolean isUse_bank() {
        return useBank;
    }

    public void setUse_bank(boolean use_bank) {
        this.useBank = use_bank;
    }

    public ParamsBean getParams() {
        return params;
    }

    public void setParams(ParamsBean params) {
        this.params = params;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public static class ParamsBean {
        private String appid;
        private String partnerid;
        private String prepayid;
        @SerializedName("package")
        private String packageX;
        private String noncestr;
        private String timestamp;
        private String sign;

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getPackageX() {
            return packageX;
        }

        public void setPackageX(String packageX) {
            this.packageX = packageX;
        }

        public String getNonce_str() {
            return noncestr;
        }

        public void setNonce_str(String nonce_str) {
            this.noncestr = nonce_str;
        }

        public String getTimestamp() {
            return timestamp;
        }

        public void setTimestamp(String timestamp) {
            this.timestamp = timestamp;
        }

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        @Override
        public String toString() {
            return "ParamsBean{" +
                    "appid='" + appid + '\'' +
                    ", partnerid='" + partnerid + '\'' +
                    ", prepayid='" + prepayid + '\'' +
                    ", packageX='" + packageX + '\'' +
                    ", noncestr='" + noncestr + '\'' +
                    ", timestamp='" + timestamp + '\'' +
                    ", sign='" + sign + '\'' +
                    '}';
        }
    }
}
