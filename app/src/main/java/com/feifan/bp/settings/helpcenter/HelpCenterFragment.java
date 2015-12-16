package com.feifan.bp.settings.helpcenter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.R;
import com.feifan.bp.base.BaseFragment;
import com.feifan.bp.browser.BrowserActivity;
import com.feifan.bp.network.UrlFactory;
import com.feifan.bp.widget.LoadingMoreListView;
import com.feifan.bp.widget.OnLoadingMoreListener;

import java.util.ArrayList;
import java.util.List;

import bp.feifan.com.refresh.PtrClassicFrameLayout;
import bp.feifan.com.refresh.PtrDefaultHandler;
import bp.feifan.com.refresh.PtrFrameLayout;
import bp.feifan.com.refresh.PtrHandler;

/**
 * Created by apple on 15-11-20.
 */
public class HelpCenterFragment extends BaseFragment implements OnLoadingMoreListener, PtrHandler {
    private PtrClassicFrameLayout mPtrFrame, mPtrFrameEmpty;
    private List<HelpCenterModel.HelpCenterData> mList = new ArrayList<>();
    private LoadingMoreListView mListView;

    private OnFragmentInteractionListener mListener;

    private Adapter mAdapter;
    private int pageIndex = 1;
    private int totalCount = 0;

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
                if (mList != null && mList.size() > 0) {
                    String strUri = UrlFactory.helpCenterDetailForHtml(mList.get(position).getmStrHelpCenterId());
                    BrowserActivity.startActivity(getActivity(), strUri);
                }
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


    /**
     * 获取列表数据
     */
    private void fetchHelpCenterList(int pageIndex) {
        showProgressBar(true);
        HelpCenterCtrl.getHelpCenter("getList", pageIndex, new Response.Listener<HelpCenterModel>() {
            @Override
            public void onResponse(HelpCenterModel helpCenterModel) {
                hideProgressBar();
                totalCount = helpCenterModel.getTotalCount();
                if (totalCount <= 0){
                    hideEmptyView();
                    mPtrFrame.setVisibility(View.GONE);
                    mPtrFrameEmpty.setVisibility(View.VISIBLE);
                    mPtrFrameEmpty.refreshComplete();
                }

                if (helpCenterModel.getStrHelpCenterData() == null) {
                    return;
                }

               if (mList.isEmpty()) {
                    mList = new ArrayList<>();
                    mList = helpCenterModel.getArryListHelpCenterData();
                    if (!mList.isEmpty() && mPtrFrame != null) {
                        getActivity();
                        hideEmptyView();
                        mPtrFrame.setVisibility(View.VISIBLE);
                        mPtrFrameEmpty.setVisibility(View.GONE);
                        mPtrFrame.refreshComplete();
                    } else if (mPtrFrameEmpty != null) {
                        hideEmptyView();
                        mPtrFrame.setVisibility(View.GONE);
                        mPtrFrameEmpty.setVisibility(View.VISIBLE);
                        mPtrFrameEmpty.refreshComplete();
                    }
                } else {
                    hideEmptyView();
                    for (int i = 0; i < helpCenterModel.getArryListHelpCenterData().size(); i++) {
                        mList.add(helpCenterModel.getArryListHelpCenterData().get(i));
                    }
                }

                if (mAdapter != null) {
                    mAdapter.notifyDataSetChanged();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                showEmptyView();
                hideProgressBar();
                if (mPtrFrameEmpty != null) {
                    mPtrFrame.refreshComplete();
                } else if (mPtrFrame != null) {
                    mPtrFrame.refreshComplete();
                }
            }
        });
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
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mListener = (OnFragmentInteractionListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * 加载更多
     */
    @Override
    public void onLoadingMore() {
        if (mList.size() >= totalCount) {
            Toast.makeText(getActivity(), getString(R.string.error_no_more_data), Toast.LENGTH_LONG).show();
        } else {
            pageIndex++;
            fetchHelpCenterList(pageIndex);
        }
        mListView.hideFooterView();
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        updateData();
    }

    /**
     * 更新数据
     */
    public void updateData() {
        pageIndex = 1;
        if(mList !=null){
            mList.clear();
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }
        fetchHelpCenterList(pageIndex);
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
            HelpCenterModel.HelpCenterData mhelpCenterData = mList.get(position);
            holder.mTvHelpCenterTitle.setText(mhelpCenterData.getmStrHelpCenterTitle());
            return convertView;
        }
    }

    private static class ViewHolder {
        private TextView mTvHelpCenterTitle;
        public static ViewHolder findAndCacheViews(View view) {
            ViewHolder holder = new ViewHolder();
            holder.mTvHelpCenterTitle = (TextView) view.findViewById(R.id.settings_help_center);
            view.setTag(holder);
            return holder;
        }
    }
}
