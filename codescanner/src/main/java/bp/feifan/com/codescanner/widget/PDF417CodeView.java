package bp.feifan.com.codescanner.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;

import bp.feifan.com.codescanner.utils.PDF417CodeUtils;


public class PDF417CodeView extends CodeView {

  public PDF417CodeView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
  }

  public PDF417CodeView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public PDF417CodeView(Context context) {
    this(context, null);
  }

  @Override
  protected Bitmap getCodeBitmap() {
    Bitmap bitmap = null;
    try {
      bitmap = PDF417CodeUtils.getEncodedBitmap(mCode, mWidth, mHeight);
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
