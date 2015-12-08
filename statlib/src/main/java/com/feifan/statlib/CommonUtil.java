package com.feifan.statlib;

import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Environment;

import org.json.JSONArray;
import org.json.JSONObject;

import com.feifan.statlib.FmsAgent.SendPolicy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.security.MessageDigest;
import java.util.UUID;

/**
 * 通用工具类
 *
 * Created by xuchunlei on 15/12/8.
 */
public class CommonUtil {

    private static final String TAG = "CommonUtil";

    private CommonUtil() {

    }

    /**
     * Testing equipment networking and networking WIFI
     *
     * @param context
     * @return true or false
     */
    public static boolean isNetworkAvailable(Context context) {
        if (checkPermissions(context, "android.permission.INTERNET")) {
            ConnectivityManager cManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            if (cManager == null)
                return false;

            NetworkInfo info = cManager.getActiveNetworkInfo();
            if (info != null && info.isAvailable()) {
                StatLog.i(TAG, "Network is available.");
                return true;
            } else {
                StatLog.i(TAG, "Network is not available.");
                return false;
            }

        } else {
            StatLog.e(TAG, "android.permission.INTERNET permission should be added into AndroidManifest.xml.");
            return false;
        }

    }

    /**
     * 检查权限
     *
     * @param context
     * @param permission
     * @return true or false
     */
    public static boolean checkPermissions(Context context, String permission) {
        PackageManager pm = context.getPackageManager();
        return pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED;
    }

    public static void saveInfoToFile(String type, JSONObject info,
                                      Context context) {
        JSONArray newdata = new JSONArray();
        try {
            newdata.put(0, info);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put(type, newdata);
            String cacheFile = context.getCacheDir() + FmsConstants.CACHE_FILE_NAME;
            Thread t = new SaveInfo( jsonObject,cacheFile);
            t.run();

        } catch (Exception e) {
            StatLog.e(TAG, e.toString());
            e.printStackTrace();
        }
    }

    /**
     * Get the current send model
     *
     * @param context
     * @return
     */
    public static SendPolicy getReportPolicyMode(Context context) {
        return FmsConstants.mReportPolicy;
    }

    /**
     * 返回该设备在此程序上的随机数。
     *
     * @param context
     *            Context对象。
     * @return 表示该设备在此程序上的随机数。
     */
    public synchronized static String getSALT(Context context) {
        String file_name = context.getPackageName().replace(".", "");
        String sdCardRoot = Environment.getExternalStorageDirectory()
                .getAbsolutePath();
        int apiLevel = Build.VERSION.SDK_INT;
        File fileFromSDCard = new File(sdCardRoot +File.separator, "."+file_name);
        File fileFromDData = new File(context.getFilesDir(),file_name);// 获取data/data/<package>/files
        //4.4之後 /storage/emulated/0/Android/data/<package>/files
        if(apiLevel>=19){
            sdCardRoot = context.getExternalFilesDir(null).getAbsolutePath();
            fileFromSDCard = new File(sdCardRoot , file_name);
        }

        String saltString = "";
        if (Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            // sdcard存在
            if (!fileFromSDCard.exists()) {

                String saltId =getSaltOnDataData(fileFromDData, file_name);
                try {
                    writeToFile(fileFromSDCard, saltId);
                } catch (Exception e) {
                    StatLog.e(TAG, e.toString());
                    e.printStackTrace();
                }
                return saltId;

            } else {
                // SD卡上存在salt
                saltString=getSaltOnSDCard(fileFromSDCard);
                try {
                    writeToFile(fileFromDData, saltString);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
                return saltString;
            }

        } else {
            // sdcard 不可用
            return getSaltOnDataData(fileFromDData, file_name);
        }

    }

    // public static SharedPreferences getSharedPreferences(Context context) {
    // return context.getSharedPreferences("ums_agent_online_setting_"
    // + context.getPackageName(), 0);
    // }

    public static String md5Appkey(String str) {
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(str.getBytes());
            byte[] arrayOfByte = localMessageDigest.digest();
            StringBuffer localStringBuffer = new StringBuffer();
            for (int i = 0; i < arrayOfByte.length; i++) {
                int j = 0xFF & arrayOfByte[i];
                if (j < 16)
                    localStringBuffer.append("0");
                localStringBuffer.append(Integer.toHexString(j));
            }
            return localStringBuffer.toString();
        } catch (Exception e) {
            StatLog.e(TAG, e.toString());
            e.printStackTrace();
        }
        return "";
    }

    private static String getSaltOnSDCard(File fileFromSDCard) {
        // TODO Auto-generated method stub
        try {
            String saltString = readSaltFromFile(fileFromSDCard);
            return saltString;
        } catch (IOException e) {
            StatLog.e(TAG, e.toString());
            e.printStackTrace();
        }
        return null;
    }

    private static String getSaltOnDataData(File fileFromDData, String file_name) {
        try {
            if (!fileFromDData.exists()) {
                String uuid = getUUID();
                writeToFile( fileFromDData, uuid);
                return uuid;
            }
            return	readSaltFromFile(fileFromDData);

        } catch (IOException e) {
            StatLog.e(TAG, e.toString());
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 读出保存在程序文件系统中的表示该设备在此程序上的唯一标识符。。
     *
     * @param file
     *            保存唯一标识符的File对象。
     * @return 唯一标识符。
     * @throws IOException
     *             IO异常。
     */
    private static String readSaltFromFile(File file) throws IOException {
        RandomAccessFile accessFile = new RandomAccessFile(file, "r");
        byte[] bs = new byte[(int) accessFile.length()];
        accessFile.readFully(bs);
        accessFile.close();
        return new String(bs);
    }

    private static String getUUID() {
        // TODO Auto-generated method stub
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 将表示此设备在该程序上的唯一标识符写入程序文件系统中
     *
     * @param file
     *            保存唯一标识符的File对象。
     * @throws IOException
     *             IO异常。
     */
    private static void writeToFile( File file, String uuid)
            throws IOException {
        file.createNewFile();
        FileOutputStream out = new FileOutputStream(file);

        out.write(uuid.getBytes());
        out.close();

    }
}
