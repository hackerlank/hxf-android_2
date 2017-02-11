package com.goodhappiness.dao;

import android.app.Activity;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.bean.MyTextWatcherPara;
import com.goodhappiness.bean.MyTextWatcherTag;
import com.goodhappiness.ui.HomepageActivity;
import com.goodhappiness.ui.lottery.LotteryDetailActivity;
import com.goodhappiness.ui.order.InventoryActivity;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 电脑 on 2016/4/7.
 */
public class MyTextWatcher implements TextWatcher {
    private EditText editText;
    private int minCount;
    private int count;
    private int buyUnit;
    private int maxCount;
    private int lastInt = 0;
    private Timer timer = new Timer();
    private TimerTask timerTask;
    private Activity activity;
    private int position;
    private int arg1 ;

    public MyTextWatcher() {
    }

    public MyTextWatcher(final Activity activity, final int position, final EditText editText, final int minCount, final int maxCount, final int buyUnit, final JudgeTextChangeListener judgeTextChangeListener) {
        this.activity = activity;
        this.position = position;
        this.editText = editText;
        this.minCount = minCount;
        this.maxCount = maxCount;
        this.buyUnit = buyUnit;
        this.lastInt = Integer.valueOf(editText.getText().toString());
        Log.e("e_", "position:" + position);
        if (judgeTextChangeListener != null) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (!GoodHappinessApplication.isInventory) {
                        stop();
                        return;
                    }
                    if (editText.getTag() != null && ((MyTextWatcherTag) editText.getTag()).getMyTextWatcher() == null) {
                        stop();
                        return;
                    }
                    if (editText.getTag() != null && ((MyTextWatcherTag) editText.getTag()).getMyTextWatcher() != null) {
                        if (((MyTextWatcherTag) editText.getTag()).getMyTextWatcher().hashCode() != MyTextWatcher.this.hashCode()) {
                            stop();
                            return;
                        }
                    }
                    if (editText.getText().toString().length() <= 0) {
                        return;
                    }
                    int newInt = Integer.valueOf(editText.getText().toString());
                    if (newInt == lastInt) {
                        return;
                    } else {
                        lastInt = newInt;
                        checkTime(newInt);
                    }
                }
            };
            timer.schedule(timerTask, 0, 2000);
        }
        if (activity instanceof LotteryDetailActivity||activity instanceof HomepageActivity) {
            timerTask = new TimerTask() {
                @Override
                public void run() {
                    if (editText.getTag() != null && ((int) editText.getTag()) == 0) {
                        stop();
                        return;
                    }
                    if (editText.getText().toString().length() <= 0)
                        return;
                    int newInt = Integer.valueOf(editText.getText().toString());
                    if (newInt == lastInt) {
                        return;
                    } else {
                        lastInt = newInt;
                        if(activity instanceof LotteryDetailActivity){
                            checkTime(newInt);
                        }else if(activity instanceof HomepageActivity){
                            arg1 = 0;
                            if (newInt > maxCount) {
                                arg1 = count = maxCount;
                            }
                            if (count < minCount) {
                                arg1 = count = minCount;
                            }
                            arg1 = count;
                            if (buyUnit > 1) {
                                if (count % buyUnit != 0 && count > buyUnit) {
                                    count = count - count % buyUnit;
                                    arg1 = count;
                                } else {

                                }
                            }
                            activity.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    editText.setText(arg1+"");
                                    Editable text = editText.getText();
                                    Spannable spanText = text;
                                    Selection.setSelection(spanText, text.length());
                                }
                            });
                        }
                    }
                }
            };
            timer.schedule(timerTask, 0, 3000);
        }
    }

    private void checkTime(int count) {
        Log.e("e_", "position:" + position + "---count" + count);
        if (activity instanceof LotteryDetailActivity) {
            Message message = ((LotteryDetailActivity) activity).handler.obtainMessage();
            message.obj = new MyTextWatcherPara(editText);
            message.arg2 = position;
            message.what = 0;
            if (count > maxCount) {
                message.arg1 = count = maxCount;
            }
            if (count < minCount) {
                message.arg1 = count = minCount;
            }
            message.arg1 = count;
            if (buyUnit > 1) {
                if (count % buyUnit != 0 && count > buyUnit) {
                    count = count - count % buyUnit;
                    message.arg1 = count;
                } else {

                }
            }
            ((LotteryDetailActivity) activity).handler.sendMessage(message);
            return;
        }
        Message message = ((InventoryActivity) activity).handler.obtainMessage();
        message.obj = new MyTextWatcherPara(editText);
        message.arg2 = position;
        if (count > maxCount) {
            message.what = 0;
            message.arg1 = count = maxCount;
        }
        if (count < minCount) {
            message.what = 0;
            message.arg1 = count = minCount;
        }
        message.arg1 = count;
        if (buyUnit > 1) {
            if (count % buyUnit != 0 && count > buyUnit) {
                count = count - count % buyUnit;
                message.what = 1;
                message.arg1 = count;
                ((MyTextWatcherPara) message.obj).setTips("参与人次需是" + buyUnit + "的倍数");
            } else {

            }
        }
        ((InventoryActivity) activity).handler.sendMessage(message);
    }

    public void stop() {
        if (timer != null && timerTask != null) {
            if (timerTask != null)
            timerTask.cancel();
            if (timer != null)
                timer.cancel();
            if (timer != null)
                timer.purge();
            timer = null;
            timerTask = null;
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }


    @Override
    public void afterTextChanged(Editable s) {
        if (s.toString().length() <= 0)
            return;
        count = Integer.valueOf(s.toString()) > 0 ? Integer.valueOf(s.toString()) : 0;
        Log.e("e_afterTextChanged", "position:" + position + "---count" + count + "---hashcode" + editText.hashCode() + "---thiscode" + MyTextWatcher.this.hashCode());
    }

    public int getMinCount() {
        return minCount;
    }

    public void setMinCount(int minCount) {
        this.minCount = minCount;
    }

    public int getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(int maxCount) {
        this.maxCount = maxCount;
        afterTextChanged(editText.getText());
    }
}