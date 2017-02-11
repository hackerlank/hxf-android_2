package com.goodhappiness.ui.social.video;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.goodhappiness.R;
import com.goodhappiness.adapter.VideoDetailAdapter;
import com.goodhappiness.bean.BitmapEntity;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.utils.FileUtils;
import com.goodhappiness.utils.IntentUtils;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;

@ContentView(R.layout.activity_video_list)
public class VideoListActivity extends BaseActivity {
    @ViewInject(R.id.video_list_gv)
    private GridView gv;
    private VideoDetailAdapter adapter;
    private ArrayList<BitmapEntity> bit = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Event({R.id.common_right_text})
    private void onEventClick(View v) {
        switch (v.getId()) {
            case R.id.common_right_text:
                int i = 0;
                boolean isSelected = false;
                for (BitmapEntity entity : bit) {
                    if (entity.isSelect()) {
                        isSelected = true;
                        break;
                    }
                    i++;
                }
                if (isSelected) {
                    if(!TextUtils.isEmpty(bit.get(i).getUri())){
                        Intent intent = new Intent(this,VideoPublishActivity.class);
                        intent.putExtra(FieldFinals.FILE_PATH,bit.get(i).getUri());
                        intent.putExtra(FieldFinals.DURATION,FileUtils.getVideoDuration(bit.get(i).getUri()));
                        startActivity(intent);
                    }
                } else {
                    showToast(R.string.please_select_video);
                }
                break;
        }
    }

    @Override
    protected void setData() {
        initView();
        if (getIntent() != null && getIntent().getExtras() != null && getIntent().getExtras().getParcelableArrayList(FieldFinals.ACTION) != null) {
            bit.addAll(getIntent().getExtras().<BitmapEntity>getParcelableArrayList(FieldFinals.ACTION));
            adapter = new VideoDetailAdapter(this, bit);
            gv.setAdapter(adapter);
            gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int i = 0;
                    boolean isSelected = false;
                    for (BitmapEntity entity : bit) {
                        if (entity.isSelect()) {
                            isSelected = true;
                            break;
                        }
                        i++;
                    }
                    if (isSelected) {
                        if (position == i) {
                            IntentUtils.startToLocalVideoPlayer(VideoListActivity.this,bit.get(position).getUri());
                        } else {
                            bit.get(i).setSelect(false);
                            bit.get(position).setSelect(true);
                        }
                    } else {
                        bit.get(position).setSelect(true);
                    }
                    adapter.notifyDataSetChanged();
                }
            });
        }
        rl_head.setBackgroundColor(getResources().getColor(R.color.video_list_head));
        iv_back.setImageResource(R.mipmap.icon_delete_default);
        tv_right.setText(R.string.continue_);
        tv_right.setTextColor(getResources().getColor(R.color.black_333_text));
    }

    @Override
    protected void reload() {

    }
}
