package com.goodhappiness.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.NewWebViewActivity;
import com.goodhappiness.R;
import com.goodhappiness.bean.ConfirmOrder;
import com.goodhappiness.bean.FocusChangeBean;
import com.goodhappiness.bean.FriendShipChangeBean;
import com.goodhappiness.bean.PostFilesBean;
import com.goodhappiness.bean.Result;
import com.goodhappiness.bean.WechatRequest;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.constant.StringFinal;
import com.goodhappiness.dao.OnSelectListener;
import com.goodhappiness.factory.DialogFactory;
import com.goodhappiness.ui.HomepageActivity;
import com.goodhappiness.ui.WebViewActivity;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.ui.lottery.LotteryDetailActivity;
import com.goodhappiness.ui.lottery.LotteryRecordActivity;
import com.goodhappiness.ui.order.PayOrderActivity;
import com.goodhappiness.ui.order.RedbagActivity;
import com.goodhappiness.ui.personal.ChargeActivity;
import com.goodhappiness.ui.personal.ExchangeRecordActivity;
import com.goodhappiness.ui.personal.WinRecordActivity;
import com.goodhappiness.ui.register.LoginActivity;
import com.goodhappiness.ui.register.RegisterActivity;
import com.goodhappiness.ui.social.FlowerActivity;
import com.goodhappiness.ui.social.FriendActivity;
import com.goodhappiness.ui.social.LikeMeActivity;
import com.goodhappiness.ui.social.PersonActivity;
import com.goodhappiness.ui.social.ReportActivity;
import com.goodhappiness.ui.social.SearchActivity;
import com.goodhappiness.ui.social.SocialDetailActivity;
import com.goodhappiness.ui.social.picture.CommentToMeActivity;
import com.goodhappiness.ui.social.picture.DisplayImgActivity;
import com.goodhappiness.ui.social.picture.EditPicUpdateActivity;
import com.goodhappiness.ui.social.video.VideoActivity;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tencent.mm.sdk.modelpay.PayReq;

import java.util.ArrayList;
import java.util.Map;

/**
 * Created by 电脑 on 2016/5/23.
 */
public class IntentUtils {

    public static void sendFeedChangeBroadcast(Context context, FriendShipChangeBean bean) {
        Intent intent = new Intent();
        intent.setAction(StringFinal.BROADCAST_FEED_CHANGE);
        intent.putExtra(FieldFinals.FEED_INFO_LIST, bean);
        context.sendBroadcast(intent);
    }

