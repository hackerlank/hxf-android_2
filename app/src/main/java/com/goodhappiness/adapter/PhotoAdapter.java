package com.goodhappiness.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.goodhappiness.R;
import com.goodhappiness.bean.Photo;
import com.goodhappiness.constant.StringFinal;
import com.goodhappiness.ui.personal.selecthead.PhotoPickerActivity;
import com.goodhappiness.widget.photopicker.OtherUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * @Class: PhotoAdapter
 * @Description: 图片适配器
 * @author: lling(www.liuling123.com)
 * @Date: 2015/11/4
 */
public class PhotoAdapter extends BaseAdapter {

    private static final int TYPE_CAMERA = 0;
    private static final int TYPE_PHOTO = 1;

    private List<Photo> mDatas;
    //存放已选中的Photo数据
    public List<String> mSelectedPhotos;
    private Context mContext;
    private int mWidth;
    //是否显示相机，默认不显示
    private boolean mIsShowCamera = false;
    //照片选择模式，默认单选
    private int mSelectMode = PhotoPickerActivity.MODE_SINGLE;
    //图片选择数量
    private int mMaxNum = PhotoPickerActivity.DEFAULT_NUM;

    private View.OnClickListener mOnPhotoClick;
    private PhotoClickCallBack mCallBack;
    public View leastView;

    public PhotoAdapter(Context context, List<Photo> mDatas) {
        this.mDatas = mDatas;
        this.mContext = context;
        int screenWidth = OtherUtils.getWidthInPx(mContext);
        mWidth = (screenWidth - OtherUtils.dip2px(mContext, 4)) / 3;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 && mIsShowCamera) {
            return TYPE_CAMERA;
        } else {
            return TYPE_PHOTO;
        }
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Photo getItem(int position) {
        if (mIsShowCamera) {
            if (position == 0) {
                return null;
            }
            return mDatas.get(position - 1);
        } else {
            return mDatas.get(position);
        }
    }

    @Override
    public long getItemId(int position) {
        return mDatas.get(position).getId();
    }

    public void setDatas(List<Photo> mDatas) {
        this.mDatas = mDatas;
    }

    public void setIsShowCamera(boolean isShowCamera) {
        this.mIsShowCamera = isShowCamera;
    }

    public boolean isShowCamera() {
        return mIsShowCamera;
    }

    public void setMaxNum(int maxNum) {
        this.mMaxNum = maxNum;
    }

    public void setPhotoClickCallBack(PhotoClickCallBack callback) {
        mCallBack = callback;
    }


    /**
     * 获取已选中相片
     *
     * @return
     */
    public List<String> getmSelectedPhotos() {
        return mSelectedPhotos;
    }

    public void setSelectMode(int selectMode) {
        this.mSelectMode = selectMode;
        if (mSelectMode == PhotoPickerActivity.MODE_MULTI) {
            initMultiMode();
        }
    }

    /**
     * 初始化多选模式所需要的参数
     */
    private void initMultiMode() {
        mSelectedPhotos = new ArrayList<String>();
        mOnPhotoClick = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallBack != null) {
                    String path = ((Photo)(v.findViewById(R.id.imageview_photo).getTag())).getPath();
                    if(!((Photo)(v.findViewById(R.id.imageview_photo).getTag())).isLoad()){
                        Toast.makeText(mContext,"文件不存在",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(mCallBack.onPhotoClick(path,v)){
                        if (mSelectedPhotos.contains(path)) {
                            mSelectedPhotos.remove(path);
                            notifyDataSetChanged();
                            return;
                        } else {
                            if (mSelectedPhotos.size() >= mMaxNum) {
                                leastView.findViewById(R.id.mask).setVisibility(View.GONE);
                                leastView.findViewById(R.id.checkmark).setSelected(false);
                                mSelectedPhotos.clear();
                                //TODO 可选张数最大值判别！！！！！！！！！
                            }
                            mSelectedPhotos.add(path);
                            leastView = v;
                            notifyDataSetChanged();
                        }
                    }
                }
            }
        };
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (getItemViewType(position) == TYPE_CAMERA) {
            convertView = LayoutInflater.from(mContext).inflate(
                    R.layout.item_camera_layout, null);
            convertView.setTag(null);
            //设置高度等于宽度
            GridView.LayoutParams lp = new GridView.LayoutParams(mWidth, mWidth);
            convertView.setLayoutParams(lp);
        } else {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.item_photo_layout, null);
                holder.photoImageView = (ImageView) convertView.findViewById(R.id.imageview_photo);
                holder.selectView = (ImageView) convertView.findViewById(R.id.checkmark);
                holder.gif = (ImageView) convertView.findViewById(R.id.gif);
                holder.maskView = convertView.findViewById(R.id.mask);
                holder.wrapLayout = (FrameLayout) convertView.findViewById(R.id.wrap_layout);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            Photo photo = getItem(position);
            if (mSelectMode == PhotoPickerActivity.MODE_MULTI) {
                holder.wrapLayout.setOnClickListener(mOnPhotoClick);
                holder.photoImageView.setTag(photo);
                holder.selectView.setVisibility(View.VISIBLE);
                if (mSelectedPhotos != null && mSelectedPhotos.contains(photo.getPath())) {
                    holder.selectView.setSelected(true);
                    holder.maskView.setVisibility(View.VISIBLE);
                } else {
                    holder.selectView.setSelected(false);
                    holder.maskView.setVisibility(View.GONE);
                }
            } else {
                holder.selectView.setVisibility(View.GONE);
            }
            Log.e("p_p",photo.getPath());
            holder.gif.setVisibility(View.GONE);
            if(photo.getPath().endsWith(".gif")){
                holder.gif.setVisibility(View.VISIBLE);
            }
            Picasso.with(mContext).load(StringFinal.IMG_URI_HEAD +photo.getPath())
                    .resize(150, 150)
                    .centerCrop()
                    .placeholder(R.mipmap.loading_default)
                    .error(R.mipmap.loading_default)
                    .into(holder.photoImageView);
//            Glide.with(mContext).load(photo.getPath()).thumbnail(0.1f).into(holder.photoImageView);
//            com.nostra13.universalimageloader.core.ImageLoader.getInstance().displayImage(StringFinal.IMG_URI_HEAD + photo.getPath(), holder.photoImageView,new ImageSize(100,100));
//                    GoodHappinessApplication.options,new ImageSize(100,100),new ImageLoadingListener() {
//                @Override
//                public void onLoadingStarted(String s, View view) {
//                }
//
//                @Override
//                public void onLoadingFailed(String s, View view, FailReason failReason) {
//                    mDatas.get(position).setLoad(false);
//                }
//
//                @Override
//                public void onLoadingComplete(String s, View view, Bitmap bitmap) {
//                }
//
//                @Override
//                public void onLoadingCancelled(String s, View view) {
//                }
//            },null);//new ImageSize(mWidth, mWidth));
        }
        return convertView;
    }

    private class ViewHolder {
        private ImageView photoImageView;
        private ImageView selectView;
        private ImageView gif;
        private View maskView;
        private FrameLayout wrapLayout;
    }

    /**
     * 多选时，点击相片的回调接口
     */
    public interface PhotoClickCallBack {
        boolean onPhotoClick(String path,View v);
    }
}
