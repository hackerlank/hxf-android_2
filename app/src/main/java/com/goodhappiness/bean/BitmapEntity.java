package com.goodhappiness.bean;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class BitmapEntity implements Parcelable{
	private Bitmap bitmap;
	private String name;
	private String uri;
	private long size;
	private String uri_thumb;
	private boolean isSelect = false;

	public BitmapEntity(String name, String uri, long size, String uri_thumb, long duration) {
		super();
		this.name = name;
		this.uri = uri;
		this.size = size;
		this.uri_thumb = uri_thumb;
		this.duration = duration;
	}

	public String getUri_thumb() {
		return uri_thumb;
	}

	public void setUri_thumb(String uri_thumb) {
		this.uri_thumb = uri_thumb;
	}
	private long duration;//持续时间
	public long getSize() {
		return size;
	}
	public void setSize(long size) {
		this.size = size;
	}
	public long getDuration() {
		return duration;
	}
	public void setDuration(long duration) {
		this.duration = duration;
	}
	public Bitmap getBitmap() {
		return bitmap;
	}
	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getUri() {
		return uri;
	}
	public void setUri(String uri) {
		this.uri = uri;
	}

	public boolean isSelect() {
		return isSelect;
	}

	public void setSelect(boolean select) {
		isSelect = select;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(this.bitmap, flags);
		dest.writeString(this.name);
		dest.writeString(this.uri);
		dest.writeLong(this.size);
		dest.writeString(this.uri_thumb);
		dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
		dest.writeLong(this.duration);
	}

	protected BitmapEntity(Parcel in) {
		this.bitmap = in.readParcelable(Bitmap.class.getClassLoader());
		this.name = in.readString();
		this.uri = in.readString();
		this.size = in.readLong();
		this.uri_thumb = in.readString();
		this.isSelect = in.readByte() != 0;
		this.duration = in.readLong();
	}

	public static final Creator<BitmapEntity> CREATOR = new Creator<BitmapEntity>() {
		@Override
		public BitmapEntity createFromParcel(Parcel source) {
			return new BitmapEntity(source);
		}

		@Override
		public BitmapEntity[] newArray(int size) {
			return new BitmapEntity[size];
		}
	};
}
