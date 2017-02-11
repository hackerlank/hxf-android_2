package com.goodhappiness.ui.social.picture;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.view.animation.LinearInterpolator;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.adapter.CommonAdapter;
import com.goodhappiness.adapter.FloderAdapter;
import com.goodhappiness.adapter.PhotoAdapter;
import com.goodhappiness.adapter.ViewHolder;
import com.goodhappiness.bean.Photo;
import com.goodhappiness.bean.PhotoFloder;
import com.goodhappiness.bean.StickerBarBean;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.StringFinal;
import com.goodhappiness.dao.OnSelectListener;
import com.goodhappiness.ui.fragment.NotifySelectFragment;
import com.goodhappiness.ui.personal.selecthead.ShowHeadImgActivity;
import com.goodhappiness.utils.AppManager;
import com.goodhappiness.utils.CameraUtils;
import com.goodhappiness.utils.PreferencesUtil;
import com.goodhappiness.widget.ClipImageBorderView;
import com.goodhappiness.widget.ClipImageLayout;
import com.goodhappiness.widget.HorizontalListView;
import com.goodhappiness.widget.RoundImageViewByXfermode2;
import com.goodhappiness.widget.photopicker.OtherUtils;
import com.goodhappiness.widget.photopicker.PhotoUtils;
import com.goodhappiness.widget.social.MyImageView;
import com.goodhappiness.widget.social.MyStickerBarView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.seles.tusdk.FilterImageView;
import org.lasque.tusdk.core.seles.tusdk.FilterImageViewInterface;
import org.lasque.tusdk.core.seles.tusdk.FilterLocalPackage;
import org.lasque.tusdk.core.seles.tusdk.FilterWrap;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.utils.image.BitmapHelper;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.widget.TuMaskRegionView;
import org.lasque.tusdk.core.view.widget.button.TuSdkImageButton;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.impl.components.camera.TuCameraFragment;
import org.lasque.tusdk.impl.components.filter.TuEditFilterBarView;
import org.lasque.tusdk.impl.components.filter.TuFilterOnlineFragment;
import org.lasque.tusdk.impl.components.sticker.TuStickerChooseFragment;
import org.lasque.tusdk.impl.components.widget.sticker.StickerView;
import org.lasque.tusdk.modules.components.TuSdkHelperComponent;
import org.lasque.tusdk.modules.components.sticker.TuEditStickerFragmentBase;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterBarInterface;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterBaseView;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterItem;
import org.lasque.tusdk.modules.view.widget.filter.GroupFilterItemViewInterface;
import org.lasque.tusdk.modules.view.widget.sticker.StickerCategory;
import org.lasque.tusdk.modules.view.widget.sticker.StickerData;
import org.lasque.tusdk.modules.view.widget.sticker.StickerGroup;
import org.lasque.tusdk.modules.view.widget.sticker.StickerLocalPackage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * EditPicUpdateActivity simple {@link Fragment} subclass.
 */

public class EditPicFragment extends TuEditStickerFragmentBase implements TuEditFilterBarView.TuEditFilterBarDelegate, PhotoAdapter.PhotoClickCallBack, MyStickerBarView.StickerBarViewDelegate, TuCameraFragment.TuCameraFragmentDelegate, TuStickerChooseFragment.TuStickerChooseFragmentDelegate {
    private static final int PHOTO = 1;
    private static final int STICKER = 2;
    private static final int FILTER = 3;
    private int currentStatus = 1;
    private ClipImageLayout mClipImageLayout;//截取矩形头像的自定义控件
    private ScrollView sv;
    private View v_hide;
    private RelativeLayout rl_show, rl_sticker, rl_cut;
    private HorizontalListView lv_sticker;
    private ImageView iv_back, iv_filterResult;
    private TextView tv_tips, tv_sticker, tv_filter, tv_right;
    private int mGridHeight = 0;
    /**
     * 图片视图
     */
    private MyImageView mImageView;
    /**
     * 贴纸视图
     */
    private StickerView mStickerView;
    /**
     * 裁剪选区视图
     */
    private TuMaskRegionView mCutRegionView;
    /**
     * 贴纸视图委托
     */
    private StickerView.StickerViewDelegate mStickerViewDelegate;
    /**
     * 贴纸栏视图
     */
    private RelativeLayout rl_chooseFragment;
    private MyStickerBarView mStickerBarView;
    Map<String, List<StickerCategory>> stickerMap;
    CommonAdapter<StickerBarBean> stickerBarBeanCommonAdapter;
    List<StickerBarBean> stickerBarBeanList = new ArrayList<>();
    /**
     * ------------ ---------------------------- ---------------------------
     **/
    private FilterWrap a;
    private FilterImageViewInterface b;
    private FilterImageView filterImageView;
    private boolean hasCameraPic = false;

    public FilterWrap getFilterWrap() {
        return this.a;
    }

    public void setFilterWrap(FilterWrap var1) {
        this.a = var1;
    }

    /**
     * 图片包装视图
     */
    private RelativeLayout mImageWrapView;
    /**
     * 滤镜组选择栏
     */
//    private GroupFilterBar mGroupFilterBar;
    /**
     * 图片编辑滤镜控制器滤镜栏视图
     */
    private TuEditFilterBarView mFilterbar;
    /********************************** Config ***********************************/
    /**
     * 需要显示的滤镜组
     */
    private List<String> mFilterGroup;
    /**
     * 开启滤镜配置选项 (默认: 开启)
     */
    private boolean mEnableFilterConfig = true;
    /**
     * 是否仅返回滤镜，不返回处理图片
     */
    private boolean mOnlyReturnFilter;
    /**
     * 行视图宽度
     */
    private int mGroupFilterCellWidth;
    /**
     * 滤镜分组列表行视图布局资源ID
     */
    private int mGroupTableCellLayoutId;
    /**
     * 滤镜列表行视图布局资源ID
     */
    private int mFilterTableCellLayoutId;
    /**
     * 滤镜组选择栏高度
     */
    private int mFilterBarHeight;
    /**
     * 开启用户滤镜历史记录
     */
    private boolean mEnableFiltersHistory;
    /**
     * 显示滤镜标题视图
     */
    private boolean mDisplayFiltersSubtitles;
    /**
     * 开启无效果滤镜 (默认: true)
     */
    private boolean mEnableNormalFilter = true;
    /**
     * 开启在线滤镜
     */
    private boolean mEnableOnlineFilter;
    /**
     * 在线滤镜控制器类型 (需要继承Fragment,以及实现org.lasque.tusdk.modules.components.filter.
     * TuFilterOnlineFragmentInterface接口)
     */
    private Class<?> mOnlineFragmentClazz;
    /**
     * 是否渲染滤镜封面
     */
    private boolean mRenderFilterThumb;

