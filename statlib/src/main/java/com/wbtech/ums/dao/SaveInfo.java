/**
 * Cobub Razor
 * <p/>
 * An open source analytics android sdk for mobile applications
 *
 * @package Cobub Razor
 * @author WBTECH Dev Team
 * @copyright Copyright (c) 2011 - 2012, NanJing Western Bridge Co.,Ltd.
 * @license http://www.cobub.com/products/cobub-razor/license
 * @link http://www.cobub.com/products/cobub-razor/
 * @filesource
 * @since Version 0.1
 */
package com.wbtech.ums.dao;

import android.content.Context;
import android.os.Environment;

import com.google.gson.Gson;
import com.wbtech.ums.UmsAgent;
import com.wbtech.ums.common.CommonUtil;
import com.wbtech.ums.model.AppstatlogModel;
import com.wbtech.ums.model.EventLogIds;
import com.wbtech.ums.model.Operation;
import com.wbtech.ums.util.FileUtilLog;
import com.wbtech.ums.util.InstallAppInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * save data to local file
 *
 * @author duzhou.xu
 */
public class SaveInfo extends Thread {
    public Context context;
    public JSONObject object;

    public SaveInfo(Context context, JSONObject object) {
        super();
        this.object = object;
        this.context = context;
    }

    @SuppressWarnings({"rawtypes", "resource"})
    @Override
    public void run() {
        super.run();
        //把数据保存到本地
        saveLog();

    }

    //把数据保存到本地
    private void saveLog() {

        if (object == null) return;
        FileUtilLog fileUtilLog = new FileUtilLog();
        Gson gson = new Gson();
        //旧数据源对象
        AppstatlogModel pastAppstatlogModel;
        //新的数据源对象
        AppstatlogModel newAppstatlogModel;
        //新老数据源结合对象
        AppstatlogModel appstatlogModel;
        try {
            //先读取出原来旧的数据
            String pastLogStr = fileUtilLog.readFileSdcardFile();
            // if("".equals(pastLogStr))return;此处不能加空判断，如果第一次进入本地没有文件
            //读出来的就是空，空但是可以继续往下执行，下面有写的操作
            pastAppstatlogModel = gson.fromJson(pastLogStr, AppstatlogModel.class);
            newAppstatlogModel = gson.fromJson(object.toString(), AppstatlogModel.class);
            appstatlogModel = installAppstatlogModel(pastAppstatlogModel, newAppstatlogModel);
            //把数据源转成json String
            String saveStr = gson.toJson(appstatlogModel);
            //保存为文本
            fileUtilLog.writeSDFile(saveStr);

            //保存完一个事件后清除商户Id和商品id，避免与下一个不需要商户与商品id事件的冲突
            EventLogIds.getInstance().clearMerchantAndProductId();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    //新事件组装到整体数据中
    private AppstatlogModel installAppstatlogModel(AppstatlogModel pastAppstatlogModel, AppstatlogModel newAppstatlogModel) {

        //因为空有可能是本地文件被删除或者还没有创建，则声称一个默认的对象,并赋予一个实例化过的事件列表
        if (pastAppstatlogModel == null) {

            pastAppstatlogModel = InstallAppInfo.getDefultAppInfo();
            pastAppstatlogModel.setEvent_log(new ArrayList<Operation>());

        }

        //旧数据的事件列表
        ArrayList<Operation> pastOperations = pastAppstatlogModel.getEvent_log();
        //新数据的时间列表
        ArrayList<Operation> newOperations = newAppstatlogModel.getEvent_log();
        if (pastOperations != null || newOperations != null) {
            //把两个的事件列表组合成一个
            pastOperations.addAll(newOperations);
        }
        //把旧数据源当做新的包含新旧数据集合的对象返回,因为旧数据包含除了事件列表之外的手机信息
        pastAppstatlogModel.setEvent_log(pastOperations);
        return pastAppstatlogModel;
    }


    //老代码无用
    @Deprecated
    private void oldCode() {

        CommonUtil.printLog("SaveInfo", object.toString());
        JSONObject existJSON = null;
        try {
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)
                    && CommonUtil.checkPermissions(context,
                    "android.permission.WRITE_EXTERNAL_STORAGE")) {
                File file = new File(Environment.getExternalStorageDirectory()
                        .getAbsolutePath()
                        + "/razor_cached_"
                        + context.getPackageName());
                // add by david
                // file.delete();
                if (file.exists()) {
                    CommonUtil.printLog("path",
                            "###file.exists" + file.getAbsolutePath());
                } else {
                    file.createNewFile();
                    CommonUtil.printLog("path", "No path");
                }

                FileInputStream in = new FileInputStream(Environment
                        .getExternalStorageDirectory().getAbsolutePath()
                        + "/razor_cached_" + context.getPackageName());
                StringBuffer sb = new StringBuffer();

                int i = 0;
                byte[] s = new byte[1024 * 4];

                while ((i = in.read(s)) != -1) {

                    sb.append(new String(s, 0, i));
                }
                // add by tianjun begin
                String event_log = "event_log";
                if (object.has(event_log)) {
                    if (sb.length() != 0) {
                        existJSON = new JSONObject(sb.toString());

                        Iterator iterator = object.keys();
                        String key = (String) iterator.next();
                        JSONArray newData = (JSONArray) object.get(event_log);
                        JSONArray exitArray = (JSONArray) existJSON
                                .get(event_log);

                        JSONObject newDataJsonObject = newData.getJSONObject(0);
                        exitArray.put(newDataJsonObject);
                        existJSON.put(event_log, exitArray);

                        // CommonUtil.printLog("SaveInfo", "###existJSON"
                        // + existJSON.toString());
                        FileOutputStream fileOutputStream = new FileOutputStream(
                                Environment.getExternalStorageDirectory()
                                        + "/razor_cached_"
                                        + context.getPackageName(), false);
                        final String jsonStr = existJSON.toString();
                        if (jsonStr == null || jsonStr.length() > 1024) {
                            fileOutputStream.close();
                            return;
                        }
                        fileOutputStream.write(existJSON.toString().getBytes());
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    } else {
                        JSONObject basicJsonObject = UmsAgent
                                .getBasicClientDataForHttps(context);
                        Iterator basicIterator = basicJsonObject.keys();
                        while (basicIterator.hasNext()) {
                            String basicKey = (String) basicIterator.next();
                            String basicString = basicJsonObject
                                    .getString(basicKey);
                            object.put(basicKey, basicString);
                        }

                        // CommonUtil.printLog("SaveInfo", "###SaveInfo Object = "
                        // + object);

                        FileOutputStream fileOutputStream = new FileOutputStream(
                                Environment.getExternalStorageDirectory()
                                        + "/razor_cached_"
                                        + context.getPackageName(), false);
                        fileOutputStream.write(object.toString().getBytes());
                        fileOutputStream.flush();
                        fileOutputStream.close();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
