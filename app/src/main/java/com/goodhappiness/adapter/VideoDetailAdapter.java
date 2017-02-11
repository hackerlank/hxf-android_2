package com.goodhappiness.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.bean.BitmapEntity;
import com.goodhappiness.bean.TimeChange;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

@SuppressLint({"InflateParams", "ViewHolder"})
public class VideoDetailAdapter extends BaseAdapter {

    private List<BitmapEntity> objects = new ArrayList<BitmapEntity>();

    @SuppressWarnings("unused")
    private Context context;
    private LayoutInflater layoutInflater;

    public VideoDetailAdapter(Context context, List<BitmapEntity> objects) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.objects = objects;
    }

    public List<BitmapEntity> setObjects(List<BitmapEntity> objects) {
        return this.objects = objects;
    }

    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public BitmapEntity getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 数据量过大可能出现错乱，暂时不用缓存策略
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.newcluedetail_listview, null);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.imgv = (ImageView) convertView.findViewById(R.id.imgv);
            viewHolder.imgSelect = (ImageView) convertView.findViewById(R.id.iv_video_select);
            viewHolder.duration = (TextView) convertView.findViewById(R.id.video_time_long);
            convertView.setTag(viewHolder);
        }
        initializeViews((BitmapEntity) getItem(position), (ViewHolder) convertView.getTag());
        return convertView;
    }

    private void initializeViews(BitmapEntity object, ViewHolder holder) {
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) holder.imgv.getLayoutParams();
        params.height = (int)((float)(GoodHappinessApplication.w-GoodHappinessApplication.perHeight*40)/3);
        params.width = params.height;
        holder.imgv.setLayoutParams(params);
        ImageLoader.getInstance().displayImage("file://" + object.getUri_thumb(), holder.imgv,GoodHappinessApplication.options);
        holder.duration.setText(TimeChange.setTime(object.getDuration()));
        if(object.isSelect()){
            holder.imgSelect.setVisibility(View.VISIBLE);
        }else{
            holder.imgSelect.setVisibility(View.GONE);
        }
    }

    protected class ViewHolder {
        private ImageView imgv;
        private ImageView imgSelect;
        private TextView duration;
    }
}

