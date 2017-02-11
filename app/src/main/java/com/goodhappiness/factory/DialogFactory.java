package com.goodhappiness.factory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.Task.GetShareInfoTask;
import com.goodhappiness.adapter.CommonAdapter;
import com.goodhappiness.adapter.ViewHolder;
import com.goodhappiness.bean.AddCar;
import com.goodhappiness.bean.BasePeriod;
import com.goodhappiness.bean.CarNum;
import com.goodhappiness.bean.ChargeResult;
import com.goodhappiness.bean.PushWinner;
import com.goodhappiness.bean.Redbag;
import com.goodhappiness.bean.Result;
import com.goodhappiness.bean.ShareItem;
import com.goodhappiness.bean.SubmitOrder;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.CheckoutResultListener;
import com.goodhappiness.dao.MyTextWatcher;
import com.goodhappiness.dao.OnAddCarListener;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.dao.OnSelectChargeCountListener;
import com.goodhappiness.dao.OnSelectListener;
import com.goodhappiness.dao.OnSendFlowerListener;
import com.goodhappiness.dao.OnShareClickListener;
import com.goodhappiness.ui.HomepageActivity;
import com.goodhappiness.ui.dialog.ChargeDialog;
import com.goodhappiness.ui.dialog.CheckoutDialog;
import com.goodhappiness.ui.dialog.ChooseDialog;
import com.goodhappiness.ui.dialog.CommentOperationDialog;
import com.goodhappiness.ui.dialog.DefaultDialog;
import com.goodhappiness.ui.dialog.ReceiveRedbagDialog;
import com.goodhappiness.ui.dialog.SendFlowerSuccessDialog;
import com.goodhappiness.ui.lottery.LotteryDetailActivity;
import com.goodhappiness.ui.order.ConfirmOrderActivity;
import com.goodhappiness.ui.personal.WinRecordActivity;
import com.goodhappiness.ui.social.picture.EditPicV5Activity;
import com.goodhappiness.ui.social.video.VideoRecordActivity;
import com.goodhappiness.utils.AlxGifHelper;
import com.goodhappiness.utils.EditTextUtils;
import com.goodhappiness.utils.FileUtils;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.IntentUtils;
import com.goodhappiness.utils.PreferencesUtil;
import com.goodhappiness.widget.WheelView;
import com.goodhappiness.wxapi.Constants;
import com.google.gson.reflect.TypeToken;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.umeng.socialize.bean.SHARE_MEDIA;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

/**
 * Created by Administrator on 2016/2/22.
 */
public class DialogFactory {

    public static String[] Sex = {"男", "女"};

    public static void createSexDialog(Context context, final TextView textView) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setContentView(R.layout.whellview_dialog);
        View view = window.getDecorView();
        final WheelView wva = (WheelView) view.findViewById(R.id.whellview);
        TextView cancel = (TextView) view.findViewById(R.id.whellview_cancel);
        TextView title = (TextView) view.findViewById(R.id.whellview_title);
        TextView positive = (TextView) view.findViewById(R.id.whellview_sure);
        wva.setOffset(1);
        title.setText("性别");
        wva.setItems(Arrays.asList(Sex));
        cancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        positive.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                textView.setText(wva.getCurrentString());
            }
        });
    }

    public static void createPublishDialog(final Context context) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.TOP);
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setContentView(R.layout.dialog_publish);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 0.8f;
        window.setAttributes(lp);
        View view = window.getDecorView();
        view.findViewById(R.id.dialog_publish_video).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IntentUtils.isUserLogined(context)) {
                    Intent intent = new Intent(context, VideoRecordActivity.class);
                    context.startActivity(intent);
                    alertDialog.dismiss();
//                        }
                }
            }
        });
        view.findViewById(R.id.dialog_publish_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (IntentUtils.isUserLogined(context)) {
                    context.startActivity(new Intent(context, EditPicV5Activity.class));
                    alertDialog.dismiss();
                }
            }
        });
        view.findViewById(R.id.dialog_publish_v).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
    public static void createPushWinnerDialog(final Context context,PushWinner pushWinner) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.show();
        alertDialog.setCanceledOnTouchOutside(true);
        Window window = alertDialog.getWindow();
