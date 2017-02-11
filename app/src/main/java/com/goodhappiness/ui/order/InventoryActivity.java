package com.goodhappiness.ui.order;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.R;
import com.goodhappiness.adapter.CommonAdapter;
import com.goodhappiness.adapter.ViewHolder;
import com.goodhappiness.bean.AddCar;
import com.goodhappiness.bean.BaseBean;
import com.goodhappiness.bean.Car;
import com.goodhappiness.bean.CarList;
import com.goodhappiness.bean.MyTextWatcherPara;
import com.goodhappiness.bean.MyTextWatcherTag;
import com.goodhappiness.bean.Result;
import com.goodhappiness.bean.SubmitOrder;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.JudgeTextChangeListener;
import com.goodhappiness.dao.MyTextWatcher;
import com.goodhappiness.dao.OnBackKeyDownClickListener;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.ui.HomepageActivity;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.utils.AppManager;
import com.goodhappiness.utils.CarUtils;
import com.goodhappiness.utils.EditTextUtils;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.IntentUtils;
import com.goodhappiness.utils.PreferencesUtil;
import com.goodhappiness.widget.refreshlayout.BGAMeiTuanRefreshViewHolder;
import com.goodhappiness.widget.refreshlayout.BGARefreshLayout;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 清单
 */
@ContentView(R.layout.activity_inventory)
public class InventoryActivity extends BaseActivity implements BGARefreshLayout.BGARefreshLayoutDelegate {
    @ViewInject(R.id.inventory_srl)
    private BGARefreshLayout srl;
    @ViewInject(R.id.inventory_list)
    private ListView lv;
    @ViewInject(R.id.inventory_tv_delete_count)
    private TextView tv_delete;
    @ViewInject(R.id.inventory_tv_join)
    private TextView tv_join;
    @ViewInject(R.id.inventory_count)
    private TextView tv_count;
    @ViewInject(R.id.inventory_total)
    private TextView tv_total;
    @ViewInject(R.id.inventory_iv_select)
    private ImageView iv_selected;
    @ViewInject(R.id.inventory_rl_empty)
    private RelativeLayout rl_empty;
    @ViewInject(R.id.inventory_rl_commit)
    private RelativeLayout rl_commit;
    @ViewInject(R.id.inventory_rl_edit)
    private RelativeLayout rl_edit;
    private boolean isCommitShow = true;
    private boolean isAllDeleted = true;
    private CommonAdapter<Car> inventoryCommonAdapter;
    private List<Car> list = new ArrayList<>();
    private MyTextWatcher myTextWatcherX;
    private int currentPosition = 0;
    private int currentFocusPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setData() {
        initView();
        tv_title.setText(getString(R.string.inventory));
        tv_right.setText(getString(R.string.edit));
        tv_right.setVisibility(View.VISIBLE);
        srl.setDelegate(this);
        srl.setRefreshViewHolder(new BGAMeiTuanRefreshViewHolder(GoodHappinessApplication.getContext(), false));
        onBackKeyDownClickListener = new OnBackKeyDownClickListener() {
            @Override
            public void onBackKeyClick() {
                AppManager.getAppManager().finishActivity(ConfirmOrderActivity.class);
                finishActivity();
            }
        };
        tv_right.setVisibility(View.INVISIBLE);
//        rl_empty.setVisibility(View.VISIBLE);
        rl_edit.setVisibility(View.INVISIBLE);
        rl_commit.setVisibility(View.INVISIBLE);
        initAdapter();
    }

