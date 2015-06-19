package com.feifan.bp.home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feifan.bp.R;
import com.feifan.bp.home.Model.MenuModel;
import com.feifan.bp.widget.AbstractSimpeListAdapter;

import java.util.List;

/**
 * Created by xuchunlei on 15/6/19.
 */
public class MenuAdapter extends AbstractSimpeListAdapter<MenuModel> {

    public MenuAdapter(Context context, List<MenuModel> data) {
        super(context);
        mData = data;
    }

    @Override
    protected View createViewFromResource(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        final MenuModel item = mData.get(position);
        ViewHolder holder;
        if (v == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            v = inflater.inflate(R.layout.manage_menu_item, null);
            holder = new ViewHolder();
            holder.menuTxv = (TextView) v;
            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }
        holder.menuTxv.setText(item.name);

        return v;
    }

    private static class ViewHolder {
        TextView menuTxv;
    }
}
