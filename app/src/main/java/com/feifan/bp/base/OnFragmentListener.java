package com.feifan.bp.base;

/**
 * Fragment监听者
 * <pre>
 *     为Fragment提供各种UI相关的操作
 * </pre>
 *
 * Created by xuchunlei on 15/12/4.
 */
public interface OnFragmentListener {

    /**
     * 开始等待
     */
    void startWaiting();

    /**
     * 结束等待
     */
    void finishWaiting();
}
