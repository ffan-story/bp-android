package com.feifan.bp.home;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.feifan.bp.Constants;
import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.PlatformState;
import com.feifan.bp.R;
import com.feifan.bp.UserProfile;
import com.feifan.bp.Utils;
import com.feifan.bp.base.BaseFragment;
import com.feifan.bp.browser.BrowserActivity;
import com.feifan.bp.envir.EnvironmentManager;
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
 * congjing
 * 消息列表
 */
public class MessageFragment extends BaseFragment implements OnLoadingMoreListener, PtrHandler {
    private PtrClassicFrameLayout mPtrFrame, mPtrFrameEmpty;
    private int pageIndex = 1;
    private int totalCount = 0;
    private LoadingMoreListView mListView;
    private List<MessageModel.MessageData> mList = new ArrayList<>();
    private Adapter mAdapter;

    private OnFragmentInteractionListener mListener;

    public static MessageFragment newInstance() {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putString(Constants.EXTRA_KEY_TITLE, Utils.getString(R.string.home_message_title_text));
        fragment.setArguments(args);
        return fragment;
    }

    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener)context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hideEmptyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mList !=null  && mList.size()>0){
            mAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取列表数据
     */
    private void fetchData(int pageIndex) {
        showProgressBar(true);
        HomeCtrl.messageList(UserProfile.getInstance().getUid() + "", pageIndex, new Response.Listener<MessageModel>() {
            @Override
            public void onResponse(MessageModel messageModel) {

                hideProgressBar();
                totalCount = messageModel.getTotalCount();
                if (totalCount<=0 && mPtrFrameEmpty != null) {
                    hideEmptyView();
                    mPtrFrame.setVisibility(View.GONE);
                    mPtrFrameEmpty.setVisibility(View.VISIBLE);
                    mPtrFrameEmpty.refreshComplete();
                }
                if (messageModel.getMessageDataList() == null) {
                    return;
                }
                if (mList == null || mList.size() <= 0) {
                    mList = new ArrayList<>();
                    mList = messageModel.getMessageDataList();
                    if (mList != null && mList.size() > 0 && mPtrFrame != null) {
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
                    for (int i = 0; i < messageModel.getMessageDataList().size(); i++) {
                        mList.add(messageModel.getMessageDataList().get(i));
                    }
                }
                mAdapter.notifyDataSetChanged();

                // FIXME add by xuchunlei
                // 获取未读提示状态
                String storeId = null;
                String merchantId = null;
                if(UserProfile.getInstance().isStoreUser()){
                    storeId = UserProfile.getInstance().getAuthRangeId();
                }else {
                    merchantId = UserProfile.getInstance().getAuthRangeId();
                }

                HomeCtrl.getUnReadtatus(merchantId, storeId, "1", new Response.Listener<ReadMessageModel>() {
                    @Override
                    public void onResponse(ReadMessageModel readMessageModel) {
                        int refundId = Integer.valueOf(EnvironmentManager.getAuthFactory().getRefundId());
                        PlatformState.getInstance().updateUnreadStatus(refundId, readMessageModel.refundCount > 0);

                        // 更新消息提示
                        if(mListener != null) {
                            mListener.onStatusChanged(readMessageModel.messageCount > 0);
                        }
                    }
                });
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

    private void setMessageListStatus(String userid, String maillnboxid,final int position) {
        HomeCtrl.setMessageStatusRead(userid, maillnboxid, new Response.Listener<MessageStatusModel>() {
            @Override
            public void onResponse(MessageStatusModel messageModel) {
                mList.get(position).setmStrMessageStatus(Constants.READ);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        });
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
                    if (mList.get(position).getmStrMessageStatus() != null && mList.get(position).getmStrMessageStatus().equals(Constants.UNREAD)) {
                        setMessageListStatus(mList.get(position).getUserid(), mList.get(position).getMaillnboxid(), position);
                    }
                    String strUri = UrlFactory.urlForHtml(mList.get(position).getmStrDetailUrl());
                    BrowserActivity.startActivity(getActivity(), strUri);

//                   Bundle args = new Bundle();
//                    args.putInt(ErrorFragment.EXTRA_KEY_ERROR_MIPMAP_ID,R.mipmap.icon_empty);
//                    args.putString(ErrorFragment.EXTRA_KEY_ERROR_MESSAGE, getActivity().getApplicationContext().getString(R.string.instant_goods_not_have_goods_tips));
//                    args.putString(ErrorFragment.EXTRA_KEY_ERROR_BTN_TEXT, "去商品管理");
//                    args.putInt(ErrorFragment.EXTRA_KEY_ERROR_BTN_TEXT_TYPE, ErrorFragment.EXTRA_KEY_ERROR_BTN_LISTENER_TO_GOODS_MANAGE);
//                    PlatformTopbarActivity.startActivity(getActivity(), ErrorFragment.class.getName(),
//                            getActivity().getApplicationContext().getString(R.string.query_result), args);

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

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onPause() {
        super.onPause();
        hideEmptyView();
    }

    /**
     * 更新数据
     */
    public void updateData() {
        pageIndex = 1;
        if(mList !=null){
            mList.clear();
            mAdapter.notifyDataSetChanged();
        }
        fetchData(pageIndex);
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
            fetchData(pageIndex);
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
                convertView = mInflater.inflate(R.layout.item_message, parent, false);
                holder = ViewHolder.findAndCacheViews(convertView);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            MessageModel.MessageData data = mList.get(position);
            if (null != data.getmStrMessageStatus() && data.getmStrMessageStatus().equals(Constants.UNREAD)) {
                holder.mImgRedPoint.setVisibility(View.VISIBLE);
                holder.mTvMessageTitle.setTextColor(context.getResources().getColor(R.color.font_color_66));
            } else {
                holder.mImgRedPoint.setVisibility(View.INVISIBLE);
                holder.mTvMessageTitle.setTextColor(context.getResources().getColor(R.color.font_color_99));
            }
            holder.mTvMessageTitle.setText(data.getmStrMessageTitle());
            holder.mTvMessageTime.setText(data.getmStrMessageTime());
            return convertView;
        }
    }

    private static class ViewHolder {
        private ImageView mImgRedPoint;
        private TextView mTvMessageTitle;
        private TextView mTvMessageTime;
        public static ViewHolder findAndCacheViews(View view) {
            ViewHolder holder = new ViewHolder();
            holder.mImgRedPoint = (ImageView) view.findViewById(R.id.img_message_redpoint);
            holder.mTvMessageTitle = (TextView) view.findViewById(R.id.tv_message_title);
            holder.mTvMessageTime = (TextView) view.findViewById(R.id.tv_message_time);
            view.setTag(holder);
            return holder;
        }
    }
}
