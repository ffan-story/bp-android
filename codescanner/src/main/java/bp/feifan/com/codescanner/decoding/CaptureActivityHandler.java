package bp.feifan.com.codescanner.decoding;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.google.zxing.BarcodeFormat;

import java.util.Collection;

import bp.feifan.com.codescanner.CameraManager;
import bp.feifan.com.codescanner.CodeScannerFragment;
import bp.feifan.com.codescanner.Contents;
import bp.feifan.com.codescanner.view.ViewfinderResultPointCallback;

/**
 * This class handles all the messaging which comprises the state machine for
 * capture.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class CaptureActivityHandler extends Handler {

  private static final String TAG = CaptureActivityHandler.class
      .getSimpleName();

  private final CodeScannerFragment fragment;
  private final DecodeThread decodeThread;
  private State state;
  private final CameraManager cameraManager;

  private enum State {
    PREVIEW, SUCCESS, DONE
  }

  public CaptureActivityHandler(CodeScannerFragment fragment,
      Collection<BarcodeFormat> decodeFormats, String characterSet,
      CameraManager cameraManager) {
    this.fragment = fragment;
    decodeThread = new DecodeThread(fragment, decodeFormats, characterSet,
        new ViewfinderResultPointCallback(null));
    decodeThread.start();
    state = State.SUCCESS;

    // Start ourselves capturing previews and decoding.
    this.cameraManager = cameraManager;
    cameraManager.startPreview();
    restartPreviewAndDecode();
  }

  @Override
  public void handleMessage(Message message) {
    switch (message.what) {
      case Contents.Message.RESTART_PREVIEW:
        Log.d(TAG, "Got restart preview message");
        restartPreviewAndDecode();
        break;
      case Contents.Message.DECODE_SUCCEEDED:
        Log.d(TAG, "Got decode succeeded message");
        state = State.SUCCESS;
        Bundle bundle = message.getData();
        Bitmap barcode = bundle == null ? null : (Bitmap) bundle
            .getParcelable(DecodeThread.BARCODE_BITMAP);
//        fragment.handleDecode((Result) message.obj, barcode);
        break;
      case Contents.Message.DECODE_FAILED:
        // We're decoding as fast as possible, so when one decode fails,
        // start another.
        state = State.PREVIEW;
        cameraManager.requestPreviewFrame(decodeThread.getHandler(),
            Contents.Message.DECODE);
        break;
      case Contents.Message.RETURN_SCAN_RESULT:
        Log.d(TAG, "Got return scan result message");
        FragmentActivity fragmentActivity = fragment.getActivity();
        if (null != fragmentActivity) {
          fragmentActivity.setResult(Activity.RESULT_OK,
              (Intent) message.obj);
          fragmentActivity.finish();
        }
        break;
      case Contents.Message.LAUNCH_PRODUCT_QUERY:
        Log.d(TAG, "Got product query message");
        String url = (String) message.obj;
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        fragment.startActivity(intent);
        break;
    }
  }

  public void quitSynchronously() {
    state = State.DONE;
    cameraManager.stopPreview();
    Message quit = Message.obtain(decodeThread.getHandler(),
        Contents.Message.QUIT);
    quit.sendToTarget();
    try {
      // Wait at most half a second; should be enough time, and onPause()
      // will timeout quickly
      decodeThread.join(500L);
    } catch (InterruptedException e) {
      // continue
    }

    // Be absolutely sure we don't send any queued up messages
    removeMessages(Contents.Message.DECODE_SUCCEEDED);
    removeMessages(Contents.Message.DECODE_FAILED);
  }

  private void restartPreviewAndDecode() {
    if (state == State.SUCCESS) {
      state = State.PREVIEW;
      cameraManager.requestPreviewFrame(decodeThread.getHandler(),
          Contents.Message.DECODE);
//      fragment.drawViewfinder();
    }
  }

}
