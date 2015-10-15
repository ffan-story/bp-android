package com.feifan.bp.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.util.Log;

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

        if(width < height) { // 如果图片是纵向的（高度大于宽度），则旋转矫正
            img = rotateBitmapByDegree(img, 90);
        }

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

    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm
     *            需要旋转的图片
     * @param degree
     *            旋转角度
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

//    /**
//     * 读取图片的旋转的角度
//     *
//     * @param path
//     *            图片绝对路径
//     * @return 图片的旋转角度
//     */
//    private int getBitmapDegree(String path) {
//        int degree = 0;
//        try {
//            // 从指定路径下读取图片，并获取其EXIF信息
//            ExifInterface exifInterface = new ExifInterface(path);
//            // 获取图片的旋转信息
//            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
//                    ExifInterface.ORIENTATION_NORMAL);
//            switch (orientation) {
//                case ExifInterface.ORIENTATION_ROTATE_90:
//                    degree = 90;
//                    break;
//                case ExifInterface.ORIENTATION_ROTATE_180:
//                    degree = 180;
//                    break;
//                case ExifInterface.ORIENTATION_ROTATE_270:
//                    degree = 270;
//                    break;
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return degree;
//    }
}
