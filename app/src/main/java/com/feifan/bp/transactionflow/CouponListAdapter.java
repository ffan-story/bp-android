package com.feifan.bp.transactionflow;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.feifan.bp.R;

/**
 * Created by konta on 2016/1/15.
 */
public class CouponListAdapter extends BaseAdapter {
    private final String TAG = "CouponListAdapter";
    private Context mContext;

    public CouponListAdapter(Context context){
        Log.e(TAG,"here ? CouponListAdapter");
        mContext = context;
    }

    @Override
    public int getCount() {
        return 5;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if(convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.check_coupon_detail_item,null);
        }
        return convertView;
    }
}
