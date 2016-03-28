package com.feifan.bp.home.function;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.feifan.bp.PlatformState;
import com.feifan.bp.R;
import com.feifan.bp.Utils;

/**
 * 功能对象
 * <pre>
 *     用来定义一个功能
 * </pre>
 * Created by xuchunlei on 16/3/26.
 */
public abstract class Function {

    // 名称，可唯一标识功能
    public String mUniqueName;

    public Function(String name) {
        this.mUniqueName = name;
    }

    /**
     * 调用
     */
    public abstract void call();

    /**
     * 启动界面类型的功能对象
     */
    public static class LaunchFunction extends Function{

        // 启动活动
        private Class<? extends AppCompatActivity> mLaunchClazz;
        // 调用参数
        private Bundle mParams;

        public LaunchFunction(String name) {
            super(name);
            mParams = new Bundle();
        }

        public LaunchFunction activity(Class<? extends AppCompatActivity> clz) {
            mLaunchClazz = clz;
            return this;
        }

        public LaunchFunction param(String key, int value) {
            mParams.putInt(key, value);
            return this;
        }

        public LaunchFunction param(String key, String value) {
            mParams.putString(key, value);
            return this;
        }

        public LaunchFunction param(String key, Bundle value) {
            mParams.putBundle(key, value);
            return this;
        }

        public LaunchFunction param(String key, Object value) {
            if(value instanceof String) {
                param(key, value.toString());
            } else if(value instanceof Integer) {
                param(key, (Integer)value);
            } else if(value instanceof Bundle) {
                param(key, (Bundle)value);
            }
            return this;
        }



        @Override
        public void call() {
            if(!Utils.isNetworkAvailable()) {
                Utils.showShortToastSafely(R.string.error_message_text_offline);
                return;
            }
            final Context appContext = PlatformState.getApplicationContext();
            Intent intent = new Intent(appContext, mLaunchClazz);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            for(String key : mParams.keySet()) {
                Object value = mParams.get(key);
                if(value instanceof Integer){
                    intent.putExtra(key, (Integer)value);
                } else if(value instanceof String) {
                    intent.putExtra(key, value.toString());
                } else if(value instanceof Bundle) {
                    intent.putExtra(key, (Bundle)value);
                } else {
                    throw new IllegalArgumentException(value.getClass().getSimpleName() + " not supported");
                }

            }
            appContext.startActivity(intent);
        }
    }
}
