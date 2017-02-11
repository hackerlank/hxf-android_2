package com.goodhappiness.ui.social.picture;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.core.TuSdkResult;
import org.lasque.tusdk.core.struct.TuSdkSize;
import org.lasque.tusdk.core.view.TuSdkViewHelper;
import org.lasque.tusdk.core.view.widget.TuMaskRegionView;
import org.lasque.tusdk.core.view.widget.button.TuSdkImageButton;
import org.lasque.tusdk.impl.components.sticker.TuStickerChooseFragment;
import org.lasque.tusdk.impl.components.sticker.TuStickerOnlineFragment;
import org.lasque.tusdk.impl.components.widget.sticker.StickerBarView;
import org.lasque.tusdk.impl.components.widget.sticker.StickerView;
import org.lasque.tusdk.modules.components.TuSdkComponentErrorListener;
import org.lasque.tusdk.modules.components.sticker.TuEditStickerFragmentBase;
import org.lasque.tusdk.modules.view.widget.sticker.StickerCategory;
import org.lasque.tusdk.modules.view.widget.sticker.StickerData;
import org.lasque.tusdk.modules.view.widget.sticker.StickerResult;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 电脑 on 2016/5/3.
 */
public class TuEditStickerFragment2 extends TuEditStickerFragmentBase implements TuStickerChooseFragment.TuStickerChooseFragmentDelegate, TuStickerOnlineFragment.TuStickerOnlineFragmentDelegate, StickerBarView.StickerBarViewDelegate {
    private TuEditStickerFragment2.TuEditStickerFragment2Delegate a;
    private List<StickerCategory> b;
    private int c;
    private int d;
    private int e = -1;
    private int f;
    private StickerView.StickerViewDelegate g;
    private ImageView h;
    private StickerView i;
    private TuMaskRegionView j;
    private StickerBarView k;
    private TuSdkImageButton l;
    private TuSdkImageButton m;
    private TuSdkImageButton n;
    private TuSdkImageButton o;
    private StickerData bgSticker;
    protected View.OnClickListener mButtonClickListener = new TuSdkViewHelper.OnSafeClickListener() {
        public void onSafeClick(View var1) {
            TuEditStickerFragment2.this.dispatcherViewClick(var1);
        }
    };

    public static int getLayoutId() {
        return TuSdkContext.getLayoutResId("tusdk_impl_component_sticker_edit_sticker_fragment");
    }

    public TuEditStickerFragment2.TuEditStickerFragment2Delegate getDelegate() {
        return this.a;
    }

    public void setDelegate(TuEditStickerFragment2.TuEditStickerFragment2Delegate var1) {
        this.a = var1;
        this.setErrorListener(var1);
    }

    public TuEditStickerFragment2() {
    }

    public View onCreateView(LayoutInflater var1, ViewGroup var2, Bundle var3) {
        this.setRootViewLayoutId(getLayoutId());

        return super.onCreateView(var1, var2, var3);
    }

    protected void notifyProcessing(TuSdkResult var1) {
        if (!this.showResultPreview(var1)) {
            if (this.a != null) {
                this.a.onTuEditStickerFragment2Edited(this, var1);
            }
        }
    }


    protected boolean asyncNotifyProcessing(TuSdkResult var1) {
        return this.a == null ? false : this.a.onTuEditStickerFragment2EditedAsync(this, var1);
    }

    public int getGridWidth() {
        return this.c;
    }

    public void setGridWidth(int var1) {
        this.c = var1;
    }

    public int getGridHeight() {
        return this.d;
    }

    public void setGridHeight(int var1) {
        this.d = var1;
    }

    public int getGridPadding() {
        return this.e;
    }

    public void setGridPadding(int var1) {
        this.e = var1;
    }

    public int getGridLayoutId() {
        return this.f;
    }

    public void setGridLayoutId(int var1) {
        this.f = var1;
    }

    public List<StickerCategory> getCategories() {
        return this.b;
    }

    public void setCategories(List<StickerCategory> var1) {
        this.b = var1;
    }

    public StickerView.StickerViewDelegate getStickerViewDelegate() {
        return this.g;
    }

    public void setStickerViewDelegate(StickerView.StickerViewDelegate var1) {
        this.g = var1;
    }

    public ImageView getImageView() {
        if (this.h == null) {
            this.h = (ImageView) this.getViewById("lsq_imageView");
        }

        return this.h;
    }

    public StickerView getStickerView() {
        if (this.i == null) {
            this.i = (StickerView) this.getViewById("lsq_stickerView");
            if (this.i != null) {
                this.i.setDelegate(this.getStickerViewDelegate());
            }
        }

        return this.i;
    }

    public TuMaskRegionView getCutRegionView() {
        if (this.j == null) {
            this.j = (TuMaskRegionView) this.getViewById("lsq_cutRegionView");
            if (this.j != null) {
                this.j.setEdgeMaskColor(TuSdkContext.getColor("lsq_background_editor"));
                this.j.setEdgeSideColor(-2130706433);
            }
        }

        return this.j;
    }

