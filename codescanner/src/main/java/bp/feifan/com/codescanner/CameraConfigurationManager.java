package bp.feifan.com.codescanner;

import android.graphics.Point;
import android.hardware.Camera;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * A class which deals with reading, parsing, and setting the camera parameters
 * which are used to configure the camera hardware.
 */
final class CameraConfigurationManager {

  private static final String TAG = CameraConfigurationManager.class.getSimpleName();

  // This is bigger than the size of a small screen, which is still supported.
  // The routine
  // below will still select the default (presumably 320x240) size for these.
  // This prevents
  // accidental selection of very low resolution on some devices.
  private static final int MIN_PREVIEW_PIXELS = 470 * 320; // normal screen
  private static final int MAX_PREVIEW_PIXELS = 1280 * 720;

  private Point screenResolution;
  private Point cameraResolution;

  CameraConfigurationManager() {}

  /**
   * Reads, one time, values from the camera that are needed by the app.
   */
  void initFromCameraParameters(Camera camera, int width, int height) {
    Camera.Parameters parameters = camera.getParameters();
    // We're landscape-only, and have apparently seen issues with display
    // thinking it's portrait
    // when waking from sleep. If it's not landscape, assume it's mistaken
    // and reverse them:
    if (width < height) {
      int temp = width;
      width = height;
      height = temp;
    }
    screenResolution = new Point(width, height);
    cameraResolution = findBestPreviewSizeValue(parameters,
        screenResolution);
  }

  void setDesiredCameraParameters(Camera camera, boolean enableAutoFocus,
      boolean enableContinuousFocus, boolean enableFrontLight, boolean enableInvertScan,
      boolean safeMode) {
    Camera.Parameters parameters = camera.getParameters();

    if (parameters == null) {
      Log.w(TAG,
              "Device error: no camera parameters are available. Proceeding without configuration.");
      return;
    }

    initializeTorch(parameters, enableFrontLight, safeMode);

    String focusMode = null;
    if (enableAutoFocus) {
      if (safeMode || enableContinuousFocus) {
        focusMode = findSettableValue(
            parameters.getSupportedFocusModes(),
            Camera.Parameters.FOCUS_MODE_AUTO);
      } else {
        focusMode = findSettableValue(
            parameters.getSupportedFocusModes(),
            "continuous-picture", // Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
                                  // in 4.0+
            "continuous-video", // Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO
                                // in 4.0+
            Camera.Parameters.FOCUS_MODE_AUTO);
      }
    }
    // Maybe selected auto-focus but not available, so fall through here:
    if (!safeMode && focusMode == null) {
      focusMode = findSettableValue(parameters.getSupportedFocusModes(),
          Camera.Parameters.FOCUS_MODE_MACRO, "edof"); // Camera.Parameters.FOCUS_MODE_EDOF
                                                       // in 2.2+
    }
    if (focusMode != null) {
      parameters.setFocusMode(focusMode);
    }

    if (enableInvertScan) {
      String colorMode = findSettableValue(parameters.getSupportedColorEffects(),
          Camera.Parameters.EFFECT_NEGATIVE);
      if (colorMode != null) {
        parameters.setColorEffect(colorMode);
      }
    }

    parameters.setPreviewSize(cameraResolution.x, cameraResolution.y);
    camera.setParameters(parameters);
  }

  Point getCameraResolution() {
    return cameraResolution;
  }

  Point getScreenResolution() {
    return screenResolution;
  }

  boolean getTorchState(Camera camera) {
    if (camera != null) {
      Camera.Parameters parameters = camera.getParameters();
      if (parameters != null) {
        String flashMode = camera.getParameters().getFlashMode();
        return flashMode != null &&
            (Camera.Parameters.FLASH_MODE_ON.equals(flashMode) ||
            Camera.Parameters.FLASH_MODE_TORCH.equals(flashMode));
      }
    }
    return false;
  }

  void setTorch(Camera camera, boolean newSetting) {
    Camera.Parameters parameters = camera.getParameters();
    doSetTorch(parameters, newSetting, false);
    camera.setParameters(parameters);
  }

  private void initializeTorch(Camera.Parameters parameters,
      boolean enableFrontLight, boolean safeMode) {
    doSetTorch(parameters, enableFrontLight, safeMode);
  }

