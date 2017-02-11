package com.goodhappiness.ui.social;

import android.view.View;
import android.widget.ListView;

import com.goodhappiness.R;
import com.goodhappiness.adapter.CommonAdapter;
import com.goodhappiness.adapter.ViewHolder;
import com.goodhappiness.bean.FlowerRank;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.widget.refreshlayout.BGARefreshLayout;
import com.umeng.analytics.MobclickAgent;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

@ContentView(R.layout.activity_flower_rank)
public class FlowerRankActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate{

    @ViewInject(R.id.srl)
    private BGARefreshLayout srl;
    @ViewInject(R.id.lv)
    private ListView lv;

    private CommonAdapter<FlowerRank> adapter;
    private List<FlowerRank> list = new ArrayList<>();
    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }
    @Override
    protected void setData() {
        initView();
        tv_title.setText(R.string.flower_rank);
        iv_right.setImageResource(R.mipmap.ico_btn1_header);
        iv_right.setVisibility(View.VISIBLE);
        list.add(new FlowerRank());
        list.add(new FlowerRank());
        list.add(new FlowerRank());
        list.add(new FlowerRank());
        list.add(new FlowerRank());
        list.add(new FlowerRank());
        adapter = new CommonAdapter<FlowerRank>(this,list,R.layout.layout_list_flower_rank) {
            @Override
            public void convert(ViewHolder helper, FlowerRank item, int position) {
                if(position<=2){
                    helper.setVisibility(R.id.iv, View.VISIBLE);
                    if(position==0){
//                        helper.setImageResource(R.id.iv,R.mipmap.ico_no1);
                    }else if(position==1){
//                        helper.setImageResource(R.id.iv,R.mipmap.ico_no2);
                    }else if(position==2){
//                        helper.setImageResource(R.id.iv,R.mipmap.ico_no3);
                    }
                }else{
                    helper.setVisibility(R.id.iv, View.GONE);
                }
            }
        };
        lv.setAdapter(adapter);
    }

    @Override
    protected void reload() {

    }


    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {

    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
