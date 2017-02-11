/** 
 * TuSDKCore
 * TuAlbumMultipleListOption.java
 *
 * @author 		Clear
 * @Date 		2014-11-29 下午1:13:36 
 * @Copyright 	(c) 2014 Lasque. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.album;

import org.lasque.tusdk.modules.components.TuSdkComponentOption;

/**
 * 系统相册控制器配置选项
 * 
 * @author Clear
 */
public class TuAlbumMultipleListOption extends TuSdkComponentOption
{
	/**
	 * 系统相册列表控制类
	 * 
	 * @param mComponentClazz
	 *            系统相册列表控制类 (默认: TuAlbumListFragment，如需自定义请继承自
	 *            TuAlbumListFragment)
	 */
	@Override
	protected Class<?> getDefaultComponentClazz()
	{
		return TuAlbumMultipleListFragment.class;
	}

	/**
	 * 获取默认根视图布局资源ID
	 * 
	 * @see #{@link org.lasque.tusdk.impl.components.album.TuAlbumMultipleListFragment}
	 * @return 根视图布局资源ID (默认: tusdk_impl_component_album_multiple_list_fragment)
	 */
	@Override
	protected int getDefaultRootViewLayoutId()
	{
		return TuAlbumMultipleListFragment.getLayoutId();
	}

	/********************************** Config ***********************************/
	/**
	 * 需要自动跳转到相册组名称 (需要设定 autoSkipToPhotoList = true)
	 */
	private String mSkipAlbumName;

	/**
	 * 行视图布局ID
	 */
	private int mCellLayoutId;
	
	/**
	 * 照片行视图布局ID
	 */
	private int mPhotoCellLayoutId;
	
	/**
	 * 一次选择的最大照片数量 (默认: 3)
	 */
	private int mMaxSelection = 3;
	
	/**
	 * 允许在多个相册中选择 (默认: 开启)
	 */
	private boolean mEnableShareSelection = true;
	
	/**
	 * 相册列表每行显示的照片数量 (默认:0, 程序自动适配设备)
	 */
	private int mPhotoColumnNumber;

	/**
	 * 弹出相册列表的高度，默认是64
	 */
	private int mPopListRowHeight = 64;

	/**
	 * 设置相册行视图布局ID
	 * 
	 * @see #{@link org.lasque.tusdk.impl.components.album.TuAlbumPopListCell}
	 * @param resId
	 *            行视图布局ID (默认: tusdk_impl_component_album_pop_list_cell)
	 */
	public void setAlbumCellLayoutId(int resId)
	{
		mCellLayoutId = resId;
	}

	/**
	 * 相册行视图布局ID
	 * 
	 * @see #{@link org.lasque.tusdk.impl.components.album.TuAlbumPopListCell}
	 * @return 行视图布局ID (默认: tusdk_impl_component_album_pop_list_cell)
	 */
	public int getAlbumCellLayoutId()
	{
		if (mCellLayoutId == 0)
		{
			mCellLayoutId = TuAlbumPopListCell.getLayoutId();
		}
		return mCellLayoutId;
	}
	
	/**
	 * 设置照片行视图布局ID
	 * 
	 * @see #{@link org.lasque.tusdk.impl.components.album.TuPhotoGridListViewCell}
	 * @param resId
	 *            照片行视图布局ID (默认: tusdk_impl_component_album_photo_grid_list_cell)
	 */
	public void setPhotoCellLayoutId(int resId)
	{
		mPhotoCellLayoutId = resId;
	}

	/**
	 * 照片行视图布局ID
	 * 
	 * @see #{@link org.lasque.tusdk.impl.components.album.TuPhotoGridListViewCell}
	 * @return 行视图布局ID (默认: tusdk_impl_component_album_photo_grid_list_cell)
	 */
	public int getPhotoCellLayoutId()
	{
		if (mPhotoCellLayoutId == 0)
		{
			mPhotoCellLayoutId = TuPhotoGridListViewCell.getLayoutId();
		}
		return mPhotoCellLayoutId;
	}
	
