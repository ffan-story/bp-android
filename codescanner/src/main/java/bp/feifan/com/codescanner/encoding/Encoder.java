package bp.feifan.com.codescanner.encoding;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;


import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.Map;

import bp.feifan.com.codescanner.Contents;

public class Encoder {

  private static final int WHITE = 0xFFFFFFFF;
  private static final int BLACK = 0xFF000000;

  public static Bitmap encodeByQRCode(Context context, Bundle b, int size,
      String type, boolean useVCard) {
    try {
      int dimension = getDimension(context, size);
      String contents = encodeQRCodeContents(b, type, useVCard);
      return encodeAsBitmap(contents, BarcodeFormat.QR_CODE, dimension);
    } catch (Exception e) {
      return null;
    }
  }

  public static Bitmap encodeByQRCode(Context context, String data, int size,
      String type) {
    Bundle b = new Bundle();
    b.putString(ContactsContract.Intents.Insert.DATA, data);
    return encodeByQRCode(context, b, size, type, false);
  }

  public static Bitmap encode(Context context, String data, int size,
      String format) {
    try {
      int dimension = getDimension(context, size);
      return encodeAsBitmap(data, getFormat(format), dimension);
    } catch (WriterException e) {
      e.printStackTrace();
      return null;
    }
  }

  public static Bitmap encode(Context context, String data, int size) {
    return encode(context, data, size, null);
  }

  public static Bitmap encode(Context context, String data, String format) {
    return encode(context, data, 0, format);
  }

  public static Bitmap encode(Context context, String data) {
    return encode(context, data, 0, null);
  }

  private static BarcodeFormat getFormat(String format) {
    if (format != null) {
      try {
        return BarcodeFormat.valueOf(format);
      } catch (IllegalArgumentException e) {}
    }
    return BarcodeFormat.QR_CODE;
  }

  private static int getDimension(Context context, int size) {
    WindowManager manager = (WindowManager) context
        .getSystemService(Context.WINDOW_SERVICE);
    DisplayMetrics metrics = new DisplayMetrics();
    manager.getDefaultDisplay().getMetrics(metrics);
    int maxSize = metrics.widthPixels;
    int height = metrics.heightPixels;
    if (maxSize > height) {
      maxSize = height;
    }
    if (size > maxSize) {
      return maxSize;
    } else if (size < maxSize / 3) {
      return maxSize / 3;
    } else {
      return size;
    }

  }

  private static String encodeQRCodeContents(Bundle bundle, String type,
      boolean useVCard) {
    String mContents = null;
    if (bundle == null) {
      return mContents;
    }
    String data = bundle.getString(ContactsContract.Intents.Insert.DATA);
    if (type.equals(Contents.Type.TEXT)) {
      if (data != null && data.length() > 0) {
        mContents = data;
      }
    } else if (type.equals(Contents.Type.EMAIL)) {
      if (data != null) {
        mContents = "mailto:" + data;
      }
    } else if (type.equals(Contents.Type.PHONE)) {
      if (data != null) {
        mContents = "tel:" + data;
      }
    } else if (type.equals(Contents.Type.SMS)) {
      if (data != null) {
        mContents = "sms:" + data;
      }
    } else if (type.equals(Contents.Type.CONTACT)) {

      String name = bundle
          .getString(ContactsContract.Intents.Insert.NAME);
      String organization = bundle
          .getString(ContactsContract.Intents.Insert.COMPANY);
      String address = bundle
          .getString(ContactsContract.Intents.Insert.POSTAL);
      Collection<String> phones = new ArrayList<String>(
          Contents.PHONE_KEYS.length);
      for (int x = 0; x < Contents.PHONE_KEYS.length; x++) {
        phones.add(bundle.getString(Contents.PHONE_KEYS[x]));
      }
      Collection<String> emails = new ArrayList<String>(
          Contents.EMAIL_KEYS.length);
      for (int x = 0; x < Contents.EMAIL_KEYS.length; x++) {
        emails.add(bundle.getString(Contents.EMAIL_KEYS[x]));
      }
      String url = bundle.getString(Contents.URL_KEY);
      String note = bundle.getString(Contents.NOTE_KEY);

      ContactEncoder mecardEncoder = useVCard ? new VCardContactEncoder()
          : new MECARDContactEncoder();
      String[] encoded = mecardEncoder.encode(
          Collections.singleton(name), organization,
          Collections.singleton(address), phones, emails, url, note);
      // Make sure we've encoded at least one field.
      if (encoded[1].length() > 0) {
        mContents = encoded[0];
      }

    } else if (type.equals(Contents.Type.LOCATION)) {
      // These must use Bundle.getFloat(), not getDouble(), it's part
      // of the API.
      float latitude = bundle.getFloat("LAT", Float.MAX_VALUE);
      float longitude = bundle.getFloat("LONG", Float.MAX_VALUE);
      if (latitude != Float.MAX_VALUE && longitude != Float.MAX_VALUE) {
        mContents = "geo:" + latitude + ',' + longitude;
      }
    }
    return mContents;
  }

  static Bitmap encodeAsBitmap(String contentsToEncode, BarcodeFormat format,
      int dimension) throws WriterException {
    if (contentsToEncode == null) {
      return null;
    }
    Map<EncodeHintType, Object> hints = null;
    String encoding = guessAppropriateEncoding(contentsToEncode);
    if (encoding != null) {
      hints = new EnumMap<EncodeHintType, Object>(EncodeHintType.class);
      hints.put(EncodeHintType.CHARACTER_SET, encoding);
    }
    MultiFormatWriter writer = new MultiFormatWriter();
    BitMatrix result;
    try {
      result = writer.encode(contentsToEncode, format, dimension,
          dimension, hints);
    } catch (IllegalArgumentException iae) {
      // Unsupported format
      return null;
    }
    int width = result.getWidth();
    int height = result.getHeight();
    int[] pixels = new int[width * height];
    for (int y = 0; y < height; y++) {
      int offset = y * width;
      for (int x = 0; x < width; x++) {
        pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
      }
    }

    Bitmap bitmap = Bitmap.createBitmap(width, height,
        Bitmap.Config.ARGB_8888);
    bitmap.setPixels(pixels, 0, width, 0, 0, width, height);
    return bitmap;
  }

  private static String guessAppropriateEncoding(CharSequence contents) {
    // Very crude at the moment
    for (int i = 0; i < contents.length(); i++) {
      if (contents.charAt(i) > 0xFF) {
        return "UTF-8";
      }
    }
    return null;
  }
}
