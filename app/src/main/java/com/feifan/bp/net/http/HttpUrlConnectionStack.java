package com.feifan.bp.net.http;

import android.util.Log;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.utils.URLEncodedUtils;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by maning on 15/6/16.
 */
public class HttpUrlConnectionStack extends HttpStack {

    private static final String TAG = "HttpUrlConnectionStack";
    @Override
    public Response connect(Request request) {

        Response response = null;
        METHOD method = request.getMethod();
        if (METHOD.GET == method) {
            response = doGetRequest(request);
        } else if (METHOD.POST == method) {
            response = doPostRequest(request);
        } else {
            // Do nothing.
        }

        return response;
    }

    private Response doGetRequest(Request request) {
        return null;
    }

    private Response doPostRequest(Request request) {

        Response response = new Response();
        try {
            String urlStr = request.getUrl();
            URL url = new URL(urlStr);

            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setReadTimeout(5000);
            urlConnection.setConnectTimeout(5000);
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            //TODO: test code
            String data = "userName=" + "18600070446"
                    + "&password=" + "abc123456";

            List<NameValuePair> params = request.getParams();
            String dataStr = URLEncodedUtils.format(params, "UTF-8");
            Log.d(TAG, "http entity=" + dataStr);

            OutputStream os = urlConnection.getOutputStream();
            os.write(dataStr.getBytes());
            os.flush();
            if (urlConnection.getResponseCode() == 200) {
                InputStream is = urlConnection.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                int len = 0;
                byte buffer[] = new byte[1024];
                while ((len = is.read(buffer)) != -1) {
                    baos.write(buffer, 0, len);
                }
                is.close();
                baos.close();
                final String result = new String(baos.toByteArray());
                Log.d(TAG, "result=" + result);
                JSONObject j = new JSONObject(result);
                String status = j.optString("status");
                String msg = j.optString("msg");
                Log.d(TAG, "msg=" + msg);
                response.setResult(result);
                response.setStatus(urlConnection.getResponseCode());
                return response;
            } else {
                System.out.println("链接失败.........");
                response.setStatus(urlConnection.getResponseCode());
                return response;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    return null;
    }

}
