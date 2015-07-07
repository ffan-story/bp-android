package bp.feifan.com.codescanner.utils;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.pdf417.encoder.PDF417Writer;

import java.util.Hashtable;

public class PDF417CodeUtils {

  public static Bitmap getEncodedBitmap(final String input, final int w, final int h) {
    final PDF417Writer PDF417_CODE_WRITER = new PDF417Writer();
    try {
      final Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
      hints.put(EncodeHintType.ERROR_CORRECTION,
          EncodeHintType.PDF417_COMPACT);
      final BitMatrix result = PDF417_CODE_WRITER.encode(input,
          BarcodeFormat.PDF_417, w, h, hints);

      final int width = result.getWidth();
      final int height = result.getHeight();
      final int[] pixels = new int[width * height];

      for (int y = 0; y < height; y++) {
        final int offset = y * width;
        for (int x = 0; x < width; x++) {
          pixels[offset + x] = result.get(x, y) ? Color.BLACK
              : Color.TRANSPARENT;
        }
      }

      final Bitmap bitmap = Bitmap.createBitmap(width, height,
          Bitmap.Config.ARGB_8888);
      bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
      return bitmap;
    } catch (final WriterException e) {
      return null;
    }
  }
}
