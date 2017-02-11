package com.goodhappiness.ui.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.goodhappiness.R;
import com.goodhappiness.dao.OnSelectListener;
import com.goodhappiness.ui.dialog.NotifyDialog;
import com.goodhappiness.widget.blur_dialog.BlurDialogFragment;

/**
 * Created by 电脑 on 2016/5/5.
 */
public class NotifyFragment extends BlurDialogFragment {

    /**
     * Bundle key used to start the blur dialog with a given scale factor
     * (float).
     */
    private static final String BUNDLE_KEY_DOWN_SCALE_FACTOR = "bundle_key_down_scale_factor";

    /**
     * Bundle key used to start the blur dialog with a given blur radius (int).
     */
    private static final String BUNDLE_KEY_BLUR_RADIUS = "bundle_key_blur_radius";

    /**
     * Bundle key used to start the blur dialog with a given dimming effect
     * policy.
     */
    private static final String BUNDLE_KEY_DIMMING = "bundle_key_dimming_effect";

    /**
     * Bundle key used to start the blur dialog with a given debug policy.
     */
    private static final String BUNDLE_KEY_DEBUG = "bundle_key_debug_effect";
    private static final String MSG = "massage";
    private static final String TITLE = "title";

    private int mRadius;
    private float mDownScaleFactor;
    private boolean mDimming;
    private boolean mDebug;
    private String msg;
    private String title;
    private OnSelectListener onSelectListener;

    /**
     * Retrieve a new instance of the sample fragment.
     *
     * @param radius          blur radius.
     * @param downScaleFactor down scale factor.
     * @param dimming         dimming effect.
     * @param debug           debug policy.
     * @return well instantiated fragment.
     */
    public static NotifyFragment newInstance(int radius, float downScaleFactor, boolean dimming, boolean debug, String title, String msg) {
        NotifyFragment fragment = new NotifyFragment();
        Bundle args = new Bundle();
        args.putInt(BUNDLE_KEY_BLUR_RADIUS, radius);
        args.putFloat(BUNDLE_KEY_DOWN_SCALE_FACTOR, downScaleFactor);
        args.putBoolean(BUNDLE_KEY_DIMMING, dimming);
        args.putBoolean(BUNDLE_KEY_DEBUG, debug);
        args.putString(MSG, msg);
        args.putString(TITLE, title);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        Bundle args = getArguments();
        mRadius = args.getInt(BUNDLE_KEY_BLUR_RADIUS);
        mDownScaleFactor = args.getFloat(BUNDLE_KEY_DOWN_SCALE_FACTOR);
        mDimming = args.getBoolean(BUNDLE_KEY_DIMMING);
        mDebug = args.getBoolean(BUNDLE_KEY_DEBUG);
        msg = args.getString(MSG);
        title = args.getString(TITLE);
    }

    public OnSelectListener getOnSelectListener() {
        return onSelectListener;
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        NotifyDialog dialog = new NotifyDialog(getActivity(), R.style.dialog, title, msg, onSelectListener);
        dialog.setCancelable(false);
        Window dialogWindow = dialog.getWindow();// 设置Dialog的位置
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER_VERTICAL | Gravity.CENTER_HORIZONTAL);// 垂直居中和水平居中
        dialogWindow.setAttributes(lp);
        dialog.show();
        return dialog;
    }

    @Override
    protected boolean isDebugEnable() {
        return mDebug;
    }

    @Override
    protected boolean isDimmingEnable() {
        return mDimming;
    }

    @Override
    protected boolean isActionBarBlurred() {
        return true;
    }

    @Override
    protected float getDownScaleFactor() {
        return mDownScaleFactor;
    }

    @Override
    protected int getBlurRadius() {
        return mRadius;
    }
}
