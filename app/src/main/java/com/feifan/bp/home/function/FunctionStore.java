package com.feifan.bp.home.function;

import android.os.Bundle;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能仓库
 * <pre>
 *     维护功能项
 * </pre>
 *
 * Created by xuchunlei on 16/3/26.
 */
public class FunctionStore {

    private Map<String, Function> mStore = new HashMap<String, Function>();

    public FunctionStore addFunction(Function func) {
        mStore.put(func.mUniqueName, func);
        return this;
    }

    /**
     * 调用功能
     * @param name
     */
    public void invokeFunc(String name) {
        Function func = mStore.get(name);
        if(func == null) {
            throw new NullPointerException("Function " + name + " not found.");
        }
        func.call();
    }

    /**
     * 调用带参数的功能
     * @param name
     * @param params
     */
    public void invokeFunc(String name, Bundle params) {
        Function func = mStore.get(name);
        if(func == null) {
            throw new NullPointerException("Function " + name + " not found.");
        }

        //添加参数
        if(params != null) {
            if(func instanceof Function.LaunchFunction) { // 启动类型的功能可携带参数
                for(String key : params.keySet()) {
                    ((Function.LaunchFunction)func).param(key, params.get(key));
                }

            }
        }

        func.call();
    }
}
