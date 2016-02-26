package com.bp.crash;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;

import com.wanda.crashsdk.BuildConfig;
import com.wanda.crashsdk.core.data.SDKConfig;
import com.wanda.crashsdk.network.HttpParams;
import com.wanda.crashsdk.pub.AppBaseInfo;
import com.wanda.crashsdk.pub.IConfiguration;
import com.wanda.crashsdk.pub.policy.AbstractHttpPolicy;
import com.wanda.crashsdk.pub.policy.IHttpPolicy;


/**
 * Created by mengmeng on 16/2/24.
 */
public class BPCrashConfig implements IConfiguration {
    private static final String APPID = BuildConfig.APPLICATION_ID; // "AndroidFeifan"; // 最好服务端生成
    private static final String ACTIVE_PATH = "/notify/active";
    private static final String JAVA_CRASH_PATH = "/notify/crash-log";
    private static final String ANR_PATH = "/notify/anr-log";
    private static final String NATIVE_CRASH_PATH = "/notify/native-log";
    private static final String SERVER_VERSION = "1";
    private static final String CLIENT_TYPE_VALUE = "2";

    private static final String UDID = "ddId";
    private static final String WDID = "wdId";
    private static final String IMEI = "imei";
    private static final String VERSION_CODE = "FFClientVersion";
    private static final String CLIENT_TYPE = "FFClientType";
    private static final String SERVER_API_VERSION = "version";

    private static final String KEY_APP_ID = "AppId";
    //BP is value 2 ,ffan client is 1
    private static final String VALUE_APP_ID = "2";

    private static final String CRASH_PRODUCT_SERVER = "http://crash.ffan.com";
    private static final String CRASH_TEST_SERVER = "http://crash.sit.ffan.com";

    private static String mChannel;
    private String mUid;


    @Override
    public String getActiveReportUrl() {
        return getBaseUrl() + ACTIVE_PATH;
    }

    @Override
    public String getJavaCrashUrl() {
        return getBaseUrl() + JAVA_CRASH_PATH;
    }

    @Override
    public String getANRUrl() {
        return getBaseUrl() + ANR_PATH;
    }

    @Override
    public String getNativeCrashUrl() {
        return getBaseUrl() + NATIVE_CRASH_PATH;
    }

    @Override
    public IHttpPolicy getHttpPolicy(Context context) {
        return new HttpPolicy(context);
    }

    @Override
    public AppBaseInfo getAppInfo(Context context) {
        return constructAppBaseInfo(context);
    }

    public void setUid(String uid){

    }

    @Override
    public boolean canDebugReport() { // debug模式下是否上报
        return true;
    }

    public static String getServerUrl() {
        if(BuildConfig.DEBUG) {
            return CRASH_TEST_SERVER;
        } else {
            return CRASH_PRODUCT_SERVER;
        }
    }

    private String getBaseUrl() {
        return getServerUrl();
    }

    /**
     * 公共上行参数
     */
    private class HttpPolicy extends AbstractHttpPolicy {
        private Context mContext;

        public HttpPolicy(Context context) {
            mContext = context;
        }

        @Override
        public HttpParams getHttpParams() {
            HttpParams params = new HttpParams();

            params.put(UDID,CrashUtil.getMyUUID(mContext));
            params.put(IMEI, CrashUtil.getMyUUID(mContext));
            params.put(CLIENT_TYPE, CLIENT_TYPE_VALUE);
            params.put(VERSION_CODE,
                    String.valueOf(SystemUtil.getVersionCode(mContext)));
            params.put(SERVER_API_VERSION, SERVER_VERSION);
            params.put(KEY_APP_ID, VALUE_APP_ID);
            return params;
        }
    }

    private AppBaseInfo constructAppBaseInfo(final Context context) {
        String packageName = context == null ? "com.feifan.bp" : context.getPackageName();

        String userId = mUid;
        // init channel
        if (TextUtils.isEmpty(mChannel)) {
            try {
                ApplicationInfo appInfo = context.getPackageManager()
                        .getApplicationInfo(packageName, PackageManager.GET_META_DATA);
                mChannel = appInfo.metaData.getString("CHANNEL");
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        }

        AppBaseInfo info = new AppBaseInfo();
        info.mDeviceId = CrashUtil.getMyUUID(context);
        info.mIMEI = SDKConfig.getIMEI();
        info.mAppId = APPID;
        info.mPackageName = packageName;
        info.mUserId = userId;
        info.mChannel = mChannel;
        info.mBuildType = BuildConfig.BUILD_TYPE;
        return info;
    }
}