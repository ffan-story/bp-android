package com.feifan.bp;

import android.os.Bundle;

/**
 * Fragment交互监听接口
 * <p>
 *     一般由持有Fragment的Activity实现，用于在Activity中实现界面切换
 * </p>
 *
 * Created by xuchunlei on 15/6/18.
 */
public interface OnFragmentInteractionListener {

    /** 交互参数键名称－调用类 */
    static final String INTERATION_KEY_FROM = "from";
    /** 交互参数键名称－目标类 */
    static final String INTERATION_KEY_TO = "to";

    /**
     * 重载该方法，可以响应界面切换事件
     */
    void onFragmentInteraction(Bundle args);
}
