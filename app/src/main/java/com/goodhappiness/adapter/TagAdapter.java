package com.goodhappiness.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.goodhappiness.R;

import java.util.List;

/**
 * Created by 电脑 on 2016/9/1.
 */
public class TagAdapter extends
        RecyclerView.Adapter<TagAdapter.ViewHolder> {

    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private LayoutInflater mInflater;
    private List<String> mDatas;

    public TagAdapter(Context context, List<String> datats) {
        mInflater = LayoutInflater.from(context);
        mDatas = datats;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View arg0) {
            super(arg0);
        }

        TextView mTxt;
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = mInflater.inflate(R.layout.layout_tv_tag,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.mTxt = (TextView) view
                .findViewById(R.id.layout_tv_tag_tv);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.mTxt.setText(mDatas.get(i));
        if (mOnItemClickLitener != null) {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnItemClickLitener.onItemClick(viewHolder.itemView, i);
                }
            });

        }

    }

}
