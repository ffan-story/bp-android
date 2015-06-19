/**
 * 
 */
package com.feifan.bp.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * 简单实现的应用于ListView的Adapter
 * @author    xuchunlei
 * create at: 2015年4月12日 下午8:01:09
 *
 */
public abstract class AbstractSimpeListAdapter<T> extends BaseAdapter {

    protected Context mContext;
    protected List<T> mData;

    public AbstractSimpeListAdapter(Context context) {
        mContext = context;
    }
    
    /* (non-Javadoc)
     * @see android.widget.Adapter#getCount()
     */
    @Override
    public int getCount() {
        return mData == null ? 0 : mData.size();
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItem(int)
     */
    @Override
    public Object getItem(int position) {
        
        return mData == null ? null : mData.get(position);
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getItemId(int)
     */
    @Override
    public long getItemId(int position) {
        
        return position;
    }

    /* (non-Javadoc)
     * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        
        return createViewFromResource(position, convertView, parent);
    }

    public void setData(List<T> data) {
        mData = data;
        notifyDataSetChanged();
    }
    
    public List<T> getData() {
        return mData;
    }
    
    /**
     * 创建显示视图
     * @param position
     * @param convertView
     * @param parent
     * @return
     * @author:     xuchunlei
     * created at: 2015年4月12日 下午8:09:51
     */
    protected abstract View createViewFromResource(int position, View convertView, ViewGroup parent);
}