//        window.setGravity(Gravity.TOP);
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        window.setContentView(R.layout.dialog_push_winner);
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.alpha = 1.0f;
        window.setAttributes(lp);
        View view = window.getDecorView();
        File file = new File(FileUtils.getStorageDirectory()+"/normal_coupon.gif");
        try {
            FileUtils.inputstreamtofile(context.getAssets().open(pushWinner.getGoodsType()==1?"normal_coupon.gif":"super_coupon.gif"),file);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        Glide.with(context).load(file).asGif().diskCacheStrategy(DiskCacheStrategy.SOURCE).into((ImageView) view.findViewById(R.id.dialog_push_winner_gif));
        if(!TextUtils.isEmpty(pushWinner.getGoodsName())){
            ((TextView)view.findViewById(R.id.dialog_push_winner_tv_name)).setText(pushWinner.getGoodsName());
        }
        AlxGifHelper.displayImage(file, (GifImageView) view.findViewById(R.id.dialog_push_winner_gif), (int) (GoodHappinessApplication.perHeight*550));
        view.findViewById(R.id.dialog_push_winner_tv_go_detail).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, WinRecordActivity.class));
                alertDialog.dismiss();
            }
        });
        view.findViewById(R.id.dialog_push_winner_iv_use).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HomepageActivity.type = HomepageActivity.SHOP;
                IntentUtils.startToHomePage(context);
                alertDialog.dismiss();
            }
        });
        view.findViewById(R.id.dialog_push_winner_iv_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    public static void createSendFlowerDialog(final Activity context, final OnSendFlowerListener listener) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.show();
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        alertDialog.setCanceledOnTouchOutside(true);
        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setContentView(R.layout.dialog_send_flower);
        View view = window.getDecorView();
        TextView tv_confirm = (TextView) view.findViewById(R.id.dialog_add_to_list_tv_confirm);
        final TextView tv_flowerCount = (TextView) view.findViewById(R.id.tv_flower_count);
        final TextView tv_sumCount = (TextView) view.findViewById(R.id.tv_sum_count);
        final EditText editText = (EditText) view.findViewById(R.id.dialog_add_to_list_cet);
        ImageView iv_reduce = (ImageView) view.findViewById(R.id.dialog_add_to_list_iv_reduce);
        ImageView iv_add = (ImageView) view.findViewById(R.id.dialog_add_to_list_iv_add);
        GridView gridView = (GridView) view.findViewById(R.id.send_gv);
        final List<ChargeResult> list = new ArrayList<>();
        list.add(new ChargeResult(1, 5));
        list.add(new ChargeResult(0, 20));
        list.add(new ChargeResult(0, 50));
        list.add(new ChargeResult(0, 100));
        final CommonAdapter<ChargeResult> adapter = new CommonAdapter<ChargeResult>(context, list, R.layout.layout_grid_charge_count) {

            @Override
            public void convert(ViewHolder helper, ChargeResult item, int position) {
                helper.setText(R.id.grid_charge_count, item.getFee() + "");
                TextView textView = helper.getView(R.id.grid_charge_count);
                if (item.getPayStatus() == 0) {
                    textView.setBackgroundResource(R.drawable.shape_for_charge_count);
                    textView.setTextColor(context.getResources().getColor(R.color.gray_999_text));
                } else {
                    textView.setBackgroundResource(R.drawable.shape_for_charge_count_click);
                    textView.setTextColor(context.getResources().getColor(R.color.join_red));
                }
            }
        };
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < list.size(); i++) {
                    if (position == i) {
                        list.get(i).setPayStatus(1);
                    } else {
                        list.get(i).setPayStatus(0);
                    }
                }
                adapter.notifyDataSetChanged();
                editText.setText(list.get(position).getFee() + "");
                tv_flowerCount.setText(list.get(position).getFee() + "");
                tv_sumCount.setText(list.get(position).getFee() + "");
            }
        });

        iv_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = EditTextUtils.editTextReduceCount(editText, 1);
                tv_flowerCount.setText(count + "");
                tv_sumCount.setText(count + "");
            }
        });
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int count = EditTextUtils.editTextAddCount(editText, 1);
                tv_flowerCount.setText(count + "");
                tv_sumCount.setText(count + "");
            }
        });
        ImageView iv_cancel = (ImageView) view.findViewById(R.id.dialog_add_to_list_iv_cancel);
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (editText.getText().toString().length() >= 8) {
                    Toast.makeText(context, "输入金额过大", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (editText.getText().toString().equals("0") || editText.getText().toString().length() == 0) {
                    Toast.makeText(context, "请输入正确金额", Toast.LENGTH_SHORT).show();
                    return;
                }
                listener.onclick(Integer.valueOf(editText.getText().toString()));
            }
        });
        editText.setText("5");
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    tv_flowerCount.setText(s);
                    tv_sumCount.setText(s);
                } else {
                    tv_flowerCount.setText("0");
                    tv_sumCount.setText("0");
                }
            }
        });
        tv_flowerCount.setText("5");
        tv_sumCount.setText("5");
    }


    public static EditText createAddToListDialog(final Activity context, final int intent_type, final BasePeriod basePeriod, final OnAddCarListener onAddCarListener, DialogInterface.OnCancelListener onCancelListener) {
        final int buyUnit = basePeriod.getGoods().getBuyUnit();
        final int min = ((buyUnit == 1) ? 1 : buyUnit);
        final int max = basePeriod.getGoods().getPrice() - basePeriod.getExistingTimes();
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        alertDialog.show();
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        alertDialog.setCanceledOnTouchOutside(true);
        alertDialog.setOnCancelListener(onCancelListener);
        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setContentView(R.layout.dialog_add_to_list);
        View view = window.getDecorView();
        TextView tv_confirm = (TextView) view.findViewById(R.id.dialog_add_to_list_tv_confirm);
        if (intent_type == LotteryDetailActivity.LOTTERY_INTENT_ADD) {

        } else if (intent_type == LotteryDetailActivity.LOTTERY_INTENT_BUY) {
            tv_confirm.setTextColor(context.getResources().getColor(R.color.black_333_text));
            tv_confirm.setBackgroundColor(context.getResources().getColor(R.color.theme_color));
            tv_confirm.setText(context.getResources().getString(R.string.lottery_title));
        }
        final EditText editText = (EditText) view.findViewById(R.id.dialog_add_to_list_cet);
        editText.setTag(1);
        editText.setText(buyUnit + "");
        editText.addTextChangedListener(new MyTextWatcher(context, 0, editText, min, max, buyUnit, null));
        ImageView iv_reduce = (ImageView) view.findViewById(R.id.dialog_add_to_list_iv_reduce);
        ImageView iv_add = (ImageView) view.findViewById(R.id.dialog_add_to_list_iv_add);
        iv_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTextUtils.editTextReduceCount(editText, buyUnit);
            }
        });
        iv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditTextUtils.editTextAddCount(editText, buyUnit);
            }
        });
        ImageView iv_cancel = (ImageView) view.findViewById(R.id.dialog_add_to_list_iv_cancel);
        iv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.cancel();
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addToCar(context, basePeriod, editText.getText().toString(), intent_type, alertDialog, onAddCarListener);
            }
        });
        final ArrayList<CarNum> integers = new ArrayList<>();
        if(buyUnit!=10){
            integers.add(new CarNum(5,false));
            integers.add(new CarNum(10,false));
            integers.add(new CarNum(15,false));
            integers.add(new CarNum(20,false));
        }else{
            integers.add(new CarNum(10,false));
            integers.add(new CarNum(20,false));
            integers.add(new CarNum(30,false));
            integers.add(new CarNum(40,false));
        }
        GridView gridView = (GridView) view.findViewById(R.id.dialog_add_to_list_gv_count);
        final CommonAdapter<CarNum> adapter = new CommonAdapter<CarNum>(context,integers,R.layout.grid_add_count_item) {
            @Override
            public void convert(ViewHolder helper, CarNum item, int position) {
                if(item.getCart_num()<=max){
                    helper.getView(R.id.grid_add_count_tv).setBackgroundResource(R.drawable.shape_for_back_thin_stroke);
                }else{
                    helper.getView(R.id.grid_add_count_tv).setBackgroundResource(R.drawable.shape_for_back_thin_stroke_gary);
                }
                helper.setText(R.id.grid_add_count_tv,item.getCart_num()+"");
                if(item.isSelect()){
                    helper.setTextColor(R.id.grid_add_count_tv,context.getResources().getColor(R.color.join_red));
                    helper.getView(R.id.grid_add_count_tv).setBackgroundResource(R.drawable.shape_for_red_thin_stroke);
                }else{
                    helper.setTextColor(R.id.grid_add_count_tv,context.getResources().getColor(R.color.black_333_text));
                }
            }
        };
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(adapter.getItem(position).getCart_num()<=max){
                    editText.setText(adapter.getItem(position).getCart_num()+"");
                    for(int i =0;i<adapter.getCount();i++){
                        integers.get(i).setSelect(false);
                    }
                    integers.get(position).setSelect(true);
                    adapter.notifyDataSetChanged();
                }
            }
        });
        return editText;
    }

    private static void addToCar(final Context context,final BasePeriod basePeriod, String count, final int action, final AlertDialog alertDialog, final OnAddCarListener onAddCarListener) {
        RequestParams params = new RequestParams(HttpFinal.CAR_ADD);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, PreferencesUtil.getStringPreferences(context, FieldFinals.DEVICE_IDENTIFIER));
        params.addBodyParameter(FieldFinals.GID, basePeriod.getGoods().getGid() + "");
        params.addBodyParameter(FieldFinals.PERIOD, basePeriod.getPeriod() + "");
        params.addBodyParameter(FieldFinals.SID, PreferencesUtil.getStringPreferences(context, FieldFinals.SID));
        params.addBodyParameter(FieldFinals.NUM, count);
        params.addBodyParameter(FieldFinals.ACTION, FieldFinals.INSERT);
        params.addBodyParameter(FieldFinals.OPTION, "del");
        HttpUtils.post(context, params, new TypeToken<Result<AddCar>>() {
        }.getType(), new OnHttpRequest() {
            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

            }

            @Override
            public void onStarted() {

            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onSuccess(Result result) {
                createOrder(context,String.valueOf(basePeriod.getPeriod()),alertDialog,onAddCarListener);
//                final AddCar carNum = (AddCar) result.getData();
//                onAddCarListener.onAddFinish(true, carNum.getCart_num());
//                CarUtils.set(context, carNum.getCart_num());
//                if (action == LotteryDetailActivity.LOTTERY_INTENT_ADD) {
//
//                } else if (action == LotteryDetailActivity.LOTTERY_INTENT_BUY) {
//                    context.startActivity(new Intent(context, InventoryActivity.class));
//                }
//                alertDialog.dismiss();
//                if (carNum.getNum() != 0)
//                    Toast.makeText(context, R.string.add_success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                onAddCarListener.onAddFinish(false, 0);
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {
            }

            @Override
            public void onFinished() {
            }
        });
    }

    private static void createOrder(final Context context, String period, final AlertDialog alertDialog, final OnAddCarListener onAddCarListener) {
        RequestParams params = new RequestParams(HttpFinal.CART_SUBMIT);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, PreferencesUtil.getDid());
        params.addBodyParameter(FieldFinals.SID, PreferencesUtil.getSid());
        params.addBodyParameter(FieldFinals.PERIOD, period);
        PreferencesUtil.setPreferences(context,FieldFinals.ORDER_PERIOD, period);
        HttpUtils.post(context,params, new TypeToken<Result<SubmitOrder>>() {
        }.getType(), new OnHttpRequest() {
            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

            }

            @Override
            public void onStarted() {
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onSuccess(Result result) {
                Intent intent = new Intent(context, ConfirmOrderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(FieldFinals.SUBMIT_ORDER, (SubmitOrder) result.getData());
                intent.putExtras(bundle);
                context.startActivity(intent);
                alertDialog.dismiss();
                onAddCarListener.onAddFinish(true, 0);
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                alertDialog.dismiss();
                onAddCarListener.onAddFinish(false, 0);
            }
        });
    }

    public static Dialog createShareDialog(int mode, final Activity context, final OnShareClickListener onShareClickListener, final String[] s) {
        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.show();
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        alertDialog.setCanceledOnTouchOutside(true);
        Window window = alertDialog.getWindow();
        window.setGravity(Gravity.BOTTOM);
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        window.setContentView(R.layout.dialog_share);
        View view = window.getDecorView();
        GridView gridView = (GridView) view.findViewById(R.id.dialog_share_gv);
        final List<ShareItem> items = new ArrayList<>();
        items.add(new ShareItem(R.mipmap.ico_share_qq, R.string.qq, 1));
        items.add(new ShareItem(R.mipmap.ico_qzone, R.string.qzone, 2));
        items.add(new ShareItem(R.mipmap.ico_share_wechat, R.string.wechat, 3));
        items.add(new ShareItem(R.mipmap.ico_share_friends, R.string.moments, 4));
//        items.add(new ShareItem(R.mipmap.ico_share_sina, R.string.sina, 5));
        if (mode == 1) {
            items.add(new ShareItem(R.mipmap.ico_delete_publish, R.string.delete, 6));
        }
        if (mode == 2) {
            items.add(new ShareItem(R.mipmap.ico_report, R.string.report, 6));
        }
        CommonAdapter<ShareItem> commonAdapter = new CommonAdapter<ShareItem>(context, items, R.layout.layout_grid_share) {
            @Override
            public void convert(ViewHolder helper, ShareItem item, int position) {
                helper.setImageResource(R.id.layout_grid_share_iv, item.getIconResources());
                helper.setText(R.id.layout_grid_share_tv, item.getSharePlatform());
                if (item.getPosition() % 4 == 0 || position == items.size()) {
                    helper.setVisibility(R.id.layout_grid_share_v, View.GONE);
                } else {
                    helper.setVisibility(R.id.layout_grid_share_v, View.VISIBLE);
                }
            }
        };
        gridView.setAdapter(commonAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position2, long id) {
                GetShareInfoTask getShareInfoTask;
                switch (items.get(position2).getSharePlatform()) {
                    case R.string.qq://QQ
                        getShareInfoTask = new GetShareInfoTask(context, SHARE_MEDIA.QQ);
                        getShareInfoTask.execute(s);
                        break;
                    case R.string.qzone://QZone
                        getShareInfoTask = new GetShareInfoTask(context, SHARE_MEDIA.QZONE);
                        getShareInfoTask.execute(s);
                        break;
                    case R.string.wechat://WeChat
                        getShareInfoTask = new GetShareInfoTask(context, SHARE_MEDIA.WEIXIN);
                        getShareInfoTask.execute(s);
                        break;
                    case R.string.moments://Friends
                        getShareInfoTask = new GetShareInfoTask(context, SHARE_MEDIA.WEIXIN_CIRCLE);
                        getShareInfoTask.execute(s);
                        break;
                    case R.string.sina://Sina
                        if (!WeiboShareSDK.createWeiboAPI(context, Constants.SINA_APP_KEY).isWeiboAppInstalled()) {
//                            new Handler().postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//                                    context.runOnUiThread(new Runnable() {
//                                        @Override
//                                        public void run() {
//                                            Toast.makeText(GoodHappinessApplication.getContext(), R.string.install_sina, Toast.LENGTH_LONG);
//                                        }
//                                    });
//                                }
//                            }, 100);
                            Toast.makeText(GoodHappinessApplication.getContext(), R.string.install_sina, Toast.LENGTH_LONG).show();
                        } else {
                            getShareInfoTask = new GetShareInfoTask(context, SHARE_MEDIA.SINA);
                            getShareInfoTask.execute(s);
                        }
                        break;
                    case R.string.delete://delete
                    case R.string.report:
                        if (onShareClickListener != null) {
                            onShareClickListener.onclick(items.get(position2).getSharePlatform());
                        }
                        break;
                }
                alertDialog.dismiss();
            }
        });
        view.findViewById(R.id.dialog_share_tv).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        return alertDialog;
    }


