package com.goodhappiness.Task;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Message;
import android.text.TextUtils;

import com.goodhappiness.R;
import com.goodhappiness.bean.BaseShareObject;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.constant.StringFinal;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.ui.base.BaseFragmentActivity;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.ShareUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.xutils.http.RequestParams;

/**
 * Created by 电脑 on 2016/6/12.
 */
public class GetShareInfoTask extends AsyncTask<String, Integer, BaseShareObject> {

    private Dialog dialog;
    private BaseShareObject shareObject;
    private SHARE_MEDIA share_media;
    private Activity context;

    public GetShareInfoTask(Activity context, SHARE_MEDIA share_media) {
        this.share_media = share_media;
        this.context = context;
        if (context instanceof BaseActivity) {
            dialog = ((BaseActivity) context).newDialog();
        } else if (context instanceof BaseFragmentActivity) {
            dialog = ((BaseFragmentActivity) context).newDialog();
        }

    }

    @Override
    protected BaseShareObject doInBackground(String... stringParams) {
        if (stringParams.length >= 4) {
            RequestParams params = new RequestParams(HttpFinal.SHARE);
            params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, stringParams[0]);
            params.addBodyParameter(FieldFinals.SID, stringParams[1]);
            params.addBodyParameter(FieldFinals.ACTION, stringParams[2]);
            params.addBodyParameter(FieldFinals.SHARE_ID, stringParams[3]);
            shareObject = (BaseShareObject) HttpUtils.postSyncFastJson(params, new BaseShareObject());//postSync(params, new TypeToken<Result<BaseShareObject>>() {
//            }.getType());
            if (shareObject != null && !TextUtils.isEmpty(shareObject.getShareImg())) {
                Bitmap bitmap = null;
                if (shareObject.getShareType() == 3) {
                    bitmap = ImageLoader.getInstance().loadImageSync(shareObject.getShareImg() + StringFinal.VIDEO_URL_END, new ImageSize(150, 150));
                } else {
                    bitmap = ImageLoader.getInstance().loadImageSync(shareObject.getShareImg(), new ImageSize(150, 150));
                }
                if (bitmap == null) {
                    bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);
                }
                shareObject.setBitmap(bitmap);
            }
                if (shareObject == null) {
                return null;
            } else {
                return shareObject;
            }
        } else {
            return null;
        }
    }

    @Override
    protected void onPostExecute(BaseShareObject baseShareObject) {
        super.onPostExecute(baseShareObject);
        if (dialog != null) {
            dialog.dismiss();
        }
        if (baseShareObject != null) {
            if (share_media == SHARE_MEDIA.SINA) {
                if (context instanceof BaseActivity) {
                    Message message = ((BaseActivity) context).mHandler.obtainMessage();
                    message.what = FieldFinals.SHARE_FLAG;
                    message.obj = baseShareObject;
                    ((BaseActivity) context).mHandler.sendMessage(message);
                } else if (context instanceof BaseFragmentActivity) {
                    Message message = ((BaseFragmentActivity) context).mHandler.obtainMessage();
                    message.what = FieldFinals.SHARE_FLAG;
                    message.obj = baseShareObject;
                    ((BaseFragmentActivity) context).mHandler.sendMessage(message);
                }
            } else {
                ShareUtil.share(context, baseShareObject, share_media);
            }
        }
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (dialog != null) {
            dialog.show();
        }
    }
}