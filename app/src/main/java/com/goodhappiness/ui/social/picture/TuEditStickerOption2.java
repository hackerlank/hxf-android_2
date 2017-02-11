package com.goodhappiness.ui.social.picture;

import com.goodhappiness.ui.social.picture.TuEditStickerFragment2;

import org.lasque.tusdk.core.TuSdkContext;
import org.lasque.tusdk.impl.activity.TuImageResultOption;
import org.lasque.tusdk.impl.components.widget.sticker.StickerView;
import org.lasque.tusdk.modules.view.widget.sticker.StickerCategory;

import java.util.List;

/**
 * Created by 电脑 on 2016/5/3.
 */
public class TuEditStickerOption2 extends TuImageResultOption {
    private List<StickerCategory> a;
    private int b;
    private int c;
    private int d = -1;
    private int e;
    private StickerView.StickerViewDelegate f;

    public TuEditStickerOption2() {
    }

    protected Class<?> getDefaultComponentClazz() {
        return TuEditStickerFragment2.class;
    }

    protected int getDefaultRootViewLayoutId() {
        return TuEditStickerFragment2.getLayoutId();
    }

    public int getGridWidth() {
        return this.b;
    }

    public void setGridWidth(int var1) {
        this.b = var1;
    }

    public int getGridHeight() {
        return this.c;
    }

    public void setGridHeight(int var1) {
        this.c = var1;
    }

    public int getGridPadding() {
        return this.d;
    }

    public void setGridPadding(int var1) {
        this.d = var1;
    }

    public void setGridPaddingDP(int var1) {
        if(var1 >= 0) {
            this.setGridPadding(TuSdkContext.dip2px((float) var1));
        }
    }

    public int getGridLayoutId() {
        return this.e;
    }

    public void setGridLayoutId(int var1) {
        this.e = var1;
    }

    public List<StickerCategory> getCategories() {
        return this.a;
    }

    public void setCategories(List<StickerCategory> var1) {
        this.a = var1;
    }

    public StickerView.StickerViewDelegate getStickerViewDelegate() {
        return this.f;
    }

    public void setStickerViewDelegate(StickerView.StickerViewDelegate var1) {
        this.f = var1;
    }

    public TuEditStickerFragment2 fragment() {
        TuEditStickerFragment2 var1;
        (var1 = (TuEditStickerFragment2)this.fragmentInstance()).setGridLayoutId(this.getGridLayoutId());
        var1.setGridWidth(this.getGridWidth());
        var1.setGridHeight(this.getGridHeight());
        var1.setGridPadding(this.getGridPadding());
        var1.setCategories(this.getCategories());
        var1.setStickerViewDelegate(this.getStickerViewDelegate());
        return var1;
    }
}
