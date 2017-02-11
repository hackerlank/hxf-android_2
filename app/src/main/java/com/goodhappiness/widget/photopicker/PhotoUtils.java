package com.goodhappiness.widget.photopicker;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import com.goodhappiness.bean.Photo;
import com.goodhappiness.bean.PhotoFloder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Class: PhotoUtils
 * @Description:
 * @author: lling(www.liuling123.com)
 * @Date: 2015/11/4
 */
public class PhotoUtils {


    public static Map<String, PhotoFloder> getPhotos(Context context) {
        Map<String, PhotoFloder> floderMap = new HashMap<String, PhotoFloder>();
        boolean isFirst = true;
        String allPhotosKey = "所有图片";
        PhotoFloder allFloder = new PhotoFloder();
        allFloder.setName(allPhotosKey);
        allFloder.setDirPath(allPhotosKey);
        allFloder.setPhotoList(new ArrayList<Photo>());
        floderMap.put(allPhotosKey, allFloder);

        Uri imageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        ContentResolver mContentResolver = context.getContentResolver();

        // 只查询jpeg和png的图片
        final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.ImageColumns.ORIENTATION};
        Cursor mCursor = mContentResolver.query(imageUri, columns,
                null, null,
                MediaStore.Images.Media.DATE_ADDED + " DESC");

        int pathIndex = mCursor
                .getColumnIndex(MediaStore.Images.Media.DATA);

//        mCursor.moveToFirst();
        while (mCursor.moveToNext()) {
            // 获取图片的路径
            String path = mCursor.getString(pathIndex);
//            if(BitmapFactory.decodeFile(path)==null){
//                continue;
//            }
            // 获取该图片的父路径名
            File parentFile = new File(path).getParentFile();
            if (parentFile == null) {
                continue;
            }
            String dirPath = parentFile.getAbsolutePath();
            if (floderMap.containsKey(dirPath)) {
                Photo photo = new Photo(path);
                PhotoFloder photoFloder = floderMap.get(dirPath);
                photoFloder.getPhotoList().add(photo);
                floderMap.get(allPhotosKey).getPhotoList().add(photo);
                continue;
            } else {
                // 初始化imageFloder
                PhotoFloder photoFloder = new PhotoFloder();
                List<Photo> photoList = new ArrayList<Photo>();
                Photo photo = new Photo(path);
                photoList.add(photo);
                photoFloder.setPhotoList(photoList);
                photoFloder.setDirPath(dirPath);
                photoFloder.setName(dirPath.substring(dirPath.lastIndexOf(File.separator) + 1, dirPath.length()));
                floderMap.put(dirPath, photoFloder);
                floderMap.get(allPhotosKey).getPhotoList().add(photo);
//                if(isFirst){
//                    floderMap.get(allPhotosKey).getPhotoList().add(photo);
//                    isFirst = false;
//                }
            }
            Log.e("p_", path);
        }
        mCursor.close();

//        File dir = new File(FileUtils.getStorageDirectory());
//        if (dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
//            String [] a = dir.list();
//            PhotoFloder photoFloder = new PhotoFloder();
//            photoFloder.setName("好幸福相册");
//            photoFloder.setDirPath(dir.getPath());
//            List<Photo> photoList = new ArrayList<Photo>();
//            for(String s:a){
//                if(s.contains(".jpg")){
//                    Log.e("t_",s);
//                    Photo photo = new Photo(dir.getPath()+s);
//                    photoList.add(photo);
//                    floderMap.get(allPhotosKey).getPhotoList().add(photo);
//                }
//            }
//            photoFloder.setPhotoList(photoList);
//            floderMap.put(dir.getPath(),photoFloder);
//        }
        return floderMap;
    }

}
