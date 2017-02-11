/** 
 * TuSDKCore
 * TuAlbumMultipleListFragment.java
 *
 * @author 		Clear
 * @Date 		2014-11-26 下午4:32:37 
 * @Copyright 	(c) 2014 Lasque. All rights reserved.
 * 
 */
package org.lasque.tusdk.impl.components.album;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.lasque.tusdk.core.TuSdk;
import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.task.AlbumTaskManager;
import org.lasque.tusdk.core.utils.ContextUtils;
import org.lasque.tusdk.core.utils.TLog;
import org.lasque.tusdk.core.utils.hardware.TuSdkGPU;
import org.lasque.tusdk.core.utils.sqllite.AlbumSqlInfo;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlHelper;
import org.lasque.tusdk.core.utils.sqllite.ImageSqlInfo;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.listview.TuSdkArrayListView.ArrayListViewItemClickListener;
import org.lasque.tusdk.core.view.listview.TuSdkIndexPath;
import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar;
import org.lasque.tusdk.core.view.widget.TuSdkNavigatorBar.NavigatorBarButtonInterface;
import org.lasque.tusdk.impl.components.widget.button.TuNavigatorDropButton;
import org.lasque.tusdk.impl.components.widget.view.TuSdkGridView.TuSdkGridViewItemClickDelegate;
import org.lasque.tusdk.modules.components.TuSdkComponentErrorListener;
import org.lasque.tusdk.modules.components.album.TuAlbumMultipleListFragmentBase;

import java.io.File;
import java.util.ArrayList;

/**
 * 系统相册控制器（带相机）
 * 
 * @author Clear
 */
public class TuAlbumMultipleListFragment extends TuAlbumMultipleListFragmentBase
{
	/**
	 * 布局ID
	 * 
	 * @return
	 */
	public static int getLayoutId()
	{
		return TuSdkContext
				.getLayoutResId("tusdk_impl_component_album_multiple_list_fragment");
	}

	/**
	 * 系统相册委托
	 * 
	 * @author Clear
	 */
	public interface TuAlbumMultipleListFragmentDelegate extends
			TuSdkComponentErrorListener
	{
		/**
		 * 选中相册组
		 * 
		 * @param fragment
		 *            系统相册控制器
		 * @param group
		 *            相册组
		 */
		void onTuAlbumFragmentSelected(TuAlbumMultipleListFragment fragment,
				AlbumSqlInfo group);
		
		/**
		 * 选中照片
		 * 
		 * @param fragment
		 *            系统相册控制器
		 * @param images
		 *            选中的照片列表
		 */
		void onTuPhotoFragmentSelected(TuAlbumMultipleListFragment fragment,
				ArrayList<ImageSqlInfo> images);
		
		/**
		 * 请求从相册界面跳转到相机界面
		 * 
		 * @param fragment
		 *            系统相册控制器
		 */
		void onTuCameraDemand(TuAlbumMultipleListFragment fragment);
	}
	
	/**
	 * 系统相册委托
	 */
	private TuAlbumMultipleListFragmentDelegate mDelegate;

	/**
	 * 系统相册列表
	 */
	private ArrayList<AlbumSqlInfo> mGroups;
	
	
	/**
	 * 自定义的标题
	 */
	private TuNavigatorDropButton titleButton;
	
	/**
	 * 当前选择的照片
	 */
	private ArrayList<ImageSqlInfo> mSelectedImages;
	
	/*************************** config *******************************/
	/**
	 * 相册行视图布局ID
	 */
	private int mAlbumCellLayoutId;
	
	/**
	 * 照片行视图布局ID
	 */
	private int mPhotoCellLayoutId;
	
	/**
	 * 一次选择的最大照片数量 (默认: 3, 0 < n <= 10)
	 */
	private int mMaxSelection = 3;
	
	/**
	 * 允许在多个相册中选择 (默认: 开启)
	 */
	private boolean mEnableShareSelection = true;
	
	/**
	 * 相册列表每行显示的照片数量 (默认:0, 程序自动适配设备)
	 */
	private int mPhotoColumnNumber = 0;
	
	/**
	 * 弹出相册列表的高度，默认是64
	 */
	private int mPopListRowHeight = 64;

