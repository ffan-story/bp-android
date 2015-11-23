package com.feifan.bp.helpcenter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.feifan.bp.Constants;
import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.R;
import com.feifan.bp.UserProfile;
import com.feifan.bp.base.BaseFragment;
import com.feifan.bp.home.HomeCtrl;
import com.feifan.bp.home.MessageModel;
import com.feifan.bp.widget.LoadingMoreListView;
import com.feifan.bp.widget.OnLoadingMoreListener;

import java.util.ArrayList;
import java.util.List;

import bp.feifan.com.refresh.PtrClassicFrameLayout;
import bp.feifan.com.refresh.PtrFrameLayout;
import bp.feifan.com.refresh.PtrHandler;

/**
 * Created by apple on 15-11-20.
 */
public class HelpCenterFragment extends BaseFragment implements OnLoadingMoreListener, PtrHandler {
    private PtrClassicFrameLayout mPtrFrame, mPtrFrameEmpty;
    private List<MessageModel.MessageData> mList = new ArrayList<>();
    private LoadingMoreListView mListView;

    private OnFragmentInteractionListener mListener;

    private Adapter mAdapter;
    public static HelpCenterFragment newInstance() {
        HelpCenterFragment fragment = new HelpCenterFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View contentView = inflater.inflate(R.layout.refresh_listview, null);
        mPtrFrame = (PtrClassicFrameLayout) contentView.findViewById(R.id.rotate_header_list_view_frame);
        mListView = (LoadingMoreListView) mPtrFrame.findViewById(R.id.rotate_header_list_view);
        mPtrFrameEmpty = (PtrClassicFrameLayout) contentView.findViewById(R.id.ptr_empty);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                if (mList != null && mList.size() > 0) {
//                    if (mList.get(position).getmStrMessageStatus() != null && mList.get(position).getmStrMessageStatus().equals(Constants.UNREAD)) {
//                        setMessageListStatus(mList.get(position).getUserid(), mList.get(position).getMaillnboxid(),position);
//                    }
//                    String strUri = UrlFactory.urlForHtml(mList.get(position).getmStrDetailUrl());
//                    BrowserActivity.startActivity(getActivity(), strUri);
//                }
            }
        });
        mAdapter = new Adapter(getActivity());
        mListView.setAdapter(mAdapter);
        mListView.setOnLoadingMoreListener(this);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrameEmpty.setPtrHandler(this);
        mPtrFrame.setPtrHandler(this);
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        mPtrFrame.setPullToRefresh(false);
        mPtrFrame.setKeepHeaderWhenRefresh(true);
        mPtrFrame.postDelayed(new Runnable() {
            @Override
            public void run() {
                mPtrFrame.autoRefresh();
            }
        }, 100);
        return contentView;
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setTitle(R.string.help_center_title);
        toolbar.setNavigationIcon(R.mipmap.ic_left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle b = new Bundle();
                b.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM, HelpCenterFragment.class.getName());
                b.putInt(OnFragmentInteractionListener.INTERATION_KEY_TYPE, OnFragmentInteractionListener.TYPE_NAVI_CLICK);
                mListener.onFragmentInteraction(b);
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onLoadingMore() {

    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return false;
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {

    }

    /**
     * 获取列表数据
     */
    private void fetchData(int pageIndex) {

    }

    class Adapter extends BaseAdapter {
        private final LayoutInflater mInflater;
        Context context;

        public Adapter(Context context) {
            this.context = context;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.item_help_center, parent, false);
                holder = ViewHolder.findAndCacheViews(convertView);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            MessageModel.MessageData data = mList.get(position);
            holder.mTvHelpCenterTitle.setText(data.getmStrMessageTitle());
            return convertView;
        }
    }

    private static class ViewHolder {
        private TextView mTvHelpCenterTitle;
        public static ViewHolder findAndCacheViews(View view) {
            ViewHolder holder = new ViewHolder();
            holder.mTvHelpCenterTitle = (TextView) view.findViewById(R.id.tv_message_title);
            view.setTag(holder);
            return holder;
        }
    }
}
