package com.goodhappiness.ui.personal;

import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.goodhappiness.R;
import com.goodhappiness.bean.Address;
import com.goodhappiness.bean.Result;
import com.goodhappiness.bean.address.City;
import com.goodhappiness.bean.address.County;
import com.goodhappiness.bean.address.Province;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.HttpFinal;
import com.goodhappiness.dao.OnHttpRequest;
import com.goodhappiness.dao.OnSelectListener;
import com.goodhappiness.factory.DialogFactory;
import com.goodhappiness.ui.base.BaseActivity;
import com.goodhappiness.ui.dialog.CityPickerDialog;
import com.goodhappiness.utils.HttpUtils;
import com.goodhappiness.utils.PreferencesUtil;
import com.goodhappiness.utils.Util;
import com.goodhappiness.widget.ClearEditText;
import com.goodhappiness.widget.UISwitchButton;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

@ContentView(R.layout.activity_add_address)
public class AddAddressActivity extends BaseActivity {
    @ViewInject(R.id.add_address_tv_delete)
    private TextView tv_delete;
    @ViewInject(R.id.add_address_district)
    private TextView tv_address;
    @ViewInject(R.id.add_address_receiver)
    private ClearEditText et_name;
    @ViewInject(R.id.add_address_phone)
    private ClearEditText et_phone;
    @ViewInject(R.id.add_address_detail_address)
    private ClearEditText et_detailAddress;
    @ViewInject(R.id.add_address_sb)
    private UISwitchButton sb;
    @ViewInject(R.id.add_address_ll_delete)
    private LinearLayout ll_delete;
    private ArrayList<Province> provinces = new ArrayList<>();
    private String addressStr = "";
    private String addressIdStr = "";
    public static final String ACTIVITY_TYPE = "ACTIVITY_TYPE";
    public static final int ACTIVITY_TYPE_ADD = 1;
    public static final int ACTIVITY_TYPE_MODIFY = 2;
    public static final int ACTIVITY_TYPE_CONFIRM = 3;
    private int activityType = 1;
    private Address listBean;
    private boolean isLoadFinish = false;
    private InputFilter[] inputFilter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onResume() {
        super.onResume();
        if(tv_title!=null&&!TextUtils.isEmpty(tv_title.getText().toString()))
        MobclickAgent.onPageStart(tv_title.getText().toString());
        MobclickAgent.onResume(this);
    }
    @Override
    public void onPause() {
        super.onPause();
        if(tv_title!=null&&!TextUtils.isEmpty(tv_title.getText().toString()))
        MobclickAgent.onPageEnd(tv_title.getText().toString());
        MobclickAgent.onPause(this);
    }
    @Override
    protected void setData() {
        initView();
        new InitAreaTask(AddAddressActivity.this).execute(0);
        sb.setChecked(false);
        inputFilter = new InputFilter[]{
                new InputFilter() {
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart,
                                               int dend) {
                        for (int i = start; i < end; i++) {
                            if (!Character.isLetterOrDigit(source.charAt(i)) && !Character.toString(source.charAt(i)).equals("_") && !Character.toString(source.charAt(i)).equals("-")) {
                                return "";
                            }
                        }
                        return null;
                    }
                }};
        et_name.setFilters(inputFilter);
        et_detailAddress.setFilters(inputFilter);
        if (ACTIVITY_TYPE_ADD == getIntent().getIntExtra(ACTIVITY_TYPE, ACTIVITY_TYPE_ADD)) {//添加地址
            activityType = ACTIVITY_TYPE_ADD;
            tv_title.setText(getString(R.string.add_address));
        } else if (ACTIVITY_TYPE_MODIFY == getIntent().getIntExtra(ACTIVITY_TYPE, ACTIVITY_TYPE_ADD)) {//修改地址
            activityType = ACTIVITY_TYPE_MODIFY;
            listBean = (Address) getIntent().getSerializableExtra(FieldFinals.ADDRESS_);
            tv_title.setText(getString(R.string.modify_address));
            ll_delete.setVisibility(View.VISIBLE);
            initData();
        } else if (ACTIVITY_TYPE_CONFIRM == getIntent().getIntExtra(ACTIVITY_TYPE, ACTIVITY_TYPE_ADD)) {//确认地址
            activityType = ACTIVITY_TYPE_CONFIRM;
            listBean = (Address) getIntent().getSerializableExtra(FieldFinals.ADDRESS_);
            if(listBean!=null){
                initData();
            }
            tv_title.setText(getString(R.string.confirm_address));
            ll_delete.setVisibility(View.VISIBLE);
            tv_delete.setText(R.string.use_the_address);
            tv_delete.setTextColor(getTheColor(R.color.advert_blue_text));
        }
        tv_right.setText(getString(R.string.save));
        tv_right.setTextColor(getTheColor(R.color.advert_blue_text));
    }

    @Override
    protected void reload() {

    }

    private void initData() {
        if (listBean != null) {
            et_name.setText(listBean.getUsername());
            et_phone.setText(listBean.getMobile());
            et_detailAddress.setText(listBean.getAddress());
            if (listBean.getIs_default() == 0) {
                sb.setChecked(false);
            } else {
                sb.setChecked(true);
            }
            if (isLoadFinish) {
                if (provinces.size() > 0) {
                    tv_address.setText(addressStr);
                }
            }
        }
    }

    private void getDistrict() {
        if(listBean==null){
            return;
        }
        addressIdStr = "";
        addressStr = "";
        for (Province province : provinces) {
            if (String.valueOf(listBean.getProvince_id()).equals(province.getAreaId())) {
                addressIdStr = province.getAreaId() + ",";
                addressStr = province.getAreaName();
                for (City city : province.getCities()) {
                    if (String.valueOf(listBean.getCity_id()).equals(city.getAreaId())) {
                        addressIdStr += city.getAreaId() + ",";
                        addressStr += city.getAreaName();
                        for (County county : city.getCounties()) {
                            if (String.valueOf(listBean.getArea_id()).equals(county.getAreaId())) {
                                addressIdStr += county.getAreaId();
                                addressStr += county.getAreaName();
                                break;
                            }
                        }
                        break;
                    }
                }
                break;
            }
        }
    }

    @Event({R.id.add_address_ll, R.id.common_right_text, R.id.add_address_tv_delete})
    private void onEventClick(View v) {
        switch (v.getId()) {
            case R.id.add_address_ll:
                if (isLoadFinish) {
                    if (provinces.size() > 0) {
                        showAddressDialog();
                    } else {
                        new InitAreaTask(AddAddressActivity.this).execute(0);
                    }
                }
                break;
            case R.id.common_right_text:
                saveAddress();
                break;
            case R.id.add_address_tv_delete:
                switch (activityType) {
                    case ACTIVITY_TYPE_CONFIRM:
                        saveAddress();
                        break;
                    case ACTIVITY_TYPE_MODIFY:
                        DialogFactory.createSelectDialog(this, getString(R.string.is_delete_address), new OnSelectListener() {
                            @Override
                            public void onSelected(boolean isSelected) {
                                if (isSelected) {
                                    deleteAddress();
                                }
                            }
                        });
                        break;
                }
                break;
        }
    }

    private void deleteAddress() {
        RequestParams params2 = new RequestParams(HttpFinal.USER_ADDRESS_DELETE);
        params2.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params2.addBodyParameter(FieldFinals.SID, getSid());
        params2.addBodyParameter(FieldFinals.ADDRESS_ID, listBean.getAddressId() + "");
        HttpUtils.post(this,params2, new TypeToken<Result<Address>>() {
        }.getType(), new OnHttpRequest() {
            @Override
            public void onLoading(long total, long current, boolean isDownloading) {

            }

            @Override
            public void onStarted() {
                dialog.show();
            }

            @Override
            public void onWaiting() {

            }

            @Override
            public void onSuccess(Result result) {
                finishActivity();
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

    private void saveAddress() {
        if (TextUtils.isEmpty(et_name.getText().toString()) || TextUtils.isEmpty(et_phone.getText().toString()) || TextUtils.isEmpty(addressIdStr) || TextUtils.isEmpty(et_detailAddress.getText().toString())) {
            showToast(R.string.please_input_all_msg);
            return;
        }
        if(et_phone.getText().toString().trim().length()!=11){
            showToast(R.string.number_limit);
            return;
        }
        RequestParams params = new RequestParams(HttpFinal.USER_ADDRESS_SAVE);
        params.addBodyParameter(FieldFinals.DEVICE_IDENTIFIER, getDid());
        params.addBodyParameter(FieldFinals.SID, getSid());
        params.addBodyParameter(FieldFinals.MOBILE, et_phone.getText().toString());
        params.addBodyParameter(FieldFinals.USERNAME, et_name.getText().toString());
        params.addBodyParameter(FieldFinals.ADDRESS, et_detailAddress.getText().toString());
        params.addBodyParameter(FieldFinals.IS_DEFAULT, sb.isChecked() ? "1" : "0");
        params.addBodyParameter(FieldFinals.ADDRESS_PATH, addressIdStr);
        params.addBodyParameter(FieldFinals.ADDRESS_HEAD, addressStr);
        if (activityType == ACTIVITY_TYPE_MODIFY||activityType==ACTIVITY_TYPE_CONFIRM) {
            if(listBean!=null)
            params.addBodyParameter(FieldFinals.ADDRESS_ID, listBean.getAddressId() + "");
        }
        HttpUtils.post(this,params, new TypeToken<Result<Address>>() {
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
                Address listBean = (Address) result.getData();
                switch (activityType) {
                    case ACTIVITY_TYPE_ADD:
                    case ACTIVITY_TYPE_MODIFY:
                        finishActivity();
                        break;
                    case ACTIVITY_TYPE_CONFIRM:
                        PreferencesUtil.setPreferences(AddAddressActivity.this, FieldFinals.ADDRESS_BEAN, listBean);
                        if (AddressListActivity.instance != null) {
                            AddressListActivity.instance.finish();
                        }
                        finishActivity();
                        break;
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

    private void showAddressDialog() {
        new CityPickerDialog(AddAddressActivity.this, provinces, null, null, null,
                new CityPickerDialog.onCityPickedListener() {

                    @Override
                    public void onPicked(Province selectProvince,
                                         City selectCity, County selectCounty) {
                        StringBuilder address = new StringBuilder();
                        StringBuilder addressId = new StringBuilder();
                        address.append(
                                selectProvince != null ? selectProvince
                                        .getAreaName() : "")
                                .append(selectCity != null ? selectCity
                                        .getAreaName() : "")
                                .append(selectCounty != null ? selectCounty
                                        .getAreaName() : "");
                        addressStr = address.toString();
                        tv_address.setText(addressStr);
                        tv_address.setTextColor(getTheColor(R.color.black_333_text));
                        addressId.append(
                                selectProvince != null ? selectProvince
                                        .getAreaId() : "").append(",")
                                .append(selectCity != null ? selectCity
                                        .getAreaId() : "").append(",")
                                .append(selectCounty != null ? selectCounty
                                        .getAreaId() : "");
                        addressIdStr = addressId.toString();
                    }
                }).show();
    }

    private class InitAreaTask extends AsyncTask<Integer, Integer, Boolean> {

        Context mContext;

        Dialog progressDialog;

        public InitAreaTask(Context context) {
            mContext = context;
            progressDialog = Util.createLoadingDialog(mContext, getString(R.string.waiting_), true,
                    0);
        }

        @Override
        protected void onPreExecute() {
            isLoadFinish = false;
            progressDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean result) {
            progressDialog.dismiss();
            if (provinces.size() > 0) {
                if (activityType == ACTIVITY_TYPE_MODIFY||activityType==ACTIVITY_TYPE_CONFIRM) {
                    getDistrict();
                    if(!TextUtils.isEmpty(addressStr)){
                        tv_address.setText(addressStr);
                        tv_address.setTextColor(getTheColor(R.color.black_333_text));
                    }
                }
            } else {
                Toast.makeText(mContext, R.string.init_data_fail, Toast.LENGTH_SHORT).show();
            }
            isLoadFinish = true;
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            String address = null;
            InputStream in = null;
            try {
                in = mContext.getResources().getAssets().open("address.txt");
                byte[] arrayOfByte = new byte[in.available()];
                in.read(arrayOfByte);
                address = EncodingUtils.getString(arrayOfByte, "UTF-8");
                JSONArray jsonList = new JSONArray(address);
                Gson gson = new Gson();
                for (int i = 0; i < jsonList.length(); i++) {
                    try {
                        provinces.add(gson.fromJson(jsonList.getString(i),
                                Province.class));
                    } catch (Exception e) {
                    }
                }
                return true;
            } catch (Exception e) {
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                    }
                }
            }
            return false;
        }

    }
}
