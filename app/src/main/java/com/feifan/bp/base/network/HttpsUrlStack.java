package com.feifan.bp.base.network;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.toolbox.HurlStack;
import com.feifan.bp.Constants;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ProtocolVersion;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.message.BasicHeader;
import org.apache.http.message.BasicHttpResponse;
import org.apache.http.message.BasicStatusLine;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

/**
 * 重载HurlStack，对Https请求信任所有证书，提升请求成功率
 * Created by maning on 15/8/26.
 */
public class HttpsUrlStack extends HurlStack {

    private final HurlStack.UrlRewriter mUrlRewriter;
    private final SSLSocketFactory mSslSocketFactory;

    public HttpsUrlStack() {
        this((HttpsUrlStack.UrlRewriter) null);
    }

    public HttpsUrlStack(HurlStack.UrlRewriter urlRewriter) {
        this(urlRewriter, (SSLSocketFactory) null);
    }

    public HttpsUrlStack(HurlStack.UrlRewriter urlRewriter, SSLSocketFactory sslSocketFactory) {
        this.mUrlRewriter = urlRewriter;
        this.mSslSocketFactory = sslSocketFactory;
    }

    @Override
    public HttpResponse performRequest(Request<?> request, Map<String, String> additionalHeaders) throws IOException, AuthFailureError {
        String url = request.getUrl();
        HashMap map = new HashMap();
        map.putAll(request.getHeaders());
        map.putAll(additionalHeaders);
        if (this.mUrlRewriter != null) {
            String parsedUrl = this.mUrlRewriter.rewriteUrl(url);
            if (parsedUrl == null) {
                throw new IOException("URL blocked by rewriter: " + url);
            }

            url = parsedUrl;
        }

        URL parsedUrl1 = new URL(url);
        HttpURLConnection connection = this.openConnection(parsedUrl1, request);
        Iterator responseCode = map.keySet().iterator();

        while (responseCode.hasNext()) {
            String protocolVersion = (String) responseCode.next();
            connection.addRequestProperty(protocolVersion, (String) map.get(protocolVersion));
        }

        setConnectionParametersForRequest(connection, request);
        ProtocolVersion protocolVersion1 = new ProtocolVersion("HTTP", 1, 1);
        int responseCode1 = connection.getResponseCode();
        if (responseCode1 == -1) {
            throw new IOException("Could not retrieve response code from HttpUrlConnection.");
        } else {
            BasicStatusLine responseStatus = new BasicStatusLine(protocolVersion1, connection.getResponseCode(), connection.getResponseMessage());
            BasicHttpResponse response = new BasicHttpResponse(responseStatus);
            response.setEntity(entityFromConnection(connection));
            Iterator var12 = connection.getHeaderFields().entrySet().iterator();

            while (var12.hasNext()) {
                Map.Entry header = (Map.Entry) var12.next();
                if (header.getKey() != null) {
                    if (header.getKey().equals(Constants.COOKIE_RESPONSE_KEY)) {
                        for (int i = 0; i < ((List) header.getValue()).size(); i++) {
                            if(((String) ((List) header.getValue()).get(i)).contains(Constants.COOKIE_VALUE)){
                                BasicHeader h = new BasicHeader((String) header.getKey(), (String) ((List) header.getValue()).get(i));
                                response.addHeader(h);
                                break;
                            }
                        }
                    } else {
                        BasicHeader h = new BasicHeader((String) header.getKey(), (String) ((List) header.getValue()).get(0));
                        response.addHeader(h);
                    }
                }
            }
            return response;
        }
    }

    private static HttpEntity entityFromConnection(HttpURLConnection connection) {
        BasicHttpEntity entity = new BasicHttpEntity();

        InputStream inputStream;
        try {
            inputStream = connection.getInputStream();
        } catch (IOException var4) {
            inputStream = connection.getErrorStream();
        }

        entity.setContent(inputStream);
        entity.setContentLength((long) connection.getContentLength());
        entity.setContentEncoding(connection.getContentEncoding());
        entity.setContentType(connection.getContentType());
        return entity;
    }

