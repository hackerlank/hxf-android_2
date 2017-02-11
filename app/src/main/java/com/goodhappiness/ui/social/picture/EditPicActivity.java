package com.goodhappiness.ui.social.picture;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
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
import com.goodhappiness.adapter.FloderAdapter;
import com.goodhappiness.adapter.PhotoAdapter;
import com.goodhappiness.bean.Photo;
import com.goodhappiness.bean.PhotoFloder;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.StringFinal;
import com.goodhappiness.dao.OnBackKeyDownClickListener;
import com.goodhappiness.dao.OnSelectListener;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.ui.fragment.NotifySelectFragment;
import com.goodhappiness.utils.CameraUtils;
import com.goodhappiness.widget.ClipImageBorderView;
import com.goodhappiness.widget.ClipImageLayout;
import com.goodhappiness.widget.photopicker.LogUtils;
import com.goodhappiness.widget.photopicker.OtherUtils;
import com.goodhappiness.widget.photopicker.PhotoUtils;
import com.umeng.analytics.MobclickAgent;

import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.impl.components.camera.TuCameraFragment;
import org.lasque.tusdk.impl.components.filter.TuEditFilterFragment;
import org.lasque.tusdk.impl.components.filter.TuEditFilterOption;
import org.lasque.tusdk.modules.components.TuSdkHelperComponent;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@ContentView(R.layout.activity_edit_pic_activity)
public class EditPicActivity extends BaseActivity implements PhotoAdapter.PhotoClickCallBack, TuEditStickerFragment2.TuEditStickerFragment2Delegate, TuEditFilterFragment.TuEditFilterFragmentDelegate,TuCameraFragment.TuCameraFragmentDelegate {
    private ClipImageLayout mClipImageLayout;//截取矩形头像的自定义控件
    @ViewInject(R.id.edit_pic_sv)
    private ScrollView sv;
    @ViewInject(R.id.edit_pic_hide_view)
    private View v_hide;
    @ViewInject(R.id.edit_pic_rl_show)
    private RelativeLayout rl_show;
    @ViewInject(R.id.edit_pic_iv_show)
    private ImageView iv_show;
    @ViewInject(R.id.edit_pic_tv_tips)
    private TextView tv_tips;

    public final static String TAG = "EditPicActivity";

    public final static int REQUEST_CAMERA = 1;
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
    private boolean isProcess = false;

