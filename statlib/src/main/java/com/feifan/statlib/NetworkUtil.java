package com.feifan.statlib;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 网络工具类
 *
 * Created by xuchunlei on 15/12/8.
 */
public class NetworkUtil {

    private NetworkUtil() {

    }

    private static final String TAG = "NetworkUtil";

    /**
     * 以Post的方式发送统计事件
     * @param url
     * @param data
     * @return
     */
    public static String Post(String url, String data) {

        StatLog.d(TAG, "URL = " + url);
        StatLog.d(TAG, "Data = " + data);
        HttpURLConnection urlConnection = null;
        try {
            URL u = new URL(url);
            urlConnection = (HttpURLConnection)u.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setChunkedStreamingMode(0);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    urlConnection.getOutputStream ());
            wr.writeBytes(data);
            wr.flush ();
            wr.close ();

            //Get Response
            InputStream is = urlConnection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();
        } catch (Exception e) {
            StatLog.e(TAG, e.toString());
            e.printStackTrace();
            return null;
        }finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
        }
    }

    /**
     * 解析响应Json字符串
     * @param json
     * @return
     */
    public static ResponseMessage parse(String json) {
        try {
            if (json == null)
                return null;
            JSONObject jsonObject = new JSONObject(json);
            ResponseMessage message = new ResponseMessage();
            message.status = jsonObject.getInt("status");
            message.msg = jsonObject.getString("msg");
            return message;
        } catch (Exception e) {
            StatLog.e(TAG, e.toString());
            e.printStackTrace();
            return null;
        }
    }
}