	/**
	 * 一次选择的最大照片数量 (默认: 3)
	 */
	public void setMaxSelection(int mMaxSelection)
	{
		this.mMaxSelection = mMaxSelection;
	}
	
	/**
	 * 一次选择的最大照片数量 (默认: 3)
	 * 
	 * @return the mMaxSelection
	 */
	public int getMaxSelection()
	{
		return this.mMaxSelection;
	}
	
	/**
	 * 允许在多个相册中选择 (默认: 开启)
	 * 
	 * @param mEnableShareSelection
	 *            true or false
	 */
	public void setEnableShareSelection(boolean mEnableShareSelection)
	{
		this.mEnableShareSelection = mEnableShareSelection;
	}
	
	/**
	 *  允许在多个相册中选择 (默认: 开启)
	 *  
	 *  @return the mMaxSelection
	 */
	public boolean isEnableShareSelection()
	{
		return this.mEnableShareSelection;
	}

	/**
	 * 需要自动跳转到相册组名称 (需要设定 autoSkipToPhotoList = true)
	 * 
	 * @return the skipAlbumName
	 */
	public String getSkipAlbumName()
	{
		return mSkipAlbumName;
	}

	/**
	 * 需要自动跳转到相册组名称 (需要设定 autoSkipToPhotoList = true)
	 * 
	 * @param skipAlbumName
	 *            the skipAlbumName to set
	 */
	public void setSkipAlbumName(String skipAlbumName)
	{
		this.mSkipAlbumName = skipAlbumName;
	}
	
	/**
	 * 相册列表每行显示的照片数量 (默认:0, 程序自动适配设备)
	 * @param mPhotoColumnNumber
	 *            the photoColumnNumber to set
	 */
	public void setPhotoColumnNumber(int mPhotoColumnNumber)
	{
		this.mPhotoColumnNumber = mPhotoColumnNumber;
	}
	
	/**
	 * 相册列表每行显示的照片数量 (默认:0, 程序自动适配设备)
	 * @return the photoColumnNumber
	 */
	public int getPhotoColumnNumber()
	{
		return this.mPhotoColumnNumber;
	}

	/**
	 * 设置弹出相册列表每一行的高度，默认是64
	 * 
	 * @param rowHeight
	 */
	public void setPopListRowHeight(int rowHeight) 
	{
		this.mPopListRowHeight = rowHeight;
	}
	
	/**
	 * 获取弹出相册列表每一行的高度，默认是64
	 * 
	 * @return
	 */
	public int getPopListRowHeight() 
	{
		return this.mPopListRowHeight;
	}

	/**
	 * 系统相册控制器配置选项
	 */
	public TuAlbumMultipleListOption()
	{

	}

	/**
	 * 创建系统相册列表控制器对象
	 * 
	 * @return 系统相册列表控制器对象
	 */
	public TuAlbumMultipleListFragment fragment()
	{
		TuAlbumMultipleListFragment fragment = this.fragmentInstance();
		// 相册行视图布局ID
		fragment.setAlbumCellLayoutId(this.getAlbumCellLayoutId());
		// 照片行视图布局ID
		fragment.setPhotoCellLayoutId(this.getPhotoCellLayoutId());
		// 一次选择的最大照片数量 (默认: 3, 0 < n <= 10)
		fragment.setMaxSelection(this.getMaxSelection());
		// 允许在多个相册中选择 (默认: 开启)
		fragment.setEnableShareSelection(this.isEnableShareSelection());
		// 需要自动跳转到相册组名称 (需要设定 autoSkipToPhotoList = true)
		fragment.setSkipAlbumName(this.getSkipAlbumName());
		// 相册列表每行显示的照片数量 (默认:0, 程序自动适配设备)
		fragment.setPhotoColumnNumber(this.getPhotoColumnNumber());
		// 设置弹出相册列表每一行的高度，默认是64
		fragment.setPopListRowHeight(this.getPopListRowHeight());
		return fragment;
	}
}