	/**
	 * 系统相册控制器
	 */
	public TuAlbumMultipleListFragment()
	{

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState)
	{
		if (this.getRootViewLayoutId() == 0)
		{
			this.setRootViewLayoutId(getLayoutId());
		}
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	/**
	 * 系统相册列表
	 * 
	 * @return the groups
	 */
	public ArrayList<AlbumSqlInfo> getGroups()
	{
		return mGroups;
	}

	/**
	 * 系统相册委托
	 * 
	 * @return the delegate
	 */
	public TuAlbumMultipleListFragmentDelegate getDelegate()
	{
		return mDelegate;
	}

	/**
	 * 系统相册委托
	 * 
	 * @param delegate
	 *            the delegate to set
	 */
	public void setDelegate(TuAlbumMultipleListFragmentDelegate delegate)
	{
		this.mDelegate = delegate;
		this.setErrorListener(mDelegate);
	}

	/**
	 * 通知获取一个相册组
	 * 
	 * @param group
	 *            相册组
	 */
	public void notifySelectedGroup(AlbumSqlInfo group)
	{
		TuPhotoGridListView photoListView = this.getPhotoGridView();
		
		if (photoListView == null) return;
		
		if (!isEnableShareSelection())
		{
			if (mSelectedImages != null) mSelectedImages.clear();
			
			photoListView.resetSelections();
		}
		
		photoListView.setAlbumInfo(group);
		
		this.setTitleWithAlbumName(group.title, false);
		
		if (this.mDelegate == null) return;
		this.mDelegate.onTuAlbumFragmentSelected(this, group);
	}
	
	private void setTitleWithAlbumName(String title, Boolean isOpen)
	{
		if(titleButton != null)
		{
			titleButton.setText(title);
			
			setTitleIconState(isOpen);
		}
	}
	
	private void setTitleIconState(Boolean isOpen) 
	{
		Drawable arrow;
		
		if(isOpen)
		{
			arrow = TuSdkContext.getDrawable("lsq_style_default_arrow_up");  
		}
		else
		{
			arrow = TuSdkContext.getDrawable("lsq_style_default_arrow_down");  
		}
		arrow.setBounds(0, 0, arrow.getMinimumWidth(), arrow.getMinimumHeight());  
		setTitleIcon(arrow);
	}
	
	private void setTitleIcon(Drawable draw)
	{
		if (titleButton == null) return;
		
		if (draw == null)
		{
			titleButton.setIconPadding(0);
	        titleButton.setIconPosition(TuNavigatorDropButton.DrawablePositions.NONE);
		}
		
		titleButton.setCompoundDrawables(null, null, draw, null);
		titleButton.requestLayout();
	}

	/**
	 * 设置相册行视图布局ID
	 * 
	 * @see #{@link org.lasque.tusdk.impl.components.album.TuAlbumPopListCell}
	 * @param resId
	 *            相册行视图布局ID (默认: tusdk_impl_component_album_multiple_list_cell)
	 */
	public void setAlbumCellLayoutId(int resId)
	{
		mAlbumCellLayoutId = resId;
	}

	/**
	 * 相册行视图布局ID
	 * 
	 * @see #{@link org.lasque.tusdk.impl.components.album.TuAlbumPopListCell}
	 * @return 行视图布局ID (默认: tusdk_impl_component_album_multiple_list_cell)
	 */
	public int getAlbumCellLayoutId()
	{
		if (mAlbumCellLayoutId == 0)
		{
			mAlbumCellLayoutId = TuAlbumPopListCell.getLayoutId();
		}
		return mAlbumCellLayoutId;
	}
	
	/**
	 * 设置照片行视图布局ID
	 * 
	 * @see #{@link org.lasque.tusdk.impl.components.album.TuPhotoGridListViewCell}
	 * @param resId
	 *            照片行视图布局ID (默认: tusdk_impl_component_album_photo_multiple_list_cell)
	 */
	public void setPhotoCellLayoutId(int resId)
	{
		mPhotoCellLayoutId = resId;
	}

	/**
	 * 行视图布局ID
	 * 
	 * @see #{@link org.lasque.tusdk.impl.components.album.TuPhotoGridListViewCell}
	 * @return 行视图布局ID (默认: tusdk_impl_component_album_photo_multiple_list_cell)
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
	 * 一次选择的最大照片数量 (默认: 3, 0 < n <= 10)
	 */
	public void setMaxSelection(int mMaxSelection)
	{
		if (mMaxSelection > 0 && mMaxSelection <= 10 )
			this.mMaxSelection = mMaxSelection;
	}
	
	/**
	 * 一次选择的最大照片数量 (默认: 3, 0 < n <= 10)
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
	 * 相册列表每行显示的照片数量 (默认:0, 程序自动适配设备)
	 * @param mPhotoColumnNumber
	 *            the photoColumnNumber to set
	 */
	public void setPhotoColumnNumber(int mPhotoColumnNumber)
	{
		if (mPhotoColumnNumber > 0)
		{
			this.mPhotoColumnNumber = mPhotoColumnNumber;
		}
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
		if (rowHeight > 0 && rowHeight != mPopListRowHeight)
		{
			this.mPopListRowHeight = rowHeight;
		}
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

	/*************************** view *******************************/
	/**
	 * 相册列表视图
	 */
	private TuAlbumPopList mAlbumListView;

	/**
	 * 相册列表视图
	 * 
	 * @return the TuAlbumGroupListView
	 */
	public TuAlbumPopList getAlbumListView()
	{
		if (mAlbumListView == null)
		{
			mAlbumListView = this.getViewById("lsq_albumListView");
			mAlbumListView.setCellLayoutId(this.getAlbumCellLayoutId());
			mAlbumListView.setItemClickListener(new AlbumListItemClickDelegate());
		}
		return mAlbumListView;
	}
	
	/**
	 * 相册显示区域，用来处理点击事件
	 */
	private RelativeLayout mAlbumListArea;
	
	private RelativeLayout getAlbumListArea()
	{
		if (mAlbumListArea == null)
		{
			mAlbumListArea = this.getViewById("lsq_albumGroupArea");
			mAlbumListArea.setOnClickListener(mViewClickListener);
		}
		return mAlbumListArea;
	}
	
	/**
	 * 相册列表视图
	 */
	private TuPhotoGridListView mPhotoGridView;
	
	/**
	 * 照片列表视图
	 * 
	 * @return the TuPhotoMultipleList
	 */
	public TuPhotoGridListView getPhotoGridView()
	{
		if (mPhotoGridView == null)
		{
			TuSdkSize screenSize = ContextUtils.getDisplaySize(this.getActivity());
			int preferGridSize = 4;
			int pad = 2;
			int photoWidth = ( screenSize.width - (preferGridSize-1) * pad ) / preferGridSize;
			
			if (this.getPhotoColumnNumber() == 0)
			{				
				if (TuSdkGPU.getGpuType().getPerformance() > 3) 
				{
					while (TuSdkContext.px2dip(photoWidth) > 180) {
						preferGridSize ++;
						photoWidth = ( screenSize.width - (preferGridSize-1) * pad ) / preferGridSize;
					}
				}
			}
			else
			{
				preferGridSize = this.getPhotoColumnNumber();
			}
			
			photoWidth = ( screenSize.width - (preferGridSize-1) * pad ) / preferGridSize;
			
			mPhotoGridView = this.getViewById("lsq_photoListView");
			mPhotoGridView.setEnableMultiSelection(this.getMaxSelection() > 1);
			mPhotoGridView.setCellLayoutId(this.getPhotoCellLayoutId());
			mPhotoGridView.setGridSize(preferGridSize);
			mPhotoGridView.setPhotoGridWidth(photoWidth);
			mPhotoGridView.setItemClickDelegate(mPhotoGridItemClickDelegate);
			mPhotoGridView.reloadData();
		}
		return mPhotoGridView;
	}

	@Override
	protected void loadView(ViewGroup view)
	{		
		super.loadView(view);
		
		if (hasRequiredPermission())
		{
			initView();
		}
		else
		{
			requestRequiredPermissions();
		}
	}
	
	/**
	 * 是否已被授予权限
	 * 
	 * @param permissionGranted
	 */
	protected void onPermissionGrantedResult(boolean permissionGranted)
	{
		if (permissionGranted)
		{
			initView();
		}
		else
		{
			String msg = TuSdkContext.getString("lsq_album_no_access", ContextUtils.getAppName(getActivity()));
			
			TuSdkViewHelper.alert(permissionAlertDelegate,getActivity(), TuSdkContext.getString("lsq_album_alert_title"),
					msg, TuSdkContext.getString("lsq_button_close"), TuSdkContext.getString("lsq_button_setting")
			);
		}
	}
	
	/** 初始化视图，loadView 之后调用 */
	protected void initView()
	{
		mGroups = ImageSqlHelper.getAlbumList(this.getActivity());
		
		if(mGroups != null) 
		{
			AlbumSqlInfo item;
			for(int i = 0, j = mGroups.size(); i<j; i++)
			{
				item = mGroups.get(i);
				//系统相册放在第一位
				if (AlbumSqlInfo.CAMERA_FOLDER.equalsIgnoreCase(item.title))
				{
					mGroups.remove(i);
					mGroups.add(0, item);
					break;
				}
			}
		}

		TuAlbumPopList listView = this.getAlbumListView();
		if (listView != null)
		{
			// 设置弹出相册列表每一行的高度，默认是64
			listView.setPopListRowHeight(this.getPopListRowHeight());
			listView.setGroups(mGroups);
			this.showView(listView, false);
		}
		
		refreshData();
	}

	@Override
	public void onDestroyView()
	{
		AlbumTaskManager.shared.resetQueues();
		super.onDestroyView();
	}

	/**
	 * 初始化导航栏
	 * 
	 * @param navigatorBar
	 */
	@Override
	protected void navigatorBarLoaded(TuSdkNavigatorBar navigatorBar)
	{
		this.setNavLeftButton(TuSdkContext.getString("lsq_nav_back"));
		
		if(titleButton == null)
		{
			TextView textView = navigatorBar.getViewById("lsq_titleView"); 
			if(textView != null) {
				navigatorBar.removeView(textView);
			}
			
	        LayoutInflater layoutInflater = LayoutInflater.from(this.getActivity());  
	        
	        int layoutID = TuSdkContext.getLayoutResId("tusdk_view_widget_navigator_title_view");
	        
	        titleButton = (TuNavigatorDropButton)layoutInflater.inflate(layoutID, null); 
	        titleButton.setIconPadding(TuSdkContext.dip2px(16));
	        titleButton.setIconPosition(TuNavigatorDropButton.DrawablePositions.END);
	        titleButton.setLayoutParams(textView.getLayoutParams());
	        navigatorBar.addView(titleButton);  
	        
	        titleButton.setOnClickListener(new View.OnClickListener() {
	             public void onClick(View v) {
	                 // Perform action on click
	            	 toggleAlbumListViewState(); 
	             }
	        });
		}
		
		if (this.getMaxSelection() > 1)
			this.setNavRightButton(TuSdkContext.getString("lsq_nav_complete"));
	}

	/**
	 * 取消按钮
	 */
	@Override
	public void navigatorBarLeftAction(NavigatorBarButtonInterface button)
	{
		this.dismissActivityWithAnim();
	}
	
	/**
	 * 完成按钮
	 */
	@Override
	public void navigatorBarRightAction(NavigatorBarButtonInterface button)
	{
		if (this.getMaxSelection() > 1 && mSelectedImages != null && mSelectedImages.size() > 0)
		{
			this.sendSelectionNotify();
		}
		else
		{
			TuSdk.messageHub().showToast(this.getActivity(), TuSdkContext.getString("lsq_album_empty_selection_msg"));
		}
	}

	@Override
	protected void viewDidLoad(ViewGroup view)
	{
		refreshData();
	}
	
	private void refreshData()
	{
		if (mGroups == null || mGroups.size() == 0) 
		{			
			TuPhotoGridListView photoListView = this.getPhotoGridView();
			photoListView.setEnableMultiSelection(this.getMaxSelection() > 1);
			photoListView.setAlbumInfo(null);
			
			this.setTitleWithAlbumName(TuSdkContext.getString("lsq_album_empty"), false);
			this.setTitleIcon(null);
		}
		else
		{
			this.autoSelectedAblumGroupAction(mGroups);
		}
	}
	
	/**
	 *  切换相册选择列表显示状态: 显示 | 隐藏
	 */
	private void toggleAlbumListViewState()
	{
		if (mGroups == null || mGroups.size() == 0) return;
		
		TuAlbumPopList listView = getAlbumListView();
	    listView.toggleAlbumListViewState();
	    
	    Boolean isHidden = listView.getStateHidden();
	    
	    this.getAlbumListArea().setVisibility(isHidden?View.GONE:View.VISIBLE);
	    
	    setTitleIconState(!isHidden);
	}
	
	// 照片列表点击事件
	private TuSdkGridViewItemClickDelegate<ImageSqlInfo, TuPhotoGridListViewCell> mPhotoGridItemClickDelegate = new TuSdkGridViewItemClickDelegate<ImageSqlInfo, TuPhotoGridListViewCell>()
	{
		@Override
		/**
		 * 列表项点击事件
		 * 
		 * @param itemData
		 *            数据
		 * @param itemView
		 *            视图
		 * @param position
		 *            视图位置
		 */
		public void onGridViewItemClick(ImageSqlInfo itemData, TuPhotoGridListViewCell itemView, int position)
		{
			onPhotoItemSelected(itemData, itemView, position);
		}
	};
	
	private int isPhotoSelected(ImageSqlInfo itemData)
	{
		if (mSelectedImages == null || mSelectedImages.size() == 0) return -1;
		
		for(int i = 0; i<mSelectedImages.size(); i++)
		{
			ImageSqlInfo item = mSelectedImages.get(i);
			
			if (item.albumId == itemData.albumId && item.id == itemData.id)
			{
				return i;
			}
		}
		
		return -1;
	}
	
	/**
	 * 照片点击事件
	 * 
	 * @param itemData
	 *            数据
	 * @param itemView
	 *            视图
	 * @param position
	 *            视图位置
	 */
	private void onPhotoItemSelected(ImageSqlInfo itemData,
			TuPhotoGridListViewCell itemView, int position)
	{
		if (itemData == null || 
				(itemData.id != TuPhotoGridListViewCell.CAMERA_PLACEHOLDER && !new File(itemData.path).exists()) )
		{
			TuSdkContext.ins().toast(TuSdkContext.getString("lsq_album_broken_msg"));
			return;
		}
		
		if(itemData != null && itemData.id == TuPhotoGridListViewCell.CAMERA_PLACEHOLDER)
		{
			if (this.mDelegate != null)
				this.mDelegate.onTuCameraDemand(this);
			return;
		}
		
		if (mSelectedImages == null)
		{
			mSelectedImages = new ArrayList<ImageSqlInfo>();
		}
		
		int index = isPhotoSelected(itemData);
		
		TuPhotoGridListView photoListView = this.getPhotoGridView();
		
		if (this.getMaxSelection() == 1)
		{
			mSelectedImages.clear();
			mSelectedImages.add(itemData);
			
			sendSelectionNotify();
			
			return;
		}
		
		if (index == -1)
		{
			if (mSelectedImages.size() < this.getMaxSelection())
			{
				mSelectedImages.add(itemData);
				photoListView.setItemSelected(position, true);
			}
			else
			{
				// TODO - display message hub
				TLog.w("[%d] photos have been selected already", this.getMaxSelection());
				TuSdk.messageHub().showToast(this.getActivity(), TuSdkContext.getString("lsq_album_max_selection_msg", this.getMaxSelection()));
			}
		}
		else
		{
			mSelectedImages.remove(index);
			photoListView.setItemSelected(position, false);
		}
	}
	
	private void sendSelectionNotify()
	{
		if (this.mDelegate == null) return;
		this.mDelegate.onTuPhotoFragmentSelected(this, mSelectedImages);
	}

	/**
	 * 列表项点击事件委托
	 * 
	 * @author Clear
	 */
	private class AlbumListItemClickDelegate implements
			ArrayListViewItemClickListener<AlbumSqlInfo, TuAlbumPopListCell>
	{
		/**
		 * 列表项点击事件
		 * 
		 * @param itemData
		 *            数据
		 * @param itemView
		 *            视图
		 * @param indexPath
		 *            索引
		 */
		@Override
		public void onArrayListViewItemClick(AlbumSqlInfo itemData,
				TuAlbumPopListCell itemView, TuSdkIndexPath indexPath)
		{
			notifySelectedGroup(itemData);
			
			toggleAlbumListViewState();
		}
	}
	
	/**
	 * 显示区域点击事件
	 */
	private OnClickListener mViewClickListener = new TuSdkViewHelper.OnSafeClickListener()
	{
		@Override
		public void onSafeClick(View v)
		{
			if(!getAlbumListView().getStateHidden())
			{
				toggleAlbumListViewState();
			}
		}
	};
}