    private HttpURLConnection openConnection(URL url, Request<?> request) throws IOException {
        HttpURLConnection connection = this.createConnection(url);
        int timeoutMs = request.getTimeoutMs();
        connection.setConnectTimeout(timeoutMs);
        connection.setReadTimeout(timeoutMs);
        connection.setUseCaches(false);
        connection.setDoInput(true);
        if ("https".equals(url.getProtocol()) && this.mSslSocketFactory != null) {
            ((HttpsURLConnection) connection).setSSLSocketFactory(this.mSslSocketFactory);
        }
        return connection;
    }

    static void setConnectionParametersForRequest(HttpURLConnection connection, Request<?> request) throws IOException, AuthFailureError {
        switch (request.getMethod()) {
            case -1:
                byte[] postBody = request.getPostBody();
                if (postBody != null) {
                    connection.setDoOutput(true);
                    connection.setRequestMethod("POST");
                    connection.addRequestProperty("Content-Type", request.getPostBodyContentType());
                    DataOutputStream out = new DataOutputStream(connection.getOutputStream());
                    out.write(postBody);
                    out.close();
                }
                break;
            case 0:
                connection.setRequestMethod("GET");
                break;
            case 1:
                connection.setRequestMethod("POST");
                addBodyIfExists(connection, request);
                break;
            case 2:
                connection.setRequestMethod("PUT");
                addBodyIfExists(connection, request);
                break;
            case 3:
                connection.setRequestMethod("DELETE");
                break;
            case 4:
                connection.setRequestMethod("HEAD");
                break;
            case 5:
                connection.setRequestMethod("OPTIONS");
                break;
            case 6:
                connection.setRequestMethod("TRACE");
                break;
            case 7:
                connection.setRequestMethod("PATCH");
                addBodyIfExists(connection, request);
                break;
            default:
                throw new IllegalStateException("Unknown method type.");
        }

    }

    private static void addBodyIfExists(HttpURLConnection connection, Request<?> request) throws IOException, AuthFailureError {
        byte[] body = request.getBody();
        if (body != null) {
            connection.setDoOutput(true);
            connection.addRequestProperty("Content-Type", request.getBodyContentType());
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.write(body);
            out.close();
        }

    }

//    @Override
//    protected HttpURLConnection createConnection(URL url) throws IOException {
//        if ("https".equals(url.getProtocol())) {
//            try {
//                SSLContext context = SSLContext.getInstance("TLS");
//
//                context.init(null, new TrustManager[]{new TrustAllManager()}, null);
//
//                HttpsURLConnection.setDefaultSSLSocketFactory(context.getSocketFactory());
//                HttpsURLConnection.setDefaultHostnameVerifier(new HostnameVerifier() {
//
//                    @Override
//                    public boolean verify(String arg0, SSLSession arg1) {
//                        return true;
//                    }
//                });
//                HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
//                return connection;
//            } catch (NoSuchAlgorithmException e) {
//                e.printStackTrace();
//                return super.createConnection(url);
//
//            } catch (KeyManagementException e) {
//                e.printStackTrace();
//                return super.createConnection(url);
//
//            }
//
//        } else {
//            return super.createConnection(url);
//        }
//    }

//    public class TrustAllManager implements X509TrustManager {
//
//        @Override
//        public void checkClientTrusted(X509Certificate[] arg0, String arg1)
//                throws CertificateException {
//            // TODO Auto-generated method stub
//
//        }
//
//        @Override
//        public void checkServerTrusted(X509Certificate[] arg0, String arg1)
//                throws CertificateException {
//            // TODO Auto-generated method stub
//
//        }
//
//        @Override
//        public X509Certificate[] getAcceptedIssuers() {
//            // TODO Auto-generated method stub
//            return null;
//        }
//    }
}
