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

    /**
     * All type of a event.
     */
    int TYPE_IDLE = 0;
    int TYPE_NAVI_CLICK = 1;

    /** 交互参数键名称－调用类 */
    String INTERATION_KEY_FROM = "from";
    /** 交互参数键名称－目标类 */
    String INTERATION_KEY_TO = "to";
    /** 交互参数键名称－toolbar标题 */
    String INTERATION_KEY_TITLE="title";

    /**
     * The key of event type.
     */
    String INTERATION_KEY_TYPE = "type";

    /**
     * 重载该方法，可以响应界面切换事件
     */
    void onFragmentInteraction(Bundle args);

    /**
     * 重载该方法，可以响应标题变更事件
     * @param title
     */
    void onTitleChanged(String title);

    /**
     * 重载该方法，可以相应状态变更事件
     * @param flag
     */
    //void onStatusChanged(boolean flag);

    /**
     * 重载该方法，可以相应状态变更事件
     * @param flag
     * @param count  数量
     */
    void onStatusChanged(boolean flag,int count);
}