    /**
     * 需要显示的滤镜名称列表 (如果为空将显示所有自定义滤镜)
     */
    public List<String> getFilterGroup() {
        return mFilterGroup;
    }

    /**
     * 需要显示的滤镜名称列表 (如果为空将显示所有自定义滤镜)
     */
    public void setFilterGroup(List<String> mFilterGroup) {
        this.mFilterGroup = mFilterGroup;
    }

    /**
     * 开启滤镜配置选项 (默认: 开启)
     */
    public boolean isEnableFilterConfig() {
        return mEnableFilterConfig;
    }

    /**
     * 开启滤镜配置选项 (默认: 开启)
     */
    public void setEnableFilterConfig(boolean mEnableFilterConfig) {
        this.mEnableFilterConfig = mEnableFilterConfig;
    }

    /**
     * 是否仅返回滤镜，不返回处理图片(默认：false)
     */
    public boolean isOnlyReturnFilter() {
        return mOnlyReturnFilter;
    }

    /**
     * 是否仅返回滤镜，不返回处理图片(默认：false)
     */
    public void setOnlyReturnFilter(boolean mOnlyReturnFilter) {
        this.mOnlyReturnFilter = mOnlyReturnFilter;
    }

    /**
     * 行视图宽度
     */
    public int getGroupFilterCellWidth() {
        return mGroupFilterCellWidth;
    }

    /**
     * 行视图宽度
     */
    public void setGroupFilterCellWidth(int mGroupFilterCellWidth) {
        this.mGroupFilterCellWidth = mGroupFilterCellWidth;
    }

    /**
     * 滤镜分组列表行视图布局资源ID
     *
     * @return 滤镜分组列表行视图布局资源ID (默认:
     * tusdk_impl_component_widget_group_filter_group_view，如需自定义请继承自
     * GroupFilterGroupView)
     * @see #
     * {@link org.lasque.tusdk.impl.components.widget.GroupFilterGroupView}
     */
    public int getGroupTableCellLayoutId() {
        return mGroupTableCellLayoutId;
    }

    /**
     * 滤镜分组列表行视图布局资源ID
     *
     * @param 滤镜分组列表行视图布局资源ID (默认:
     *                        tusdk_impl_component_widget_group_filter_group_view，如需自定义请继承自
     *                        GroupFilterGroupView)
     * @see #
     * {@link org.lasque.tusdk.impl.components.widget.GroupFilterGroupView}
     */
    public void setGroupTableCellLayoutId(int mGroupTableCellLayoutId) {
        this.mGroupTableCellLayoutId = mGroupTableCellLayoutId;
    }

    /**
     * 滤镜列表行视图布局资源ID
     *
     * @return 滤镜列表行视图布局资源ID (默认:
     * tusdk_impl_component_widget_group_filter_item_view，如需自定义请继承自
     * GroupFilterItemView)
     * @see #
     * {@link org.lasque.tusdk.impl.components.widget.GroupFilterItemView}
     */
    public int getFilterTableCellLayoutId() {
        return mFilterTableCellLayoutId;
    }

    /**
     * 滤镜列表行视图布局资源ID
     *
     * @param 滤镜列表行视图布局资源ID (默认:
     *                      tusdk_impl_component_widget_group_filter_item_view，如需自定义请继承自
     *                      GroupFilterItemView)
     * @see #
     * {@link org.lasque.tusdk.impl.components.widget.GroupFilterItemView}
     */
    public void setFilterTableCellLayoutId(int mFilterTableCellLayoutId) {
        this.mFilterTableCellLayoutId = mFilterTableCellLayoutId;
    }

    /**
     * 滤镜组选择栏高度
     */
    public int getFilterBarHeight() {
        return mFilterBarHeight;
    }

    /**
     * 滤镜组选择栏高度
     */
    public void setFilterBarHeight(int mFilterBarHeight) {
        this.mFilterBarHeight = mFilterBarHeight;
    }

    /**
     * 开启用户滤镜历史记录
     */
    public boolean isEnableFiltersHistory() {
        return mEnableFiltersHistory;
    }

    /**
     * 开启用户滤镜历史记录
     */
    public void setEnableFiltersHistory(boolean mEnableFiltersHistory) {
        this.mEnableFiltersHistory = mEnableFiltersHistory;
    }

    /**
     * 显示滤镜标题视图
     */
    public boolean isDisplayFiltersSubtitles() {
        return mDisplayFiltersSubtitles;
    }

    /**
     * 显示滤镜标题视图
     */
    public void setDisplayFiltersSubtitles(boolean mDisplayFiltersSubtitles) {
        this.mDisplayFiltersSubtitles = mDisplayFiltersSubtitles;
    }

    /**
     * 开启无效果滤镜 (默认: true)
     */
    public boolean isEnableNormalFilter() {
        return mEnableNormalFilter;
    }

    /**
     * 开启无效果滤镜 (默认: true)
     */
    public void setEnableNormalFilter(boolean mEnableNormalFilter) {
        this.mEnableNormalFilter = mEnableNormalFilter;
    }

    /**
     * 开启在线滤镜
     */
    public boolean isEnableOnlineFilter() {
        return mEnableOnlineFilter;
    }

    /**
     * 开启在线滤镜
     */
    public void setEnableOnlineFilter(boolean mEnableOnlineFilter) {
        this.mEnableOnlineFilter = mEnableOnlineFilter;
    }

    /**
     * 在线滤镜控制器类型 (需要继承Fragment,以及实现TuFilterOnlineFragmentInterface接口)
     *
     * @see {@link org.lasque.tusdk.modules.components.filter.TuFilterOnlineFragmentInterface}
     */
    public Class<?> getOnlineFragmentClazz() {
        if (mOnlineFragmentClazz == null) {
            mOnlineFragmentClazz = TuFilterOnlineFragment.class;
        }
        return mOnlineFragmentClazz;
    }

