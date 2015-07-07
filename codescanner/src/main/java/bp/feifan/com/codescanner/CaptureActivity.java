package bp.feifan.com.codescanner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;

import java.io.IOException;
import java.util.Vector;

import bp.feifan.com.codescanner.decoding.CaptureActivityHandler;
import bp.feifan.com.codescanner.decoding.ImageDecoder;
import bp.feifan.com.codescanner.decoding.InactivityTimer;
import bp.feifan.com.codescanner.view.ViewfinderView;

public class CaptureActivity extends Fragment implements Callback {
  private static final String TAG = CaptureActivity.class.getSimpleName();
  public static final String ACTION_EXCLUDE_PICTURE =
      "com.wanda.sdk.zxing.CaptureActivity.ACTION_EXCLUDE_PICTURE";
  public static final String SCAN_CODE_TIP = "scan_code_tip";
  public static final String IS_INVISIBLE_TIP_ICON = "is_invisible_tip_icon";
  /** Note:the size is the type of SP */
  public static final String SCAN_CODE_TIP_TEXT_SIZE = "scan_code_tip_text_size";

  public static final int REQUEST_CODE = 1;

  private CaptureActivityHandler handler;
  private ViewfinderView viewfinderView;
  private boolean hasSurface;
  private Vector<BarcodeFormat> decodeFormats;
  private String characterSet;
  private InactivityTimer inactivityTimer;
  private CameraManager mCameraManager;
  private Button mLoadPictureButton;
  private Button mLightButton;
  private TextView scanCodeTipTextView;
  private ProgressDialog mDecodeDialog;

  private Result mResult;

  public static final String CHECK_TYPE = "check_all_orientation";
  private boolean mCheckAllOrientation;

  private View view;
  private LinearLayout mTipView;
  private ImageView mTipIconView;
  private RelativeLayout mBottom;
  private CaptureActivityOfResult captureActivityOfResult;

  private boolean isVisibleTipIcon;

