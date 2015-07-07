package bp.feifan.com.codescanner;

public interface CaptureActivityOfResult {

  void getScanCodeResult(String resultText, long timeStamp,
                         String barcodeFormat);

}