    @Override
    protected void reload() {
        initList();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getString(R.string.inventory));
        MobclickAgent.onResume(this);
        GoodHappinessApplication.isInventory = true;
        initList();
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getString(R.string.inventory));
        MobclickAgent.onPause(this);
        GoodHappinessApplication.isInventory = false;
        inventoryCommonAdapter.notifyDataSetChanged();
    }

    private void initAdapter() {
        inventoryCommonAdapter = new CommonAdapter<Car>(this, list, R.layout.layout_list_inventory) {
            @Override
            public void convert(ViewHolder helper, final Car item, final int position) {
                helper.setText(R.id.layout_list_car_name, item.getInfo().getGoods().getName());
                helper.setText(R.id.layout_list_inventory_tv_period, String.format(getString(R.string.format_what_period, item.getInfo().getPeriod())));//item.getInfo().getPeriod() + "期");
                helper.setText(R.id.layout_list_car_price, String.format(getString(R.string.format_need_people_surplus, item.getInfo().getGoods().getPrice())));//"总需" + item.getInfo().getGoods().getPrice() + "人，剩余");
                helper.setText(R.id.layout_list_car_remain, (item.getInfo().getGoods().getPrice() - item.getInfo().getExistingTimes()) + "");
                final EditText editText = helper.getView(R.id.dialog_add_to_list_cet);
                ImageView iv_add = helper.getView(R.id.layout_list_inventory_iv_add);
                iv_add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditTextUtils.editTextAddCount(editText, item.getInfo().getGoods().getBuyUnit());

                    }
                });
                ImageView iv_sub = helper.getView(R.id.layout_list_inventory_iv_sub);
                iv_sub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        EditTextUtils.editTextReduceCount(editText, item.getInfo().getGoods().getBuyUnit());//list.get(position)
                    }
                });
                if (editText.getTag() != null && ((MyTextWatcherTag) editText.getTag()).getMyTextWatcher() != null) {
                    myTextWatcherX = ((MyTextWatcherTag) editText.getTag()).getMyTextWatcher();
                }
                editText.setTag(new MyTextWatcherTag(null, position, 0));
                helper.setText(R.id.dialog_add_to_list_cet, item.getNum() + "");
                if (item.getNum() > (item.getInfo().getGoods().getPrice() - item.getInfo().getExistingTimes())) {
                    Message message = handler.obtainMessage();
                    message.obj = new MyTextWatcherPara(editText);
                    message.arg2 = position;
                    int count = (item.getInfo().getGoods().getPrice() - item.getInfo().getExistingTimes());
                    message.arg1 = count;
                    if (item.getInfo().getGoods().getBuyUnit() > 1) {
                        if (count % item.getInfo().getGoods().getBuyUnit() != 0 && count > item.getInfo().getGoods().getBuyUnit()) {
                            count = count - count % item.getInfo().getGoods().getBuyUnit();
                            message.what = 1;
                            message.arg1 = count;
                            ((MyTextWatcherPara) message.obj).setTips(String.format(getString(R.string.format_inventory_wrong_per, item.getInfo().getGoods().getBuyUnit())));//"参与人次需是" + item.getInfo().getGoods().getBuyUnit() + "的倍数");
                        }
                    }
                    handler.sendMessage(message);
                }
                if (!TextUtils.isEmpty(item.getTips())) {
                    helper.setText(R.id.layout_list_inventory_tv_count_tips, item.getTips());
                    helper.setVisibility(R.id.layout_list_inventory_tv_count_tips, View.VISIBLE);
                } else {
                    helper.setVisibility(R.id.layout_list_inventory_tv_count_tips, View.GONE);
                }
                if (item.getInfo().getGoods().getGoodsType() == 1) {
                    helper.setImageResource(R.id.layout_list_inventory_iv, R.mipmap.img_ptlq);
                } else {
                    helper.setImageResource(R.id.layout_list_inventory_iv, R.mipmap.img_cjlq);
                }
                ImageView imageView = helper.getView(R.id.layout_list_inventory_iv_select);
                View hideView = helper.getView(R.id.layout_list_inventory_v_hide);
                editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if (hasFocus) {
                            currentFocusPosition = position;
                        }
                    }
                });
                editText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getFocus((EditText) v,true);
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(v,InputMethodManager.SHOW_FORCED);
                    }
                });