    /**
     * 在线滤镜控制器类型 (需要继承Fragment,以及实现TuFilterOnlineFragmentInterface接口)
     *
     * @see {@link org.lasque.tusdk.modules.components.filter.TuFilterOnlineFragmentInterface}
     */
    public void setOnlineFragmentClazz(Class<?> mOnlineFragmentClazz) {
        this.mOnlineFragmentClazz = mOnlineFragmentClazz;
    }

    /**
     * 是否渲染滤镜封面 (使用设置的滤镜直接渲染，需要拥有滤镜列表封面设置权限，请访问TuSDK.com控制台)
     */
    public boolean isRenderFilterThumb() {
        return mRenderFilterThumb;
    }

    /**
     * 是否渲染滤镜封面 (使用设置的滤镜直接渲染，需要拥有滤镜列表封面设置权限，请访问TuSDK.com控制台)
     */
    public void setRenderFilterThumb(boolean isRender) {
        mRenderFilterThumb = isRender;
    }

    /**
     * ---------------------------------------------------------------------
     */
    public final static int REQUEST_CAMERA = 1;
    public static EditPicActivity instance;
    /**
     * 是否显示相机
     */
    public final static String EXTRA_SHOW_CAMERA = "is_show_camera";
    /**
     * 照片选择模式
     */
    public final static String EXTRA_SELECT_MODE = "select_mode";
    /**
     * 点击跳转模式
     */
    public final static String EXTRA_JUMP_MODE = "jump_mode";
    /**
     * 最大选择数量
     */
    public final static String EXTRA_MAX_MUN = "max_num";
    /**
     * 单选
     */
    public final static int MODE_SINGLE = 0;
    /**
     * 多选
     */
    public final static int MODE_MULTI = 1;
    /**
     * 默认最大选择数量
     */
    public final static int DEFAULT_NUM = 9;

    private final static String ALL_PHOTO = "所有图片";
    /**
     * 是否显示相机，默认不显示
     */
    private boolean mIsShowCamera = false;
    /**
     * 照片选择模式，默认是单选模式
     */
    private int mSelectMode = 0;
    /**
     * 最大选择数量，仅多选模式有用
     */
    private int mMaxNum;
    /**
     * 最大选择数量，仅多选模式有用
     */
    private int mJumpMode = 0;
    private boolean isCamera = false;
    public final static int JUMP_MODE_SELECT_HEAD = 0;

    private GridView mGridView;
    private Map<String, PhotoFloder> mFloderMap;
    private List<Photo> mPhotoLists = new ArrayList<>();
    private ArrayList<String> mSelectList = new ArrayList<>();
    private PhotoAdapter mPhotoAdapter;
    private ProgressDialog mProgressDialog;
    private ListView mFloderListView;

    private TextView mPhotoNameTV;
    private LinearLayout ll_name;
    private Button mCommitBtn;
    private int gridHeight = 0;
    /**
     * 文件夹列表是否处于显示状态
     */
    boolean mIsFloderViewShow = false;
    /**
     * 文件夹列表是否被初始化，确保只被初始化一次
     */
    boolean mIsFloderViewInit = false;

    /**
     * 拍照时存储拍照结果的临时文件
     */
    private File mTmpFile;
    private String currentImaPath;
    private boolean isGridShow = false;

    private Bitmap bitmap;
    public TuSdkHelperComponent componentHelper;
    private GestureDetector mGesture = null;
    private int firstPosition;
    public static String firstImgPath = "";

    /**
     * 布局ID
     */
    public static int getLayoutId() {
        return TuSdkContext.getLayoutResId("fragment_edit_pic");
    }

