/** 
 * TuSDKCore
 * TuPhotoMultipleListCell.java
 *
 * @author 		Yanlin
 * @Date 		2015-08-30 下午4:28:17 
 * @Copyright 	(c) 2015 Lasque. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.album;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.task.AlbumTaskManager;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
import org.lasque.tusdk.core.view.listview.TuSdkCellRelativeLayout;
import org.lasque.tusdk.core.view.recyclerview.TuSdkMultiSelectableCellViewInterface;
import org.lasque.tusdk.core.view.widget.button.TuSdkButton;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * 相片列表单元格视图
 * 
 * @author Clear
 */
public class TuPhotoGridListViewCell extends TuSdkCellRelativeLayout<ImageSqlInfo> implements TuSdkMultiSelectableCellViewInterface
{
	/**
	 * 布局ID
	 * 
	 * @return
	 */
	public static int getLayoutId()
	{
		return TuSdkContext
				.getLayoutResId("tusdk_impl_component_album_photo_grid_list_cell");
	}
	
	/**
	 * 相册图标在列表中的占位符
	 */
	public static final int CAMERA_PLACEHOLDER = -1;

	public TuPhotoGridListViewCell(Context context)
	{
		super(context);
	}

	public TuPhotoGridListViewCell(Context context, AttributeSet attrs)
	{
		super(context, attrs);
	}

	public TuPhotoGridListViewCell(Context context, AttributeSet attrs, int defStyle)
	{
		super(context, attrs, defStyle);
	}

	/**
	 * 图片视图
	 */
	private ImageView mPosterView;
	
	/**
	 * 选中标识
	 */
	private TuSdkButton mSelectedView;
	
	/**
	 * 是否支持多选
	 */
	private boolean mEnableMultiSelection = false;

	/**
	 * 图片视图
	 * 
	 * @return the mPosterView
	 */
	public ImageView getPosterView()
	{
		if (mPosterView == null)
		{
			mPosterView = this.getViewById("lsq_posterView");
		}
		return mPosterView;
	}
	
	public TuSdkButton getSelectedView()
	{
		if (mSelectedView == null)
		{
			mSelectedView = this.getViewById("lsq_item_selected");
		}
		return mSelectedView;
	}
	
	/**
	 * 设置视图宽度
	 * 
	 * @param width
	 */
	public void setWidth(int width)
	{
		this.setWidth(this, width);
	}
	
	/**
	 * 设置视图高度
	 * 
	 * @param height
	 */
	public void setHeight(int height)
	{
		this.setHeight(this, height);
	}
	
	@Override
	public void loadView()
	{
		super.loadView();
		this.getPosterView();
		this.getSelectedView();
	}

	@Override
	protected void bindModel()
	{
		ImageSqlInfo data = this.getModel();
		
		ImageView img = getPosterView();
		
		if(data.id == CAMERA_PLACEHOLDER)
		{
			this.showViewIn(getSelectedView(), false);
			img.setScaleType(ImageView.ScaleType.CENTER);
			img.setImageDrawable(TuSdkContext.getDrawable("lsq_style_default_album_camera"));
			img.setBackgroundColor(TuSdkContext.getColor("lsq_color_orange"));
		}
		else
		{
			this.showViewIn(getSelectedView(), mEnableMultiSelection);
			img.setScaleType(ImageView.ScaleType.CENTER_CROP);
			AlbumTaskManager.shared.loadThumbImage(img,data);
			img.setBackgroundColor(TuSdkContext.getColor("lsq_background_photo_cell"));
		}
	}

	@Override
	public void viewNeedRest()
	{
		ViewCompat.setAlpha(this.getPosterView(), 1.f);
		AlbumTaskManager.shared.cancelLoadImage(this.getPosterView());
		if (this.getPosterView() != null)
		{
			this.getPosterView().setImageBitmap(null);
		}
		TuSdkButton selectView = this.getSelectedView();
		if (selectView != null)
		{
			selectView.setSelected(false);
			selectView.setText("");
			this.showViewIn(selectView, false);
		}
		super.viewNeedRest();
	}

	@Override
	public void viewWillDestory()
	{
		this.viewNeedRest();
		super.viewWillDestory();
	}

	@Override
	public void onCellSelected(int position, int selectionIndex)
	{
		if (this.getSelectedView() != null)
		{
			this.getSelectedView().setSelected(true);
			this.getSelectedView().setText("" + (selectionIndex+1));
		}
	}
	
	@Override
	public void onCellDeselected()
	{
		if (this.getSelectedView() != null)
		{
			this.getSelectedView().setSelected(false);
			this.getSelectedView().setText("");
		}
	}
	
	/**
	 * 是否支持多选
	 * 
	 * @param enableMultiSelection
	 *            true or false
	 */
	public void onCellInit(boolean enableMultiSelection)
	{
		mEnableMultiSelection = enableMultiSelection;
	}
}

