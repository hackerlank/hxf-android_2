package com.goodhappiness.wxapi;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.bean.ConfirmOrder;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.factory.DialogFactory;
import com.goodhappiness.ui.order.ConfirmOrderActivity;
import com.goodhappiness.ui.order.InventoryActivity;
import com.goodhappiness.ui.order.PayOrderActivity;
import com.goodhappiness.ui.personal.ChargeActivity;
import com.goodhappiness.ui.personal.ChargeResultActivity;
import com.goodhappiness.ui.personal.ExchangeRecordActivity;
import com.goodhappiness.utils.AppManager;
import com.goodhappiness.utils.IntentUtils;
import com.goodhappiness.utils.PreferencesUtil;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;


public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, Constants.WX_APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }
    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart((getString(R.string.wechat_pay_back)));
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd((getString(R.string.wechat_pay_back)));
        MobclickAgent.onPause(this);
    }
    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        ConfirmOrder order = PreferencesUtil.getPreferences(WXPayEntryActivity.this, FieldFinals.CONFIRM_ORDER);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            switch (resp.errCode) {
                case -2://返回
                    finish();
                    if(GoodHappinessApplication.activityPayType== PayOrderActivity.activityPayTypeFlower){
                        IntentUtils.sendFlowerSendBroadcast(WXPayEntryActivity.this, order.getClassName(), order.getFlowerCount(), false);
                    }
                    break;
                case -1://失败
                    DialogFactory.createDefaultDialog(this, getString(R.string.pay_fail), new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            WXPayEntryActivity.this.finish();
                        }
                    });
                    if(GoodHappinessApplication.activityPayType== PayOrderActivity.activityPayTypeFlower){
//                        IntentUtils.sendFlowerSendBroadcast(WXPayEntryActivity.this, order.getClassName(), order.getFlowerCount(), false);
                    }
                    break;
                case 0://成功
                    if(GoodHappinessApplication.activityPayType== PayOrderActivity.activityPayType){
                        IntentUtils.startToNewWebView(WXPayEntryActivity.this,PreferencesUtil.getStringPreferences(WXPayEntryActivity.this,FieldFinals.PAY_RESULT_URL));
                    }else if(GoodHappinessApplication.activityPayType == ChargeActivity.activityPayType){
                        startActivity(new Intent(WXPayEntryActivity.this, ChargeResultActivity.class));
                    }else if(GoodHappinessApplication.activityPayType== PayOrderActivity.activityPayTypeFlower){
                        IntentUtils.sendFlowerSendBroadcastWith(WXPayEntryActivity.this, order.getClassName(), order.getFlowerCount(), true, order.getToUserInfo().getNickname(), order.getToUserInfo().getUid());
                    }if(GoodHappinessApplication.activityPayType== ExchangeRecordActivity.activityPayType){
                    startActivity(new Intent(WXPayEntryActivity.this, ExchangeRecordActivity.class));
                }
                    finishThis();
                    break;
            }
        }
    }
    private void finishThis(){
        AppManager.getAppManager().finishActivities(new Class[]{InventoryActivity.class, ConfirmOrderActivity.class
        ,PayOrderActivity.class});
        finish();
    }
}