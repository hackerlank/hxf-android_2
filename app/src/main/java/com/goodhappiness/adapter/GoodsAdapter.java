package com.goodhappiness.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.bean.ProductList;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

/**
 * Created by 电脑 on 2016/11/3.
 */
public class GoodsAdapter extends
        RecyclerView.Adapter<GoodsAdapter.ViewHolder> {

    public interface OnItemClickLitener {
        void onItemClick(ProductList view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private LayoutInflater mInflater;
    private List<ProductList> mDatas;

    public GoodsAdapter(Context context, List<ProductList> datats) {
        mInflater = LayoutInflater.from(context);
        mDatas = datats;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        ImageView mImg;
        RelativeLayout mRe;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.activity_goods_item,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.mImg = (ImageView) view
                .findViewById(R.id.id_index_gallery_item_image);
        viewHolder.mRe = (RelativeLayout) view
                .findViewById(R.id.id_index_gallery_item_rl);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        if(i==mDatas.size()-1){
            viewHolder.mImg.setVisibility(View.GONE);
            viewHolder.mRe.setVisibility(View.VISIBLE);
        }else{
            viewHolder.mImg.setVisibility(View.VISIBLE);
            viewHolder.mRe.setVisibility(View.GONE);
            if(mDatas.get(i).getProductImage().size()>0){
                ImageLoader.getInstance().displayImage(mDatas.get(i).getProductImage().get(0), viewHolder.mImg, GoodHappinessApplication.options);
            }
        }
        if (mOnItemClickLitener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(mDatas.get(i), i);
                }
            });

        }

    }

}
