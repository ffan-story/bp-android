package com.feifan.bp.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xuchunlei on 15/10/12.
 */
public class ImageUtil {
    private static final String TAG = ImageUtil.class.getSimpleName();
    private ImageUtil(){

    }

    /**
     * 将位图按指定尺寸转换为流
     * @param img
     * @param dstWidth
     * @param dstHeight
     * @return
     */
    public static InputStream makeStream(Bitmap img, int dstWidth, int dstHeight, int maxBytes) {
        int width = img.getWidth();
        int height = img.getHeight();

        if(width > dstWidth || height > dstHeight) {

            img = Bitmap.createScaledBitmap(img, dstWidth, dstHeight, true);
        }
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();

        img.compress(Bitmap.CompressFormat.JPEG, 100, byteStream);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 100;
        while ( byteStream.toByteArray().length / 1024 > maxBytes) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            byteStream.reset();
            options -= 10;//每次都减少10
            img.compress(Bitmap.CompressFormat.JPEG, options, byteStream);//这里压缩options%，把压缩后的数据存放到baos中
        }

        LogUtil.i(TAG, "Make " + img.getWidth() + "x" + img.getHeight() + " " + byteStream.size() / 1024 + "k image to stream");
        InputStream in = new ByteArrayInputStream(byteStream.toByteArray());
        return in;
    }

    /*** * 读取图片属性：旋转的角度
    * @param path 图片绝对路径
    * @return degree旋转的角度
    */
    public static int readPictureDegree(String path) {
        int degree  = 0;
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
    /*
     * 旋转图片
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();;
        matrix.postRotate(angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }
}
