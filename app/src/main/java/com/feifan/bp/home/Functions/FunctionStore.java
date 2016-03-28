package com.feifan.bp.home.Functions;

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
}
