package com.feifan.bp.widget;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.PopupWindow;

import com.feifan.bp.R;
import com.feifan.bp.UserProfile;
import com.feifan.bp.browser.ShopListAdapter;

/**
 * Created by Frank on 15/10/27.
 */
public class SelectPopWindow extends PopupWindow {

    private View mMenuView;

    private int lastSelectPos;

    public SelectPopWindow(final Activity context, int selectPos) {
        super(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.select_shop, null);
        this.lastSelectPos = selectPos;
        this.setContentView(mMenuView);
        this.setWidth(LayoutParams.MATCH_PARENT);
        this.setHeight(1000);
        this.setFocusable(true);
        this.setAnimationStyle(R.style.PopWindowAnim);
        ImageView mCancelView = (ImageView) mMenuView.findViewById(R.id.menu_cancel);
        mCancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        RecyclerView mShopList = (RecyclerView) mMenuView.findViewById(R.id.rv_shoplist);
        mShopList.setLayoutManager(new LinearLayoutManager(context));
        mShopList.addItemDecoration(new DividerItemDecoration(context,DividerItemDecoration.VERTICAL_LIST));

        UserProfile userProfile = UserProfile.getInstance();
        int storeNum = userProfile.getStoreNum();
        String[] dataset = new String[storeNum];
        for(int i =0;i<storeNum;i++){
            dataset[i] = userProfile.getStoreName(i);
        }
        ShopListAdapter mAdapter = new ShopListAdapter(context,dataset,lastSelectPos);
        mShopList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new ShopListAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int postion) {
                //Toast.makeText(context, "选择了第"+(postion+1)+"个门店", Toast.LENGTH_SHORT).show();
                lastSelectPos = postion;
                dismiss();
            }
        });
    }

    public int getSelectPos() {
        return lastSelectPos;
    }

    public void setSelectPos(int lastSelectPos) {
        this.lastSelectPos = lastSelectPos;
    }
}
