package com.feifan.bp.base;

import android.view.ViewGroup;

public interface IEmptyView {
    static enum EmptyViewPlaceHolderType {
        /**
         * EmptyView 添加到Decor View上面
         */
        PlaceHolderTypeInsertToDecor,
        /**
         * EmptyView添加到指定的View上面
         */
        PlaceHolderTypeInsertToView,

        /**
         * EmptyView本身已经在View上面，这里只需要控制显示和隐藏即可
         */
        PlaceHolderTypeNoNeedInsert;
    }

    static enum EmptyViewAlignment {
        AlignmentTop, AlignmentBottom, AlignmentCenter;
    }

    public ViewGroup getEmptyView();

    public void showEmptyView();

    public void hideEmptyView();


}
