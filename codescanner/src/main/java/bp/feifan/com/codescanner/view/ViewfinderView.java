package bp.feifan.com.codescanner.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.NinePatchDrawable;
import android.util.AttributeSet;
import android.view.View;

import com.google.zxing.ResultPoint;

import java.util.ArrayList;
import java.util.List;

import bp.feifan.com.codescanner.CameraManager;
import bp.feifan.com.codescanner.R;

public final class ViewfinderView extends View {

  private static final long ANIMATION_DELAY = 80L;
  private static final int CURRENT_POINT_OPACITY = 0xA0;
  private static final int MAX_RESULT_POINTS = 20;
  private static final int POINT_SIZE = 6;

  private CameraManager cameraManager;
  private final Paint paint;
  private Bitmap resultBitmap;
  private NinePatchDrawable mFrameDrawable;
  private Bitmap mScanBitmap;
  private Rect mRect;
  private final int maskColor;
  private final int resultColor;
  // private final int resultPointColor;
  private List<ResultPoint> possibleResultPoints;
  // private List<ResultPoint> lastPossibleResultPoints;
  private int mScanY;
  private int mScanHeight;
  private final int mScanOffset = 5;

  // for the bug it will save the ract if getrect is null
  private Rect mCacheRect;

  // This constructor is used when the class is built from an XML resource.
  public ViewfinderView(Context context, AttributeSet attrs) {
    super(context, attrs);

    // Initialize these once for performance rather than calling them every
    // time in onDraw().
    paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    Resources resources = getResources();
    maskColor = resources.getColor(R.color.viewfinder_mask);
    resultColor = resources.getColor(R.color.result_view);
    // resultPointColor = resources.getColor(R.color.possible_result_points);
    possibleResultPoints = new ArrayList<ResultPoint>(5);
    // lastPossibleResultPoints = null;

    mFrameDrawable = (NinePatchDrawable) getResources().getDrawable(
        R.drawable.bitmap_frame);
    mScanBitmap = BitmapFactory.decodeResource(resources,
        R.drawable.bitmap_scan);
    mScanHeight = mScanBitmap.getHeight();
  }

  public void setCameraManager(CameraManager cameraManager) {
    this.cameraManager = cameraManager;
  }

  private Rect getScanFrame() {
    Rect rect = cameraManager.getFramingRect();
    if (rect == null) {
      return mCacheRect;
    }
    if (cameraManager.isPortrait()) {
      int width = this.getWidth();
      mCacheRect = new Rect(width - rect.bottom, rect.left, width
          - rect.top, rect.right);
      return mCacheRect;
    } else {
      mCacheRect = rect;
      return mCacheRect;
    }
  }

  // private Rect getFramingRectInPreview() {
  // Rect frame = cameraManager.getFramingRectInPreview();
  //
  // if (cameraManager.isPortrait() && frame != null) {
  // int width = this.getWidth();
  // return new Rect(width - frame.bottom, frame.left,
  // width - frame.top, frame.right);
  // } else {
  // return frame;
  // }
  // }

  @Override
  public void onDraw(Canvas canvas) {
    if (cameraManager == null) {
      return; // not ready yet, early draw before done configuring
    }
    Rect frame = getScanFrame();
    if (frame == null) {
      return;
    }
    int width = canvas.getWidth();
    int height = canvas.getHeight();

    // Draw the exterior (i.e. outside the framing rect) darkened
    paint.setColor(resultBitmap != null ? resultColor : maskColor);
    canvas.drawRect(0, 0, width, frame.top, paint);
    canvas.drawRect(0, frame.top, frame.left, frame.bottom, paint);
    canvas.drawRect(frame.right, frame.top, width, frame.bottom, paint);
    canvas.drawRect(0, frame.bottom, width, height, paint);

    mRect = new Rect(frame.left - 2, frame.top - 2, frame.right + 2,
        frame.bottom + 2);
    mFrameDrawable.setBounds(mRect);
    mFrameDrawable.draw(canvas);

    if (resultBitmap != null) {
      // Draw the opaque result bitmap over the scanning rectangle
      paint.setAlpha(CURRENT_POINT_OPACITY);
      canvas.drawBitmap(resultBitmap, null, frame, null);
    } else {
      // Draw a scan bitmap
      Rect r = new Rect(frame);
      if (mScanY < frame.top
          || (mScanY + mScanOffset + mScanHeight) > frame.bottom) {
        mScanY = frame.top;
      } else {
        mScanY += mScanOffset;
      }
      r.top = mScanY;
      r.bottom = mScanY + mScanHeight;
      canvas.drawBitmap(mScanBitmap, null, r, null);

      // Comment: remove the draw scan code point in screen
      //
      // Rect previewFrame = getFramingRectInPreview();
      // float scaleX = frame.width() / (float) previewFrame.width();
      // float scaleY = frame.height() / (float) previewFrame.height();
      //
      // List<ResultPoint> currentPossible = possibleResultPoints;
      // List<ResultPoint> currentLast = lastPossibleResultPoints;
      // int frameLeft = frame.left;
      // int frameTop = frame.top;
      // if (currentPossible.isEmpty()) {
      // lastPossibleResultPoints = null;
      // } else {
      // possibleResultPoints = new ArrayList<ResultPoint>(5);
      // lastPossibleResultPoints = currentPossible;
      // paint.setAlpha(CURRENT_POINT_OPACITY);
      // paint.setColor(resultPointColor);
      // synchronized (currentPossible) {
      // for (ResultPoint point : currentPossible) {
      // canvas.drawCircle(frameLeft
      // + (int) (point.getX() * scaleX), frameTop
      // + (int) (point.getY() * scaleY), POINT_SIZE,
      // paint);
      // }
      // }
      // }
      // if (currentLast != null) {
      // paint.setAlpha(CURRENT_POINT_OPACITY / 2);
      // paint.setColor(resultPointColor);
      // synchronized (currentLast) {
      // float radius = POINT_SIZE / 2.0f;
      // for (ResultPoint point : currentLast) {
      // canvas.drawCircle(frameLeft
      // + (int) (point.getX() * scaleX), frameTop
      // + (int) (point.getY() * scaleY), radius, paint);
      // }
      // }
      // }
      postInvalidateDelayed(ANIMATION_DELAY, frame.left - POINT_SIZE,
          frame.top - POINT_SIZE, frame.right + POINT_SIZE,
          frame.bottom + POINT_SIZE);
    }
  }

  public void drawViewfinder() {
    Bitmap resultBitmap = this.resultBitmap;
    this.resultBitmap = null;
    if (resultBitmap != null) {
      resultBitmap.recycle();
    }
    invalidate();
  }

  /**
   * Draw a bitmap with the result points highlighted instead of the live
   * scanning display.
   * 
   * @param barcode
   *          An image of the decoded barcode.
   */
  public void drawResultBitmap(Bitmap barcode) {
    resultBitmap = barcode;
    invalidate();
  }

  public void addPossibleResultPoint(ResultPoint point) {
    List<ResultPoint> points = possibleResultPoints;
    synchronized (points) {
      points.add(point);
      int size = points.size();
      if (size > MAX_RESULT_POINTS) {
        // trim it
        points.subList(0, size - MAX_RESULT_POINTS / 2).clear();
      }
    }
  }

}
