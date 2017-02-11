package com.goodhappiness.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.goodhappiness.R;
import com.goodhappiness.bean.database.SearchHistory;

import java.util.Collections;
import java.util.List;

/**
 * Created by 电脑 on 2016/9/1.
 */
public class SearchHistoryAdapter extends
        RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder>
{

    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
    }

    private OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }

    private LayoutInflater mInflater;
    private List<SearchHistory> mDatas;

    public SearchHistoryAdapter(Context context,List<SearchHistory> list)
    {
        mInflater = LayoutInflater.from(context);
        mDatas = list;
        Collections.reverse(mDatas);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        public ViewHolder(View arg0)
        {
            super(arg0);
        }

        TextView mTxt;
    }

    @Override
    public int getItemCount()
    {
        return mDatas.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i)
    {
        View view = mInflater.inflate(R.layout.tv_tag,
                viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);

        viewHolder.mTxt = (TextView) view
                .findViewById(R.id.tv_history);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i)
    {
       viewHolder.mTxt.setText(mDatas.get(i).getItem());
        if (mOnItemClickLitener != null)
        {
            viewHolder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mOnItemClickLitener.onItemClick(viewHolder.itemView, i);
                }
            });

        }

    }

}
