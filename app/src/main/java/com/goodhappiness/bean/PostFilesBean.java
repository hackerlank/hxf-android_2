package com.goodhappiness.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by 电脑 on 2016/5/14.
 */
public class PostFilesBean extends BaseBean implements Parcelable {
    private String fileId;
    private String fileUrl;
    private String gifPath;
    private Bitmap bitmap;
    private String duration;
    private int width;
    private boolean isProcessed = false;
    private boolean isTrans = false;
    private int scrollY = 0;
    private int y = 0;
    private int x = 0;
    private float scale =0;
    private int bitmapWidth = 0;
    private int bitmapHeight = 0;
    private boolean isLock = false;
    /**
     *1:图片 2:声音 3:视频 4.GIF
     */
    private int fileType;
    public PostFilesBean() {
    }
    public PostFilesBean(String url,int fileType) {
        this.fileUrl = url;
        this.fileType= fileType;
    }
    public PostFilesBean(int fileType,String gifPath) {
        this.gifPath = gifPath;
        this.fileType= fileType;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public boolean isLock() {
        return isLock;
    }

    public void setLock(boolean lock) {
        isLock = lock;
    }

    @Override
    public String toString() {
        return "PostFilesBean{" +
                "fileId='" + fileId + '\'' +
                ", fileUrl='" + fileUrl + '\'' +
                ", gifPath='" + gifPath + '\'' +
                ", bitmap=" + bitmap +
                ", duration='" + duration + '\'' +
                ", width=" + width +
                ", isProcessed=" + isProcessed +
                ", isTrans=" + isTrans +
                ", scrollY=" + scrollY +
                ", y=" + y +
                ", x=" + x +
                ", scale=" + scale +
                ", bitmapWidth=" + bitmapWidth +
                ", bitmapHeight=" + bitmapHeight +
                ", isLock=" + isLock +
                ", fileType=" + fileType +
                '}';
    }

    public int getBitmapWidth() {
        return bitmapWidth;
    }

    public void setBitmapWidth(int bitmapWidth) {
        this.bitmapWidth = bitmapWidth;
    }

    public int getBitmapHeight() {
        return bitmapHeight;
    }

    public void setBitmapHeight(int bitmapHeight) {
        this.bitmapHeight = bitmapHeight;
    }

    public int getScrollY() {
        return scrollY;
    }

    public void setScrollY(int scrollY) {
        this.scrollY = scrollY;
    }

    public float getScale() {
        return scale;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }


    public boolean isTrans() {
        return isTrans;
    }

    public void setTrans(boolean trans) {
        isTrans = trans;
    }

    public boolean isProcessed() {
        return isProcessed;
    }

    public void setProcessed(boolean processed) {
        isProcessed = processed;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public String getGifPath() {
        return gifPath;
    }

    public void setGifPath(String gifPath) {
        this.gifPath = gifPath;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fileId);
        dest.writeString(this.fileUrl);
        dest.writeString(this.gifPath);
        dest.writeParcelable(this.bitmap, flags);
        dest.writeString(this.duration);
        dest.writeInt(this.width);
        dest.writeByte(this.isProcessed ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isTrans ? (byte) 1 : (byte) 0);
        dest.writeInt(this.scrollY);
        dest.writeFloat(this.scale);
        dest.writeInt(this.bitmapWidth);
        dest.writeInt(this.bitmapHeight);
        dest.writeByte(this.isLock ? (byte) 1 : (byte) 0);
        dest.writeInt(this.fileType);
    }

    protected PostFilesBean(Parcel in) {
        this.fileId = in.readString();
        this.fileUrl = in.readString();
        this.gifPath = in.readString();
        this.bitmap = in.readParcelable(Bitmap.class.getClassLoader());
        this.duration = in.readString();
        this.width = in.readInt();
        this.isProcessed = in.readByte() != 0;
        this.isTrans = in.readByte() != 0;
        this.scrollY = in.readInt();
        this.scale = in.readFloat();
        this.bitmapWidth = in.readInt();
        this.bitmapHeight = in.readInt();
        this.isLock = in.readByte() != 0;
        this.fileType = in.readInt();
    }

    public static final Creator<PostFilesBean> CREATOR = new Creator<PostFilesBean>() {
        @Override
        public PostFilesBean createFromParcel(Parcel source) {
            return new PostFilesBean(source);
        }

        @Override
        public PostFilesBean[] newArray(int size) {
            return new PostFilesBean[size];
        }
    };
}
