package com.goodhappiness.dao;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.goodhappiness.bean.PostFilesBean;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.ui.social.picture.DisplayImgActivity;

import java.util.ArrayList;

/**
 * Created by 电脑 on 2016/9/20.
 */
public class SingleOnclick implements View.OnClickListener {

    private int index;
    private ArrayList<PostFilesBean> postFiles;
    private Context context;

    public SingleOnclick(Context context,int index, String url) {
        this.index = index;
        this.postFiles = new ArrayList<>();
        postFiles.add(new PostFilesBean(url,1));
        this.context = context;
    }

    public SingleOnclick(Context context,int index, ArrayList<PostFilesBean> postFiles) {
        this.index = index;
        this.postFiles = postFiles;
        this.context = context;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(context, DisplayImgActivity.class);
        intent.putExtra(FieldFinals.ACTION, FieldFinals.ONLINE);
        intent.putExtra(FieldFinals.POSITION, index);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(FieldFinals.FEED_INFO_LIST, postFiles);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}