package com.wbtech.ums.util;

import android.os.Environment;

import com.wanda.base.config.GlobalConfig;
import com.wbtech.ums.common.CommonUtil;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by dupengfei on 15/8/4.
 * <p/>
 * 特定日志操作包下的文件操作类
 */
public class FileUtilLog {

    //log保存路径文件
    private static final String fileName = Environment.getExternalStorageDirectory()
            .getAbsolutePath()
            + "/razor_cached_"
            + GlobalConfig.getAppContext().getPackageName();

    //从本地文件读取文本转成String
    public String readFileSdcardFile() throws IOException {

        CommonUtil.printLog("fileName", fileName);
        isFileExists();
        String res = "";

        try {
            FileInputStream fin = new FileInputStream(fileName);

            int length = fin.available();

            byte[] buffer = new byte[length];
            fin.read(buffer);

            res = EncodingUtils.getString(buffer, "UTF-8");

            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        CommonUtil.printLog("#######readFileSdcardFile", res);
        return res;
    }


    //写文件
    public void writeSDFile(String write_str) throws IOException {

        CommonUtil.printLog("#######writeSDFile", write_str);

        isFileExists();
        File file = new File(fileName);

        FileOutputStream fos = new FileOutputStream(file);

        byte[] bytes = write_str.getBytes();

        fos.write(bytes);

        fos.close();
    }


    //删除文件，上传成功之后把本地文件删除
    public void deleteSDFile() {

        File file = new File(fileName);
        if (file.exists()) file.delete();

        CommonUtil.printLog("SaveInfo",
                "###GetInfoFromFile file.delete()");
    }


    //判断文件是否存在,不存在新建
    private void isFileExists() throws IOException {

        File file = new File(fileName);
        if (file.exists()) {
            CommonUtil.printLog("   if (file.exists()) ", "true");
            return;
        } else {
            CommonUtil.printLog("   if (file.exists()) ", "not");
            file.createNewFile();
        }

    }

}