//                if (position == currentPosition && isCommitShow && currentPosition == currentFocusPosition) {
//                    getFocus(editText);
//                }
                if (position == currentFocusPosition && isCommitShow) {
                    getFocus(editText,true);
                }else{
                    getFocus(editText,false);
                }
                if (!isCommitShow) {
                    imageView.setVisibility(View.VISIBLE);
                    hideView.setVisibility(View.VISIBLE);
                    hideView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    });
                } else {
                    imageView.setVisibility(View.GONE);
                    hideView.setVisibility(View.GONE);
                }
                if (item.is_select()) {
                    imageView.setImageResource(R.mipmap.ico_click_dil);
                } else {
                    imageView.setImageResource(R.mipmap.ico_click_ndil);
                }
                LinearLayout ll = helper.getView(R.id.layout_list_inventory_ll);
                ll.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!isCommitShow) {
                            Car inventory = list.get(position);
                            inventory.setIs_select(!inventory.is_select());
                            list.set(position, inventory);
                            inventoryCommonAdapter.notifyDataSetChanged();
                            countingSelected();
                        }
                    }
                });

                if (editText.getTag() != null && ((MyTextWatcherTag) editText.getTag()).getMyTextWatcher() == null) {
                    MyTextWatcher myTextWatcher = new MyTextWatcher(InventoryActivity.this, position, editText, item.getInfo().getGoods().getBuyUnit(), (item.getInfo().getGoods().getPrice() - item.getInfo().getExistingTimes()), item.getInfo().getGoods().getBuyUnit(), new JudgeTextChangeListener() {
                        @Override
                        public void textChange(EditText text, int count) {
                            addToCar(((MyTextWatcherTag) text.getTag()).getPosition(), count);
                        }
                    });
                    ((MyTextWatcherTag) editText.getTag()).setMyTextWatcher(myTextWatcher);
                    ((MyTextWatcherTag) editText.getTag()).setCode(myTextWatcher.hashCode());
                    if (myTextWatcherX != null) {
                        editText.removeTextChangedListener(myTextWatcherX);
                    }
                    editText.addTextChangedListener(myTextWatcher);
                } else {
                    if (((MyTextWatcherTag) editText.getTag()).getMyTextWatcher() != null) {//复用了
                        editText.removeTextChangedListener(myTextWatcherX);
                        MyTextWatcher myTextWatcher = new MyTextWatcher(InventoryActivity.this, position, editText, item.getInfo().getGoods().getBuyUnit(), (item.getInfo().getGoods().getPrice() - item.getInfo().getExistingTimes()), item.getInfo().getGoods().getBuyUnit(), new JudgeTextChangeListener() {
                            @Override
                            public void textChange(EditText text, int count) {
                                addToCar(((MyTextWatcherTag) text.getTag()).getPosition(), count);
                            }
                        });
                        editText.setTag(new MyTextWatcherTag(myTextWatcher, position, 0));
                        editText.addTextChangedListener(myTextWatcher);
                    }
                }

                if (!TextUtils.isEmpty(item.getMention())) {
                    helper.setVisibility(R.id.layout_list_inventory_tv_tips, View.VISIBLE);
                    helper.setText(R.id.layout_list_inventory_tv_tips, item.getMention());
                } else {
                    helper.setVisibility(R.id.layout_list_inventory_tv_tips, View.GONE);
                }
                if (!GoodHappinessApplication.isInventory) {
                    if (editText.getTag() != null) {
                        MyTextWatcherTag myTextWatcherTag = (MyTextWatcherTag) editText.getTag();
                        MyTextWatcher myTextWatcherX = myTextWatcherTag.getMyTextWatcher();
                        editText.addTextChangedListener(null);
                        myTextWatcherX.stop();
                        myTextWatcherX = null;
                        editText.setTag(null);
                    }
                }

            }
        };
        lv.setAdapter(inventoryCommonAdapter);
        lv.requestFocus();
        lv.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                lv.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
                EditText editText = (EditText) view.findViewById(R.id.dialog_add_to_list_cet);
                editText.requestFocus();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                lv.setDescendantFocusability(ViewGroup.FOCUS_BEFORE_DESCENDANTS);
            }
        });
    }

    private void getFocus(EditText editText,boolean b) {
        if(b){
            editText.setFocusable(true);
            editText.setFocusableInTouchMode(true);
            editText.requestFocus();
            Editable text = editText.getText();
            Spannable spanText = text;
            Selection.setSelection(spanText, text.length());
        }else{
            editText.setFocusable(false);
            editText.clearFocus();
            editText.setFocusableInTouchMode(false);
        }
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (msg.obj != null && msg.obj instanceof MyTextWatcherPara && msg.arg1 != 0) {
                        MyTextWatcherPara myTextWatcherPara = ((MyTextWatcherPara) msg.obj);
                        if (myTextWatcherPara.getEditText().getTag() != null) {
                            currentPosition = ((MyTextWatcherTag) myTextWatcherPara.getEditText().getTag()).getPosition();
                            addToCar(((MyTextWatcherTag) myTextWatcherPara.getEditText().getTag()).getPosition(), msg.arg1);
                        }
                    }
                    break;
                case 1:
                    if (msg.obj != null && msg.obj instanceof MyTextWatcherPara && msg.arg1 != 0) {
                        MyTextWatcherPara myTextWatcherPara = ((MyTextWatcherPara) msg.obj);
                        if (!TextUtils.isEmpty(myTextWatcherPara.getTips())) {
                            list.get(((MyTextWatcherTag) myTextWatcherPara.getEditText().getTag()).getPosition()).setTips(myTextWatcherPara.getTips());
                        }
                        if (myTextWatcherPara.getEditText().getTag() != null) {
                            currentPosition = ((MyTextWatcherTag) myTextWatcherPara.getEditText().getTag()).getPosition();
                            addToCar(((MyTextWatcherTag) myTextWatcherPara.getEditText().getTag()).getPosition(), msg.arg1);
                        }
                    }
                    break;
            }
        }
    };

    private void addToCar(final int position, final int num) {
        if (list.size() <= 0) {
            return;
        }
        RequestParams params = new RequestParams(HttpFinal.CAR_ADD);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.GID, list.get(position).getInfo().getGoods().getGid() + "");
        params.addBodyParameter(FieldFinals.PERIOD, list.get(position).getInfo().getPeriod() + "");
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.NUM, num + "");
        params.addBodyParameter(FieldFinals.ACTION, FieldFinals.UPDATE);
        HttpUtils.post(this,params, new TypeToken<Result<AddCar>>() {
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
                AddCar addCar = (AddCar) result.getData();
                if (addCar != null && !TextUtils.isEmpty(addCar.getMention())) {
                    list.get(position).getInfo().setExistingTimes(list.get(position).getInfo().getGoods().getPrice() - addCar.getNum());
                    list.get(position).setMention(addCar.getMention());
                }
                if (addCar.getNum() != 0) {
                    list.get(position).setNum(addCar.getNum());
                    countingTotal();
                    inventoryCommonAdapter.notifyDataSetChanged();
                } else {
                    initList();
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
                srl.endRefreshing();
            }
        });
    }

    private void initList() {
        if(isFinishing()){
            return;
        }
        newDialog().show();
        RequestParams params = new RequestParams(HttpFinal.CAR_LIST);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        HttpUtils.post(this,params, new TypeToken<Result<CarList>>() {
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
                final CarList carList = (CarList) result.getData();
                if (carList != null && carList.getCars() != null && carList.getCars().size() > 0) {
                    list.clear();
                    GoodHappinessApplication.isInventory = false;
                    inventoryCommonAdapter.notifyDataSetChanged();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            InventoryActivity.this.runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    list.addAll(carList.getCars());
                                    GoodHappinessApplication.isInventory = true;
//                                    tv_delete.setText("共选中" + list.size() + "件商品");
                                    inventoryCommonAdapter.notifyDataSetChanged();
                                    countingTotal();
                                    if (list.size() <= 2 && GoodHappinessApplication.isFirstToInventory) {
                                        GoodHappinessApplication.isFirstToInventory = false;
                                        srl.endRefreshing();
                                        initList();
                                        return;
                                    } else {
                                        GoodHappinessApplication.isFirstToInventory = false;
                                    }
                                    if (list.size() < 1) {
                                        tv_right.setVisibility(View.INVISIBLE);
                                        rl_empty.setVisibility(View.VISIBLE);
                                        rl_edit.setVisibility(View.INVISIBLE);
                                        rl_commit.setVisibility(View.INVISIBLE);
                                        CarUtils.set(InventoryActivity.this, 0);
                                    } else {
                                        rl_empty.setVisibility(View.GONE);
                                        tv_right.setVisibility(View.VISIBLE);
                                        if (!isCommitShow) {
                                            rl_commit.setVisibility(View.INVISIBLE);
                                            rl_edit.setVisibility(View.VISIBLE);
                                            tv_right.setText(getString(R.string.cancel));
                                        } else {
                                            rl_commit.setVisibility(View.VISIBLE);
                                            rl_edit.setVisibility(View.INVISIBLE);
                                            tv_right.setText(getString(R.string.edit));
                                        }
                                        CarUtils.set(InventoryActivity.this, list.size());
                                    }
                                    dialog.dismiss();
                                    srl.endRefreshing();
                                }
                            });
                        }
                    }, 500);
                } else {
                    rl_empty.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
                showEmptyView(true);
                srl.endRefreshing();
            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                dialog.dismiss();
                srl.endRefreshing();
            }
        });
    }


    @Event({R.id.inventory_tv_join, R.id.inventory_tv_commit, R.id.common_right_text, R.id.inventory_iv_select, R.id.inventory_tv_delete})
    private void onclick(View v) {
        switch (v.getId()) {
            case R.id.inventory_tv_join:
                HomepageActivity.type = HomepageActivity.LOTTERY;
                IntentUtils.startToHomePage(this);
                finishActivity();
                break;
            case R.id.common_right_text:
                if (isCommitShow) {
                    rl_commit.setVisibility(View.INVISIBLE);
                    rl_edit.setVisibility(View.VISIBLE);
                    tv_right.setText(getString(R.string.cancel));
                } else {
                    rl_commit.setVisibility(View.VISIBLE);
                    rl_edit.setVisibility(View.INVISIBLE);
                    tv_right.setText(getString(R.string.edit));
                }
                inventoryCommonAdapter.notifyDataSetChanged();
                isCommitShow = !isCommitShow;
                break;
            case R.id.inventory_iv_select:
                boolean flag = false;
                if (!isAllDeleted) {
                    flag = true;
                }
                int count = 0;
                for (Car inventory : list) {
                    inventory.setIs_select(flag);
                    list.set(count++, inventory);
                }
                if (isAllDeleted) {
                    iv_selected.setImageResource(R.mipmap.ico_click_ndil);
                } else {
                    iv_selected.setImageResource(R.mipmap.ico_click_dil);
                }
                inventoryCommonAdapter.notifyDataSetChanged();
                isAllDeleted = !isAllDeleted;
                countingSelected();
                break;
            case R.id.inventory_tv_delete:
                if (getDeleteCount() != 0) {
                    int deleteCount = 0;
                    String period = "";
                    ArrayList<Integer> deletePosition = new ArrayList<>();
                    for (Car inventory : list) {
                        if (inventory.is_select()) {
                            deletePosition.add(deleteCount);
                            period += inventory.getInfo().getPeriod() + ",";
                        }
                        deleteCount++;
                    }
                    period = period.substring(0, period.length() - 1);
                    deleteCar(period, deletePosition);
                }
                break;
            case R.id.inventory_tv_commit:
                if (!isUserLogined()) {
                    return;
                }
                if (list.size() <= 0)
                    return;
                newDialog().show();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                createOrder();
                            }
                        });
                    }
                }, 3000);
                break;
        }
    }

    private void createOrder() {
        String period = "";
        for (Car inventory : list) {
            period += inventory.getInfo().getPeriod() + ",";
        }
        period = period.substring(0, period.length() - 1);
        RequestParams params = new RequestParams(HttpFinal.CART_SUBMIT);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.PERIOD, period);
        PreferencesUtil.setPreferences(this,FieldFinals.ORDER_PERIOD, period);
        HttpUtils.post(this,params, new TypeToken<Result<SubmitOrder>>() {
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
                Intent intent = new Intent(InventoryActivity.this, ConfirmOrderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable(FieldFinals.SUBMIT_ORDER, (SubmitOrder) result.getData());
                intent.putExtras(bundle);
                startTheActivity(intent);
                finish();
            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(Callback.CancelledException cex) {

            }

            @Override
            public void onFinished() {
                dialog.dismiss();
            }
        });
    }

    private void deleteCar(String period, final ArrayList<Integer> deletePosition) {
        RequestParams params = new RequestParams(HttpFinal.CAR_DELETE);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.PERIOD, period);
        HttpUtils.post(this,params, new TypeToken<Result<BaseBean>>() {
        }.getType(), new OnHttpRequest() {
            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

            }

            @Override
            public void onStarted() {
                newDialog().show();
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onSuccess(Result result) {
                //删除成功后的操作
                Collections.reverse(deletePosition);
                for (int i : deletePosition) {
                    list.remove(i);
                }
                countingSelected();
                countingTotal();
                CarUtils.set(InventoryActivity.this, list.size());
                inventoryCommonAdapter.notifyDataSetChanged();
                if (list.size() == 0) {
                    finishActivity();
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
                dialog.dismiss();
            }
        });
    }

    /**
     * 获取删除的个数
     *
     * @return
     */
    private int getDeleteCount() {
        int deleteCount = 0;
        for (Car inventory : list) {
            if (inventory.is_select()) {
                deleteCount++;
            }
        }
        return deleteCount;
    }

    /**
     * 计算商品数和总价
     */
    private void countingTotal() {
        tv_count.setText(list.size() + "");
        int total = 0;
        for (Car car : list) {
            total += car.getNum();
        }
        tv_total.setText(total + "");
    }

    /**
     * 计算删除数量
     */
    private void countingSelected() {
        int deleteCount = getDeleteCount();
        tv_delete.setText("共选中" + deleteCount + "件商品");
        if (deleteCount == list.size()) {
            isAllDeleted = true;
            iv_selected.setImageResource(R.mipmap.ico_click_dil);
        } else {
            isAllDeleted = false;
            iv_selected.setImageResource(R.mipmap.ico_click_ndil);
        }
    }

    @Override
    public void onBGARefreshLayoutBeginRefreshing(BGARefreshLayout refreshLayout) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initList();
                    }
                });
            }
        }, 2000);
    }

    @Override
    public boolean onBGARefreshLayoutBeginLoadingMore(BGARefreshLayout refreshLayout) {
        return false;
    }
}
