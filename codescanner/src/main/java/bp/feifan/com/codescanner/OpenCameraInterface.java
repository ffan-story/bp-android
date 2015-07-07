package bp.feifan.com.codescanner;

import android.hardware.Camera;

/**
 * Provides an abstracted means to open a {@link Camera}. The API changes over
 * Android API versions and this allows the app to use newer API methods while
 * retaining backwards-compatible behavior.
 */
public interface OpenCameraInterface {

  Camera open();

  Camera open(int cameraId);

}
