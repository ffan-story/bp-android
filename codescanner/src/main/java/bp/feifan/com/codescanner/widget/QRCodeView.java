package bp.feifan.com.codescanner.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;

import bp.feifan.com.codescanner.utils.QRCodeUtils;


public class QRCodeView extends CodeView {

  private Bitmap mCenterLogoBmp;
  private Paint mPaint;

  public QRCodeView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public QRCodeView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public QRCodeView(Context context) {
    this(context, null);
  }

  public void setCenterLogoBitmap(Bitmap bitmap) {
    mCenterLogoBmp = bitmap;
    mPaint = new Paint();
  }

  @Override
  protected Bitmap getCodeBitmap() {
    Bitmap bitmap = null;
    try {
      bitmap = QRCodeUtils.getEncodedBitmap(mCode, mWidth);
      if (mCenterLogoBmp != null) {
        int logoWidth = mCenterLogoBmp.getWidth();
        int logoHeight = mCenterLogoBmp.getHeight();
        int x = (bitmap.getWidth() - logoWidth) / 2;
        int y = (bitmap.getHeight() - logoHeight) / 2;
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(mCenterLogoBmp, x, y, mPaint);
      }

    } catch (OutOfMemoryError e) {}
    return bitmap;
  }

  @Override
  protected void measureCodeSize(int width, int height) {
    mWidth = width > height ? height : width;
    mHeight = mWidth;
  }

  @Override
  protected boolean isValid(String code) {
    // TODO Auto-generated method stub
    return true;
  }
}
