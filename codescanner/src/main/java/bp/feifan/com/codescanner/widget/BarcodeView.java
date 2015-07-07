package bp.feifan.com.codescanner.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;

import com.google.zxing.BarcodeFormat;

import bp.feifan.com.codescanner.utils.BarcodeUtils;

public class BarcodeView extends CodeView {
  public enum Format {
    CODABAR, CODE_39, CODE_128, EAN_13, EAN_8, UPC_A
  }

  protected BarcodeFormat mFormat;

  public BarcodeView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public BarcodeView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public BarcodeView(Context context) {
    this(context, null);
  }

  public void setBarcodeFormat(Format format) {
    switch (format) {
      case CODABAR:
        mFormat = BarcodeFormat.CODABAR;
        break;
      case CODE_39:
        mFormat = BarcodeFormat.CODE_39;
        break;
      case CODE_128:
        mFormat = BarcodeFormat.CODE_128;
        break;
      case EAN_13:
        mFormat = BarcodeFormat.EAN_13;
        break;
      case EAN_8:
        mFormat = BarcodeFormat.EAN_8;
        break;
      case UPC_A:
        mFormat = BarcodeFormat.UPC_A;
        break;
      default:
        throw new IllegalArgumentException();
    }
  }

  @Override
  protected Bitmap getCodeBitmap() {
    Bitmap bitmap = null;
    try {
      bitmap = BarcodeUtils.getEncodedBitmap(mFormat, mCode, mWidth,
              mHeight);
    } catch (OutOfMemoryError e) {}
    return bitmap;
  }

  @Override
  protected void measureCodeSize(int width, int height) {
    mWidth = width;
    mHeight = height;
  }

  @Override
  protected boolean isValid(String code) {
    // TODO Auto-generated method stub
    return true;
  }
}