  public void setCaptureActivityOfResult(
      CaptureActivityOfResult captureActivityOfResult) {
    this.captureActivityOfResult = captureActivityOfResult;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
      Bundle savedInstanceState) {
    view = inflater.inflate(R.layout.scan_code_view, container, false);
    // 初始化 CameraManager
    Window window = getActivity().getWindow();
    window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    mCameraManager = new CameraManager();

    hasSurface = false;
    inactivityTimer = new InactivityTimer(getActivity());
    mLoadPictureButton = (Button) view
        .findViewById(R.id.select_code_from_album);

    mLoadPictureButton.setVisibility(View.VISIBLE);
    mLoadPictureButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View arg0) {
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.setType("image/*");
        try {
          startActivityForResult(i, REQUEST_CODE);
        } catch (ActivityNotFoundException e) {}
      }
    });
    mLightButton = (Button) view.findViewById(R.id.open_light);
    mLightButton.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        if (!mCameraManager.isBackTorchOpened()) {
          mCameraManager.openBackTorch();
        } else {
          mCameraManager.closeBackTorch();
        }
      }
    });
    mTipView = (LinearLayout) view.findViewById(R.id.tip_view);
    mTipIconView = (ImageView) view.findViewById(R.id.tip_icon);
    scanCodeTipTextView = (TextView) view
        .findViewById(R.id.scan_code_my_code_tip_in_discount_tv);
    mBottom = (RelativeLayout) view.findViewById(R.id.bottom);
    Bundle bundle = getArguments();
    if (null != bundle) {
      String tip = bundle.getString(SCAN_CODE_TIP);
      boolean needExcludePicture = bundle
          .getBoolean(ACTION_EXCLUDE_PICTURE);
      if (null != tip) {
        scanCodeTipTextView.setText(tip);
      }
      int textSize = bundle.getInt(SCAN_CODE_TIP_TEXT_SIZE);
      if (textSize > 0) {
        scanCodeTipTextView.setTextSize(textSize);
      }
      if (needExcludePicture) {
        mBottom.setVisibility(View.INVISIBLE);
      }
      isVisibleTipIcon = bundle.getBoolean(IS_INVISIBLE_TIP_ICON);
      if (isVisibleTipIcon) {
        mTipIconView.setVisibility(View.GONE);
      }

    }
    mCheckAllOrientation = getActivity().getIntent().getBooleanExtra(
        CHECK_TYPE, false);

    return view;
  }

  @SuppressWarnings("deprecation")
  @Override
  public void onResume() {
    super.onResume();
    viewfinderView = (ViewfinderView) view
        .findViewById(R.id.viewfinder_view);
    viewfinderView.setCameraManager(mCameraManager);
    SurfaceView surfaceView = (SurfaceView) view
        .findViewById(R.id.preview_view);
    SurfaceHolder surfaceHolder = surfaceView.getHolder();

    if (hasSurface) {
      initCamera(surfaceHolder);
    } else {
      surfaceHolder.addCallback(this);
      surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }
    inactivityTimer.onResume();
    decodeFormats = null;
    characterSet = null;
  }

  @Override
  public void onPause() {
    if (handler != null) {
      handler.quitSynchronously();
      handler = null;
    }
    inactivityTimer.onPause();
    mCameraManager.closeDriver();
    if (!hasSurface) {
      SurfaceView surfaceView = (SurfaceView) view
          .findViewById(R.id.preview_view);
      SurfaceHolder surfaceHolder = surfaceView.getHolder();
      surfaceHolder.removeCallback(this);
    }
    super.onPause();
  }

  @Override
  public void onDestroy() {
    if (inactivityTimer != null) {
      inactivityTimer.shutdown();
    }
    super.onDestroy();
  }

  private void initCamera(SurfaceHolder surfaceHolder) {
    try {
      viewfinderView.measure(0, 0);
      int maxWidth = viewfinderView.getWidth();
      int minWidth = maxWidth * 2 / 3;
      int maxHeight = viewfinderView.getHeight();
      mCameraManager.openDriver(surfaceHolder, maxWidth,
          viewfinderView.getHeight());
      mCameraManager.changeOrientationPortrait(true);
      int topOffset = (maxHeight - minWidth) / 3;
      int bottomHeight = mBottom.getHeight();
      int tipHeight = mTipView.getHeight();

      LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTipView
          .getLayoutParams();

      // adjust the display position, the distance is 1/3 blank
      lp.topMargin = topOffset
          + minWidth
          + (maxHeight - topOffset - minWidth - bottomHeight - tipHeight)
          / 3;

      lp.width = minWidth;
      mTipView.setLayoutParams(lp);

      int tipIconWidth = 0;
      if (!isVisibleTipIcon) {
        tipIconWidth = mTipIconView.getWidth();
      }

      int textMaxWidth = minWidth - tipIconWidth
          - scanCodeTipTextView.getPaddingLeft()
          - scanCodeTipTextView.getPaddingRight();
      scanCodeTipTextView.setMaxWidth(textMaxWidth);

      mCameraManager.setManualFramingWidthOffset(minWidth, minWidth,
          topOffset, -1);
      // set zoomscale maybe cause that setParameters failed
      mCameraManager.setZoomScale(0.1f);
    } catch (IOException ioe) {
      ioe.printStackTrace();
      return;
    } catch (RuntimeException e) {
      e.printStackTrace();
      return;
    }
    if (handler == null) {
      handler = new CaptureActivityHandler(this, decodeFormats,
          characterSet, mCameraManager);
    }
  }

  @Override
  public void surfaceChanged(SurfaceHolder holder, int format, int width,
      int height) {

  }

  @Override
  public void surfaceCreated(SurfaceHolder holder) {
    if (!hasSurface) {
      hasSurface = true;
      initCamera(holder);
    }

  }

  @Override
  public void surfaceDestroyed(SurfaceHolder holder) {
    hasSurface = false;

  }

  public ViewfinderView getViewfinderView() {
    return viewfinderView;
  }

  public Handler getHandler() {
    return handler;
  }

  public CameraManager getCameraManager() {
    return mCameraManager;
  }

  public void drawViewfinder() {
    viewfinderView.drawViewfinder();

  }

  public void handleDecode(Result rawResult, Bitmap barcode) {
    Log.d(TAG, "handleDecode() result" + rawResult.getText());
    inactivityTimer.onActivity();
    viewfinderView.drawResultBitmap(barcode);
    if (barcode != null) {
      drawResultPoints(barcode, rawResult);
    }
    mResult = rawResult;
    if (mDecodeDialog != null && mDecodeDialog.isShowing()) {
      mDecodeDialog.dismiss();
    }
    onDecodeEnd();

  }

  /**
   * Superimpose a line for 1D or dots for 2D to highlight the key features of
   * the barcode.
   * 
   * @param barcode
   *          A bitmap of the captured image.
   * @param rawResult
   *          The decoded results which contains the points to draw.
   */
  private void drawResultPoints(Bitmap barcode, Result rawResult) {
    ResultPoint[] points = rawResult.getResultPoints();
    if (points != null && points.length > 0) {
      Canvas canvas = new Canvas(barcode);
      Paint paint = new Paint();
      paint.setColor(getActivity().getResources().getColor(
          R.color.result_image_border));
      paint.setStrokeWidth(3.0f);
      paint.setStyle(Paint.Style.STROKE);
      Rect border = new Rect(2, 2, barcode.getWidth() - 2,
          barcode.getHeight() - 2);
      canvas.drawRect(border, paint);

      paint.setColor(getActivity().getResources().getColor(
          R.color.result_points));
      if (points.length == 2) {
        paint.setStrokeWidth(4.0f);
        drawLine(canvas, paint, points[0], points[1]);
      } else if (points.length == 4
          && (rawResult.getBarcodeFormat() == BarcodeFormat.UPC_A || rawResult
              .getBarcodeFormat() == BarcodeFormat.EAN_13)) {
        // Hacky special case -- draw two lines, for the barcode and
        // metadata
        drawLine(canvas, paint, points[0], points[1]);
        drawLine(canvas, paint, points[2], points[3]);
      } else {
        paint.setStrokeWidth(10.0f);
        for (ResultPoint point : points) {
          canvas.drawPoint(point.getX(), point.getY(), paint);
        }
      }
    }
  }

  private static void drawLine(Canvas canvas, Paint paint, ResultPoint a,
      ResultPoint b) {
    canvas.drawLine(a.getX(), a.getY(), b.getX(), b.getY(), paint);
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
      final Uri uri = data.getData();
      if (uri == null) {
        return;
      }
      showDecodeDialog();
      new Thread() {
        public void run() {
          startDecode(uri);
        }
      }.start();
      return;
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  private void startDecode(Uri uri) {
    mResult = ImageDecoder.decode(getActivity(), uri, mCheckAllOrientation);
    getActivity().runOnUiThread(new Runnable() {
      @Override
      public void run() {
        if (mDecodeDialog != null && mDecodeDialog.isShowing()) {
          mDecodeDialog.dismiss();
        }
        if (mResult == null) {
          Toast.makeText(getActivity(), R.string.decode_fail, Toast.LENGTH_SHORT).show();
        } else {
          onDecodeEnd();
        }
      }
    });
  }

  private void showDecodeDialog() {
    if (mDecodeDialog == null) {
      mDecodeDialog = new ProgressDialog(getActivity());
      mDecodeDialog.setTitle(R.string.scan_cade_dialog_title);
      mDecodeDialog.setMessage(getString(R.string.dialog_message));
    }
    mDecodeDialog.show();
  }

  private void onDecodeEnd() {
    Log.d(TAG, " result=" + mResult.getText());
    if (null != captureActivityOfResult) {
      if (mResult != null) {
        Log.d(TAG, " result=" + mResult.getText());
        captureActivityOfResult.getScanCodeResult(mResult.getText(),
            mResult.getTimestamp(), mResult.getBarcodeFormat()
                .name());
      } else {
        captureActivityOfResult.getScanCodeResult(null, 0, null);
      }
    }
  }

  public boolean checkAllOrientation() {
    return mCheckAllOrientation;
  }
}
