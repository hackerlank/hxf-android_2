package com.goodhappiness.bean;

import org.lasque.tusdk.modules.view.widget.sticker.StickerData;

/**
 * Created by 电脑 on 2016/5/20.
 */
public class StickerBarBean {
    private String name;
    private StickerData bg;

    public StickerBarBean(String name, StickerData bg) {
        this.name = name;
        this.bg = bg;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StickerData getBg() {
        return bg;
    }

    public void setBg(StickerData bg) {
        this.bg = bg;
    }
}
