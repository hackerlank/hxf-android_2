package com.goodhappiness.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 电脑 on 2016/4/9.
 */
public class DeviceInfo extends BaseBean implements Serializable {


    private static final long serialVersionUID = -230556024117327915L;
    /**
     * deviceId : 868191024948836
     * deviceResolution : 720*1280
     * deviceSysVersion : 4.4.4
     * deviceType : android
     * deviceIdentifier : c958c745a4853b250f743b2de8f6aba0
     * updateVersionInfo : {"versionCode":101,"url":"http://www.baidu.com","content":"版本更新","versionName":"v1.0.1","isRequired":0}
     */

    private String deviceId;
    private String deviceResolution;
    private String deviceSysVersion;
    private String deviceType;
    private String deviceIdentifier;
    private String chatToken;
    private int showLottery;

    public int getShowLottery() {
        return showLottery;
    }

    public void setShowLottery(int showLottery) {
        this.showLottery = showLottery;
    }

    private List<Banners> banners;

    public List<Banners> getBanners() {
        return banners;
    }

    public void setBanners(List<Banners> banners) {
        this.banners = banners;
    }

    public String getChatToken() {
        return chatToken;
    }

    public void setChatToken(String chatToken) {
        this.chatToken = chatToken;
    }

    /**
     * versionCode : 101
     * url : http://www.baidu.com
     * content : 版本更新
     * versionName : v1.0.1
     * isRequired : 0
     */


    private UpdateVersionInfoBean updateVersionInfo;

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getDeviceResolution() {
        return deviceResolution;
    }

    public void setDeviceResolution(String deviceResolution) {
        this.deviceResolution = deviceResolution;
    }

    public String getDeviceSysVersion() {
        return deviceSysVersion;
    }

    public void setDeviceSysVersion(String deviceSysVersion) {
        this.deviceSysVersion = deviceSysVersion;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public String getDeviceIdentifier() {
        return deviceIdentifier;
    }

    public void setDeviceIdentifier(String deviceIdentifier) {
        this.deviceIdentifier = deviceIdentifier;
    }

    public UpdateVersionInfoBean getUpdateVersionInfo() {
        return updateVersionInfo;
    }

    public void setUpdateVersionInfo(UpdateVersionInfoBean updateVersionInfo) {
        this.updateVersionInfo = updateVersionInfo;
    }

    public static class Banners implements Serializable {

        private String imgUrl;
        private String appUrl;

        public String getImgUrl() {
            return imgUrl;
        }

        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

        public String getAppUrl() {
            return appUrl;
        }

        public void setAppUrl(String appUrl) {
            this.appUrl = appUrl;
        }
    }

    public static class UpdateVersionInfoBean implements Serializable {


        private static final long serialVersionUID = 8711314083314928863L;
        private int versionCode;
        private String url;
        private String content;
        private String versionName;
        private int isRequired;


        public int getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(int versionCode) {
            this.versionCode = versionCode;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public int getIsRequired() {
            return isRequired;
        }

        public void setIsRequired(int isRequired) {
            this.isRequired = isRequired;
        }

        @Override
        public String toString() {
            return "UpdateVersionInfoBean{" +
                    "versionCode=" + versionCode +
                    ", url='" + url + '\'' +
                    ", content='" + content + '\'' +
                    ", versionName='" + versionName + '\'' +
                    ", isRequired=" + isRequired +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "DeviceInfo{" +
                "deviceId='" + deviceId + '\'' +
                ", deviceResolution='" + deviceResolution + '\'' +
                ", deviceSysVersion='" + deviceSysVersion + '\'' +
                ", deviceType='" + deviceType + '\'' +
                ", deviceIdentifier='" + deviceIdentifier + '\'' +
                ", updateVersionInfo=" + updateVersionInfo +
                '}';
    }
}