    public StickerBarView getStickerBarView() {
        if (this.k == null) {
            this.k = (StickerBarView) this.getViewById("lsq_sticker_bar");
            if (this.k != null) {
                this.k.setGridLayoutId(this.getGridLayoutId());
                this.k.setGridWidth(this.getGridWidth());
                this.k.setGridPadding(this.getGridPadding());
                this.k.setGridHeight(this.getGridHeight());
                this.k.setDelegate(this);
            }
        }

        return this.k;
    }

    public TuSdkImageButton getCancelButton() {
        if (this.l == null) {
            this.l = (TuSdkImageButton) this.getViewById("lsq_bar_cancelButton");
            if (this.l != null) {
                this.l.setOnClickListener(this.mButtonClickListener);
            }
        }

        return this.l;
    }

    public TuSdkImageButton getCompleteButton() {
        if (this.m == null) {
            this.m = (TuSdkImageButton) this.getViewById("lsq_bar_completeButton");
            if (this.m != null) {
                this.m.setOnClickListener(this.mButtonClickListener);
            }
        }

        return this.m;
    }

    public TuSdkImageButton getListButton() {
        if (this.n == null) {
            this.n = (TuSdkImageButton) this.getViewById("lsq_bar_listButton");
            if (this.n != null) {
                this.n.setOnClickListener(this.mButtonClickListener);
            }
        }

        return this.n;
    }

    public TuSdkImageButton getOnlineButton() {
        if (this.o == null) {
            this.o = (TuSdkImageButton) this.getViewById("lsq_bar_onlineButton");
            if (this.o != null) {
                this.o.setOnClickListener(this.mButtonClickListener);
            }
        }

        return this.o;
    }

    protected void dispatcherViewClick(View var1) {
        if (this.equalViewIds(var1, this.getCancelButton())) {
            this.handleBackButton();
        } else if (this.equalViewIds(var1, this.getCompleteButton())) {
            this.handleCompleteButton();
        } else if (this.equalViewIds(var1, this.getListButton())) {
            this.handleListButton();
        } else {
            if (this.equalViewIds(var1, this.getOnlineButton())) {
                this.handleOnlineButton();
            }

        }
    }

    protected void loadView(ViewGroup var1) {
        super.loadView(var1);
        this.getImageView();
        this.getStickerView();
        this.getCutRegionView();
        this.getCancelButton();
        this.getCompleteButton();
        this.getListButton();
        this.getOnlineButton();
        if (this.getStickerBarView() != null) {
            this.getStickerBarView().loadCategories(this.getCategories());
        }

    }

    protected void viewDidLoad(ViewGroup var1) {
        super.viewDidLoad(var1);
        this.loadImageWithThread();
    }

    protected void asyncLoadImageCompleted(Bitmap var1) {
        super.asyncLoadImageCompleted(var1);
        if (var1 != null) {
            this.setImageRegionMask(var1);
        }
    }

    protected void setImageRegionMask(Bitmap var1) {
        if (var1 != null) {
            if (this.getImageView() != null) {
                this.getImageView().setImageBitmap(var1);
            }

            if (this.getCutRegionView() != null) {
                this.getCutRegionView().setRegionRatio(TuSdkSize.create(var1).getRatioFloat());
            }

        }
    }

    protected void handleListButton() {
        TuStickerChooseFragment var1;
        (var1 = new TuStickerChooseFragment()).setDelegate(this);
        this.presentModalNavigationActivity(var1, true);
    }

    public void onTuStickerChooseFragmentSelected(TuStickerChooseFragment var1, StickerData var2) {
        if (var1 != null) {
            var1.dismissActivityWithAnim();
        }

        this.appendStickerItem(var2);
    }

    protected void handleOnlineButton() {
        TuStickerOnlineFragment var1;
        (var1 = new TuStickerOnlineFragment()).setDelegate(this);
        this.presentModalNavigationActivity(var1, true);
    }

    public void onTuStickerOnlineFragmentSelected(TuStickerOnlineFragment var1, StickerData var2) {
        if (var1 != null) {
            var1.dismissActivityWithAnim();
        }

        this.appendStickerItem(var2);
    }

    public void onStickerBarViewSelected(StickerBarView var1, StickerData var2) {
        ArrayList<StickerResult> list = (ArrayList<StickerResult>) this.getStickerView().getResults(new Rect());
        if (list != null && list.size() > 0) {
            for (StickerResult result : list) {
                long a = result.item.groupId;
                if (a == 938) {
                    Toast.makeText(getActivity(), "只能选择一张背景", Toast.LENGTH_LONG).show();
                    return;
                }
                Log.e("k_", a + "");
            }
        }
        this.appendStickerItem(var2);
    }

    public void onStickerBarViewEmpty(StickerBarView var1, StickerCategory var2) {
        this.handleOnlineButton();
    }

    public interface TuEditStickerFragment2Delegate extends TuSdkComponentErrorListener {
        void onTuEditStickerFragment2Edited(TuEditStickerFragment2 var1, TuSdkResult var2);

        boolean onTuEditStickerFragment2EditedAsync(TuEditStickerFragment2 var1, TuSdkResult var2);
    }
}