//    public static Dialog createDistrictDialog(Context context,DistrictDialog.OnDistrictSelectedListener listener) {
//        DistrictDialog dialog = new DistrictDialog(context,R.style.dialog,listener);
//        dialog.setCancelable(true);
//        Window dialogWindow = dialog.getWindow();// 设置Dialog的位置
//        dialogWindow.setWindowAnimations(R.style.dialogWindowAnim);
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        dialogWindow.setGravity(Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL | Gravity.FILL_HORIZONTAL);// 垂直居中和水平居中
//        dialogWindow.setAttributes(lp);
//        dialog.show();
//        return dialog;
//    }

    public static Dialog createDatePickerDialog(Activity activity, DatePickerDialog.OnDateSetListener dateListener) {
        Calendar calendar = Calendar.getInstance();
        Dialog dialog = new DatePickerDialog(activity, dateListener,
                calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
        return dialog;
    }

    public static Dialog createTimePickerDialog(Activity activity, TimePickerDialog.OnTimeSetListener timeListener) {
        Calendar calendar = Calendar.getInstance();
        Dialog dialog = new TimePickerDialog(activity, timeListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE), true);
        dialog.show();
        return dialog;
    }

//    public static Dialog createCommonDialog(Context context, int dialogType, UpdateDialog.onSubmitListener listener) {
//        UpdateDialog dialog = new UpdateDialog(context, R.style.dialog, dialogType);
//        dialog.setCancelable(false);
//        Window dialogWindow = dialog.getWindow();// 设置Dialog的位置
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        dialogWindow.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);// 垂直居中和水平居中
//        dialog.setOnSubmitListener(listener);
//        dialogWindow.setAttributes(lp);
//        dialog.show();
//        return dialog;
//    }
//

    public static Dialog createDefaultDialog(Context context, String msg) {
        return createDefaultDialog(context, msg, null);
    }

    public static Dialog createDefaultDialog(Context context, String msg, DialogInterface.OnCancelListener cancelListener) {
        DefaultDialog dialog = new DefaultDialog(context, R.style.dialog, msg, cancelListener);
        dialog.setCanceledOnTouchOutside(false);
        Window dialogWindow = dialog.getWindow();// 设置Dialog的位置
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);// 垂直居中和水平居中
        dialogWindow.setAttributes(lp);
        try {
            dialog.show();
        } catch (Exception e) {
        }
        return dialog;
    }

    public static Dialog createReceiveRedbagDialog(Context context, List<Redbag> redbags) {
        ReceiveRedbagDialog dialog = new ReceiveRedbagDialog(context, R.style.dialog, redbags);
        dialog.setCanceledOnTouchOutside(false);
        Window dialogWindow = dialog.getWindow();// 设置Dialog的位置
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);// 垂直居中和水平居中
        dialogWindow.setAttributes(lp);
        dialog.show();
        return dialog;
    }

    public static Dialog createCheckoutDialog(Context context, CheckoutResultListener checkoutResultListener) {
        CheckoutDialog dialog = new CheckoutDialog(context, R.style.dialog, checkoutResultListener);
        dialog.setCanceledOnTouchOutside(false);
        Window dialogWindow = dialog.getWindow();// 设置Dialog的位置
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);// 垂直居中和水平居中
        dialogWindow.setAttributes(lp);
        dialog.show();
        return dialog;
    }

    public static Dialog createFlowerSendDialog(Context context) {
        SendFlowerSuccessDialog dialog = new SendFlowerSuccessDialog(context, R.style.dialog);
        dialog.setCanceledOnTouchOutside(false);
        Window dialogWindow = dialog.getWindow();// 设置Dialog的位置
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);// 垂直居中和水平居中
        dialogWindow.setAttributes(lp);
        dialog.show();
        return dialog;
    }

    public static Dialog createCommentOperationDialog(Context context, AdapterView.OnItemClickListener onItemClickListener, boolean hasDelete) {
        CommentOperationDialog dialog = new CommentOperationDialog(context, R.style.dialog, onItemClickListener, hasDelete);
        dialog.setCanceledOnTouchOutside(true);
        Window dialogWindow = dialog.getWindow();// 设置Dialog的位置
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);// 垂直居中和水平居中
        dialogWindow.setAttributes(lp);
        dialog.show();
        return dialog;
    }

    public static Dialog createChargeDialog(Context context, OnSelectChargeCountListener onSelectChargeCountListener, int count) {
        ChargeDialog dialog = new ChargeDialog(context, R.style.dialog, onSelectChargeCountListener, count);
        dialog.setCancelable(false);
        Window dialogWindow = dialog.getWindow();// 设置Dialog的位置
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);// 垂直居中和水平居中
        dialogWindow.setAttributes(lp);
        dialog.show();
        return dialog;
    }

    public static Dialog createSelectDialog(Context context, String msg, String content, OnSelectListener onSelectListener) {
        ChooseDialog dialog = new ChooseDialog(context, R.style.dialog, msg, content, onSelectListener, 0);
        dialog.setCancelable(false);
        Window dialogWindow = dialog.getWindow();// 设置Dialog的位置
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);// 垂直居中和水平居中
        dialogWindow.setAttributes(lp);
        dialog.show();
        return dialog;
    }

    public static Dialog createSelectDialog(Context context, String msg, OnSelectListener onSelectListener) {
        ChooseDialog dialog = new ChooseDialog(context, R.style.dialog, msg, "", onSelectListener, 0);
        dialog.setCancelable(false);
        Window dialogWindow = dialog.getWindow();// 设置Dialog的位置
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);// 垂直居中和水平居中
        dialogWindow.setAttributes(lp);
        dialog.show();
        return dialog;
    }

    //退出将清除所有效果
    public static Dialog createChooseDialog(Context context, String msg, OnSelectListener onSelectListener) {
        ChooseDialog dialog = new ChooseDialog(context, R.style.dialog, "", msg, onSelectListener, R.drawable.shape_for_black_trans);
        dialog.setCancelable(false);
        Window dialogWindow = dialog.getWindow();// 设置Dialog的位置
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);// 垂直居中和水平居中
        dialogWindow.setAttributes(lp);
        dialog.show();
        return dialog;
    }
//    public static Dialog createUpdateDialog(Context context, int dialogType, UpdateDialog.onSubmitListener listener, String msg, boolean isForceUpdate) {
//        UpdateDialog dialog = new UpdateDialog(context, R.style.dialog, dialogType, msg, isForceUpdate);
//        dialog.setCancelable(false);
//        Window dialogWindow = dialog.getWindow();// 设置Dialog的位置
//        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
//        dialogWindow.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);// 垂直居中和水平居中
//        dialog.setOnSubmitListener(listener);
//        dialogWindow.setAttributes(lp);
//        dialog.show();
//        return dialog;
//    }
}