  private void doSetTorch(Camera.Parameters parameters, boolean newSetting, boolean safeMode) {
    String flashMode;
    if (newSetting) {
      flashMode = findSettableValue(parameters.getSupportedFlashModes(),
          Camera.Parameters.FLASH_MODE_TORCH,
          Camera.Parameters.FLASH_MODE_ON);
    } else {
      flashMode = findSettableValue(parameters.getSupportedFlashModes(),
          Camera.Parameters.FLASH_MODE_OFF);
    }
    if (flashMode != null) {
      parameters.setFlashMode(flashMode);
    }

    if (!safeMode) {
      setExposure(parameters, newSetting);
    }
    // MX2 may cause setCameraParams Exception
  }

  private static final float MAX_EXPOSURE_COMPENSATION = 1.5f;
  private static final float MIN_EXPOSURE_COMPENSATION = 0.0f;

  private void setExposure(Camera.Parameters parameters, boolean lightOn) {
    int minExposure = parameters.getMinExposureCompensation();
    int maxExposure = parameters.getMaxExposureCompensation();
    if (minExposure != 0 || maxExposure != 0) {
      float step = parameters.getExposureCompensationStep();
      int desiredCompensation;
      if (lightOn) {
        // Light on; set low exposue compensation
        desiredCompensation = Math.max(
            (int) (MIN_EXPOSURE_COMPENSATION / step), minExposure);
      } else {
        // Light off; set high compensation
        desiredCompensation = Math.min(
            (int) (MAX_EXPOSURE_COMPENSATION / step), maxExposure);
      }
      Log.i(TAG, "Setting exposure compensation to "
          + desiredCompensation + " / "
          + (step * desiredCompensation));
      parameters.setExposureCompensation(desiredCompensation);
    } else {
      Log.i(TAG, "Camera does not support exposure compensation");
    }
  }

  private Point findBestPreviewSizeValue(Camera.Parameters parameters,
      Point screenResolution) {
    List<Camera.Size> rawSupportedSizes = parameters.getSupportedPreviewSizes();
    if (rawSupportedSizes == null) {
      Camera.Size defaultSize = parameters.getPreviewSize();
      return new Point(defaultSize.width, defaultSize.height);
    }

    // Sort by size, descending
    List<Camera.Size> supportedPreviewSizes = new ArrayList<Camera.Size>(rawSupportedSizes);
    Collections.sort(supportedPreviewSizes, new Comparator<Camera.Size>() {
      @Override
      public int compare(Camera.Size a, Camera.Size b) {
        int aPixels = a.height * a.width;
        int bPixels = b.height * b.width;
        if (bPixels < aPixels) {
          return -1;
        }
        if (bPixels > aPixels) {
          return 1;
        }
        return 0;
      }
    });

    Point bestSize = null;
    float screenAspectRatio = (float) screenResolution.x
        / (float) screenResolution.y;

    float diff = Float.POSITIVE_INFINITY;
    for (Camera.Size supportedPreviewSize : supportedPreviewSizes) {
      int realWidth = supportedPreviewSize.width;
      int realHeight = supportedPreviewSize.height;
      int pixels = realWidth * realHeight;
      if (pixels < MIN_PREVIEW_PIXELS || pixels > MAX_PREVIEW_PIXELS) {
        continue;
      }
      boolean isCandidatePortrait = realWidth < realHeight;
      int maybeFlippedWidth = isCandidatePortrait ? realHeight
          : realWidth;
      int maybeFlippedHeight = isCandidatePortrait ? realWidth
          : realHeight;
      if (maybeFlippedWidth == screenResolution.x
          && maybeFlippedHeight == screenResolution.y) {
        Point exactPoint = new Point(realWidth, realHeight);
        return exactPoint;
      }
      float aspectRatio = (float) maybeFlippedWidth
          / (float) maybeFlippedHeight;
      float newDiff = Math.abs(aspectRatio - screenAspectRatio);
      if (newDiff < diff) {
        bestSize = new Point(realWidth, realHeight);
        diff = newDiff;
      }
    }

    if (bestSize == null) {
      Camera.Size defaultSize = parameters.getPreviewSize();
      bestSize = new Point(defaultSize.width, defaultSize.height);
    }
    return bestSize;
  }

  private static String findSettableValue(Collection<String> supportedValues,
      String... desiredValues) {
    String result = null;
    if (supportedValues != null) {
      for (String desiredValue : desiredValues) {
        if (supportedValues.contains(desiredValue)) {
          result = desiredValue;
          break;
        }
      }
    }
    return result;
  }
}
