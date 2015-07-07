package bp.feifan.com.codescanner.utils;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import java.util.Hashtable;

public class QRCodeUtils {

  public static Bitmap getEncodedBitmap(final String input, final int size) {
    final QRCodeWriter QR_CODE_WRITER = new QRCodeWriter();
    try {
      final Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
      hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
      final BitMatrix result = QR_CODE_WRITER.encode(input,
          BarcodeFormat.QR_CODE, size, size, hints);

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
