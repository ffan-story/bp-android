package bp.feifan.com.codescanner.decoding;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;

import java.util.Map;

import bp.feifan.com.codescanner.CaptureActivity;
import bp.feifan.com.codescanner.Contents;

final class DecodeHandler extends Handler {

  private static final String TAG = DecodeHandler.class.getSimpleName();

  private final CaptureActivity activity;

  private final MultiFormatReader multiFormatReader;
  private boolean running = true;

  DecodeHandler(CaptureActivity activity, Map<DecodeHintType, Object> hints) {
    multiFormatReader = new MultiFormatReader();
    multiFormatReader.setHints(hints);
    this.activity = activity;
  }

  @Override
  public void handleMessage(Message message) {
    if (!running) {
      return;
    }
    switch (message.what) {
      case Contents.Message.DECODE:
        decode((byte[]) message.obj, message.arg1, message.arg2);
        break;
      case Contents.Message.QUIT:
        running = false;
        Looper.myLooper().quit();
        break;
    }
  }

  /**
   * A factory method to build the appropriate LuminanceSource object based on
   * the format of the preview buffers, as described by Camera.Parameters.
   * 
   * @param data
   *          A preview frame.
   * @param width
   *          The width of the image.
   * @param height
   *          The height of the image.
   * @param isPortrait
   *          The orientation of the image
   * @return A PlanarYUVLuminanceSource instance.
   */
  public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data,
      int width, int height, boolean isPortrait) {
    Rect rect = activity.getCameraManager().getFramingRectInPreview();
    if (rect == null) {
      return null;
    }

    if (isPortrait) {
      return new PlanarYUVLuminanceSource(data, height, width, height
          - rect.bottom, rect.left, rect.height(), rect.width(),
          false);
    } else {
      return new PlanarYUVLuminanceSource(data, width, height, rect.left,
          rect.top, rect.width(), rect.height(), false);
    }
  }

  /**
   * Decode the data within the viewfinder rectangle, and time how long it
   * took. For efficiency, reuse the same reader objects from one decode to
   * the next.
   * 
   * @param data
   *          The YUV preview frame.
   * @param width
   *          The width of the preview frame.
   * @param height
   *          The height of the preview frame.
   */
  private void decode(byte[] data, int width, int height) {
    long start = System.currentTimeMillis();
    byte[] target = new byte[height * width];
    for (int i = 0; i < width; i++) {
      for (int j = 0; j < height; j++) {
        target[i * height + j] = data[(height - 1 - j) * width + i];
      }
    }
    Result rawResult = null;
    PlanarYUVLuminanceSource source = buildLuminanceSource(target, width,
        height, true);
    rawResult = startDecode(source);

    if (rawResult == null && activity.checkAllOrientation()) {
      source = buildLuminanceSource(data, width, height, false);
      rawResult = startDecode(source);
    }

    Handler handler = activity.getHandler();
    if (rawResult != null) {
      // Don't log the barcode contents for security.
      long end = System.currentTimeMillis();
      Log.d(TAG, "Found barcode in " + (end - start) + " ms");
      if (handler != null) {
        Message message = Message.obtain(handler,
            Contents.Message.DECODE_SUCCEEDED, rawResult);
        Bundle bundle = new Bundle();
        Bitmap grayscaleBitmap = toBitmap(source,
            source.renderCroppedGreyscaleBitmap());
        bundle.putParcelable(DecodeThread.BARCODE_BITMAP,
            grayscaleBitmap);
        message.setData(bundle);
        message.sendToTarget();
      }
    } else {
      if (handler != null) {
        Message message = Message.obtain(handler,
            Contents.Message.DECODE_FAILED);
        message.sendToTarget();
      }
    }
  }

  private Result startDecode(LuminanceSource source) {
    if (source != null) {
      BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
      try {
        return multiFormatReader.decodeWithState(bitmap);
      } catch (ReaderException re) {
        // continue
      } finally {
        multiFormatReader.reset();
      }
    }
    return null;
  }

  private static Bitmap toBitmap(LuminanceSource source, int[] pixels) {
    int width = source.getWidth();
    int height = source.getHeight();
    Bitmap bitmap = Bitmap.createBitmap(width, height,
        Bitmap.Config.ARGB_8888);
    bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
    return bitmap;
  }

}
