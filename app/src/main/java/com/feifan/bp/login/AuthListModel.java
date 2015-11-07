package com.feifan.bp.login;

import android.util.JsonReader;
import android.util.JsonWriter;
import android.util.Log;

import com.feifan.bp.envir.AuthSupplier.IAuthFactory;
import com.feifan.bp.envir.EnvironmentManager;
import com.feifan.bp.network.BaseModel;
import com.feifan.bp.util.IOUtil;
import com.feifan.bp.util.LogUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 权限列表模型
 *
 * Created by xuchunlei on 15/10/31.
 */
public class AuthListModel extends BaseModel {

    public List<AuthItem> list;

    public String historyUrl;

    public AuthListModel(JSONObject json) {
        super(json);
    }

    @Override
    protected void parseData(String json) throws JSONException {
        super.parseData(json);
        JSONArray array = new JSONArray(json);
        JSONArray menuArray = array.optJSONObject(0).optJSONArray("menu");
        list = new ArrayList<AuthItem>();
        for(int i = 0;i < menuArray.length();i++) {
            JSONObject item = menuArray.optJSONObject(i);
            int id = item.optInt("id");

            IAuthFactory factory = EnvironmentManager.getAuthFactory();

            if(factory.getHistoryId().equals(String.valueOf(id))){
                historyUrl = item.optString("url");
                continue;
            }

//            if(factory.getAuthFilter().containsKey(id)){
                AuthItem aItem = new AuthItem();
                aItem.id = id;
                aItem.name = item.optString("name");
                aItem.url = item.optString("url");
                list.add(aItem);
//            }
        }
        Collections.sort(list);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for(AuthItem item : list) {
            result.append("{").append(item.toString()).append("}").append(",");
        }
        result.append("history=" + historyUrl);
        return result.toString();
    }

    /**
     * 转换成Json格式的字符串
     * <pre>
     *     用于存储，不要用在其他场合
     * </pre>
     * @return
     */
    public String toJsonString() {
        String result = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        JsonWriter writer = null;
        try {
            writer = new JsonWriter(new OutputStreamWriter(out, "UTF-8"));
            writer.setIndent("");

            writer.beginArray();
            for(AuthItem item : list) {
                LogUtil.i(TAG, item.toString());
                item.writeTo(writer);
            }
            writer.endArray();
            writer.flush();

            byte[] bytes = out.toByteArray();
            result = new String(bytes);
            LogUtil.i(TAG, "Auth list json format:" + result);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(writer != null) {
                IOUtil.closeQuietly(writer);
            }
        }
        return result;
    }

    /**
     * 权限项
     */
    public static class AuthItem implements Comparable<AuthItem>{
        /**
         * 权限项ID
         */
        public int id;

        /**
         * 权限项名称
         */
        public String name;

        /**
         * 权限项关联URL
         */
        public String url;

        public AuthItem(){

        }

        public AuthItem(int id, String name, String url) {
            this.id = id;
            this.name = name;
            this.url = url;
        }

        /**
         * 将对象转换到Json格式流对象
         *
         * @param writer
         */
        public void writeTo(JsonWriter writer) {
            try {
                writer.beginObject();
                writer.name("id").value(id);
                writer.name("name").value(name);
                writer.name("url").value(url);
                writer.endObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public static AuthItem readFrom(JsonReader reader) {
            AuthItem item = new AuthItem();
            try {
                reader.beginObject();
                while (reader.hasNext()) {
                    String name = reader.nextName();
                    if (name.equals("id")) {
                        item.id = reader.nextInt();
                    } else if(name.equals("name")){
                        item.name = reader.nextString();
                    } else if (name.equals("url")) {
                        item.url = reader.nextString();
                    } else {
                        reader.skipValue();
                    }
                }
                reader.endObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return item;
        }

        @Override
        public String toString() {
            return "id=" + id + ",name=" + name + ",url=" + url;
        }

        @Override
        public int compareTo(AuthItem another) {
            return id - another.id;
        }
    }
}
