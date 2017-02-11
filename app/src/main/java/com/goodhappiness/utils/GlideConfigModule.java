package com.goodhappiness.utils;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;

/**
 * Created by 电脑 on 2016/10/19.
 */
public class GlideConfigModule implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();

        int customMemoryCacheSize = (int) (1.2 * defaultMemoryCacheSize);
        int customBitmapPoolSize = (int) (1.2 * defaultBitmapPoolSize);

        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));
        // 指定位置在packageName/cache/glide_cache,大小为MAX_CACHE_DISK_SIZE的磁盘缓存
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, "glide_cache", 100*1024*1024));
//        //指定内存缓存大小
//        builder.setMemoryCache(new LruResourceCache(ConfigConstants.MAX_CACHE_MEMORY_SIZE));
//        //全部的内存缓存用来作为图片缓存
//        builder.setBitmapPool(new LruBitmapPool(ConfigConstants.MAX_CACHE_MEMORY_SIZE));
        builder.setDecodeFormat(DecodeFormat.PREFER_RGB_565);//和Picasso配置一样
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
    }
}
