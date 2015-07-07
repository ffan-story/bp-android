package bp.feifan.com.codescanner.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.ImageView;

public abstract class CodeView extends ImageView {
  protected String mCode;
  protected BitmapDrawable mCodeBitmapDrawable;
  protected int mWidth;
  protected int mHeight;

  protected CodeView(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context, attrs, defStyle);
  }

  protected CodeView(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
    init(context, attrs, 0);
  }

  protected CodeView(Context context) {
    this(context, null);
    init(context, null, 0);
  }

  private void init(Context context, AttributeSet attrs, int defStyle) {
    setScaleType(ScaleType.FIT_CENTER);
  }

  @Override
  protected void onDetachedFromWindow() {
    recycleCodeBitmap();
    super.onDetachedFromWindow();
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int width = getMeasuredWidth();
    int height = getMeasuredHeight();
    if (width > 0 && height > 0) {
      measureCodeSize(width, height);
      setMeasuredDimension(mWidth, mHeight);

      if (!TextUtils.isEmpty(mCode) && mCodeBitmapDrawable == null) {
        updateCodeBitmap();
      }
    }
  }

  public void setCode(String code) {
    if (TextUtils.isEmpty(code) || !isValid(code)) {
      return;
    }

    if (mCode == null || !mCode.equals(code)) {
      recycleCodeBitmap();
      mCode = code;
    }
    requestLayout();
  }

  private void updateCodeBitmap() {
    if (mWidth > 0 && mHeight > 0) {
      recycleCodeBitmap();
      if (!TextUtils.isEmpty(mCode)) {
        Bitmap bitmap = getCodeBitmap();
        if (bitmap != null) {
          mCodeBitmapDrawable = new BitmapDrawable(getResources(),
              bitmap);
          setImageDrawable(mCodeBitmapDrawable);
        }
      }
    }
  }

  private void recycleCodeBitmap() {
    setImageDrawable(null);
    if (mCodeBitmapDrawable != null) {
      mCodeBitmapDrawable.setCallback(null);
      if (mCodeBitmapDrawable.getBitmap() != null
          && !mCodeBitmapDrawable.getBitmap().isRecycled()) {
        mCodeBitmapDrawable.getBitmap().recycle();
      }
    }
    mCodeBitmapDrawable = null;
  }

  protected abstract boolean isValid(String code);

  protected abstract Bitmap getCodeBitmap();

  protected abstract void measureCodeSize(int width, int height);
}
