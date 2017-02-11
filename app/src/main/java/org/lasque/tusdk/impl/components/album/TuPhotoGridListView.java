/** 
 * TuSDKCore
 * TuPhotoMultipleList.java
 *
 * @author 		Clear
 * @Date 		2014-11-28 上午11:17:42 
 * @Copyright 	(c) 2014 Lasque. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.album;

import java.util.ArrayList;

import org.lasque.tusdk.core.utils.sqllite.AlbumSqlInfo;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlHelper;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
import org.lasque.tusdk.impl.components.widget.view.TuSdkGridView;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;

/**
 * 相册组照片列表控制器，使用网格化布局显示照片
 * 
 * @author Clear
 */
public class TuPhotoGridListView extends TuSdkGridView<ImageSqlInfo, TuPhotoGridListViewCell>
{
	
	public TuPhotoGridListView(Context context, AttributeSet attrs,
			int defStyle)
	{
		super(context, attrs, defStyle);
	}

	public TuPhotoGridListView(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public TuPhotoGridListView(Context context)
	{
		super(context);
	}

	/**
	 * 系统相册数据库信息
	 */
	private AlbumSqlInfo mAlbumInfo;
	
	/**
	 * 系统相册数据库信息
	 * 
	 * @return the mAlbumInfo
	 */
	public AlbumSqlInfo getAlbumInfo()
	{
		return mAlbumInfo;
	}

	/**
	 * 系统相册数据库信息
	 * 
	 * @param mAlbumInfo
	 *            the mAlbumInfo to set
	 */
	public void setAlbumInfo(AlbumSqlInfo mAlbumInfo)
	{
		this.mAlbumInfo = mAlbumInfo;
		
		ArrayList<ImageSqlInfo> mPhotos;
		
		if (mAlbumInfo != null) {
			mPhotos = ImageSqlHelper.getPhotoList(getContext(), mAlbumInfo.id, true);
		}
		else
		{
			mPhotos = new ArrayList<ImageSqlInfo>();
		}
		
		//添加相机图标
		ImageSqlInfo cameraInfo = new ImageSqlInfo();
		cameraInfo.id = TuPhotoGridListViewCell.CAMERA_PLACEHOLDER;
		mPhotos.add(0, cameraInfo);
		
		this.setModeList(mPhotos);
		
		this.reloadData();
	}
	
	// 行视图宽度
	private int mPhotoGridWidth;

	/**
	 * 行视图宽度
	 * 
	 * @return the mPhotoGridWidth
	 */
	public int getPhotoGridWidth()
	{
		return mPhotoGridWidth;
	}

	/**
	 * 行视图宽度
	 * 
	 * @param mPhotoGridWidth
	 *            the mPhotoGridWidth to set
	 */
	public void setPhotoGridWidth(int mPhotoGridWidth)
	{
		this.mPhotoGridWidth = mPhotoGridWidth;
	}	
	
	@Override
	public void loadView()
	{
		super.loadView();
		
		this.setItemAnimator(null);
		
		this.setHasFixedSize(true);
	}
	
	@Override
	protected void onViewCreated(TuPhotoGridListViewCell view, ViewGroup parent,
			int viewType)
	{
		if (this.getPhotoGridWidth() > 0)
		{
			view.setWidth(this.getPhotoGridWidth());
			view.setHeight(this.getPhotoGridWidth());
		}
	}

	@Override
	protected void onViewBinded(TuPhotoGridListViewCell view, int position)
	{

	}
}
