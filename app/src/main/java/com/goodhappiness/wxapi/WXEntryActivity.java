package com.goodhappiness.wxapi;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.bean.ThirdLogin;
import com.goodhappiness.bean.WechatAuth;
import com.goodhappiness.bean.WechatThirdLoginResult;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.ui.register.LoginActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.modelmsg.ShowMessageFromWX;
import com.tencent.mm.sdk.modelmsg.WXAppExtendObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;


public class WXEntryActivity extends BaseActivity implements IWXAPIEventHandler {
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.entry);
        
        GoodHappinessApplication.wxapi.handleIntent(getIntent(), this);
    }

	@Override
	protected void setData() {

	}

	@Override
	protected void reload() {

	}
	@Override
	public void onResume() {
		super.onResume();
		MobclickAgent.onPageStart((getString(R.string.wechat_back)));
		MobclickAgent.onResume(this);
	}
	@Override
	public void onPause() {
		super.onPause();
		MobclickAgent.onPageEnd((getString(R.string.wechat_back)));
		MobclickAgent.onPause(this);
	}
	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		setIntent(intent);
		GoodHappinessApplication.wxapi.handleIntent(intent, this);
	}

	// 微信发送请求到第三方应用时，会回调到该方法
	@Override
	public void onReq(BaseReq req) {
		Toast.makeText(this, "openid = " + req.openId, Toast.LENGTH_SHORT).show();
		switch (req.getType()) {
		case ConstantsAPI.COMMAND_GETMESSAGE_FROM_WX:
			Toast.makeText(this, "COMMAND_GETMESSAGE_FROM_WX", Toast.LENGTH_SHORT).show();
			break;
		case ConstantsAPI.COMMAND_SHOWMESSAGE_FROM_WX:
			goToShowMsg((ShowMessageFromWX.Req) req);
			break;
		case ConstantsAPI.COMMAND_LAUNCH_BY_WX:
			Toast.makeText(this, "COMMAND_LAUNCH_BY_WX", Toast.LENGTH_SHORT).show();
			break;
		default:
			break;
		}
	}

	// 第三方应用发送到微信的请求处理后的响应结果，会回调到该方法
	@Override
	public void onResp(BaseResp resp) {
//		Toast.makeText(this, "openid = " + resp.openId, Toast.LENGTH_SHORT).show();
		if (resp.getType() == ConstantsAPI.COMMAND_SENDAUTH) {
			if(((SendAuth.Resp) resp).errCode==0){
				RequestParams params = new RequestParams(HttpFinal.WECHAT_AUTH);
				params.addBodyParameter("appid", Constants.WX_APP_ID);
				params.addBodyParameter("secret", Constants.WX_APP_SECRET);
				params.addBodyParameter(FieldFinals.CODE, ((SendAuth.Resp) resp).code);
				params.addBodyParameter("grant_type", "authorization_code");
				x.http().post(params, new Callback.ProgressCallback<String>() {
					@Override
					public void onWaiting() {

					}

					@Override
					public void onStarted() {
						newDialog().show();
					}

					@Override
					public void onLoading(long total, long current, boolean isDownloading) {

					}

					@Override
					public void onSuccess(String result) {
						Gson gson = new Gson();
						WechatAuth wechatAuth = gson.fromJson(result, new TypeToken<WechatAuth>() {
						}.getType());
						if(wechatAuth!=null&&!TextUtils.isEmpty(wechatAuth.getAccess_token())){
							getUserInfo(wechatAuth);
						}else{
							finishActivity();
						}
					}

					@Override
					public void onError(Throwable ex, boolean isOnCallback) {
						dialog.dismiss();
					}

					@Override
					public void onCancelled(CancelledException cex) {

					}

					@Override
					public void onFinished() {
						finish();
					}
				});
			}else {
				finish();
			}
			return;
		}
		String result = "";
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
			result = "分享成功";
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			break;
		default:
			result = "返回";
			break;
		}
		finish();
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
	}

	private void getUserInfo(final WechatAuth wechatAuth) {
		RequestParams params = new RequestParams(HttpFinal.WECHAT_USERINFO);
		params.addBodyParameter("access_token", wechatAuth.getAccess_token());
		params.addBodyParameter("openid", wechatAuth.getOpenid());
		x.http().post(params, new Callback.CommonCallback<String>() {
			@Override
			public void onSuccess(String result) {
				Gson gson = new Gson();
				WechatThirdLoginResult thirdLoginResult = gson.fromJson(result, new TypeToken<WechatThirdLoginResult>() {
				}.getType());
				if(thirdLoginResult!=null){
					ThirdLogin thirdLogin = new ThirdLogin();
					thirdLogin.setUsername(thirdLoginResult.getNickname());
					thirdLogin.setAvatar(thirdLoginResult.getHeadimgurl());
					thirdLogin.setAction("wx");
					thirdLogin.setToken(wechatAuth.getAccess_token());
					thirdLogin.setOpenid(thirdLoginResult.getUnionid());
					Intent intent = new Intent(WXEntryActivity.this, LoginActivity.class);
					intent.putExtra(FieldFinals.ACTION,thirdLogin);
					startActivity(intent);
				}
			}

			@Override
			public void onError(Throwable ex, boolean isOnCallback) {

			}

			@Override
			public void onCancelled(CancelledException cex) {

			}

			@Override
			public void onFinished() {
				dialog.dismiss();
				finishActivity();
			}
		});
	}

	private void goToShowMsg(ShowMessageFromWX.Req showReq) {
		WXMediaMessage wxMsg = showReq.message;
		WXAppExtendObject obj = (WXAppExtendObject) wxMsg.mediaObject;

		StringBuffer msg = new StringBuffer(); // 组织一个待显示的消息内容
		msg.append("description: ");
		msg.append(wxMsg.description);
		msg.append("\n");
		msg.append("extInfo: ");
		msg.append(obj.extInfo);
		msg.append("\n");
		msg.append("filePath: ");
		msg.append(obj.filePath);

		Intent intent = new Intent(this, ShowFromWXActivity.class);
		intent.putExtra(Constants.STitle, wxMsg.title);
		intent.putExtra(Constants.SMessage, msg.toString());
		intent.putExtra(Constants.BAThumbData, wxMsg.thumbData);
		startActivity(intent);
		finish();
	}
}