package com.goodhappiness.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.util.Log;
import android.widget.ImageView;

import com.goodhappiness.GoodHappinessApplication;
import com.goodhappiness.constant.OtherFinals;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;

import java.io.BufferedInputStream;
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
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.UUID;

/**
 * 图片工具
 *
 * @author Lanyan
 */
public class BitmapUtil {

    private static final int TEMP_STORAGE_SIZE = 1024 * 100;
    private static File myCaptureFile = null;

    public static void loadImage(String url) {
        Log.e("k_", url);
        ImageLoader.getInstance().loadImage(url, GoodHappinessApplication.options, null);
    }

    public static Bitmap getRecBitmap(Bitmap bitmap, int scrollDistance) {
        if (scrollDistance < 0) {
            scrollDistance = 0;
        }
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width >= height) {
            if (scrollDistance + height <= width) {
                return Bitmap.createBitmap(bitmap,scrollDistance,0,height,height);
            } else {
                return Bitmap.createBitmap(bitmap,width-height,0,height,height);
            }
        } else {
            if (scrollDistance + width <= height) {
                return Bitmap.createBitmap(bitmap,0,scrollDistance,width,width);
            } else {
                return Bitmap.createBitmap(bitmap,height-width,0,width,width);
            }
        }
    }

    public static int readPictureDegree(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm     需要旋转的图片
     * @param degree 旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
        }
        if (returnBm == null) {
            returnBm = bm;
        }
        if (bm != returnBm) {
            bm.recycle();
        }
        return returnBm;
    }

    /**
     * 将Bitmap转成二进制
     *
     * @param bitmap
     * @return
     */
    public static byte[] getBitmapByte(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return out.toByteArray();
    }

    /**
     * @return Bitmap 返回类型
     * @throws
     * @Title: getBitmapFromUrl
     * @说 明:从服务器获取Bitmap
     * @参 数: @param url
     * @参 数: @return
     */
    public static Bitmap getBitmapFromUrl(String url) {
        Bitmap bitmap = null;
        HttpClient httpClient = new DefaultHttpClient();
        // 设置超时时间
        HttpConnectionParams.setConnectionTimeout(new BasicHttpParams(),
                6 * 1000);
        HttpGet get = new HttpGet(url);
        try {
            HttpResponse response = httpClient.execute(get);
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                bitmap = BitmapFactory.decodeStream(entity.getContent());
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    /**
     * 保存图片到sd卡
     *
     * @param bmp
     * @param filename
     * @return
     */
    public static boolean saveBitmap2file(Bitmap bmp, String filename) {
        CompressFormat format = Bitmap.CompressFormat.JPEG;
        int quality = 100;
        OutputStream stream = null;
        try {
            stream = new FileOutputStream(filename);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return bmp.compress(format, quality, stream);
    }

    /**
     * 从SD卡里面读取图片
     *
     * @param path 图片的完整路径，包含文件名
     * @return Bitmap
     * @since v 1.0
     */
    public static Bitmap getBitmapBySD(String path, String fileName) {
        Bitmap b = null;
        try {
            File f = null;
            if (fileName != null)
                f = new File(path, fileName);
            else
                f = new File(path);
            // Decode image size
            BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;

            FileInputStream fis = new FileInputStream(f);
            BitmapFactory.decodeStream(fis, null, o);
            fis.close();

            int scale = 1;
            int IMAGE_MAX_SIZE = 200;
            if (o.outHeight > IMAGE_MAX_SIZE || o.outWidth > IMAGE_MAX_SIZE) {
                scale = (int) Math.pow(
                        2,
                        (int) Math.round(Math.log(IMAGE_MAX_SIZE
                                / (double) Math.max(o.outHeight, o.outWidth))
                                / Math.log(0.5)));
            }

            // Decode with inSampleSize
            BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            fis = new FileInputStream(f);
            b = BitmapFactory.decodeStream(fis, null, o2);
            fis.close();
        } catch (Exception e) {
        }
        return b;
    }

    /**
     * 网络图片保存成本地图片
     *
     * @param picUrl
     */
    public static void savePictureFromNet(final String picUrl, final String name) {
        new Thread() {
            @Override
            public void run() {
                super.run();
                try {
                    File dir = new File(name);
                    if (!dir.exists()) {// Launch camera to take photo for
                        dir.mkdirs();// 创建照片的存储目录
                    }
                    URL url = new URL(picUrl);
                    HttpURLConnection conn = (HttpURLConnection) url
                            .openConnection();
                    conn.setConnectTimeout(5000);
                    // 获取到文件的大小
                    InputStream is = conn.getInputStream();
                    File file = new File(name);
                    FileOutputStream fos = new FileOutputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(is);
                    byte[] buffer = new byte[1024];
                    int len;
                    // int total = 0;
                    while ((len = bis.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                        // total += len;
                    }
                    fos.close();
                    bis.close();
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    public final static String saveNetPic(final String url, final String fileName) {
        try {
            URL myFileUrl = new URL(url);
            HttpURLConnection conn;
            conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();
            InputStream is = conn.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(is);
            String path = OtherFinals.DIR_IMG;
            File dirFile = new File(path);
            if (!dirFile.exists()) {
                dirFile.mkdir();
            }
            myCaptureFile = new File(path + fileName);
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(myCaptureFile));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return myCaptureFile.getAbsolutePath();
    }

    public static String saveFile(Bitmap bm, String fileName) {
        File myCaptureFile = null;
        try {
            String path = OtherFinals.DIR_IMG;
            File dirFile = new File(path);
            if (!dirFile.exists()) {
                dirFile.mkdir();
            }
            myCaptureFile = new File(path + fileName);
            BufferedOutputStream bos = new BufferedOutputStream(
                    new FileOutputStream(myCaptureFile));
            bm.compress(Bitmap.CompressFormat.JPEG, 80, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return myCaptureFile.getAbsolutePath();
    }

    /**
     * 将图片的网络地址转化为唯一的文件名
     *
     * @param url
     * @return
     */
    public static String urlToUuid(String url) {
        return UUID.nameUUIDFromBytes(url.getBytes()).toString();
    }

    /**
     * 将Bitmap转化成byte 图片质量不改变
     *
     * @param bm
     * @return
     */

    public static byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    /**
     * 把字节数组保存为一个文件
     */
    public static File getFileFromBytes(byte[] b, String outputFile) {
        BufferedOutputStream stream = null;
        File file = null;
        try {
            file = new File(outputFile);
            FileOutputStream fstream = new FileOutputStream(file);
            stream = new BufferedOutputStream(fstream);
            stream.write(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * 根据ico名字获取图片id
     *
     * @param activity
     * @param iconName
     * @return
     */

    public static int getResIDByResName(Activity activity, String iconName) {
        Resources resources = activity.getResources();
        int indentify = resources.getIdentifier(activity.getPackageName()
                + ":drawable/" + iconName, null, null);
        return indentify;
    }

    /**
     * 把字节数组保存为bitmap
     */
    public static Bitmap deCodeFromByte(byte[] data) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        return bitmap;
    }

    /**
     * 根据宽高生成新的图片
     *
     * @param filePath
     * @param w
     * @param h
     * @return
     */
    public static Bitmap scaleBitmap(String filePath, int w, int h) {
        Options o = new Options();
        o.inJustDecodeBounds = true;
        Bitmap tmp = BitmapFactory.decodeFile(filePath, o);
        o.inJustDecodeBounds = false;
        int width = (int) Math.ceil(o.outWidth / (float) w);
        int height = (int) Math.ceil(o.outHeight / (float) h);
        if (width > 1 && height > 1) {
            if (height > width) {
                o.inSampleSize = height;
            } else {
                o.inSampleSize = width;
            }
        }
        tmp = BitmapFactory.decodeFile(filePath, o);

        return tmp;
    }

    /**
     * 保存图片
     *
     * @param bitmap
     * @param file
     * @return
     */
    public static boolean saveImg(Bitmap bitmap, File file) {
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.JPEG, 70, out)) {
                out.flush();
                out.close();
            }
            return true;
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 将文件转化成Bitmap
     *
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public static Bitmap getBmp(File file) throws FileNotFoundException {

        if (file == null)
            return null;
        Options o = new Options();
        o.inJustDecodeBounds = true;
        Bitmap tmp = BitmapFactory.decodeFile(file.getAbsolutePath(), o);
        o.inJustDecodeBounds = false;

        int rate = (int) (o.outHeight / (float) o.outWidth);
        if (rate <= 0) {
            rate = 1;
        }
        o.inSampleSize = rate;
        o.inPurgeable = true;
        o.inInputShareable = true;

        tmp = BitmapFactory.decodeFile(file.getAbsolutePath(), o);

        return tmp;
    }

    /**
     * 获得bitmap
     *
     * @param url
     * @return
     */
    // public static Bitmap getBmp(String url) {
    // InputStream in = null;
    // try {
    //
    // in = HttpUtils.createHttpURLConnectionPost(new URL(url))
    // .getInputStream();
    //
    // if (in != null) {
    // Options o = new Options();
    // o.inPreferredConfig = Config.RGB_565;
    // // o.inSampleSize = 2;
    // o.inPurgeable = true;
    // o.inInputShareable = true;
    //
    // return BitmapFactory.decodeStream(in, null, o);
    // }
    //
    // } catch (Exception e) {
    // e.printStackTrace();
    // } finally {
    // if (in != null)
    // try {
    // in.close();
    // } catch (IOException e) {
    // e.printStackTrace();
    // in = null;
    // }
    // }
    // return null;
    // }

    /**
     * 返回圆角图片
     *
     * @param bitmap 需要转换的位图
     * @param pixels 圆角半径
     * @return
     **/
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        if (bitmap == null) {
            return bitmap;
        }

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }

    /**
     * 图片旋转
     *
     * @param bitmap
     * @param degree
     * @return
     */
    public static Bitmap rotate(Bitmap bitmap, int degree) {
        Matrix matrix = new Matrix();
        matrix.postRotate(degree); /* 翻转90度 */
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        return Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
    }

    public static Bitmap createReflectedBitmap(Bitmap srcBitmap) {
        if (null == srcBitmap) {
            return null;
        }

        // The gap between the reflection bitmap and original bitmap.
        final int REFLECTION_GAP = 4;

        int srcWidth = srcBitmap.getWidth();
        int srcHeight = srcBitmap.getHeight();
        int reflectionWidth = srcBitmap.getWidth();
        int reflectionHeight = srcBitmap.getHeight() / 2;

        if (0 == srcWidth || srcHeight == 0) {
            return null;
        }

        // The matrix
        Matrix matrix = new Matrix();
        matrix.preScale(1, -1);

        try {
            // The reflection bitmap, width is same with original's, height is
            // half of original's.
            Bitmap reflectionBitmap = Bitmap.createBitmap(srcBitmap, 0,
                    srcHeight / 2, srcWidth, srcHeight / 2, matrix, false);

            if (null == reflectionBitmap) {
                return null;
            }

            // Create the bitmap which contains original and reflection bitmap.
            Bitmap bitmapWithReflection = Bitmap.createBitmap(reflectionWidth,
                    srcHeight + reflectionHeight + REFLECTION_GAP,
                    Config.ARGB_8888);

            if (null == bitmapWithReflection) {
                return null;
            }

            // Prepare the canvas to draw stuff.
            Canvas canvas = new Canvas(bitmapWithReflection);

            // Draw the original bitmap.
            canvas.drawBitmap(srcBitmap, 0, 0, null);

            // Draw the reflection bitmap.
            canvas.drawBitmap(reflectionBitmap, 0, srcHeight + REFLECTION_GAP,
                    null);

            // srcBitmap.recycle();
            reflectionBitmap.recycle();

            Paint paint = new Paint();
            paint.setAntiAlias(true);
            LinearGradient shader = new LinearGradient(0, srcHeight, 0,
                    bitmapWithReflection.getHeight() + REFLECTION_GAP,
                    0x70FFFFFF, 0x00FFFFFF, TileMode.MIRROR);
            paint.setShader(shader);
            paint.setXfermode(new PorterDuffXfermode(
                    android.graphics.PorterDuff.Mode.DST_IN));

            // Draw the linear shader.
            canvas.drawRect(0, srcHeight, srcWidth,
                    bitmapWithReflection.getHeight() + REFLECTION_GAP, paint);

            return bitmapWithReflection;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取res中的bitmap对象
     *
     * @param paramResources
     * @param res
     * @param width          长度
     * @param height         高度
     * @return bitmap 对象
     */
    public static Bitmap decodeSampledBitmapFromResource(
            Resources paramResources, int res, int width, int height) {
        InputStream localInputStream = paramResources.openRawResource(res);
        BitmapFactory.Options localOptions = new BitmapFactory.Options();
        localOptions.inTempStorage = new byte[TEMP_STORAGE_SIZE];
        localOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        localOptions.inPurgeable = true;
        localOptions.inInputShareable = true;
        localOptions.inSampleSize = calculateInSampleSize(localOptions, width,
                height);
        localOptions.inJustDecodeBounds = false;
        return BitmapFactory.decodeStream(localInputStream, null, localOptions);
    }

    /**
     * 根据屏幕计算缩放比率
     *
     * @param paramOptions
     * @param width        长度
     * @param height       高度
     * @return 缩放比率
     */
    public static int calculateInSampleSize(BitmapFactory.Options paramOptions,
                                            int width, int height) {
        int i = paramOptions.outHeight;
        int j = paramOptions.outWidth;
        int k = 1;
        if ((i > width) || (j > width)) {
            int m = Math.round(i / height);
            int n = Math.round(j / width);
            if (m < n)
                ;
            for (k = m; ; k = n) {
                float f1 = j * i;
                float f2 = 2 * (width * height);
                while (f1 / (k * k) > f2)
                    k++;
            }
        }
        return k;
    }

    /**
     * 手动回收imageView图片资源
     *
     * @param imageView
     */
    public static void releaseImageViewResouce(ImageView imageView) {
        if (imageView == null)
            return;
        Drawable drawable = imageView.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bitmapDrawable = (BitmapDrawable) drawable;
            Bitmap bitmap = bitmapDrawable.getBitmap();
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }

    public static Bitmap compressImage(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, baos);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        long a = 0;
        while ((a = (baos.toByteArray().length / 1024)) > 32) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();//重置baos即清空baos
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);//这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;//每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());//把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);//把ByteArrayInputStream数据生成图片
        return bitmap;
    }

    /**
     * 根据图像URL创建Bitmap
     *
     * @param url URL地址
     * @return bitmap
     */
    public Bitmap CreateImage(String url) {
        Bitmap bitmap = null;
        if (url == null || url.equals("")) {
            return null;
        }
        try {
            FileInputStream fis = new FileInputStream(url);
            BitmapFactory.Options opts = new BitmapFactory.Options();
            opts.inPreferredConfig = Bitmap.Config.ARGB_4444;
            opts.inTempStorage = new byte[100 * 1024];
            opts.inPurgeable = true;
            opts.inInputShareable = true;
            bitmap = BitmapFactory.decodeStream(fis, null, opts);

        } catch (OutOfMemoryError e) {

            System.gc();
        } catch (FileNotFoundException e) {

        }
        return bitmap;
    }

}
