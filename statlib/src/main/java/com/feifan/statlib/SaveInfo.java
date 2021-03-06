package com.feifan.statlib;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

/**
 * Created by xuchunlei on 15/12/8.
 */
class SaveInfo extends Thread {
    public JSONObject object;
    private final static String TAG = "SaveInfo";
    private String path = "";

    public SaveInfo( JSONObject object,String path) {
        super();
        this.object = object;
        this.path = path;
    }

    @Override
    public void run() {
        super.run();
        SaveInfo.saveData(this.object,this.path);

    }

    /**
     * 保存事件信息到文件
     * @param object
     * @param path
     */
    public static void saveData(JSONObject object,String path) {
//        File file;
//        StatLog.d(TAG, "Save cache file "+path);
//        StatLog.d(TAG, "json data "+object.toString());
//
//        JSONObject existJSON = null;
//        try {
//
//            file = new File(path);
//            if (file.exists()) {
//                StatLog.i(TAG, "file exist " + file.getAbsolutePath());
//            } else {
//                file.createNewFile();
//            }
//            //
//            if (file.length() > 1024 * 1024) {
//                file.delete();
//                file = new File(path);
//                file.createNewFile();
//            }
//            FileInputStream in = new FileInputStream(path);
//            StringBuffer sb = new StringBuffer();
//
//            int i = 0;
//            byte[] s = new byte[1024 * 4];
//
//            while ((i = in.read(s)) != -1) {
//                sb.append(new String(s, 0, i));
//            }
//            in.close();
//            if (sb.length() != 0) {
//                existJSON = new JSONObject(sb.toString());
//
//                Iterator<String> iterator = object.keys();
//                while (iterator.hasNext()) {
//                    String key = iterator.next();
//                    JSONArray newData = object.getJSONArray(key);
//
//                    if (existJSON.has(key)) {
//                        JSONArray newDataArray = existJSON.getJSONArray(key);
//                        newDataArray.put(newData.get(0));
//                    } else {
//                        existJSON.put(key, object.getJSONArray(key));
//                    }
//                }
//                FileOutputStream fileOutputStream = new FileOutputStream(
//                        path, false);
//                fileOutputStream.write(existJSON.toString().getBytes());
//                fileOutputStream.flush();
//                fileOutputStream.close();
//
//            } else {
//                Iterator<String> iterator = object.keys();
//                JSONObject jsonObject = new JSONObject();
//                while (iterator.hasNext()) {
//                    String key = iterator.next();
//                    JSONArray array = object.getJSONArray(key);
//
//                    jsonObject.put(key, array);
//
//                }
//                jsonObject.put("appkey", AppInfo.getAppKey());
//
//                FileOutputStream fileOutputStream = new FileOutputStream(
//                        path, false);
//                fileOutputStream.write(jsonObject.toString().getBytes());
//                fileOutputStream.flush();
//                fileOutputStream.close();
//                StatLog.i(TAG, "seve info finshed");
//            }
//        } catch (Exception e) {
//            StatLog.e(TAG, e.toString());
//            e.printStackTrace();
//        }
    }
}
