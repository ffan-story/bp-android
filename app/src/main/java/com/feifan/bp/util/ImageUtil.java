package com.feifan.bp.util;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
        if(img.getWidth() > dstWidth || img.getHeight() > dstHeight) {
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
}
