package com.goodhappiness.provider;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.bean.ConfirmOrder;
import com.goodhappiness.bean.Result;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.dao.OnSelectListener;
import com.goodhappiness.dao.OnSendFlowerListener;
import com.goodhappiness.factory.DialogFactory;
import com.goodhappiness.ui.register.RegisterActivity;
import com.goodhappiness.ui.social.im.ConversationActivity;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.IntentUtils;
import com.goodhappiness.utils.PreferencesUtil;
import com.google.gson.reflect.TypeToken;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import io.rong.imkit.RongContext;
import io.rong.imkit.widget.provider.InputProvider;

/**
 * Created by 电脑 on 2016/7/5.
 */
public class FlowerExtendProvider extends InputProvider.ExtendProvider {
    private Context context;
    public FlowerExtendProvider(RongContext context) {
        super(context);
        this.context = context;
    }

    /**
     * 设置展示的图标
     *
     * @param context
     * @return
     */
    @Override
    public Drawable obtainPluginDrawable(Context context) {
        this.context = context;
        return context.getResources().getDrawable(R.mipmap.ico_flower_key);
    }

    /**
     * 设置图标下的title
     *
     * @param context
     * @return
     */
    @Override
    public CharSequence obtainPluginTitle(Context context) {
        return context.getString(R.string.flower_send);
    }

    /**
     * click 事件
     *
     * @param view
     */
    @Override
    public void onPluginClick(View view) {
        if (PreferencesUtil.getIntPreferences(GoodHappinessApplication.getContext(), FieldFinals.IS_BIND, 0) == 0) {
            DialogFactory.createSelectDialog(context, "您未完善信息，是否完善信息", new OnSelectListener() {
                @Override
                public void onSelected(boolean isSelected) {
                    if (isSelected) {
                        Intent intent = new Intent(context, RegisterActivity.class);
                        intent.putExtra(FieldFinals.ACTION, RegisterActivity.REGISTER_TYPE_BIND);
                        context.startActivity(intent);
                    }
                }
            });
        } else {
            DialogFactory.createSendFlowerDialog(getCurrentFragment().getActivity(), new OnSendFlowerListener() {
                @Override
                public void onclick(Integer count) {
                    confirmFlowerOrder(context,count);
                }
            });
        }
    }

    private void confirmFlowerOrder(Context context,final Integer count) {
        RequestParams params = new RequestParams(HttpFinal.DONATE_CONFIRM);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, ((ConversationActivity)getCurrentFragment().getActivity()).getDid());
        params.addBodyParameter(FieldFinals.SID, ((ConversationActivity)getCurrentFragment().getActivity()).getSid());
        params.addBodyParameter(FieldFinals.UID, getCurrentConversation().getTargetId() + "");
        params.addBodyParameter(FieldFinals.NUM, count + "");
        HttpUtils.post(context,params, new TypeToken<Result<ConfirmOrder>>() {
        }.getType(), new OnHttpRequest() {
            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

            }

            @Override
            public void onStarted() {
                ((ConversationActivity)getCurrentFragment().getActivity()).newDialog().show();
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onSuccess(Result result) {
                if (result != null && result.getData() != null) {
                    ConfirmOrder confirmOrder = (ConfirmOrder) result.getData();
                    confirmOrder.setOrderType(1);
                    confirmOrder.setClassName(((ConversationActivity) getCurrentFragment().getActivity()).getClass().getName());
                    confirmOrder.setFlowerCount(count);
                    IntentUtils.startToPayOrder(getCurrentFragment().getActivity(), confirmOrder);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                ((ConversationActivity)getCurrentFragment().getActivity()).dialog.dismiss();
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }
}
