package bp.feifan.com.codescanner.decoding;

import android.content.Context;
import android.content.res.Resources.NotFoundException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.RGBLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.common.HybridBinarizer;

import java.io.FileNotFoundException;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.Vector;

public class ImageDecoder {
  private Map<DecodeHintType, Object> hints;
  private MultiFormatReader multiFormatReader;
  private float mImageMaxWidth;

  private ImageDecoder(Collection<BarcodeFormat> decodeFormats,
      String characterSet, ResultPointCallback resultPointCallback,
      float width) {
    hints = new EnumMap<DecodeHintType, Object>(DecodeHintType.class);

    if (decodeFormats == null || decodeFormats.size() == 0) {
      decodeFormats = new Vector<BarcodeFormat>();
      decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
      decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
      decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
    }
    hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

    if (characterSet != null) {
      hints.put(DecodeHintType.CHARACTER_SET, characterSet);
    }

    multiFormatReader = new MultiFormatReader();
    multiFormatReader.setHints(hints);
    mImageMaxWidth = width;
  }

  private Result startDecode(int w, int h, Bitmap b) {
    if (b == null) {
      return null;
    }
    Result rawResult = null;

    int[] pixs = new int[w * h];
    b.getPixels(pixs, 0, w, 0, 0, w, h);
    RGBLuminanceSource source = new RGBLuminanceSource(w, h, pixs);
    if (source != null) {
      try {
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(
            source));
        rawResult = multiFormatReader.decodeWithState(bitmap);
      } catch (ReaderException re) {
        re.printStackTrace();
        // continue
      } catch (NotFoundException e) {
        // continue
      } finally {
        multiFormatReader.reset();
      }
    }

    return rawResult;
  }

  private Result decode(Bitmap b, boolean CheckAll) {
    int w = b.getWidth();
    int h = b.getHeight();
    if (w > mImageMaxWidth) {
      h = (int) (mImageMaxWidth / w * h);
      w = (int) mImageMaxWidth;
    }
    Bitmap target = Bitmap.createScaledBitmap(b, w, h, true);
    Result rawResult = startDecode(w, h, target);
    if (rawResult == null && CheckAll) {
      Matrix m = new Matrix();
      m.setRotate(90);
      Bitmap tar = Bitmap.createBitmap(target, 0, 0, w, h, m, true);
      rawResult = startDecode(tar.getWidth(), tar.getHeight(), tar);
    }
    return rawResult;
  }

  public static Result decode(Context context, Uri uri, boolean checkAll) {
    return decode(context, uri, null, null, null, checkAll);
  }

  public static Result decode(Context context, Uri uri,
      Collection<BarcodeFormat> decodeFormats, String characterSet,
      ResultPointCallback resultPointCallback, boolean checkAll) {
    try {
      Bitmap b = BitmapFactory.decodeStream(context.getContentResolver()
          .openInputStream(uri));
      return decode(context, b, null, null, null, checkAll);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static Result decode(Context context, Bitmap bitmap, boolean checkAll) {
    return decode(context, bitmap, null, null, null, checkAll);
  }

  public static Result decode(Context context, Bitmap bitmap,
      Collection<BarcodeFormat> decodeFormats, String characterSet,
      ResultPointCallback resultPointCallback, boolean checkAll) {
    if (bitmap == null) {
      return null;
    }
    WindowManager manager = (WindowManager) context
        .getSystemService(Context.WINDOW_SERVICE);
    DisplayMetrics metrics = new DisplayMetrics();
    manager.getDefaultDisplay().getMetrics(metrics);
    ImageDecoder decode = new ImageDecoder(decodeFormats, characterSet,
        resultPointCallback, metrics.widthPixels * 2 / 3);
    return decode.decode(bitmap, checkAll);
  }

}