    public static void sendFlowerSendBroadcast(Context context, String action, int count, boolean isSuccess) {
        Intent intent = new Intent();
        intent.setAction(StringFinal.BROADCAST_FLOWER_SEND);
        intent.putExtra(FieldFinals.ACTION, action);
        intent.putExtra(FieldFinals.COUNT, count);
        intent.putExtra(FieldFinals.RESULT, isSuccess);
        context.sendBroadcast(intent);
    }
    public static void sendFlowerSendBroadcast(Context context, String action) {
        Intent intent = new Intent();
        intent.setAction(StringFinal.BROADCAST_PUSH_EXTRAS);
        intent.putExtra(FieldFinals.PUSH_EXTRAS, action);
        context.sendBroadcast(intent);
    }
    public static void startToLocalVideoPlayer(Context context,String path) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        String type = "video/mp4";
        Uri uri = Uri.parse("file://"+path);
        intent.setDataAndType(uri, type);
        context.startActivity(intent);
    }
    public static void sendFlowerSendBroadcastWith(Context context, String action, int count, boolean isSuccess, String nickName, long uid) {
        Intent intent = new Intent();
        intent.setAction(StringFinal.BROADCAST_FLOWER_SEND);
        intent.putExtra(FieldFinals.ACTION, action);
        intent.putExtra(FieldFinals.COUNT, count);
        intent.putExtra(FieldFinals.RESULT, isSuccess);
        intent.putExtra(FieldFinals.NICKNAME, nickName);
        intent.putExtra(FieldFinals.UID, uid);
        context.sendBroadcast(intent);
    }

    public static void sendNewMsgBroadcast(Context context) {
        Intent intent = new Intent();
        intent.setAction(StringFinal.BROADCAST_NEW_MSG);
        context.sendBroadcast(intent);
    }

    public static void sendOffLineBroadcast(Context context) {
        Intent intent = new Intent();
        intent.setAction(StringFinal.BROADCAST_OFF_LINE);
        context.sendBroadcast(intent);
    }

    public static void sendFocusChangeBroadcast(Context context, FocusChangeBean bean) {
        Intent intent = new Intent();
        intent.setAction(StringFinal.BROADCAST_FOCUS_CHANGE);
        intent.putExtra(FieldFinals.FOCUS_CHANGE, bean);
        context.sendBroadcast(intent);
    }

    public static void startToPayOrder(Context context, ConfirmOrder bean) {
        Intent intent = new Intent(context, PayOrderActivity.class);
        intent.putExtra(FieldFinals.CONFIRM_ORDER, bean);
        context.startActivity(intent);
        checkIntentJump(context);
    }

    public static void startToFlower(Context context, int position) {
        Intent intent = new Intent(context, FlowerActivity.class);
        intent.putExtra(FieldFinals.POSITION, position);
        context.startActivity(intent);
        checkIntentJump(context);
    }

    public static void startToRedbag(Context context, int position) {
        Intent intent = new Intent(context, RedbagActivity.class);
        intent.putExtra(FieldFinals.POSITION, position);
        context.startActivity(intent);
        checkIntentJump(context);
    }

    public static void startToExchangeRecord(Context context) {
        Intent intent = new Intent(context, ExchangeRecordActivity.class);
        context.startActivity(intent);
        checkIntentJump(context);
    }

    public static void startToCommentToMe(Context context) {
        Intent intent = new Intent(context, CommentToMeActivity.class);
        context.startActivity(intent);
        checkIntentJump(context);
    }

    public static void startToLikeToMe(Context context) {
        Intent intent = new Intent(context, LikeMeActivity.class);
        context.startActivity(intent);
        checkIntentJump(context);
    }

    public static void startToLotteryDetail(Context context, long period) {
        Intent intent = new Intent(context, LotteryDetailActivity.class);
        intent.putExtra(FieldFinals.PERIOD, period);
        context.startActivity(intent);
        checkIntentJump(context);
    }
    public static void startToSearchTag(Context context, String tag) {
        Intent intent = new Intent(context, SearchActivity.class);
        intent.putExtra(FieldFinals.SEARCH_KEY, tag);
        intent.putExtra(FieldFinals.POSITION, 1);
        context.startActivity(intent);
        checkIntentJump(context);
    }
    public static void startToDisplayImage(Context context, ArrayList<PostFilesBean> postFiles,int position) {
        Intent intent = new Intent(context, DisplayImgActivity.class);
        intent.putExtra(FieldFinals.ACTION, FieldFinals.LOCAL);
        intent.putExtra(FieldFinals.POSITION, position);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList(FieldFinals.FEED_INFO_LIST, postFiles);
        intent.putExtras(bundle);
        context.startActivity(intent);
        checkIntentJump(context);
    }
    public static void startToDisplayImage(Context context, String url) {
        Intent intent = new Intent(context, DisplayImgActivity.class);
        intent.putExtra(FieldFinals.ACTION, FieldFinals.ONLINE);
        intent.putExtra(FieldFinals.POSITION, 0);
        Bundle bundle = new Bundle();
        ArrayList<PostFilesBean> postFiles = new ArrayList<>();
        postFiles.add(new PostFilesBean(url,1));
        bundle.putParcelableArrayList(FieldFinals.FEED_INFO_LIST, postFiles);
        intent.putExtras(bundle);
        context.startActivity(intent);
        checkIntentJump(context);
    }
    public static void startToDisplayImageGif(Context context, String url) {
        Intent intent = new Intent(context, DisplayImgActivity.class);
        intent.putExtra(FieldFinals.ACTION, FieldFinals.ONLINE);
        intent.putExtra(FieldFinals.POSITION, 0);
        Bundle bundle = new Bundle();
        ArrayList<PostFilesBean> postFiles = new ArrayList<>();
        postFiles.add(new PostFilesBean(url,4));
        bundle.putParcelableArrayList(FieldFinals.FEED_INFO_LIST, postFiles);
        intent.putExtras(bundle);
        context.startActivity(intent);
        checkIntentJump(context);
    }

    public static void startToFriendship(Context context, int position, int from_type, long uid) {
        Intent intent = new Intent(context, FriendActivity.class);
        intent.putExtra(FieldFinals.POSITION, position);
        intent.putExtra(FieldFinals.TYPE, from_type);
        intent.putExtra(FieldFinals.UID, uid);
        context.startActivity(intent);
        checkIntentJump(context);
    }

    public static void startToReport(Context context, String type, long id) {
        Intent intent = new Intent(context, ReportActivity.class);
        intent.putExtra(FieldFinals.TYPE, type);
        intent.putExtra(FieldFinals.POST_ID, id);
        context.startActivity(intent);
        checkIntentJump(context);
    }

    public static void startToShop(Context context) {
        HomepageActivity.type = HomepageActivity.SHOP;
        Intent intent = new Intent(context, HomepageActivity.class);
        context.startActivity(intent);
        checkIntentJump(context);
    }
    public static void startToHomePage(Context context) {
        Intent intent = new Intent(context, HomepageActivity.class);
        context.startActivity(intent);
        checkIntentJump(context);
    }

    public static void startToRecharge(Context context) {
        Intent intent = new Intent(context, ChargeActivity.class);
        context.startActivity(intent);
        checkIntentJump(context);
    }

    public static void startToWebView(Context context, String url) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra(FieldFinals.URL, url);
        context.startActivity(intent);
        checkIntentJump(context);
    }
    public static void startToWebViewWithData(Context context, String url) {
        Intent intent = new Intent(context, NewWebViewActivity.class);
        intent.putExtra(FieldFinals.HTML, url);
        context.startActivity(intent);
        checkIntentJump(context);
    }
    public static void startToVideo(Context context, String url) {
        Intent intent = new Intent(context, VideoActivity.class);
        intent.putExtra(FieldFinals.URL, url);
        context.startActivity(intent);
        checkIntentJump(context);
    }
    public static void startToLogin(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
        checkIntentJump(context);
    }

    public static void startToNewWebView(Context context, String url) {
        Intent intent = new Intent(context, NewWebViewActivity.class);
        intent.putExtra(FieldFinals.NEW_URL, url);
        context.startActivity(intent);
        checkIntentJump(context);
    }

    public static void startToExchangeDetail(Context context) {
        Intent intent = new Intent(context, ExchangeRecordActivity.class);
        context.startActivity(intent);
        checkIntentJump(context);
    }

    public static void startToPerson(Context context, long uid) {
        Intent intent = new Intent(context, PersonActivity.class);
        intent.putExtra(FieldFinals.ACTION, uid);
        context.startActivity(intent);
        checkIntentJump(context);
    }
    public static void startToPerson(Context context, long uid,String clazzName) {
        Intent intent = new Intent(context, PersonActivity.class);
        intent.putExtra(FieldFinals.ACTION, uid);
        intent.putExtra(FieldFinals.CLAZZ_NAME, clazzName);
        context.startActivity(intent);
        checkIntentJump(context);
    }

    public static void startToSocialDetail(Context context, long feedId) {
        Intent intent = new Intent(context, SocialDetailActivity.class);
        intent.putExtra(FieldFinals.FEED_ID, feedId);
        context.startActivity(intent);
        checkIntentJump(context);
    }

    public static void startToEditPic(Context context, int action) {
        Intent intent = new Intent(context, EditPicUpdateActivity.class);
        intent.putExtra(FieldFinals.ACTION, action);
        context.startActivity(intent);
        checkIntentJump(context);
    }

    private static void checkIntentJump(Context context) {
        if (context instanceof Activity) {
            ((Activity) context).overridePendingTransition(R.anim.push_right_in, R.anim.push_left_out);
        }
    }

    public static boolean checkURL(final Context context, String url) {
        String[] data;
        if (url.contains(HttpFinal.GOTO_LOGIN)) {
            Intent loginIntent = new Intent(context, LoginActivity.class);
            if (url.contains("=")) {
                String loginString = URLParseUtils.TruncateUrlPage(url);
                loginString = loginString.substring(loginString.indexOf("="), loginString.length());
                if (!TextUtils.isEmpty(loginString)) {
                    loginIntent.putExtra(FieldFinals.URL, loginString);
                }
            }
            context.startActivity(loginIntent);
            checkIntentJump(context);
            return true;
        }
        if (url.contains(HttpFinal.GOTO_GOODS_DETAIL)) {
            data = url.split("=");
            if (data.length >= 2) {
                startToLotteryDetail(context, Long.valueOf(data[1]));
            } else {
                return false;
            }
            return true;
        }
        if (url.contains(HttpFinal.GOTO_USER_INFO)) {
            data = url.split("=");
            if (data.length >= 2) {
                startToPerson(context, Long.valueOf(data[1]));
            } else {
                return false;
            }
            return true;
        }
        if (url.contains(HttpFinal.GOTO_EXCHANGE)) {
            if (isUserLogined(context)) {
                startToExchangeDetail(context);
            }
            return true;
        }
        if (url.contains(HttpFinal.GOTO_RECHARGE)) {
            if (isUserLogined(context)) {
                startToRecharge(context);
            }
            return true;
        }
        if (url.contains(HttpFinal.GOTO_SHOP)) {
            HomepageActivity.type = HomepageActivity.SHOP;
            startToHomePage(context);
            return true;
        }
        if (url.contains(HttpFinal.GOTO_FEEDS)) {
            HomepageActivity.type = HomepageActivity.FRIENDS;
            startToHomePage(context);
            return true;
        }
        if (url.contains(HttpFinal.GOTO_GOODS)) {
            HomepageActivity.type = HomepageActivity.LOTTERY;
            AppManager.getAppManager().finishActivity(WebViewActivity.class);
            startToHomePage(context);
            return true;
        }
        if (url.contains(HttpFinal.GOTO_WINNER)) {
            context.startActivity(new Intent(context, WinRecordActivity.class));
            return true;
        }
        if (url.contains(HttpFinal.GOTO_REGISTER)) {
            Intent registerIntent = new Intent(context, RegisterActivity.class);
            if (!TextUtils.isEmpty(PreferencesUtil.getStringPreferences(context, FieldFinals.SID))) {
                if (PreferencesUtil.getIntPreferences(GoodHappinessApplication.getContext(), FieldFinals.IS_BIND, 0) == 0) {
                    registerIntent.putExtra(FieldFinals.ACTION, RegisterActivity.REGISTER_TYPE_BIND);
                    context.startActivity(registerIntent);
                    return true;
                } else {
                    HomepageActivity.type = HomepageActivity.MINE;
                    startToHomePage(context);
                    return true;
                }
            }
            registerIntent.putExtra(FieldFinals.ACTION, RegisterActivity.REGISTER_TYPE_NORMAL);
            context.startActivity(registerIntent);
            return true;
        }
        if (url.contains(HttpFinal.GOTO_BIND_USER_MOBILE)) {
            Intent register2Intent = new Intent(context, RegisterActivity.class);
            register2Intent.putExtra(FieldFinals.ACTION, RegisterActivity.REGISTER_TYPE_BIND);
            context.startActivity(register2Intent);
            return true;
        }
        if (url.contains(HttpFinal.GOTO_WINNER)) {
            context.startActivity(new Intent(context, WinRecordActivity.class));
            return true;
        }
        if (url.contains(HttpFinal.GOTO_PERIOD)) {
            context.startActivity(new Intent(context, LotteryRecordActivity.class));
            AppManager.getAppManager().finishActivity(WebViewActivity.class);
            return true;
        }
        if (url.contains(HttpFinal.GOTO_SHARE)) {
        Map<String, String> map = URLParseUtils.URLRequest(url);
        final String action = map.get(FieldFinals.ACTION);
        if (!TextUtils.isEmpty(action)) {
            DialogFactory.createShareDialog(0, (Activity) context, null, new String[]{PreferencesUtil.getDid(), PreferencesUtil.getSid(), action, String.valueOf(PreferencesUtil.getUid())});
        }
        return true;
    }
    if (url.contains(HttpFinal.GOTO_REDBAG)) {
        Map<String, String> redBag = URLParseUtils.URLRequest(url);
        if (!TextUtils.isEmpty(redBag.get(FieldFinals.USE_STATUS))) {
            startToRedbag(context, Integer.valueOf(redBag.get(FieldFinals.USE_STATUS)));
            return true;
        }
    }
        if (url.contains(HttpFinal.GOTO_TABBAR)) {
            Map<String, String> stringMap = URLParseUtils.URLRequest(url);
            if (!TextUtils.isEmpty(stringMap.get(FieldFinals.INDEX))) {
                switch (stringMap.get(FieldFinals.INDEX)) {
                    case "1":
                        HomepageActivity.type = HomepageActivity.LOTTERY;
                        break;
                    case "2":
                        HomepageActivity.type = HomepageActivity.REVELATION;
                        break;
                    case "3":
                        HomepageActivity.type = HomepageActivity.FRIENDS;
                        break;
                    case "4":
                        HomepageActivity.type = HomepageActivity.SHOP;
                        break;
                    case "5":
                        HomepageActivity.type = HomepageActivity.MINE;
                        break;
                    default:
                        HomepageActivity.type = HomepageActivity.SHOP;
                        break;
                }
                context.startActivity(new Intent(context, HomepageActivity.class));
                return true;
            }
        }
        if (url.contains(HttpFinal.GOTO_WEIXINPAY)) {
            int firstIndex = url.indexOf("?");
            final String a = url.substring(firstIndex + 1, url.length());
            Gson mGson = new Gson();
            Result res = mGson.fromJson(new String(Base64.decode(a, 0)).toString(), new TypeToken<Result<WechatRequest>>() {
            }.getType());
            wechatPay(context, (WechatRequest) res.getData());
            return true;
        }

        return false;
    }

    private static void wechatPay(final Context context, final WechatRequest signParams) {

        if (context instanceof BaseActivity) {
            ((BaseActivity) context).mHandler.post(new Runnable() {
                @Override
                public void run() {
                    ((BaseActivity) context).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            GoodHappinessApplication.activityPayType = ExchangeRecordActivity.activityPayType;
                            PayReq request = new PayReq();
                            request.appId = signParams.getParams().getAppid();
                            request.partnerId = signParams.getParams().getPartnerid();
                            request.prepayId = signParams.getParams().getPrepayid();
                            request.packageValue = signParams.getParams().getPackageX();
                            request.nonceStr = signParams.getParams().getNonce_str();
                            request.timeStamp = signParams.getParams().getTimestamp();
                            request.sign = signParams.getParams().getSign();
                            GoodHappinessApplication.wxapi.sendReq(request);
                            PreferencesUtil.setPreferences(context, FieldFinals.PAY_RESULT_URL, signParams.getUrl());
                        }
                    });
                }
            });

        }

    }


    public static boolean isUserLogined(Context context) {
        if (!"".equals(PreferencesUtil.getStringPreferences(context, FieldFinals.SID))) {
            return true;
        } else {
            context.startActivity(new Intent(context, LoginActivity.class));
            return false;
        }
    }

    public static boolean checkHasBind(final Activity activity) {
        if (PreferencesUtil.getIntPreferences(GoodHappinessApplication.getContext(), FieldFinals.IS_BIND, 0) == 0) {
            DialogFactory.createSelectDialog(activity, "您未完善信息，是否完善信息", new OnSelectListener() {
                @Override
                public void onSelected(boolean isSelected) {
                    if (isSelected) {
                        Intent intent = new Intent(activity, RegisterActivity.class);
                        intent.putExtra(FieldFinals.ACTION, RegisterActivity.REGISTER_TYPE_BIND);
                        activity.startActivity(intent);
                    }
                }
            });
            return false;
        } else {
            return true;
        }
    }

}
























