package bp.feifan.com.codescanner.utils;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.Writer;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.oned.CodaBarWriter;
import com.google.zxing.oned.Code128Writer;
import com.google.zxing.oned.Code39Writer;
import com.google.zxing.oned.EAN13Writer;
import com.google.zxing.oned.EAN8Writer;
import com.google.zxing.oned.UPCAWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

public class BarcodeUtils {

  public static Bitmap getEncodedBitmap(
      final BarcodeFormat format, final String input, final int w,
      final int h) {

    Writer CODE_WRITER = null;
    if (BarcodeFormat.CODABAR == format) {
      CODE_WRITER = new CodaBarWriter();
    } else if (BarcodeFormat.CODE_39 == format) {
      CODE_WRITER = new Code39Writer();
    } else if (BarcodeFormat.CODE_128 == format) {
      CODE_WRITER = new Code128Writer();
    } else if (BarcodeFormat.EAN_13 == format) {
      CODE_WRITER = new EAN13Writer();
    } else if (BarcodeFormat.EAN_8 == format) {
      CODE_WRITER = new EAN8Writer();
    } else if (BarcodeFormat.UPC_A == format) {
      CODE_WRITER = new UPCAWriter();
    } else {
      throw new IllegalArgumentException();
    }
    try {
      final Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
      hints.put(EncodeHintType.MARGIN, 10);
      hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
      final BitMatrix result = CODE_WRITER.encode(input, format, w, h,
          hints);

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
