package com.goodhappiness.ui.social.picture;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.adapter.FloderAdapter;
import com.goodhappiness.adapter.PhotoAdapter;
import com.goodhappiness.bean.Photo;
import com.goodhappiness.bean.PhotoFloder;
import com.goodhappiness.bean.PostFilesBean;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.StringFinal;
import com.goodhappiness.dao.HorizontalScrollViewListener;
import com.goodhappiness.dao.OnSelectListener;
import com.goodhappiness.dao.ScrollViewListener;
import com.goodhappiness.factory.DialogFactory;
import com.goodhappiness.ui.base.BaseFragmentActivity;
import com.goodhappiness.ui.personal.selecthead.PhotoPickerActivity;
import com.goodhappiness.utils.AlxGifHelper;
import com.goodhappiness.utils.BitmapUtil;
import com.goodhappiness.utils.CameraUtils;
import com.goodhappiness.utils.FileUtils;
import com.goodhappiness.utils.PreferencesUtil;
import com.goodhappiness.widget.DisPatchGridView;
import com.goodhappiness.widget.ObservableHorizontalScrollView;
import com.goodhappiness.widget.ObservableScrollView;
import com.goodhappiness.widget.photopicker.OtherUtils;
import com.goodhappiness.widget.photopicker.PhotoUtils;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.common.api.GoogleApiClient;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.impl.activity.TuFragment;
import org.lasque.tusdk.impl.components.camera.TuCameraFragment;
import org.lasque.tusdk.impl.components.filter.TuEditFilterFragment;
import org.lasque.tusdk.impl.components.filter.TuEditFilterOption;
import org.lasque.tusdk.impl.components.sticker.TuEditStickerFragment;
import org.lasque.tusdk.impl.components.sticker.TuEditStickerOption;
import org.lasque.tusdk.modules.components.TuSdkHelperComponent;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import pl.droidsonroids.gif.GifImageView;

@ContentView(R.layout.activity_edit_pic_v5)
public class EditPicV5Activity extends BaseFragmentActivity implements TuEditFilterFragment.TuEditFilterFragmentDelegate, TuEditStickerFragment.TuEditStickerFragmentDelegate, PhotoAdapter.PhotoClickCallBack, TuCameraFragment.TuCameraFragmentDelegate {
    private static final int ON_CAMERA_CAPTURED = 0;
    private static final String TAG = "EditPicV5Activity";
    @ViewInject(R.id.tv_tips)
    private TextView tv_tips;
    @ViewInject(R.id.v_hide)
    private View v_hide;
    @ViewInject(R.id.edit_pic_sv)
    private ScrollView sv;
    @ViewInject(R.id.vp)
    private ViewPager vp;
    @ViewInject(R.id.floder_ll_name)
    private LinearLayout ll_name;
    @ViewInject(R.id.photo_gridview)
    private DisPatchGridView mGridView;
    @ViewInject(R.id.floder_name)
    private TextView mPhotoNameTV;
    /**
     * 文件夹列表是否处于显示状态
     */
    boolean mIsFloderViewShow = false;
    /**
     * 文件夹列表是否被初始化，确保只被初始化一次
     */
    boolean mIsFloderViewInit = false;
    private final static String ALL_PHOTO = "所有图片";
    /**
     * 照片选择模式，默认是单选模式
     */
    private int mSelectMode = 0;
    private Map<String, PhotoFloder> mFloderMap;
    private List<Photo> mPhotoLists = new ArrayList<>();
    private ArrayList<String> mSelectList = new ArrayList<>();
    private PhotoAdapter mPhotoAdapter;
    private ProgressDialog mProgressDialog;
    private ListView mFloderListView;
    private boolean isGridShow = false;
    private GestureDetector mGesture = null;
    private int firstPosition;
    private int gridHeight;
    private String firstImgPath;
    private ArrayList<PostFilesBean> list = new ArrayList<>();
    ImageGalleryAdapter galleryAdapter;
    private int currentDisplayImgPosition = 0;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    protected void setData() {
        initView();
        iv_back.setImageResource(R.mipmap.ico_clos);
        tv_title.setTextColor(getTheColor(R.color.white));
        tv_right.setText(R.string.continue_);
        tv_right.setTextColor(getTheColor(R.color.white));
        iv_mid.setImageResource(R.mipmap.ico_img_unlock);
        findViewById(R.id.common_rl_head).setBackgroundColor(getTheColor(R.color.black_333_text));
        initPager();
        initGridView();
//        onBackKeyDownClickListener = new OnBackKeyDownClickListener() {
//            @Override
//            public void onBackKeyClick() {
//                Log.e("q_", "ttt" + list.toString());
//            }
//        };
    }

