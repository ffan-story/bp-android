/**
 * Cobub Razor
 *
 * An open source analytics android sdk for mobile applications
 *
 * @package Cobub Razor
 * @author WBTECH Dev Team
 * @copyright Copyright (c) 2011 - 2012, NanJing Western Bridge Co.,Ltd.
 * @license http://www.cobub.com/products/cobub-razor/license
 * @link http://www.cobub.com/products/cobub-razor/
 * @since Version 0.1
 * @filesource
 */
package com.wbtech.ums.common;

import android.util.Log;

import com.wbtech.ums.objects.MyMessage;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Socket;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

public class NetworkUitlity {
  public static long paramleng = 256L;
  public static String DEFAULT_CHARSET = " HTTP.UTF_8";

  @SuppressWarnings("deprecation")
  public static MyMessage post(String url, String data) {

    return post(url,data,UmsConstants.POST_TYPE_DEFAULT);
  }


  public static MyMessage post(String url, String data,String type){
    CommonUtil.printLog("ums", url);
    CommonUtil.printLog("#######post_type", type);
    String returnContent = "";
    MyMessage message = new MyMessage();
    HttpClient httpclient = new DefaultHttpClient();
    HttpPost httppost = new HttpPost(url);
    try {
      Map<String, String> parmas = new HashMap<String, String>();
      parmas.put("content", data);
      parmas.put("type",type);
      ArrayList<BasicNameValuePair> pairs = new ArrayList<BasicNameValuePair>();
      if(parmas != null){
        Set<String> keys = parmas.keySet();
        for(Iterator<String> i = keys.iterator(); i.hasNext();) {
          String key = (String)i.next();
          pairs.add(new BasicNameValuePair(key, parmas.get(key)));
        }
      }


      UrlEncodedFormEntity se = new UrlEncodedFormEntity(pairs, HTTP.UTF_8);
      CommonUtil.printLog("postdata", "content=" + data);
      se.setContentType("application/x-www-form-urlencoded");
      httppost.setEntity(se);
      HttpResponse response = httpclient.execute(httppost);
      int status = response.getStatusLine().getStatusCode();
      CommonUtil.printLog("ums", status + "");
      String returnXML = EntityUtils.toString(response.getEntity());
      returnContent = URLDecoder.decode(returnXML);
      switch (status) {
        case 200:
          message.setFlag(true);
          message.setMsg(returnContent);
          break;

        default:
          message.setFlag(false);
          message.setMsg(returnContent);
          break;
      }
    } catch (Exception e) {
      JSONObject jsonObject = new JSONObject();

      try {
        jsonObject.put("err", e.toString());
        returnContent = jsonObject.toString();
        message.setFlag(false);
        message.setMsg(returnContent);
      } catch (JSONException e1) {
        e1.printStackTrace();
      }

    }
    CommonUtil.printLog("UMSAGENT", message.getMsg());
    return message;
  }

  private static final int CONNECTION_TIMEOUT = 10000;

  public static MyMessage postByHttps(String url, JSONObject data) {
    CommonUtil.printLog("ums", url);
    Log.i("", "############ postByHttps data  = " + data);
    String returnContent = "";
    MyMessage message = new MyMessage();
    HttpPost httppost = new HttpPost(url);

    try {
      KeyStore trustStore = KeyStore.getInstance(KeyStore
          .getDefaultType());
      trustStore.load(null, null);

      SSLSocketFactory sf = new SSLSocketFactoryImp(trustStore);
      sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

      StringEntity se = new StringEntity("content=" + data, HTTP.UTF_8);
      // CommonUtil.printLog("postdata", "content=" + data);
      se.setContentType("application/x-www-form-urlencoded");
      httppost.setEntity(se);

      HttpParams httpParameters = new BasicHttpParams();
      HttpConnectionParams.setConnectionTimeout(httpParameters,
          CONNECTION_TIMEOUT);
      HttpConnectionParams.setSoTimeout(httpParameters,
          CONNECTION_TIMEOUT);

      // HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
      // HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

      SchemeRegistry registry = new SchemeRegistry();
      registry.register(new Scheme("https", sf, 443));

      ClientConnectionManager ccm = new ThreadSafeClientConnManager(
          httpParameters, registry);
      HttpClient httpclient = new DefaultHttpClient(ccm, httpParameters);

      HttpResponse response = httpclient.execute(httppost);
      int status = response.getStatusLine().getStatusCode();
      CommonUtil.printLog("ums", status + "");
      String returnXML = EntityUtils.toString(response.getEntity());
      returnContent = URLDecoder.decode(returnXML);
      Log.i("", "############ status = " + status + " returnContent = " + returnContent);
      switch (status) {
        case 200:
          message.setFlag(true);
          message.setMsg(returnContent);
          break;

        default:
          message.setFlag(false);
          message.setMsg(returnContent);
          break;
      }
    } catch (Exception e) {
      JSONObject jsonObject = new JSONObject();

      try {
        jsonObject.put("err", e.toString());
        returnContent = jsonObject.toString();
        message.setFlag(false);
        message.setMsg(returnContent);
      } catch (JSONException e1) {
        e1.printStackTrace();
      }

    }
    CommonUtil.printLog("UMSAGENT", message.getMsg());
    return message;
  }

  public static class SSLSocketFactoryImp extends SSLSocketFactory {
    final SSLContext sslContext = SSLContext.getInstance("TLS");

    public SSLSocketFactoryImp(KeyStore truststore)
        throws NoSuchAlgorithmException, KeyManagementException,
        KeyStoreException, UnrecoverableKeyException {
      super(truststore);

      TrustManager tm = new X509TrustManager() {
        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
          return null;
        }

        @Override
        public void checkClientTrusted(
            java.security.cert.X509Certificate[] chain,
            String authType)
            throws java.security.cert.CertificateException {}

        @Override
        public void checkServerTrusted(
            java.security.cert.X509Certificate[] chain,
            String authType)
            throws java.security.cert.CertificateException {}
      };
      sslContext.init(null, new TrustManager[] {tm}, null);
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port,
        boolean autoClose) throws IOException, UnknownHostException {
      return sslContext.getSocketFactory().createSocket(socket, host,
          port, autoClose);
    }

    @Override
    public Socket createSocket() throws IOException {
      return sslContext.getSocketFactory().createSocket();
    }
  }
}
