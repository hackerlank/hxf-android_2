package com.goodhappiness.ui.social.picture;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.bean.PostFilesBean;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.StringFinal;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.utils.AlxGifHelper;
import com.goodhappiness.utils.FileUtils;
import com.goodhappiness.utils.PreferencesUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.pnikosis.materialishprogress.ProgressWheel;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

@ContentView(R.layout.activity_display_img)
public class DisplayImgActivity extends BaseActivity {
    public static final int GALLERY_TYPE_ONLINE = 1;
    public static final int GALLERY_TYPE_LOCAL = 2;
    @ViewInject(R.id.vp)
    private ViewPager vp;
    private int galleryType = 0;
    private int currentPosition = 0;
    private ArrayList<PostFilesBean> list = new ArrayList<>();

    @Override
    protected void setData() {
        initView();
        initPager();
    }

    private void initPager() {
        if (getIntent() != null) {
            if (getIntent().getExtras() != null && getIntent().getExtras().getParcelableArrayList(FieldFinals.FEED_INFO_LIST) != null) {
                ArrayList<PostFilesBean> postFilesBeen = getIntent().getExtras().getParcelableArrayList(FieldFinals.FEED_INFO_LIST);
                if (postFilesBeen != null && postFilesBeen.size() > 0) {
                    for (PostFilesBean bean : postFilesBeen) {
                        if (bean.isTrans()) {
                            byte[] b = PreferencesUtil.getPreferences(DisplayImgActivity.this, bean.getFileId());
                            if (b != null) {
                                bean.setBitmap(BitmapFactory.decodeByteArray(b, 0, b.length));
                            }
                        }
                    }
                    list.addAll(postFilesBeen);
                    Log.e("q_", postFilesBeen.toString());
                    ImageGalleryAdapter galleryAdapter = new ImageGalleryAdapter(list);
                    vp.setAdapter(galleryAdapter);
                    currentPosition = getIntent().getIntExtra(FieldFinals.POSITION, 0);
                    vp.setCurrentItem(currentPosition);
                    vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                        @Override
                        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                        }

                        @Override
                        public void onPageSelected(int position) {
                            currentPosition = position;
                            setTitle();
                        }

                        @Override
                        public void onPageScrollStateChanged(int state) {

                        }
                    });
                    setTitle();
                    if (FieldFinals.ONLINE.equals(getIntent().getStringExtra(FieldFinals.ACTION))) {
                        galleryType = GALLERY_TYPE_ONLINE;
                    } else if (FieldFinals.LOCAL.equals(getIntent().getStringExtra(FieldFinals.ACTION))) {
                        galleryType = GALLERY_TYPE_LOCAL;
                    } else {
                        galleryType = 0;
                    }
                }
            }
        }
    }

    private void setTitle() {
        if (list != null && list.size() > 0 && currentPosition != -1
                ) {
            tv_title.setText((currentPosition + 1) + "/" + list.size());
        }
    }

    @Override
    protected void reload() {

    }

    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    class ImageGalleryAdapter extends PagerAdapter {

        private final List<PostFilesBean> images;


        // region Constructors
        public ImageGalleryAdapter(ArrayList<PostFilesBean> images) {
            this.images = images;
        }
        // endregion

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            LayoutInflater inflater = (LayoutInflater) container.getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = inflater.inflate(R.layout.layout_pager_image_gallery, null);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view.findViewById(R.id.vv).getLayoutParams();
            params.height = (int) (GoodHappinessApplication.h - GoodHappinessApplication.perHeight * 80);
            view.findViewById(R.id.vv).setLayoutParams(params);
            if (galleryType != 0) {
                final ImageView imageView = (ImageView) view.findViewById(R.id.gallery_iv);
                RelativeLayout relativeLayout = (RelativeLayout) view.findViewById(R.id.gif_group);
                imageView.setVisibility(View.VISIBLE);
                relativeLayout.setVisibility(View.GONE);
                if (images.get(position).getFileType() == 1) {//image
                    if (galleryType == GALLERY_TYPE_ONLINE) {
                        imageView.setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(final View v) {
                                new AlertDialog.Builder(DisplayImgActivity.this).setItems(new String[]{"保存"}, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        saveImage(position);
                                    }
                                }).show();
                                return false;
                            }
                        });
                        ImageLoader.getInstance().displayImage(images.get(position).getFileUrl(), imageView, GoodHappinessApplication.options, new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String s, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String s, View view, FailReason failReason) {

                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
//                                params.width = GoodHappinessApplication.w;
//                                params.height = bitmap.getHeight() * GoodHappinessApplication.w / bitmap.getWidth();
//                                imageView.setLayoutParams(params);
                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {

                            }
                        });
                    } else if (galleryType == GALLERY_TYPE_LOCAL) {
                        if (images.get(position).isProcessed()) {
                            if (images.get(position).getBitmap() != null) {
                                RelativeLayout.LayoutParams paramsBitmap = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
                                paramsBitmap.width = GoodHappinessApplication.w;
                                paramsBitmap.height = images.get(position).getBitmap().getHeight() * GoodHappinessApplication.w / images.get(position).getBitmap().getWidth();
                                imageView.setLayoutParams(paramsBitmap);
                                imageView.setImageBitmap(images.get(position).getBitmap());
                            }
                        } else {
                            ImageLoader.getInstance().displayImage(StringFinal.IMG_URI_HEAD + images.get(position).getGifPath(), imageView, GoodHappinessApplication.options, new ImageLoadingListener() {
                                @Override
                                public void onLoadingStarted(String s, View view) {

                                }

                                @Override
                                public void onLoadingFailed(String s, View view, FailReason failReason) {

                                }

                                @Override
                                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                                    RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) imageView.getLayoutParams();
//                                    params.width = GoodHappinessApplication.w;
//                                    params.height = bitmap.getHeight() * GoodHappinessApplication.w / bitmap.getWidth();
//                                    imageView.setLayoutParams(params);
                                }

                                @Override
                                public void onLoadingCancelled(String s, View view) {

                                }
                            });
                        }
                    }
                } else if (images.get(position).getFileType() == 4) {//gif
                    imageView.setVisibility(View.GONE);
                    relativeLayout.setVisibility(View.VISIBLE);
                    if (galleryType == GALLERY_TYPE_ONLINE) {
                        if (!TextUtils.isEmpty(images.get(position).getFileUrl()))
                            if (images.get(position).getFileUrl().contains(FieldFinals.WATERMARK)) {
                                String s[] = images.get(position).getFileUrl().split("\\?");
//                                Glide.with(DisplayImgActivity.this).load(s[0]).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).
//                                        into(imageView);
                                AlxGifHelper.displayImage(s[0],
                                        (GifImageView) view.findViewById(R.id.gif_photo_view),
                                        (ProgressWheel) view.findViewById(R.id.progress_wheel),
                                        (TextView) view.findViewById(R.id.tv_progress),
                                        GoodHappinessApplication.w
                                );
                            } else {
                                AlxGifHelper.displayImage(images.get(position).getFileUrl(),
                                        (GifImageView) view.findViewById(R.id.gif_photo_view),
                                        (ProgressWheel) view.findViewById(R.id.progress_wheel),
                                        (TextView) view.findViewById(R.id.tv_progress),
                                        GoodHappinessApplication.w);
                            }
                        view.findViewById(R.id.gif_photo_view).setOnLongClickListener(new View.OnLongClickListener() {
                            @Override
                            public boolean onLongClick(final View v) {
                                new AlertDialog.Builder(DisplayImgActivity.this).setItems(new String[]{"保存"}, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        saveGif(position);
                                    }
                                }).show();
                                return false;
                            }
                        });
                    } else if (galleryType == GALLERY_TYPE_LOCAL) {
                        if (!TextUtils.isEmpty(images.get(position).getGifPath())) {
                            AlxGifHelper.displayImage(new File(images.get(position).getGifPath()), (GifImageView) view.findViewById(R.id.gif_photo_view), GoodHappinessApplication.w);
                            view.findViewById(R.id.progress_wheel).setVisibility(View.GONE);
                            view.findViewById(R.id.tv_progress).setVisibility(View.GONE);
                        }
//                            Glide.with(DisplayImgActivity.this).load(images.get(position).getGifPath()).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into(imageView);
                    }
                }
            }

            container.addView(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            return view;
        }

        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 0:
                        showToast("保存成功，图片路径为：" + ((String) msg.obj));
                        break;
                    case 1:
                        showToast("保存成功，Gif路径为：" + ((String) msg.obj));
                        break;
                }
            }
        };

        private void saveImage(final int position) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    File file = FileUtils.saveImgFile(DisplayImgActivity.this, ImageLoader.getInstance().loadImageSync(images.get(position).getFileUrl()), FileUtils.getStorageDirectory());
                    if (file != null) {
                        Message message = handler.obtainMessage();
                        message.what = 0;
                        message.obj = file.getAbsolutePath();
                        handler.sendMessage(message);
                    }
                }
            }).start();
        }

        private void saveGif(final int position) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    String gifPath = "";
                    if (images.get(position).getFileUrl().contains(FieldFinals.WATERMARK)) {
                        String s[] = images.get(position).getFileUrl().split("\\?");
                        gifPath = s[0];
                    }else{
                        gifPath = images.get(position).getFileUrl();
                    }
                    String md5Url = AlxGifHelper.getMd5(gifPath);
                    String path = DisplayImgActivity.this.getCacheDir().getAbsolutePath() + "/" + md5Url;//带.tmp后缀的是没有下载完成的，用于加载第一帧，不带tmp后缀是下载完成的，
                    //这样做的目的是为了防止一个图片正在下载的时候，另一个请求相同url的imageView使用未下载完毕的文件显示一半图像
                    File file = new File(path);
                    if(file!=null){
                        File gifFile = FileUtils.saveGifFile(DisplayImgActivity.this,file,FileUtils.getStorageDirectory());
                        if(gifFile!=null&&file.exists()){
                            Message message = handler.obtainMessage();
                            message.what = 1;
                            message.obj = gifFile.getAbsolutePath();
                            handler.sendMessage(message);
                        }
                    }
                }
            }).start();
        }

        @Override
        public int getCount() {
            return images.size();
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

}