    private void setImgLockStatus(boolean isLock) {
        if (isLock) {
            iv_mid.setImageResource(R.mipmap.ico_img_lock);
        } else {
            iv_mid.setImageResource(R.mipmap.ico_img_unlock);
        }
    }

    private void initPager() {
        galleryAdapter = new ImageGalleryAdapter(list);
        vp.setAdapter(galleryAdapter);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                currentDisplayImgPosition = position;
                setImgLockStatus(list.get(position).isLock());
                setTitle();
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    class TransBitmapToPreferenceTask extends AsyncTask<Integer, Integer, ArrayList<PostFilesBean>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            newDialog().show();
        }

        @Override
        protected ArrayList<PostFilesBean> doInBackground(Integer... params) {
            ArrayList<PostFilesBean> postFilesBeen = new ArrayList<>();
            int i = 0;
            for (PostFilesBean bean : list) {
                PostFilesBean filesBean = new PostFilesBean();
                filesBean.setFileType(bean.getFileType());
                filesBean.setGifPath(bean.getGifPath());
                filesBean.setFileUrl(bean.getFileUrl());
                filesBean.setBitmap(null);
                String name = TAG + i;
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                if (bean.getFileType() == 1) {
                    if (bean.getBitmap() != null) {
                        filesBean.setProcessed(true);
                        filesBean.setTrans(true);
                        Bitmap bitmap = bean.getBitmap();
                        if (!bean.isLock()) {
                            bitmap = BitmapUtil.getRecBitmap(bitmap, list.get(i).getScrollY());
                        }
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] data = baos.toByteArray();
                        filesBean.setFileId(name);
                        PreferencesUtil.setPreferences(EditPicV5Activity.this, name, data);
                    } else {
                        if (!list.get(i).isLock()) {
                            Bitmap bitmap;
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inSampleSize =2;
                            bitmap = BitmapUtil.getRecBitmap(BitmapFactory.decodeFile(list.get(i).getGifPath(),options), list.get(i).getScrollY());
//                            bitmap = BitmapUtil.getRecBitmap(ImageLoader.getInstance().loadImageSync(StringFinal.IMG_URI_HEAD + list.get(i).getGifPath(), GoodHappinessApplication.options), list.get(i).getScrollY());
                            list.get(i).setBitmap(bitmap);
                            list.get(i).setProcessed(true);
                            list.get(i).setTrans(true);
                            filesBean.setProcessed(true);
                            filesBean.setTrans(true);
                            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                            byte[] data = baos.toByteArray();
                            PreferencesUtil.setPreferences(EditPicV5Activity.this, name, data);
                        }
//                        else {
//                            bitmap = ImageLoader.getInstance().loadImageSync(StringFinal.IMG_URI_HEAD + list.get(i).getGifPath(), GoodHappinessApplication.options);
//                        }
                        filesBean.setFileId(name);
                    }
                }
                i++;
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                postFilesBeen.add(filesBean);
            }
            return postFilesBeen;
        }

        @Override
        protected void onPostExecute(ArrayList<PostFilesBean> postFilesBeen) {
            super.onPostExecute(postFilesBeen);
            galleryAdapter.notifyDataSetChanged();
            Intent intent = new Intent(EditPicV5Activity.this, PublishActivity.class);
            Bundle bundle = new Bundle();
            bundle.putParcelableArrayList(FieldFinals.FEED_INFO_LIST, postFilesBeen);
            Log.e("q_", postFilesBeen.toString());
            intent.putExtras(bundle);
            dialog.dismiss();
            startActivity(intent);
        }
    }

    @Event({R.id.common_mid, R.id.v_hide, R.id.tv_tips, R.id.common_right_text, R.id.iv_left, R.id.iv_right, R.id.edit_pic_tv_sticker, R.id.edit_pic_tv_filter})
    private void onEventClick(View v) {
        switch (v.getId()) {
            case R.id.common_mid:
                if (list.size() > 0 && list.size() > currentDisplayImgPosition) {
                    if (list.get(currentDisplayImgPosition).getFileType() == 1) {
                        boolean isLock = !list.get(currentDisplayImgPosition).isLock();
                        if (isLock) {
                            showToast(R.string.pic_lock_tips);
                        } else {
                            showToast(R.string.pic_unlock_tips);
                        }
                        setImgLockStatus(isLock);
                        if (list.size() > currentDisplayImgPosition) {
                            list.get(currentDisplayImgPosition).setLock(isLock);
                            galleryAdapter.notifyDataSetChanged();
                        }
                    }
                } else if (list != null && list.size() == 0) {
                    showToast(R.string.please_add_pic);
                }
                break;
            case R.id.v_hide:
                checkGridShow(false);
                break;
            case R.id.tv_tips:
                checkGridShow(false);
                break;
            case R.id.common_right_text:
                if (list.size() > 0) {
//                    transBitmapToPreference();
                    DialogFactory.createChooseDialog(this, "确定要裁剪选中的" + list.size() + "张图", new OnSelectListener() {
                        @Override
                        public void onSelected(boolean isSelected) {
                            if (isSelected) {
                                new TransBitmapToPreferenceTask().execute(0);
                            }
                        }
                    });
                } else if (list != null && list.size() == 0) {
                    showToast(R.string.please_add_pic);
                }
                break;
            case R.id.iv_left:
                if (list.size() >= currentDisplayImgPosition) {
                    if (currentDisplayImgPosition >= 1) {
                        vp.setCurrentItem(currentDisplayImgPosition - 1);
                    }
                }
                break;
            case R.id.iv_right:
                if (list.size() > currentDisplayImgPosition) {
                    vp.setCurrentItem(currentDisplayImgPosition + 1);
                }
                break;
            case R.id.edit_pic_tv_sticker:
            case R.id.edit_pic_tv_filter:
                if (!isProcess) {
                    isProcess = true;
                    if (list != null && list.size() > 0 && currentDisplayImgPosition != -1) {
                        if (currentImgIsGif()) {
                            showToast(R.string.do_not_process_with_gif);
                        } else {
                            if (v.getId() == R.id.edit_pic_tv_sticker) {
                                startToStickerProcess();
                            } else if (v.getId() == R.id.edit_pic_tv_filter) {
                                startToFilterProcess();
                            }
                        }
                    } else {
                        showToast(R.string.select_pic_to_process);
                    }
                }
                break;
        }
    }

    private boolean isProcess = false;

    private void startToFilterProcess() {
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
        option.setShowResultPreview(false);
        TuEditFilterFragment fragment = option.fragment();
        if (list.get(currentDisplayImgPosition).isProcessed()) {
            fragment.setImage(list.get(currentDisplayImgPosition).getBitmap());
        } else {
//            fragment.setImage(BitmapFactory.decodeFile(list.get(currentDisplayImgPosition).getGifPath()));
//            fragment.setImage(BitmapUtil.getRecBitmap(ImageLoader.getInstance().loadImageSync(StringFinal.IMG_URI_HEAD + list.get(currentDisplayImgPosition).getGifPath(), GoodHappinessApplication.options), list.get(currentDisplayImgPosition).getScrollY()));
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = 2;
            fragment.setImage(BitmapFactory.decodeFile(list.get(currentDisplayImgPosition).getGifPath(), opts));
            //ImageLoader.getInstance().loadImageSync(StringFinal.IMG_URI_HEAD + list.get(currentDisplayImgPosition).getGifPath())
        }
        fragment.setDelegate(this);
        isProcess = false;
        // 开启贴纸编辑界面
        new TuSdkHelperComponent(this).pushModalNavigationActivity(fragment, true);
    }

    private void startToStickerProcess() {
        TuEditStickerOption option = new TuEditStickerOption();
        // 是否在控制器结束后自动删除临时文件
        option.setAutoRemoveTemp(true);
        // 设置贴纸单元格的高度
        option.setGridHeight(150);
        // 设置贴纸单元格的间距
        option.setGridPadding(8);
        // 是否显示处理结果预览图 (默认：关闭，调试时可以开启)
        option.setShowResultPreview(false);
        TuEditStickerFragment fragment = option.fragment();
        // 输入的图片对象 (处理优先级: Image > TempFilePath > ImageSqlInfo)
        if (list.get(currentDisplayImgPosition).isProcessed()) {
            fragment.setImage(list.get(currentDisplayImgPosition).getBitmap());
        } else {
//            fragment.setImage(BitmapUtil.getRecBitmap(BitmapFactory.decodeFile(list.get(currentDisplayImgPosition).getGifPath()),list.get(currentDisplayImgPosition).getScrollY()));
//            fragment.setImage(BitmapUtil.getRecBitmap(ImageLoader.getInstance().loadImageSync(StringFinal.IMG_URI_HEAD + list.get(currentDisplayImgPosition).getGifPath(), GoodHappinessApplication.options), list.get(currentDisplayImgPosition).getScrollY()));
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inSampleSize = 2;
            fragment.setImage(BitmapFactory.decodeFile(list.get(currentDisplayImgPosition).getGifPath(), opts));
        }
        fragment.setDelegate(this);
        isProcess = false;
        // 开启贴纸编辑界面
        new TuSdkHelperComponent(this).pushModalNavigationActivity(fragment, true);
    }

    private boolean currentImgIsGif() {
        if (list != null && list.size() > 0 && currentDisplayImgPosition != -1) {
            if (list.size() >= currentDisplayImgPosition) {
                if (list.get(currentDisplayImgPosition).getGifPath().endsWith("gif")) {
                    return true;
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    private void initGridView() {
        mGesture = new GestureDetector(this, new GestureListener());
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) mGridView.getLayoutParams();
        layoutParams.width = GoodHappinessApplication.w;
        gridHeight = (int) ((float) GoodHappinessApplication.h * 3 / 5);
        layoutParams.height = gridHeight;
        mGridView.setLayoutParams(layoutParams);
        vp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkGridShow(false);
            }
        });
        RelativeLayout.LayoutParams vpParams = (RelativeLayout.LayoutParams) vp.getLayoutParams();
        vpParams.height = vpParams.width = GoodHappinessApplication.w;
        vp.setLayoutParams(vpParams);

        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tv_tips.getLayoutParams();
        params.height = params.width = GoodHappinessApplication.w;
        tv_tips.setLayoutParams(params);

        sv.smoothScrollTo(0, 0);
        if (!OtherUtils.isExternalStorageAvailable()) {
            showToast(R.string.no_sd_card);
            return;
        }
        getPhotosTask.execute();
    }


    private void checkGridShow(boolean b) {
        if (b) {//上滑显示下面
            if (!isGridShow) {
                sv.smoothScrollTo(0, gridHeight);
                v_hide.setVisibility(View.VISIBLE);
                isGridShow = true;
            }
        } else {//下滑显示上面
            if (isGridShow) {
                v_hide.setVisibility(View.GONE);
                sv.smoothScrollTo(0, 0);
                isGridShow = false;
            }
        }
    }

    private void getPhotosSuccess() {
        mProgressDialog.dismiss();
        mPhotoLists.addAll(mFloderMap.get(ALL_PHOTO).getPhotoList());
        if (mPhotoLists.size() > 0) {
            firstImgPath = mPhotoLists.get(0).getPath();
        }
        mPhotoAdapter = new PhotoAdapter(this, mPhotoLists);
        mPhotoAdapter.setIsShowCamera(true);
        mPhotoAdapter.setSelectMode(PhotoPickerActivity.MODE_MULTI);
        mPhotoAdapter.setMaxNum(9);
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
                if (firstVisibleItem + visibleItemCount == totalItemCount) {
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
                if (list.size() >= 9) {
                    showToast(R.string.select_photo_limit);
                    return;
                }
                if (mPhotoAdapter.isShowCamera() && position == 0) {
                    showCamera();
                    return;
                }
                Log.e("click", mPhotoAdapter.getItem(position).getPath());
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
        mSelectList.add(path);
    }

    private void showCamera() {
        CameraUtils.showSample(this, this);
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
            params.height = (int) ((float) GoodHappinessApplication.h * 27 / 40);
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
                        mPhotoAdapter.setIsShowCamera(true);
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
            mProgressDialog = ProgressDialog.show(EditPicV5Activity.this, null, getString(R.string.loading_));
        }

        @Override
        protected Object doInBackground(Object[] params) {
            mFloderMap = PhotoUtils.getPhotos(EditPicV5Activity.this);
            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            getPhotosSuccess();
        }
    };
    boolean isDeleting = false;

    @Override
    public boolean onPhotoClick(String path, View v) {
        if (mPhotoAdapter.getmSelectedPhotos().size() >= 9) {
            if (mPhotoAdapter.getmSelectedPhotos().contains(path)) {//remove
                return removePagerPhoto(path);
            }
            showToast(R.string.select_photo_limit);
            return false;
        }
        if (isPagerHasPath(path)) {
            return removePagerPhoto(path);
        } else {
            iv_mid.setVisibility(View.VISIBLE);
            if (path.endsWith("gif")) {
                list.add(new PostFilesBean(4, path));
            } else {
                list.add(new PostFilesBean(1, path));
            }
            if (list.size() == 0) {
                currentDisplayImgPosition = 0;
                iv_mid.setVisibility(View.GONE);
            } else if (list.size() == 1) {
                currentDisplayImgPosition = 0;
            }
            setTitle();
            galleryAdapter.notifyDataSetChanged();
            if (list.size() >= 1) {
                vp.setCurrentItem(list.size() - 1);
            }
        }
        Log.e("size", "..." + list.size());
        tv_tips.setVisibility(View.GONE);
        return true;
    }

    private void setLeftRightImageStatus(int status) {
        switch (status) {
            case 0:
                findViewById(R.id.iv_left).setVisibility(View.GONE);
                findViewById(R.id.iv_right).setVisibility(View.VISIBLE);
                break;
            case 1:
                findViewById(R.id.iv_left).setVisibility(View.VISIBLE);
                findViewById(R.id.iv_right).setVisibility(View.VISIBLE);
                break;
            case 2:
                findViewById(R.id.iv_left).setVisibility(View.VISIBLE);
                findViewById(R.id.iv_right).setVisibility(View.GONE);
                break;
        }
    }

    private boolean isPagerHasPath(String path) {
        boolean hasPath = false;
        for (PostFilesBean bean : list) {
            if (bean.getGifPath().equals(path)) {
                hasPath = true;
                break;
            }
        }
        return hasPath;
    }

    /**
     * 设置标题
     */
    private void setTitle() {
//        if (list != null && list.size() > 0 && currentDisplayImgPosition != -1
//                ) {
//            tv_title.setText((currentDisplayImgPosition + 1) + "/" + list.size());
//        }
        if (list.size() > 1) {
            if (currentDisplayImgPosition == 0) {
                setLeftRightImageStatus(0);
            } else if (currentDisplayImgPosition > 0 && currentDisplayImgPosition < list.size() - 1) {
                setLeftRightImageStatus(1);
            } else {
                setLeftRightImageStatus(2);
            }
        } else {
            findViewById(R.id.iv_right).setVisibility(View.GONE);
            findViewById(R.id.iv_left).setVisibility(View.GONE);
        }
    }

    private boolean removePagerPhoto(String path) {
        if (isDeleting) {
            return false;
        } else {
            isDeleting = true;
            boolean isRemove = false;
            int i = 0;
            for (PostFilesBean bean : list) {
                if (bean.getGifPath().equals(path)) {
                    isRemove = true;
                    break;
                }
                i++;
            }
            if (isRemove) {
                list.remove(i);
            }
            isDeleting = false;
        }
        if (list.size() == 0) {
            currentDisplayImgPosition = -1;
            tv_tips.setVisibility(View.VISIBLE);
            iv_mid.setVisibility(View.GONE);
            tv_title.setText("");
        } else {
            setTitle();
        }
        galleryAdapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public void onTuEditStickerFragmentEdited(TuEditStickerFragment fragment, TuSdkResult result) {
        list.get(currentDisplayImgPosition).setProcessed(true);
        list.get(currentDisplayImgPosition).setBitmap(result.image);
        galleryAdapter.notifyDataSetChanged();
        fragment.hubDismissRightNow();
        fragment.dismissActivityWithAnim();
    }

    @Override
    public boolean onTuEditStickerFragmentEditedAsync(TuEditStickerFragment fragment, TuSdkResult result) {
        return true;
    }

    @Override
    public void onTuEditFilterFragmentEdited(TuEditFilterFragment fragment, TuSdkResult result) {
        list.get(currentDisplayImgPosition).setProcessed(true);
        list.get(currentDisplayImgPosition).setBitmap(result.image);
        galleryAdapter.notifyDataSetChanged();
        fragment.hubDismissRightNow();
        fragment.dismissActivityWithAnim();
    }

    @Override
    public boolean onTuEditFilterFragmentEditedAsync(TuEditFilterFragment fragment, TuSdkResult result) {
        return true;
    }

    class ImageGalleryAdapter extends PagerAdapter {

        private final List<PostFilesBean> images;
        private int mChildCount = 0;

        // region Constructors
        public ImageGalleryAdapter(ArrayList<PostFilesBean> images) {
            this.images = images;
        }

        // endregion
        @Override
        public void notifyDataSetChanged() {
            mChildCount = getCount();
            super.notifyDataSetChanged();
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            LayoutInflater inflater = (LayoutInflater) container.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.layout_pager_image_gallery_edit, null);
            final ObservableScrollView scrollView = (ObservableScrollView) view.findViewById(R.id.sv);
            final ObservableHorizontalScrollView horizontalScrollView = (ObservableHorizontalScrollView) view.findViewById(R.id.hsv);
            final ImageView imageView = (ImageView) view.findViewById(R.id.gallery_iv);
            final ImageView imageViewLock = (ImageView) view.findViewById(R.id.iv_lock);
            if (!images.get(position).isLock()) {
                scrollView.setScrollViewListener(new ScrollViewListener() {
                    @Override
                    public void onScrollChanged(ObservableScrollView scrollView, int x, int y, int oldx, int oldy) {
                        if (images.size() > position && images.get(position) != null && images.get(position).getScale() != 0) {
                            images.get(position).setScrollY((int) (images.get(position).getScale() * y));
                            images.get(position).setY(y);
                            Log.e("q_", "onScrollChanged y x:" + x + "  y:" + y + "  oldx:" + oldx + "   oldy:" + oldy + "   " + images.get(position).getScrollY());
                        }
                    }
                });
                horizontalScrollView.setScrollViewListener(new HorizontalScrollViewListener() {
                    @Override
                    public void onScrollChanged(ObservableHorizontalScrollView scrollView, int x, int y, int oldx, int oldy) {
                        if (images.size() > position && images.get(position) != null && images.get(position).getScale() != 0) {
                            images.get(position).setScrollY((int) (images.get(position).getScale() * x));
                            images.get(position).setX(x);
                            Log.e("q_", "onScrollChanged x x:" + x + "  y:" + y + "  oldx:" + oldx + "   oldy:" + oldy + "   " + images.get(position).getScrollY());
                        }
                    }
                });
//                iv_mid.setImageResource(R.mipmap.ico_img_unlock);
            } else {
//                iv_mid.setImageResource(R.mipmap.ico_img_lock);
            }
            final ImageView horImageView = (ImageView) view.findViewById(R.id.gallery_iv_hor);
            RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.gif_group);
            RelativeLayout byId = (RelativeLayout) view.findViewById(R.id.rl_content);
            ViewGroup.LayoutParams params = byId.getLayoutParams();
            params.width = params.height = (int) (GoodHappinessApplication.w - GoodHappinessApplication.perHeight * 20);
            byId.setLayoutParams(params);
            imageView.setVisibility(View.GONE);
            imageViewLock.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.GONE);
            horizontalScrollView.setVisibility(View.GONE);
            if (images.get(position).getFileType() == 1) {//image
                if (!TextUtils.isEmpty(images.get(position).getGifPath()) && !images.get(position).isProcessed()) {//origin
                    ImageLoader.getInstance().displayImage(StringFinal.IMG_URI_HEAD + images.get(position).getGifPath(), imageView, GoodHappinessApplication.options, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {
                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {
                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                            if (images.size() <= position) {
                                return;
                            }
                            images.get(position).setBitmapWidth(bitmap.getWidth());
                            images.get(position).setBitmapHeight(bitmap.getHeight());
                            if (!images.get(position).isLock()) {
                                if (bitmap.getHeight() > bitmap.getWidth()) {
                                    if (images.get(position).getY() != 0) {
                                        new Handler().postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                EditPicV5Activity.this.runOnUiThread(new Runnable() {
                                                    @Override
                                                    public void run() {
                                                        if (images.size() > position)
                                                            scrollView.smoothScrollTo(0, images.get(position).getY());
                                                    }
                                                });
                                            }
                                        }, 500);
                                    }
                                    imageView.setVisibility(View.VISIBLE);
                                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                                    params.width = (int) (GoodHappinessApplication.w - GoodHappinessApplication.perHeight * 20);
                                    params.height = bitmap.getHeight() * params.width / bitmap.getWidth();
                                    imageView.setLayoutParams(params);
                                    images.get(position).setScale((float) bitmap.getHeight() / params.height);
                                } else {
                                    loadHorizontalView(position, horImageView, horizontalScrollView);
                                }
                            } else {
                                imageViewLock.setVisibility(View.VISIBLE);
                                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageViewLock.getLayoutParams();
                                if (bitmap.getHeight() > bitmap.getWidth()) {
                                    params.height = (int) (GoodHappinessApplication.w - GoodHappinessApplication.perHeight * 20);
                                    params.width = (int) (((float) bitmap.getWidth() * params.height) / bitmap.getHeight());
                                    imageViewLock.setLayoutParams(params);
                                } else {
                                    params.width = (int) (GoodHappinessApplication.w - GoodHappinessApplication.perHeight * 20);
                                    params.height = (int) (((float) bitmap.getWidth() * params.width) / bitmap.getWidth());
                                    imageViewLock.setLayoutParams(params);
                                }
                                imageViewLock.setImageBitmap(bitmap);
                            }
                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {
                        }
                    });
                } else if (images.get(position).getBitmap() != null) {//process
                    Bitmap bitmap = images.get(position).getBitmap();
                    if (!images.get(position).isLock()) {
                        imageView.setVisibility(View.VISIBLE);
                        if (bitmap.getHeight() > bitmap.getWidth()) {
                            if (images.get(position).getY() != 0) {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        EditPicV5Activity.this.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (images.size() > position)
                                                    scrollView.smoothScrollTo(0, images.get(position).getY());
                                            }
                                        });
                                    }
                                }, 500);
                            }
                            imageView.setVisibility(View.VISIBLE);
                            RelativeLayout.LayoutParams paramsBitmapUnlock = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                            paramsBitmapUnlock.width = (int) (GoodHappinessApplication.w - GoodHappinessApplication.perHeight * 20);
                            paramsBitmapUnlock.height = bitmap.getHeight() * params.width / bitmap.getWidth();
                            imageView.setLayoutParams(paramsBitmapUnlock);
                            images.get(position).setScale((float) bitmap.getHeight() / paramsBitmapUnlock.height);
                            imageView.setImageBitmap(bitmap);
                        } else {
                            loadHorizontalView(bitmap, position, horImageView, horizontalScrollView);
                        }
                    } else {
                        imageViewLock.setVisibility(View.VISIBLE);
                        RelativeLayout.LayoutParams paramsBitmapLock = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                        if (bitmap.getHeight() > bitmap.getWidth()) {
                            paramsBitmapLock.height = (int) (GoodHappinessApplication.w - GoodHappinessApplication.perHeight * 20);
                            paramsBitmapLock.width = (int) (((float) bitmap.getWidth() * paramsBitmapLock.height) / bitmap.getHeight());
                            imageViewLock.setLayoutParams(paramsBitmapLock);
                        } else {
                            paramsBitmapLock.width = (int) (GoodHappinessApplication.w - GoodHappinessApplication.perHeight * 20);
                            paramsBitmapLock.height = (int) (((float) bitmap.getWidth() * paramsBitmapLock.width) / bitmap.getWidth());
                            imageView.setLayoutParams(paramsBitmapLock);
                        }
                        imageViewLock.setImageBitmap(bitmap);
                    }
                }
            } else if (images.get(position).getFileType() == 4) {//gif
                if (!TextUtils.isEmpty(images.get(position).getGifPath())) {
                    relativeLayout.setVisibility(View.VISIBLE);
                    view.findViewById(R.id.progress_wheel).setVisibility(View.GONE);
                    view.findViewById(R.id.tv_progress).setVisibility(View.GONE);
                    AlxGifHelper.displayImage(new File(images.get(position).getGifPath()), (GifImageView) view.findViewById(R.id.gif_photo_view), GoodHappinessApplication.w);
//                    Glide.with(EditPicV5Activity.this).load(images.get(position).getGifPath()).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
                }
            }
            container.addView(view, 0);
            return view;
        }

        private void loadHorizontalView(Bitmap bitmap, final int position, final ImageView imageView, final ObservableHorizontalScrollView horizontalScrollView) {
            horizontalScrollView.setVisibility(View.VISIBLE);
            if (images.get(position).getX() != 0) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        EditPicV5Activity.this.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (images.size() > position)
                                    horizontalScrollView.smoothScrollTo(images.get(position).getX(), 0);
                            }
                        });
                    }
                }, 500);
            }
            ViewGroup.LayoutParams params = imageView.getLayoutParams();
            params.height = (int) (GoodHappinessApplication.w - GoodHappinessApplication.perHeight * 20);
            params.width = (int) ((float) bitmap.getWidth() * params.height / bitmap.getHeight());
            imageView.setLayoutParams(params);
            images.get(position).setScale((float) bitmap.getWidth() / params.width);
            imageView.setImageBitmap(bitmap);
        }

        private void loadHorizontalView(final int position, final ImageView imageView, final ObservableHorizontalScrollView horizontalScrollView) {
            ImageLoader.getInstance().displayImage(StringFinal.IMG_URI_HEAD + images.get(position).getGifPath(), imageView, GoodHappinessApplication.options, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String s, View view) {
                }

                @Override
                public void onLoadingFailed(String s, View view, FailReason failReason) {
                }

                @Override
                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    horizontalScrollView.setVisibility(View.VISIBLE);
                    if (images.get(position).getX() != 0) {
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                EditPicV5Activity.this.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (images.size() > position)
                                            horizontalScrollView.smoothScrollTo(images.get(position).getX(), 0);
                                    }
                                });
                            }
                        }, 500);
                    }
                    ViewGroup.LayoutParams params = imageView.getLayoutParams();
                    params.height = (int) (GoodHappinessApplication.w - GoodHappinessApplication.perHeight * 20);
                    params.width = (int) ((float) bitmap.getWidth() * params.height / bitmap.getHeight());
                    imageView.setLayoutParams(params);
                    images.get(position).setScale((float) bitmap.getWidth() / params.width);
                    Log.e("q_", bitmap.getHeight() + "   " + params.height);
                }

                @Override
                public void onLoadingCancelled(String s, View view) {
                }
            });
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public int getItemPosition(Object object) {
            if (mChildCount > 0) {
                mChildCount--;
                return POSITION_NONE;
            }
            return super.getItemPosition(object);
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ON_CAMERA_CAPTURED:
                    if (!TextUtils.isEmpty((String) msg.obj)) {
                        ArrayList<Photo> photos = new ArrayList<>();
                        photos.addAll(mPhotoLists);
                        mPhotoLists.clear();
                        mPhotoLists.add(new Photo((String) msg.obj));
                        mPhotoAdapter.getmSelectedPhotos().add((String) msg.obj);
                        mPhotoLists.addAll(photos);
                        photos.clear();
                        mPhotoAdapter.notifyDataSetChanged();
                        list.add(new PostFilesBean(1, (String) msg.obj));
                        tv_tips.setVisibility(View.GONE);
                        galleryAdapter.notifyDataSetChanged();
                    }
                    dialog.dismiss();
                    break;
            }
        }
    };

    @Override
    public void onTuCameraFragmentCaptured(TuCameraFragment fragment, TuSdkResult tuSdkResult) {
        newDialog().show();
        Message message = handler.obtainMessage();
        message.obj = FileUtils.saveImgFile(EditPicV5Activity.this, tuSdkResult.image, FileUtils.getStorageDirectory()).getAbsolutePath();
        message.what = ON_CAMERA_CAPTURED;
        handler.sendMessage(message);
        fragment.hubDismissRightNow();
        fragment.dismissActivityWithAnim();
    }

    @Override
    public boolean onTuCameraFragmentCapturedAsync(TuCameraFragment fragment, TuSdkResult result) {
        return true;
    }

    @Override
    public void onTuAlbumDemand(TuCameraFragment fragment) {

    }

    @Override
    public void onComponentError(TuFragment tuFragment, TuSdkResult tuSdkResult, Error error) {

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
}