    private Bitmap bitmap;
    public TuSdkHelperComponent componentHelper;
    private GestureDetector mGesture = null;
    private int firstPosition;
    public static String firstImgPath="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void setData() {
        initIntentParams();
        initView();
        iv_back.setImageResource(R.mipmap.icon_delete_default);
        tv_right.setText("继续");
        gridHeight = (int) ((float) GoodHappinessApplication.h * 3 / 5);
        initView2();
        if (!OtherUtils.isExternalStorageAvailable()) {
            Toast.makeText(this, "没有SD卡!", Toast.LENGTH_LONG).show();
            return;
        }
        getPhotosTask.execute();
    }

    @Override
    protected void reload() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firstImgPath = null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
        isCamera = false;
        mSelectList.clear();
    }

    private void initView2() {
        componentHelper = new TuSdkHelperComponent(this);
        mGridView = (GridView) findViewById(R.id.photo_gridview);
        mGesture = new GestureDetector(this, new GestureListener());
        ll_name = (LinearLayout) findViewById(R.id.floder_ll_name);
        mPhotoNameTV = (TextView) findViewById(R.id.floder_name);
        onBackKeyDownClickListener = new OnBackKeyDownClickListener() {
            @Override
            public void onBackKeyClick() {
                showNotification();
            }
        };
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
        NotifySelectFragment fragment = NotifySelectFragment.newInstance(2, 5, false, false, "", "退出将清除所有效果",0);
        fragment.setOnSelectListener(new OnSelectListener() {
            @Override
            public void onSelected(boolean isSelected) {
                if (isSelected) {
                    finishActivity();
                }
            }
        });
        fragment.show(getFragmentManager(), "exit");
    }

    private void showSticker() {
        TuEditStickerOption2 option = new TuEditStickerOption2();

        // 是否在控制器结束后自动删除临时文件
        option.setAutoRemoveTemp(true);
        // 设置贴纸单元格的高度
        option.setGridHeight(150);
        // 设置贴纸单元格的间距
        option.setGridPadding(8);

        TuEditStickerFragment2 fragment = option.fragment();

        // 输入的图片对象 (处理优先级: Image > TempFilePath > ImageSqlInfo)
        fragment.setImage(bitmap);

        fragment.setDelegate(this);

        // 开启贴纸编辑界面
        componentHelper.pushModalNavigationActivity(fragment, true);
    }

    private void showFilter() {
        TuEditFilterOption option = new TuEditFilterOption();

        // 控制器类型，这里指定为扩展类 ExtendFilterFragment
        option.setComponentClazz(ExtendFilterFragment.class);
        // 设置根视图布局资源ID
        // option.setRootViewLayoutId(TuEditFilterFragment.getLayoutId());

        // 是否在控制器结束后自动删除临时文件
        option.setAutoRemoveTemp(true);
        // 显示滤镜标题视图
        option.setDisplayFiltersSubtitles(true);
        // 开启在线滤镜
        option.setEnableOnlineFilter(true);
        // 开启用户滤镜历史记录
        option.setEnableFiltersHistory(true);

        // 需要显示的滤镜名称列表 (如果为空将显示所有自定义滤镜)
        // 访问文档查看详细用法  @see-http://tusdk.com/docs/android/customize-filter
        // option.setFilterGroup(Arrays.asList(filters));

        // 是否渲染滤镜封面 (使用设置的滤镜直接渲染，需要拥有滤镜列表封面设置权限，请访问TuSDK.com控制台)
         option.setRenderFilterThumb(true);

        // 是否显示处理结果预览图 (默认：关闭，调试时可以开启)
//        option.setShowResultPreview(true);

        TuEditFilterFragment fragment = option.fragment();

        // 输入的图片对象 (处理优先级: Image > TempFilePath > ImageSqlInfo)
        fragment.setImage(bitmap);

        fragment.setDelegate(this);
        // 开启贴纸编辑界面
        componentHelper.pushModalNavigationActivity(fragment, true);
//        FragmentManager fragmentManager = getSupportFragmentManager();
//        FragmentTransaction transaction =
//                fragmentManager.beginTransaction();
//
//        // 将滤镜fragment加入Activity
//        transaction.add(R.id.frameLayout, fragment);
//        transaction.commit();
    }

    @Event({R.id.common_right_text, R.id.edit_pic_tv_sticker, R.id.edit_pic_tv_filter})
    private void onEventClick(View v) {
        switch (v.getId()) {
            case R.id.edit_pic_tv_sticker:
                if (hasPath()||bitmap!=null) {
                    if (!hasBitmap()||!isProcess) {
                        bitmap = mClipImageLayout.clip();
                        if (bitmap != null)
                            showSticker();
                    } else {
                        showSticker();
                    }
                }
                break;
            case R.id.edit_pic_tv_filter:
                if (hasPath()||bitmap!=null) {
                    if (!hasBitmap()||!isProcess) {
                        bitmap = mClipImageLayout.clip();
                        if (bitmap != null)
                            showFilter();
                    } else {
                        showFilter();
                    }
                }
                break;
            case R.id.common_right_text:
                if (hasPath()||bitmap!=null) {
                    if(bitmap==null){
                        bitmap = mClipImageLayout.clip();
                    }
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
                    byte[] data = baos.toByteArray();
                    Intent intent = new Intent(this, PublishActivity.class);
                    intent.putExtra(FieldFinals.IMAGE_PATH, data);
                    startTheActivity(intent);
                }
                break;
        }
    }

    private boolean hasBitmap() {
        if (bitmap == null) {

            return false;
        } else {
            return true;
        }
    }

    private boolean hasPath() {
        if (TextUtils.isEmpty(currentImaPath)) {
            if(bitmap==null)
            showToast("请选择图片再进入下一步处理");
            return false;
        } else {
            return true;
        }
    }

    /**
     * 初始化选项参数
     */
    private void initIntentParams() {
        mIsShowCamera = getIntent().getBooleanExtra(EXTRA_SHOW_CAMERA, true);
        mSelectMode = getIntent().getIntExtra(EXTRA_SELECT_MODE, MODE_MULTI);
        mJumpMode = getIntent().getIntExtra(EXTRA_JUMP_MODE, JUMP_MODE_SELECT_HEAD);
        mMaxNum = getIntent().getIntExtra(EXTRA_MAX_MUN, 1);
        if (mSelectMode == MODE_MULTI) {
//            //如果是多选模式，需要将确定按钮初始化以及绑定事件
//            mCommitBtn = (Button) findViewById(R.id.commit);
//            mCommitBtn.setVisibility(View.VISIBLE);
//            mCommitBtn.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mSelectList.addAll(mPhotoAdapter.getmSelectedPhotos());
//                    returnData();
//                }
//            });
        }
    }

    private void getPhotosSuccess() {
        mProgressDialog.dismiss();
        mPhotoLists.addAll(mFloderMap.get(ALL_PHOTO).getPhotoList());
        if(mPhotoLists.size()>0){
            firstImgPath = mPhotoLists.get(0).getPath();
        }
        mPhotoAdapter = new PhotoAdapter(this.getApplicationContext(), mPhotoLists);
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
                toggleFloderList(floders);
            }
        });
        ll_name.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                toggleFloderList(floders);
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
        LogUtils.e(TAG, "selectPhoto");
        if (photo == null) {
            return;
        }
        String path = photo.getPath();
        if (mSelectMode == MODE_SINGLE) {
            mSelectList.add(path);
            returnData();
        }
    }

    @Override
    public boolean onPhotoClick(final String path, final View v) {
        LogUtils.e(TAG, "onPhotoClick");
        if (bitmap == null) {
            selectPicture(path);
            return true;
        } else {
            NotifySelectFragment fragment = NotifySelectFragment.newInstance(2, 5, false, false, "", "是否更换图片?",0);
            fragment.setOnSelectListener(new OnSelectListener() {
                @Override
                public void onSelected(boolean isSelected) {
                    if (isSelected) {
                        bitmap = null;
                        isProcess = false;
                        selectPicture(path);
                        mPhotoAdapter.mSelectedPhotos.clear();
                        mPhotoAdapter.mSelectedPhotos.add(path);
                        iv_show.setVisibility(View.GONE);
                        if(mPhotoAdapter.leastView!=null){
                            mPhotoAdapter.leastView.findViewById(R.id.mask).setVisibility(View.GONE);
                            mPhotoAdapter.leastView.findViewById(R.id.checkmark).setSelected(false);
                        }
                        v.findViewById(R.id.mask).setVisibility(View.VISIBLE);
                        v.findViewById(R.id.checkmark).setSelected(true);
                        mPhotoAdapter.leastView = v;
                    }
                }
            });
            fragment.show(getFragmentManager(), "changePic");
            return false;
        }
    }

    private void selectPicture(String path) {
        checkGridShow(true);
        tv_tips.setVisibility(View.GONE);
        iv_show.setVisibility(View.GONE);
        isProcess = false;
        currentImaPath = path;
//        if (ImageLoader.getInstance().getBitmap(currentImaPath) != null) {
        if (com.nostra13.universalimageloader.core.ImageLoader.getInstance().loadImageSync(currentImaPath) != null) {
            if (rl_show.getChildCount() > 0) {
                rl_show.removeAllViews();
            }
            mClipImageLayout = new ClipImageLayout(this, null, new BitmapDrawable(
                    com.nostra13.universalimageloader.core.ImageLoader.getInstance().loadImageSync(StringFinal.IMG_URI_HEAD+currentImaPath)), 0, ClipImageBorderView.BORDER_STYLE_RECTANGLE);//new BitmapDrawable(PictureUtil.getSmallBitmap(img_path)));
            mClipImageLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkGridShow(false);
                }
            });
            android.view.ViewGroup.LayoutParams lp = new RelativeLayout.LayoutParams(GoodHappinessApplication.w, GoodHappinessApplication.w);
            rl_show.addView(mClipImageLayout, lp);
        } else {
            showToast("无法加载图片");
        }
    }

    /**
     * b true上滑
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
     * 返回选择图片的路径
     */
    private void returnData() {
//        currentImaPath = mSelectList.get(0);
//        if(ImageLoader.getInstance().getBitmap(currentImaPath)!=null){
//            if(rl_show.getChildCount()>0){
//                rl_show.removeAllViews();
//            }
//            mClipImageLayout = new ClipImageLayout(this, null, new BitmapDrawable(ImageLoader.getInstance().getBitmap(currentImaPath)),0);//new BitmapDrawable(PictureUtil.getSmallBitmap(img_path)));
//            android.view.ViewGroup.LayoutParams lp = new RelativeLayout.LayoutParams(GoodHappinessApplication.w, GoodHappinessApplication.w);
//            rl_show.addView(mClipImageLayout, lp);
//        }else{
//            showToast("无法加载图片");
//        }
    }

    /**
     * 显示或者隐藏文件夹列表
     *
     * @param floders
     */
    private void toggleFloderList(final List<PhotoFloder> floders) {
        //初始化文件夹列表
        if (!mIsFloderViewInit) {
            ViewStub floderStub = (ViewStub) findViewById(R.id.floder_stub);
            floderStub.inflate();
            View dimLayout = findViewById(R.id.dim_layout);
            mFloderListView = (ListView) findViewById(R.id.listview_floder);
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mFloderListView.getLayoutParams();
            params.width = GoodHappinessApplication.w;
            params.height = (int) ((float) GoodHappinessApplication.h * 23 / 40);
            mFloderListView.setLayoutParams(params);
            final FloderAdapter adapter = new FloderAdapter(this, floders);
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
     * 选择文件夹
     *
     * @param photoFloder
     */
    public void selectFloder(PhotoFloder photoFloder) {
        mPhotoAdapter.setDatas(photoFloder.getPhotoList());
        mPhotoAdapter.notifyDataSetChanged();
    }

    /**
     * 获取照片的异步任务
     */
    private AsyncTask getPhotosTask = new AsyncTask() {
        @Override
        protected void onPreExecute() {
            mProgressDialog = ProgressDialog.show(EditPicActivity.this, null, "loading...");
        }

        @Override
        protected Object doInBackground(Object[] params) {
            mFloderMap = PhotoUtils.getPhotos(
                    EditPicActivity.this.getApplicationContext());
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            getPhotosSuccess();
        }
    };

    class GestureListener extends GestureDetector.SimpleOnGestureListener
    {

        @Override
        public boolean onDoubleTap(MotionEvent e)
        {
            // TODO Auto-generated method stub
            return super.onDoubleTap(e);
        }

        @Override
        public boolean onDown(MotionEvent e)
        {
            // TODO Auto-generated method stub
            return super.onDown(e);
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY)
        {
            // TODO Auto-generated method stub
            Log.i("TEST", "onFling:velocityX = " + velocityX + " velocityY" + velocityY);
            Log.i("TEST", "onFling:firstPosition = " + firstPosition);
            if(Math.abs(velocityY)>2000){
                if(velocityY>0){//下滑
                    if(firstPosition==0){
                        checkGridShow(false);
                    }
                }else{//上滑
                    checkGridShow(true);
                }
            }
            return super.onFling(e1, e2, velocityX, velocityY);
        }

        @Override
        public void onLongPress(MotionEvent e)
        {
            // TODO Auto-generated method stub
            super.onLongPress(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY)
        {
            // TODO Auto-generated method stub
            return super.onScroll(e1, e2, distanceX, distanceY);
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e)
        {
            // TODO Auto-generated method stub
            return super.onSingleTapUp(e);
        }

    }

    /**
     * 选择相机
     */
    private void showCamera() {
        CameraUtils.showSample(this, this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 相机拍照完成后，返回图片路径
        if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK) {
                if (mTmpFile != null) {
                    mSelectList.add(mTmpFile.getAbsolutePath());
                    isCamera = true;
                    returnData();
                }
            } else {
                if (mTmpFile != null && mTmpFile.exists()) {
                    mTmpFile.delete();
                }
            }
        }
    }

    @Override
    public void onTuEditStickerFragment2Edited(TuEditStickerFragment2 fragment, TuSdkResult var2) {
        bitmap = var2.image;
        isProcess = true;
        handler.sendEmptyMessage(0);
        fragment.hubDismissRightNow();
        fragment.dismissActivityWithAnim();
    }

    @Override
    public boolean onTuEditStickerFragment2EditedAsync(TuEditStickerFragment2 fragment, TuSdkResult var2) {


        return true;
    }

    @Override
    public void onComponentError(TuFragment tuFragment, TuSdkResult tuSdkResult, Error error) {

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what==0){
                rl_show.removeAllViews();
                RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) iv_show.getLayoutParams();
                layoutParams.width = layoutParams.height = GoodHappinessApplication.w;
                iv_show.setLayoutParams(layoutParams);
                iv_show.setImageBitmap(bitmap);
                iv_show.setVisibility(View.VISIBLE);
            }else if(msg.what==1){
                if (rl_show.getChildCount() > 0) {
                    rl_show.removeAllViews();
                }
                checkGridShow(true);
                tv_tips.setVisibility(View.GONE);
                iv_show.setVisibility(View.GONE);
                mClipImageLayout = new ClipImageLayout(EditPicActivity.this, null, new BitmapDrawable(bitmap), 0, ClipImageBorderView.BORDER_STYLE_RECTANGLE);//new BitmapDrawable(PictureUtil.getSmallBitmap(img_path)));
                mClipImageLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        checkGridShow(false);
                    }
                });
                android.view.ViewGroup.LayoutParams lp = new RelativeLayout.LayoutParams(GoodHappinessApplication.w, GoodHappinessApplication.w);
                rl_show.addView(mClipImageLayout, lp);
            }
        }
    };

    @Override
    public void onTuEditFilterFragmentEdited(TuEditFilterFragment tuEditFilterFragment, TuSdkResult tuSdkResult) {
        bitmap = tuSdkResult.image;
        handler.sendEmptyMessage(0);
        isProcess = true;
        tuEditFilterFragment.hubDismissRightNow();
        tuEditFilterFragment.dismissActivityWithAnim();
    }

    @Override
    public boolean onTuEditFilterFragmentEditedAsync(TuEditFilterFragment tuEditFilterFragment, TuSdkResult tuSdkResult) {

        return true;
    }

    @Override
    public void onTuCameraFragmentCaptured(TuCameraFragment tuCameraFragment, TuSdkResult tuSdkResult) {
        bitmap = tuSdkResult.image;
        handler.sendEmptyMessage(1);
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
}