    public EditPicFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getResString(R.string.picture_edit));
        if (GoodHappinessApplication.isNeedFinish) {
            GoodHappinessApplication.isNeedFinish = false;
            dismissActivity();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getResString(R.string.picture_edit));
    }

    @Override
    protected void loadView(ViewGroup viewGroup) {
        super.loadView(viewGroup);
        findView();
        getStickerData();
        getFilterbar();
        getImageWrapView();
        handler.sendEmptyMessageDelayed(2, 1000);
        getStickerView();
        getCutRegionView();
        getStickerBarView().setGridHeight((int) (80 * GoodHappinessApplication.perHeight));
        getStickerBarView().setGridWidth((int) (80 * GoodHappinessApplication.perHeight));
        setData();
    }

    public <T extends View & FilterImageViewInterface> FilterImageView getImageView2() {
        if (this.b == null && this.getImageWrapView() != null) {
            this.b = filterImageView;
            this.b.enableTouchForOrigin();
            RelativeLayout.LayoutParams var1;
            (var1 = new RelativeLayout.LayoutParams(GoodHappinessApplication.w - (int) (GoodHappinessApplication.perHeight * 20), GoodHappinessApplication.w - (int) (GoodHappinessApplication.perHeight * 20))).addRule(13);
            filterImageView.setLayoutParams(var1);
        }

        return (FilterImageView) this.b;
    }

    /**
     * 图片包装视图
     */
    public RelativeLayout getImageWrapView() {
        if (mImageWrapView == null) {
            mImageWrapView = this.getViewById("lsq_imageWrapView");
        }
        return mImageWrapView;
    }

    private GroupFilterBarInterface.GroupFilterBarDelegate l = new GroupFilterBarInterface.GroupFilterBarDelegate() {
        public boolean onGroupFilterSelected(GroupFilterBarInterface var1, GroupFilterItemViewInterface var2, GroupFilterItem var3) {
            return true;
        }
    };

    /**
     * 滤镜栏
     */
    public TuEditFilterBarView getFilterbar() {
        if (mFilterbar == null) {
            mFilterbar = this.getViewById("lsq_filter_bar");
            if (mFilterbar != null) {
                this.configGroupFilterView(mFilterbar);
                // 绑定选择委托
                mFilterbar.setDelegate(this);
            }
        }
        return mFilterbar;
    }

    protected void configGroupFilterView(GroupFilterBaseView view) {
        if (view == null) return;
        // 行视图宽度
        view.setGroupFilterCellWidth((int) (120 * GoodHappinessApplication.perHeight));
        // 滤镜组选择栏高度
        view.setFilterBarHeight((int) (162 * GoodHappinessApplication.perHeight));
        // 滤镜分组列表行视图布局资源ID
        view.setGroupTableCellLayoutId(R.layout.my_group_filter_group_view);
        // 滤镜列表行视图布局资源ID
        view.setFilterTableCellLayoutId(R.layout.my_group_filter_group_view);
        // 指定显示的滤镜组
        view.setFilterGroup(this.getFilterGroup());
        // 开启滤镜配置选项
        view.setEnableFilterConfig(true);
        // 开启用户滤镜历史记录
        view.setEnableHistory(this.isEnableFiltersHistory());
        // 显示滤镜标题视图
        view.setDisplaySubtitles(false);
        // 设置控制器
        view.setActivity(this.getActivity());
//         开启无效果滤镜 (默认: true)
        view.setEnableNormalFilter(true);
//         开启在线滤镜
        view.setEnableOnlineFilter(true);
//         在线滤镜控制器类型 (需要继承Fragment,以及实现TuFilterOnlineFragmentInterface接口)
        view.setOnlineFragmentClazz(this.getOnlineFragmentClazz());
//         是否渲染滤镜封面 (使用设置的滤镜直接渲染，需要拥有滤镜列表封面设置权限，请访问TuSDK.com控制台)
        view.setRenderFilterThumb(true);
    }

    private void getStickerData() {
        List<StickerCategory> a = StickerLocalPackage.shared().getCategories();
        stickerMap = new HashMap<>();
        for (StickerCategory category : a) {
            if (category.copy().datas != null)
                for (StickerGroup stickerGroup : category.copy().datas) {
                    StickerCategory stickerCategory = new StickerCategory();
                    stickerCategory.append(stickerGroup);
                    stickerCategory.id = category.id;
                    stickerCategory.name = category.name;
                    List<StickerCategory> list = new ArrayList<>();
                    list.add(stickerCategory);
                    stickerMap.put(stickerGroup.name, list);
                }
        }
        if (!stickerMap.isEmpty()) {
            Iterator iterator = stickerMap.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry entry = (Map.Entry) iterator.next();
                stickerBarBeanList.add(new StickerBarBean((String) entry.getKey(), ((List<StickerCategory>) entry.getValue()).get(0).datas.get(0).stickers.get(0)));
            }
            stickerBarBeanCommonAdapter = new CommonAdapter<StickerBarBean>(getActivity(), stickerBarBeanList, R.layout.layout_list_sticker_bar) {
                @Override
                public void convert(ViewHolder helper, StickerBarBean item, int position) {
                    StickerLocalPackage.shared().loadThumb(item.getBg(), (ImageView) helper.getView(R.id.layout_list_sticker_bar_iv));
                    if (mGridHeight != 0) {
                        RoundImageViewByXfermode2 imageView = helper.getView(R.id.layout_list_sticker_bar_iv);
                        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) imageView.getLayoutParams();
                        params.height = params.width = mGridHeight - (int) (22 * GoodHappinessApplication.perHeight);
                        imageView.setLayoutParams(params);
//                        imageView.setBorderRadius(10);
                    }
                }
            };

            lv_sticker.setAdapter(stickerBarBeanCommonAdapter);
            lv_sticker.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    getStickerBarView().loadCategories(stickerMap.get(stickerBarBeanList.get(position).getName()));
                    try {
                        getStickerBarView().refreshCateDatas(stickerMap.get(stickerBarBeanList.get(position).getName()).get(0).datas.get(0).groupId);
                    } catch (Exception e) {
                        getStickerBarView().refreshCateDatas();
                    }
                }
            });
            getStickerBarView().loadCategories(stickerMap.get(stickerBarBeanList.get(0).getName()));
            getStickerBarView().refreshCateDatas(stickerMap.get(stickerBarBeanList.get(0).getName()).get(0).datas.get(0).groupId);
        }

    }

    @Override
    protected void viewDidLoad(ViewGroup view) {
        super.viewDidLoad(view);
        this.loadImageWithThread();
    }

    @Override
    protected void notifyProcessing(TuSdkResult tuSdkResult) {
        iv_filterResult.setImageBitmap(tuSdkResult.image);
        getStickerView().cancelAllStickerSelected();
        Bitmap q = PublishActivity.convertViewToBitmap(rl_cut);
        iv_filterResult.setImageBitmap(null);
        hubDismiss();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        q.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        q = null;
        PreferencesUtil.setPreferences(getActivity(), FieldFinals.VALUE, data);
        if (EditPicUpdateActivity.intent_type == EditPicUpdateActivity.INTENT_PUBLISH) {
            Intent intent = new Intent(getActivity(), PublishActivity.class);
//            intent.putExtra(FieldFinals.IMAGE_PATH, data);
            startActivity(intent);
        } else {
            Intent intent = new Intent(getActivity(), ShowHeadImgActivity.class);
//            intent.putExtra(FieldFinals.IMAGE_PATH, data);
            startActivity(intent);
        }
    }

    protected void handleCompleteButton2() {

        setEditView();
        final TuSdkResult var1;
        (var1 = new TuSdkResult()).filterWrap = this.getFilterWrap();
        (new Thread(new Runnable() {
            public void run() {
                asyncEditWithResult(var1);
            }
        })).start();
    }

    protected void asyncEditWithResult(TuSdkResult var1) {
        this.loadOrginImage(var1);
        if (var1.filterWrap != null && var1.image != null) {
            float var2 = TuSdkSize.create(var1.image).limitScale();
            var1.image = BitmapHelper.imageScale(var1.image, var2);
            var1.image = var1.filterWrap.process(var1.image);
        }
        this.asyncProcessingIfNeedSave(var1);
    }

    /**
     * 异步加载图片完成
     */
    @Override
    protected void asyncLoadImageCompleted(Bitmap image) {
        super.asyncLoadImageCompleted(image);
        if (image == null) return;
        this.setImageRegionMask(image);
    }

    /**
     * 设置图片选区遮罩
     */
    protected void setImageRegionMask(Bitmap image) {
        if (image == null) return;
        if (this.getCutRegionView() != null) {
            this.getCutRegionView().setRegionRatio(TuSdkSize.create(image).getRatioFloat());
        }
    }

    @Override
    protected boolean asyncNotifyProcessing(TuSdkResult tuSdkResult) {
        return true;
    }

    private void findView() {
        rl_cut = this.getViewById(R.id.rl_cut);
        iv_filterResult = this.getViewById(R.id.filter_result);
        filterImageView = this.getViewById(R.id.filterimageview);
        rl_chooseFragment = this.getViewById(R.id.fragment_edit_pic_rl_sticker_house);
        sv = this.getViewById("edit_pic_sv");
        v_hide = this.getViewById("edit_pic_hide_view");
        rl_show = this.getViewById("edit_pic_rl_show");
        tv_tips = this.getViewById("edit_pic_tv_tips");
        iv_back = this.getViewById("common_left");
        tv_right = this.getViewById("common_right_text");
        tv_sticker = this.getViewById("edit_pic_tv_sticker");
        tv_filter = this.getViewById("edit_pic_tv_filter");
        lv_sticker = this.getViewById("fragment_edit_pic_lv_sticker");
        rl_sticker = this.getViewById("fragment_edit_pic_rl_sticker");
        rl_chooseFragment.setOnClickListener(mButtonClickListener);
    }

    /**
     * 贴纸栏视图
     */
    public MyStickerBarView getStickerBarView() {
        if (mStickerBarView == null) {
            mStickerBarView = this.getViewById("lsq_sticker_bar");
            if (mStickerBarView != null) {
                mStickerBarView.setDelegate(this);
            }
        }
        return mStickerBarView;
    }

    @Override
    public StickerView getStickerView() {
        if (mStickerView == null) {
            mStickerView = this.getViewById("lsq_stickerView");
            if (mStickerView != null) {
                mStickerView.setDelegate(mStickerViewDelegate);
            }
        }
        return mStickerView;
    }

    @Override
    public TuMaskRegionView getCutRegionView() {
        if (mCutRegionView == null) {
            mCutRegionView = this.getViewById("lsq_cutRegionView");
            if (mCutRegionView != null) {
                mCutRegionView.setEdgeMaskColor(TuSdkContext.getColor("lsq_background_editor"));
                mCutRegionView.setEdgeSideColor(0x80FFFFFF);
            }
        }
        return mCutRegionView;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.setRootViewLayoutId(getLayoutId());
        mStickerViewDelegate = new StickerView.StickerViewDelegate() {
            @Override
            public boolean canAppendSticker(StickerView stickerView, StickerData stickerData) {
                return true;
            }
        };
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void setData() {
        initIntentParams();
        iv_back.setImageResource(R.mipmap.icon_delete_default);
        if (EditPicUpdateActivity.intent_type == EditPicUpdateActivity.INTENT_PUBLISH) {
            tv_right.setText(R.string.continue_);
        } else {
            tv_right.setText(R.string.complete);
        }
        iv_back.setOnClickListener(mButtonClickListener);
        tv_right.setOnClickListener(mButtonClickListener);
        tv_sticker.setOnClickListener(mButtonClickListener);
        tv_filter.setOnClickListener(mButtonClickListener);
        gridHeight = (int) ((float) GoodHappinessApplication.h * 3 / 5);
        initGridView();
        if (!OtherUtils.isExternalStorageAvailable()) {
            Toast.makeText(getActivity(), R.string.no_sd_card, Toast.LENGTH_LONG).show();
            return;
        }
        getPhotosTask.execute();
    }

    private void initGridView() {
        mGridView = this.getViewById(R.id.photo_gridview);
        mGesture = new GestureDetector(getActivity(), new GestureListener());
        ll_name = this.getViewById(R.id.floder_ll_name);
        mPhotoNameTV = this.getViewById(R.id.floder_name);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mGridView.getLayoutParams();
        layoutParams.width = GoodHappinessApplication.w;
        layoutParams.height = gridHeight;
        mGridView.setLayoutParams(layoutParams);
        v_hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkGridShow(false);
            }
        });
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv_tips.getLayoutParams();
        params.height = params.width = GoodHappinessApplication.w;
        tv_tips.setLayoutParams(params);
        sv.smoothScrollTo(0, 0);
    }

    private void showNotification() {
        NotifySelectFragment fragment = NotifySelectFragment.newInstance(2, 5, false, false, getString(R.string.exit_this_edit_), getString(R.string.exit_will_delete_all_), R.drawable.shape_for_black_trans);
        fragment.setOnSelectListener(new OnSelectListener() {
            @Override
            public void onSelected(boolean isSelected) {
                if (isSelected) {
                    finish();
                }
            }
        });
        fragment.show(getActivity().getFragmentManager(), "exit");
    }

    private void doTransAction(int action) {
        currentStatus = action;
        resetBottomView();
        if (action == PHOTO) {
            mGridView.setVisibility(View.VISIBLE);
            mPhotoNameTV.setTextColor(getResources().getColor(R.color.gray_666_text));
            return;
        }
        if (hasCameraPic||bitmap == null) {
            setEditView();
        }
        if (mIsFloderViewShow) {
            outAnimatorSet.start();
            mIsFloderViewShow = false;
        }
        switch (action) {
            case STICKER:
                rl_sticker.setVisibility(View.VISIBLE);
                tv_sticker.setTextColor(getResources().getColor(R.color.gray_666_text));
                break;
            case FILTER:
                getFilterbar().setVisibility(View.VISIBLE);
                tv_filter.setTextColor(getResources().getColor(R.color.gray_666_text));
                break;
        }
    }

    private void setEditView() {
        bitmap = mClipImageLayout.clip();
        rl_show.removeAllViews();
        rl_show.setVisibility(View.GONE);
        setRectSize(getImageWrapView());
        setRectSize(getStickerView());
        setRectSize(getCutRegionView());
        setImage(bitmap);
        getImageView2().setImage(bitmap);
        loadImageWithThread();
        if (this.getFilterbar() == null) return;
        this.getFilterbar().setThumbImage(bitmap);
        this.getFilterbar().setDefaultShowState(true);
        if (this.getFilterWrap() != null) {
            this.getFilterbar().loadFilters(this.getFilterWrap().getOption());
        } else {
            this.getFilterbar().loadFilters(null);
        }
        rl_cut.setVisibility(View.VISIBLE);
    }

    private void dismissEditView() {
        rl_cut.setVisibility(View.GONE);
        getStickerView().setVisibility(View.GONE);
        getCutRegionView().setVisibility(View.GONE);
    }

    private void setRectSize(View view) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) view.getLayoutParams();
        layoutParams.width = layoutParams.height = GoodHappinessApplication.w - (int) (GoodHappinessApplication.perHeight * 20);
        view.setLayoutParams(layoutParams);
        view.setVisibility(View.VISIBLE);
    }

    private void resetBottomView() {
        mGridView.setVisibility(View.GONE);
        rl_sticker.setVisibility(View.GONE);
        getFilterbar().setVisibility(View.GONE);
        mPhotoNameTV.setTextColor(getResources().getColor(R.color.gray_999_text));
        tv_sticker.setTextColor(getResources().getColor(R.color.gray_999_text));
        tv_filter.setTextColor(getResources().getColor(R.color.gray_999_text));
    }

    private void finish() {
        hubDismissRightNow();
        dismissActivityWithAnim();
        AppManager.getAppManager().finishActivity(EditPicUpdateActivity.class);
    }

    /**
     * 按钮点击事件
     */
    protected View.OnClickListener mButtonClickListener = new TuSdkViewHelper.OnSafeClickListener() {
        @Override
        public void onSafeClick(View v) {
            // 分发视图点击事件
            dispatcherViewClick(v);
        }
    };

    /**
     * 分发视图点击事件
     */
    protected void dispatcherViewClick(View v) {
        if (this.equalViewIds(v, tv_right)) {
            if (hasCameraPic||hasPath() || bitmap != null) {
                this.handleCompleteButton2();
            } else {
                Toast.makeText(getActivity(), R.string.select_pic_to_process, Toast.LENGTH_LONG).show();
            }
        } else if (this.equalViewIds(v, iv_back)) {
            onBackPressed();
        } else if (this.equalViewIds(v, tv_sticker)) {
            if (hasCameraPic||hasPath() || bitmap != null) {
                if (currentStatus != STICKER)
                    doTransAction(STICKER);
            }
        } else if (this.equalViewIds(v, tv_filter)) {
            if (hasCameraPic||hasPath() || bitmap != null) {
                if (currentStatus != FILTER)
                    doTransAction(FILTER);
            }
        } else if (this.equalViewIds(v, rl_chooseFragment)) {
            this.handleListButton();
        }
    }

    /**
     * 开启完整贴纸选择列表
     */
    protected void handleListButton() {
        TuStickerChooseFragment ofragment = new TuStickerChooseFragment();
        ofragment.setDelegate(this);
        this.presentModalNavigationActivity(ofragment, false);
    }

    private boolean hasPath() {
        if (TextUtils.isEmpty(currentImaPath)) {
            if (bitmap == null) {
                Toast.makeText(getActivity(), R.string.select_pic_to_process, Toast.LENGTH_LONG).show();
            }
            return false;
        } else {
            return true;
        }
    }

    /**
     * 初始化选项参数
     */
    private void initIntentParams() {
        mIsShowCamera = true;
        mSelectMode = MODE_MULTI;
        mJumpMode = JUMP_MODE_SELECT_HEAD;
        mMaxNum = 1;
    }

    private void getPhotosSuccess() {
        mProgressDialog.dismiss();
        mPhotoLists.addAll(mFloderMap.get(ALL_PHOTO).getPhotoList());
        if (mPhotoLists.size() > 0) {
            firstImgPath = mPhotoLists.get(0).getPath();
        }
        mPhotoAdapter = new PhotoAdapter(getActivity().getApplicationContext(), mPhotoLists);
        mPhotoAdapter.setIsShowCamera(mIsShowCamera);
        mPhotoAdapter.setSelectMode(mSelectMode);
        mPhotoAdapter.setMaxNum(mMaxNum);
        mPhotoAdapter.setPhotoClickCallBack(this);
        mGridView.setAdapter(mPhotoAdapter);
        mGridView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return mGesture.onTouchEvent(event);
            }
        });
        mGridView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                firstPosition = firstVisibleItem;
                if(firstVisibleItem+visibleItemCount==totalItemCount){
                    checkGridShow(true);
                }
            }
        });
        Set<String> keys = mFloderMap.keySet();
        final List<PhotoFloder> floders = new ArrayList<>();
        for (String key : keys) {
            if (ALL_PHOTO.equals(key)) {
                PhotoFloder floder = mFloderMap.get(key);
                floder.setIsSelected(true);
                floders.add(0, floder);
            } else {
                floders.add(mFloderMap.get(key));
            }
        }
        mPhotoNameTV.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (currentStatus == PHOTO) {
                    toggleFloderList(floders);
                } else {
                    doTransAction(PHOTO);
                }
            }
        });
        ll_name.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (currentStatus == PHOTO) {
                    toggleFloderList(floders);
                } else {
                    doTransAction(PHOTO);
                }
            }
        });

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mPhotoAdapter.isShowCamera() && position == 0) {
                    showCamera();
                    return;
                }
                selectPhoto(mPhotoAdapter.getItem(position));
            }
        });
    }

    /**
     * 点击选择某张照片
     *
     * @param photo
     */
    private void selectPhoto(Photo photo) {
        if (photo == null) {
            return;
        }
        String path = photo.getPath();
        if (mSelectMode == MODE_SINGLE) {
            mSelectList.add(path);
        }
    }

    @Override
    public boolean onPhotoClick(final String path, final View v) {
        if (bitmap == null) {
            selectPicture(path);
            return true;
        } else {
            NotifySelectFragment fragment = NotifySelectFragment.newInstance(2, 5, false, false, getString(R.string.is_change_pic), " ", R.drawable.shape_for_black_trans);
            fragment.setOnSelectListener(new OnSelectListener() {
                @Override
                public void onSelected(boolean isSelected) {
                    if (isSelected) {
                        bitmap = null;
                        hasCameraPic = false;
                        selectPicture(path);
                        mPhotoAdapter.mSelectedPhotos.clear();
                        mPhotoAdapter.mSelectedPhotos.add(path);
                        rl_cut.setVisibility(View.GONE);
                        if (mPhotoAdapter.leastView != null) {
                            mPhotoAdapter.leastView.findViewById(R.id.mask).setVisibility(View.GONE);
                            mPhotoAdapter.leastView.findViewById(R.id.checkmark).setSelected(false);
                        }
                        v.findViewById(R.id.mask).setVisibility(View.VISIBLE);
                        v.findViewById(R.id.checkmark).setSelected(true);
                        mPhotoAdapter.leastView = v;
                        getStickerView().removeAllViews();
                        dismissEditView();
                    }
                }
            });
            fragment.show(getActivity().getFragmentManager(), "changePic");
            return false;
        }
    }

    private void selectPicture(String path) {
        checkGridShow(true);
        tv_tips.setVisibility(View.GONE);
        rl_cut.setVisibility(View.GONE);
        currentImaPath = path;
        rl_show.setVisibility(View.VISIBLE);
        Bitmap bitmap;
        if ((bitmap = ImageLoader.getInstance().loadImageSync(StringFinal.IMG_URI_HEAD+currentImaPath)) != null) {
            if (rl_show.getChildCount() > 0) {
                rl_show.removeAllViews();
            }
            mClipImageLayout = new ClipImageLayout(getActivity(), null, new BitmapDrawable(bitmap), 0, ClipImageBorderView.BORDER_STYLE_RECTANGLE);//new BitmapDrawable(PictureUtil.getSmallBitmap(img_path)));
            mClipImageLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkGridShow(false);
                }
            });
            android.view.ViewGroup.LayoutParams lp = new RelativeLayout.LayoutParams(GoodHappinessApplication.w, GoodHappinessApplication.w);
            rl_show.addView(mClipImageLayout, lp);
        } else {
//            showToast("无法加载图片");
        }
    }

    /**
     * b true上滑
     *
     * @param b
     */
    private void checkGridShow(boolean b) {
        if (b) {//上滑显示下面
            if (!isGridShow) {
                sv.smoothScrollTo(0, gridHeight);
                isGridShow = true;
                v_hide.setVisibility(View.VISIBLE);
            }
        } else {//下滑显示上面
            if (isGridShow) {
                sv.smoothScrollTo(0, 0);
                isGridShow = false;
                v_hide.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 显示或者隐藏文件夹列表
     *
     * @param floders
     */
    private void toggleFloderList(final List<PhotoFloder> floders) {
        //初始化文件夹列表
        if (!mIsFloderViewInit) {
            ViewStub floderStub = (ViewStub) this.getViewById(R.id.floder_stub);
            floderStub.inflate();
            View dimLayout = this.getViewById(R.id.dim_layout);
            mFloderListView = (ListView) this.getViewById(R.id.listview_floder);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mFloderListView.getLayoutParams();
            params.width = GoodHappinessApplication.w;
            params.height = (int) ((float) GoodHappinessApplication.h * 23 / 40);
            mFloderListView.setLayoutParams(params);
            final FloderAdapter adapter = new FloderAdapter(getActivity(), floders);
            mFloderListView.setAdapter(adapter);
            mFloderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    for (PhotoFloder floder : floders) {
                        floder.setIsSelected(false);
                    }
                    PhotoFloder floder = floders.get(position);
                    floder.setIsSelected(true);
                    adapter.notifyDataSetChanged();

                    mPhotoLists.clear();
                    mPhotoLists.addAll(floder.getPhotoList());
                    if (ALL_PHOTO.equals(floder.getName())) {
                        mPhotoAdapter.setIsShowCamera(mIsShowCamera);
                    } else {
                        mPhotoAdapter.setIsShowCamera(false);
                    }
                    //这里重新设置adapter而不是直接notifyDataSetChanged，是让GridView返回顶部
                    mGridView.setAdapter(mPhotoAdapter);
                    mPhotoNameTV.setText(floder.getName());
                    toggle();
                }
            });
            dimLayout.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    if (mIsFloderViewShow) {
                        toggle();
                        return true;
                    } else {
                        return false;
                    }
                }
            });
            initAnimation(dimLayout);
            mIsFloderViewInit = true;
        }
        toggle();
    }

    /**
     * 弹出或者收起文件夹列表
     */
    private void toggle() {
        if (mIsFloderViewShow) {
            outAnimatorSet.start();
            mIsFloderViewShow = false;
        } else {
            inAnimatorSet.start();
            mIsFloderViewShow = true;
        }
    }


    /**
     * 初始化文件夹列表的显示隐藏动画
     */
    AnimatorSet inAnimatorSet = new AnimatorSet();
    AnimatorSet outAnimatorSet = new AnimatorSet();

    private void initAnimation(View dimLayout) {
        ObjectAnimator alphaInAnimator, alphaOutAnimator, transInAnimator, transOutAnimator;
        //获取actionBar的高
        alphaInAnimator = ObjectAnimator.ofFloat(dimLayout, "alpha", 0f, 0.7f);
        alphaOutAnimator = ObjectAnimator.ofFloat(dimLayout, "alpha", 0.7f, 0f);
        transInAnimator = ObjectAnimator.ofFloat(mFloderListView, "translationY", GoodHappinessApplication.h, GoodHappinessApplication.h / 4);
        transOutAnimator = ObjectAnimator.ofFloat(mFloderListView, "translationY", GoodHappinessApplication.h / 4, GoodHappinessApplication.h);

        LinearInterpolator linearInterpolator = new LinearInterpolator();

        inAnimatorSet.play(transInAnimator).with(alphaInAnimator);
        inAnimatorSet.setDuration(300);
        inAnimatorSet.setInterpolator(linearInterpolator);
        outAnimatorSet.play(transOutAnimator).with(alphaOutAnimator);
        outAnimatorSet.setDuration(300);
        outAnimatorSet.setInterpolator(linearInterpolator);
    }

    /**
     * 获取照片的异步任务
     */
    private AsyncTask getPhotosTask = new AsyncTask() {
        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(getActivity(), null, getString(R.string.loading_));
        }

        @Override
        protected Object doInBackground(Object[] params) {
            mFloderMap = PhotoUtils.getPhotos(
                    getActivity().getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            getPhotosSuccess();
        }
    };

    @Override
    public void onComponentError(TuFragment tuFragment, TuSdkResult tuSdkResult, Error error) {

    }

    @Override
    public boolean onFilterSelected(TuEditFilterBarView view, GroupFilterItem itemData) {
        if (itemData.type == GroupFilterItem.GroupFilterItemType.TypeFilter) {
//            FilterLocalPackage.shared().loadFilterThumb(getImageView(),itemData.filterOption);
            return handleSwitchFilter(itemData.getFilterCode());
        }
        return true;
    }

    protected boolean handleSwitchFilter(final String var1) {
        if (var1 != null && this.getImageView2() != null) {
            if (this.a != null && this.a.equalsCode(var1)) {
                return false;
            } else {
                this.hubStatus(TuSdkContext.getString("lsq_edit_filter_processing"));
                (new Thread(new Runnable() {
                    public void run() {
                        asyncProcessingFilter(var1);
                    }
                })).start();
                return true;
            }
        } else {
            return false;
        }
    }

    protected void asyncProcessingFilter(String var1) {
        this.a = FilterLocalPackage.shared().getFilterWrap(var1);
        if (this.a != null) {
            (this.getImageView2()).setFilterWrap(this.a);
        }

        (new Handler(Looper.getMainLooper())).post(new Runnable() {
            public void run() {
                processedFilter();
            }
        });
    }

    protected void processedFilter() {
        hubDismiss();
        notifyFilterConfigView();
    }

    public void notifyFilterConfigView() {
        if (this.getFilterbar() == null) return;
        this.getFilterbar().setFilter(this.getFilterWrap());
    }

    @Override
    public void onFilterConfigRequestRender(TuEditFilterBarView view) {
        if (this.getImageView2() != null) this.getImageView2().requestRender();
    }

    @Override
    public void onTuStickerChooseFragmentSelected(TuStickerChooseFragment fragment, StickerData data) {
        if (fragment != null) {
            fragment.dismissActivityWithAnim();
        }
        this.appendStickerItem(data);
    }

    class GestureListener extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            // TODO Auto-generated method stub
            Log.i("TEST", "onDoubleTap");
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onDown(MotionEvent e) {
            // TODO Auto-generated method stub
            Log.i("TEST", "onDown");
            return super.onDown(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            // TODO Auto-generated method stub
            Log.i("TEST", "onFling:velocityX = " + velocityX + " velocityY" + velocityY);
            if (Math.abs(velocityY) > 1000) {
                if (velocityY > 0) {//下滑
                    if (firstPosition == 0) {
                        checkGridShow(false);
                    }
                } else {//上滑
                    checkGridShow(true);
                }
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public void onLongPress(MotionEvent e) {
            // TODO Auto-generated method stub
            Log.i("TEST", "onLongPress");
            super.onLongPress(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            // TODO Auto-generated method stub
            Log.i("TEST", "onScroll:distanceX = " + distanceX + " distanceY = " + distanceY);
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            // TODO Auto-generated method stub
            Log.i("TEST", "onSingleTapUp");
            return super.onSingleTapUp(e);
        }

    }

    /**
     * 选择相机
     */
    private void showCamera() {
        CameraUtils.showSample(getActivity(), this);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
//                rl_show.removeAllViews();
//                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_show.getLayoutParams();
//                layoutParams.width = layoutParams.height = GoodHappinessApplication.w;
//                iv_show.setLayoutParams(layoutParams);
//                iv_show.setImageBitmap(bitmap);
//                iv_show.setVisibility(View.VISIBLE);
            } else if (msg.what == 1) {
                if (msg.obj != null) {
                    rl_show.setVisibility(View.VISIBLE);
                    if (rl_show.getChildCount() > 0) {
                        rl_show.removeAllViews();
                    }
                    checkGridShow(true);
                    tv_tips.setVisibility(View.GONE);
                    rl_cut.setVisibility(View.GONE);
                    mClipImageLayout = new ClipImageLayout(getActivity(), null, new BitmapDrawable((Bitmap)msg.obj), 0, ClipImageBorderView.BORDER_STYLE_RECTANGLE);//new BitmapDrawable(PictureUtil.getSmallBitmap(img_path)));
                    mClipImageLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            checkGridShow(false);
                        }
                    });
                    android.view.ViewGroup.LayoutParams lp = new RelativeLayout.LayoutParams(GoodHappinessApplication.w, GoodHappinessApplication.w);
                    rl_show.addView(mClipImageLayout, lp);
                }
            } else if (msg.what == 2) {
                LinearLayout.LayoutParams rl_stickerParams = (LinearLayout.LayoutParams) rl_sticker.getLayoutParams();
                rl_stickerParams.height = GoodHappinessApplication.h - GoodHappinessApplication.w - (int) (170 * GoodHappinessApplication.perHeight);
                mGridHeight = (int) ((float) (rl_stickerParams.height / 2));
                getStickerBarView().setGridHeight(mGridHeight);
                getStickerBarView().setGridWidth(mGridHeight);
                rl_sticker.setLayoutParams(rl_stickerParams);
                stickerBarBeanCommonAdapter.notifyDataSetChanged();
                LinearLayout.LayoutParams filterBarParams = (LinearLayout.LayoutParams) getFilterbar().getLayoutParams();
                filterBarParams.height = rl_stickerParams.height;
                getFilterbar().setLayoutParams(filterBarParams);
                getFilterbar().setVisibility(View.GONE);//162 120
                getFilterbar().setFilterBarHeight((int) (rl_stickerParams.height - 60 * GoodHappinessApplication.perHeight));
                int filterCellWith = (int) (((float) 120 * (rl_stickerParams.height - 60 * GoodHappinessApplication.perHeight)) / 160);
                getFilterbar().setGroupFilterCellWidth(filterCellWith);
                TuSdkImageButton button = EditPicFragment.this.getViewById(R.id.lsq_filter_back_button);
                RelativeLayout.LayoutParams buttonParams = (RelativeLayout.LayoutParams) button.getLayoutParams();
                int backBtnWith = (int) (((float) 120 * (rl_stickerParams.height - 90 * GoodHappinessApplication.perHeight)) / 160);
                buttonParams.width = buttonParams.height= backBtnWith;
                buttonParams.setMargins((filterCellWith-backBtnWith)/2,20,0,0);
                button.setLayoutParams(buttonParams);
                // 滤镜组选择栏高度
            }
        }
    };

    @Override
    public void onTuCameraFragmentCaptured(TuCameraFragment tuCameraFragment, TuSdkResult tuSdkResult) {
//        bitmap = tuSdkResult.image;
        Message message = handler.obtainMessage();
        message.obj = tuSdkResult.image;
        message.what = 1;
        hasCameraPic = true;
        handler.sendMessage(message);
        tuCameraFragment.hubDismissRightNow();
        tuCameraFragment.dismissActivityWithAnim();
    }

    @Override
    public boolean onTuCameraFragmentCapturedAsync(TuCameraFragment tuCameraFragment, TuSdkResult tuSdkResult) {

        return true;
    }

    @Override
    public void onTuAlbumDemand(TuCameraFragment tuCameraFragment) {

    }

    @Override
    public void onStickerBarViewSelected(MyStickerBarView view, StickerData data) {
        this.appendStickerItem(data);
    }

    @Override
    public void onStickerBarViewEmpty(MyStickerBarView view, StickerCategory cate) {

    }

    @Override
    public boolean onBackPressed() {
        if (hasCameraPic||!TextUtils.isEmpty(currentImaPath)) {
            showNotification();
        } else {
            finish();
        }
        return false;
    }

}
