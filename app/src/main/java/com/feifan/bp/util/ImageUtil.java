package com.feifan.bp.util;

import android.graphics.Bitmap;

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
    public static InputStream makeStream(Bitmap img, int dstWidth, int dstHeight) {
        if(img.getWidth() > dstWidth || img.getHeight() > dstHeight) {
            img = Bitmap.createScaledBitmap(img, dstWidth, dstHeight, true);
        }
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG, 100, byteStream);
        LogUtil.i(TAG, "Make " + img.getWidth() + "x" + img.getHeight() + " " + img.getByteCount() + " image to stream");
        InputStream in = new ByteArrayInputStream(byteStream.toByteArray());
        return in;
    }
}
