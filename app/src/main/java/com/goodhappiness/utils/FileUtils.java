package com.goodhappiness.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.constant.FieldFinals;
import com.goodhappiness.constant.OtherFinals;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/***
 * @author liu
 * @ClassName: FileUtils
 * @Description: 文件操作类
 * @date 2015-2-2 上午10:01:12
 */
public class FileUtils {
    /**
     * sd卡的根目录
     */
    private static String mSdRootPath = Environment.getExternalStorageDirectory().getPath();
    /**
     * 手机的缓存根目录
     */
    private static String mDataRootPath = null;
    /**
     * 保存Image的目录名
     */
    private final static String FOLDER_NAME = "/GoodHappiness/扑多相册";
    private final static String IMG_FOLDER_NAME = "/GoodHappiness/image";

    public FileUtils(Context context) {
        mDataRootPath = context.getCacheDir().getPath();
    }


    public static File saveResourceToFile(Context context, int res, String name) {
        File file = new File(getSaveDirectory() + "/" + name + ".png");
        if (!file.exists()) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            BitmapFactory.decodeResource(context.getResources(), res).compress(CompressFormat.PNG, 100, baos);
            byte[] b;
            b = baos.toByteArray();
            file = saveImgFile(context, b, getSaveDirectory(), name);
            PreferencesUtil.setPreferences(context, name, file.getAbsolutePath());
            return file;
        }
        return file;
    }
    public static void inputstreamtofile(InputStream ins,File file) {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static byte[] File2byte(String filePath) {
        byte[] buffer = null;
        try {
            File file = new File(filePath);
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            bos.close();
            buffer = bos.toByteArray();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer;
    }

    /**
     * 获取储存Image的目录
     *
     * @return
     */
    public static String getSaveDirectory() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ? mSdRootPath + IMG_FOLDER_NAME : mDataRootPath + IMG_FOLDER_NAME;
    }

    public static String getStorageDirectory() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED) ? mSdRootPath + FOLDER_NAME : mDataRootPath + FOLDER_NAME;
    }

    public static String getDownloadStorageDirectory(Context context) {
        if (Environment.getExternalStorageState().equals( // 如果有SD卡，就存SD卡
                Environment.MEDIA_MOUNTED)) {
            return Environment.getExternalStorageDirectory() + File.separator + OtherFinals.GOOD_HAPPINESS_HEAD;
        } else { // 保存在手机
            return context.getFilesDir() + File.separator + OtherFinals.GOOD_HAPPINESS_HEAD;
        }
    }

    public static void createRootFile(Context context) {
        String path = getDownloadStorageDirectory(context);
        Log.i("mark", path);
        File folderFile = new File(path);
        if (!folderFile.exists()) {
            folderFile.mkdirs();
        }
    }

    public static void deleteFile(String path) {
        File file = new File(path);
        if (file.exists()) {
            file.delete();
        }
    }

    public static boolean isFileExists2(String path) {
        File file = new File(path);
        return file.exists();
    }

    /**
     * 根据byte数组，生成文件存放到手机内存当中
     */
    public static File saveImgFile(Context context, byte[] bfile, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + "/" + fileName + ".jpg");
            if (!file.exists()) {
                file.createNewFile();
            }
            Log.e("f_", file.getAbsolutePath());
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            // 最后通知图库更新
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            context.sendBroadcast(intent);
            return file;
        }
    }

    public static ContentValues getVideoContentValues(File paramFile) {
        long paramLong = System.currentTimeMillis();
        ContentValues localContentValues = new ContentValues();
        localContentValues.put(MediaStore.Video.Media.DURATION, getVideoDuration(paramFile.getAbsolutePath()));
        localContentValues.put(MediaStore.Video.Media.TITLE, paramFile.getName());
        localContentValues.put(MediaStore.Video.Media.DISPLAY_NAME, paramFile.getName());
        localContentValues.put(MediaStore.Video.Media.MIME_TYPE, "video/mp4");
        localContentValues.put(MediaStore.Video.Media.DATE_TAKEN, Long.valueOf(paramLong));
        localContentValues.put(MediaStore.Video.Media.DATE_MODIFIED, Long.valueOf(paramLong));
        localContentValues.put(MediaStore.Video.Media.DATE_ADDED, Long.valueOf(paramLong));
        localContentValues.put(MediaStore.Video.Media.DATA, paramFile.getAbsolutePath());

        return localContentValues;
    }

    public static int getVideoDuration(String filePath) {
        MediaPlayer mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(filePath);
            mediaPlayer.prepare();
            return mediaPlayer.getDuration();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mediaPlayer.setDisplay(null);
            mediaPlayer.reset();
            mediaPlayer.release();
            mediaPlayer = null;
        }
        return 0;
    }

    /**
     * 根据byte数组，生成文件存放到手机内存当中
     */
    public static File saveImgFile(Context context, byte[] bfile, String filePath) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + "/" + "happiness_" + System.currentTimeMillis() + ".jpg");
            if (!file.exists()) {
                file.createNewFile();
            }
            Log.e("f_", file.getAbsolutePath());
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            // 最后通知图库更新
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            context.sendBroadcast(intent);
            return file;
        }
    }

    /**
     * 根据byte数组，生成文件存放到手机内存当中
     */
    public static File saveGifFile(Context context,File oldFile, String filePath) {
        if (oldFile == null) {
            return null;
        }
        String ff = "";
        try {
            int bytesum = 0;
            int byteread = 0;
            ff = filePath + "/" + "happiness_" + System.currentTimeMillis() + ".gif";
            if (oldFile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldFile.getAbsolutePath()); //读入原文件
                FileOutputStream fs = new FileOutputStream(ff);
                byte[] buffer = new byte[1444];
                int length;
                while ((byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        } catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }
        File resultFile = new File(ff);
        if(resultFile!=null){
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(resultFile);
            intent.setData(uri);
            context.sendBroadcast(intent);
        }
        return resultFile;
    }

    public static File saveImgFile(Context context, Bitmap bitmap, String filePath) {
        if (bitmap == null) {
            return null;
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bfile = baos.toByteArray();
        try {
            baos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
                dir.mkdirs();
            }
            file = new File(filePath + "/" + "happiness_" + System.currentTimeMillis() + ".jpg");
            if (!file.exists()) {
                file.createNewFile();
            }
            Log.e("f_", file.getAbsolutePath());
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            // 最后通知图库更新
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(file);
            intent.setData(uri);
            context.sendBroadcast(intent);
            return file;
        }
    }

    /**
     * 把程序拍摄的照片放到 SD卡的 Pictures目录中 sheguantong 文件夹中
     *
     * @return
     * @throws IOException
     */
    @SuppressLint("SimpleDateFormat")
    public static File createImageFile() throws IOException {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String timeStamp = format.format(new Date());
        String imageFileName = "gopapa_head_" + timeStamp + ".jpg";
        File image = new File(PictureUtil.getAlbumDir(), imageFileName);
        GoodHappinessApplication.currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    /**
     * 保存Image的方法，有sd卡存储到sd卡，没有就存储到手机目录
     *
     * @param bitmap
     * @throws IOException
     */
    public void savaBitmap(String url, Bitmap bitmap) throws IOException {
        if (bitmap == null) {
            return;
        }
        String path = getStorageDirectory();
        File folderFile = new File(path);
        if (!folderFile.exists()) {
            folderFile.mkdirs();
        }
        File file = new File(path + File.separator + getFileName(url));
        file.createNewFile();
        FileOutputStream fos = new FileOutputStream(file);
        bitmap.compress(CompressFormat.JPEG, 100, fos);
        fos.flush();
        fos.close();
    }

    /**
     * 从手机或者sd卡获取Bitmap
     *
     * @return
     */
    public Bitmap getBitmap(String url) {
        return BitmapFactory.decodeFile(getStorageDirectory() + File.separator + getFileName(url));
    }

    /**
     * 判断文件是否存在
     *
     * @param fileName
     * @return
     */
    public boolean isFileExists(String fileName) {
        return new File(getStorageDirectory() + File.separator + getFileName(fileName)).exists();
    }

    /**
     * 获取文件的大小
     *
     * @return
     */
    public long getFileSize(String url) {
        return new File(getStorageDirectory() + File.separator + getFileName(url)).length();
    }

    /**
     * 删除SD卡或者手机的缓存图片和目录
     */
    public void deleteFile() {
        File dirFile = new File(getStorageDirectory());
        if (!dirFile.exists()) {
            return;
        }
        if (dirFile.isDirectory()) {
            String[] children = dirFile.list();
            for (int i = 0; i < children.length; i++) {
                new File(dirFile, children[i]).delete();
            }
        }

        dirFile.delete();
    }

    public static Bitmap getUserHeadImg(Context context) {
        File file = new File(PreferencesUtil.getStringPreferences(context, FieldFinals.IMAGE_FILE_PATH));
        if (file != null && file.exists()) {
            return BitmapFactory.decodeFile(file.getAbsolutePath());
        }
        return null;
    }

    /**
     * @return String 返回类型
     * @throws
     * @Title: getFileName
     * @说 明: 根据url截取文件名
     * @参 数: @param url
     * @参 数: @return
     */
    public String getFileName(String url) {
        return url.substring(url.lastIndexOf("/") + 1);
    }

    /***
     * @param @param  voiceName 文件名
     * @param @return
     * @return String
     * @throws
     * @Title: getVoicePath
     * @Description: 根据文件名字得到文件路径
     */
    public static String getVoicePath() {
        String path = null;
        if (Environment.getExternalStorageState().equals( // 如果有SD卡，就存SD卡
                Environment.MEDIA_MOUNTED)) {
            path = Environment.getExternalStorageDirectory() + "/gopapa/voice";
        } else { // 保存在手机
            path = Environment.getDataDirectory() + "/gopapa/voice";
        }
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        return path;
    }

    /**
     * @param @param  voiceName
     * @param @return
     * @return String
     * @throws
     * @Title: getVoicePath
     * @Description: 根据名字得到当前录音的路径
     */
    public static String getVoicePath(String voiceName) {
        return getVoicePath() + "/" + voiceName;
    }

    /***
     * @param @param  voiceName 文件名
     * @param @return
     * @return InputStream
     * @throws
     * @Title: getVoiceStream
     * @Description: 根据文件路径，读取文件转化为InputStream
     */
    public static InputStream getVoiceStream(String path) {
        FileInputStream fs = null;
        InputStream is = null;
        byte[] buffer = null;
        try {
            fs = new FileInputStream(path);
            if (fs != null) {
                int length = fs.available();
                buffer = new byte[length];
                fs.read(buffer);
                fs.close();
                is = new ByteArrayInputStream(buffer);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return is;
    }

    /**
     * 把程序拍摄的照片放到 SD卡的 Pictures目录中 sheguantong 文件夹中
     *
     * @return
     * @throws IOException
     */
    // @SuppressLint("SimpleDateFormat")
    // public static File createImageFile() throws IOException {
    //
    // SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd_HHmmss");
    // String timeStamp = format.format(new Date());
    // String imageFileName = "gopapa_head_" + timeStamp + ".jpg";
    // File image = new File(PictureUtil.getAlbumDir(), imageFileName);
    // MyApplication.currentPhotoPath = image.getAbsolutePath();
    // return image;
    // }
    //
    // public static Bitmap getUserHeadImg(){
    // File file = new File(MyApplication.preferences.getString("head_file",
    // ""));
    // if(file != null && file.exists()){
    // return BitmapFactory.decodeFile(file.getAbsolutePath());
    // }
    // return null;
    // }
    public static String saveImgFile(String str, String filePath, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        File file = new File(filePath + "/" + fileName);
        try {
            File dir = new File(filePath);
            if (!dir.exists() && dir.isDirectory()) {// 判断文件目录是否存在
                dir.mkdirs();
            }
            if (!file.exists()) {
                file.createNewFile();
            }
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(str.getBytes());
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file.getAbsolutePath();
    }

    public static void scanFile(Context mContext) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) { // 判断SDK版本是不是4.4或者高于4.4
            String[] paths = new String[]{Environment.getExternalStorageDirectory().toString()};
            MediaScannerConnection.scanFile(mContext, paths, null, null);
        } else {
            final Intent intent;
            if (Environment.getExternalStorageDirectory().isDirectory()) {
                intent = new Intent(Intent.ACTION_MEDIA_MOUNTED);
                intent.setClassName("com.android.providers.media", "com.android.providers.media.MediaScannerReceiver");
                intent.setData(Uri.fromFile(Environment.getExternalStorageDirectory()));
                mContext.sendBroadcast(intent);
            }
//			else {
//				intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
//				intent.setData(Uri.fromFile(new File(path)));
//			}
        }
    }

    @TargetApi(19)
    public static String getImageAbsolutePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
