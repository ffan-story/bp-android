package com.feifan.bp.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.feifan.bp.Constants;
import com.feifan.bp.R;
import com.feifan.bp.UserProfile;
import com.feifan.bp.base.BaseFragment;
import com.feifan.bp.browser.BrowserActivity;
import com.feifan.bp.network.UrlFactory;
import com.feifan.bp.util.LogUtil;
import com.feifan.bp.widget.LoadingMoreListView;
import com.feifan.bp.widget.OnLoadingMoreListener;

import java.util.ArrayList;
import java.util.List;

import bp.feifan.com.refresh.PtrClassicFrameLayout;
import bp.feifan.com.refresh.PtrDefaultHandler;
import bp.feifan.com.refresh.PtrFrameLayout;
import bp.feifan.com.refresh.PtrHandler;

/**
 * 消息列表
 */
public class MessageFragment extends BaseFragment implements OnLoadingMoreListener, PtrHandler {
    private static final String TAG = MessageFragment.class.getSimpleName();

    private PtrClassicFrameLayout mPtrFrame, mPtrFrameEmpty;
    private int pageIndex = 1;
    private int totalCount = 0;
    LoadingMoreListView mListView;
    private List<MessageModel.MessageData> mList = new ArrayList<MessageModel.MessageData>();
    private Adapter mAdapter;

    public static MessageFragment newInstance() {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public MessageFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * 获取列表数据
     */
    private void fetchData(int pageIndex) {
        HomeCtrl.messageList(UserProfile.getInstance().getUid() + "", pageIndex, new Response.Listener<MessageModel>() {
            @Override
            public void onResponse(MessageModel messageModel) {
                totalCount = messageModel.getTotalCount();
                if (mList == null || mList.size() <= 0) {
                    mList = new ArrayList<MessageModel.MessageData>();
                    mList = messageModel.getMessageDataList();
                } else {
                    for (int i = 0; i < messageModel.getMessageDataList().size(); i++) {
                        mList.add(messageModel.getMessageDataList().get(i));
                    }
                    mListView.hideFooterView();
                }
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setMessageListStatus(String userid, String maillnboxid) {
        HomeCtrl.setMessageStatusRead(userid, maillnboxid, new Response.Listener<MessageStatusModel>() {
            @Override
            public void onResponse(MessageStatusModel messageModel) {
            }
        });
    }


    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setTitle(R.string.home_message_title_text);
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
                        setMessageListStatus(mList.get(position).getUserid(), mList.get(position).getMaillnboxid());
                        mList.get(position).setmStrMessageStatus(Constants.READ);
                        mAdapter.notifyDataSetChanged();
                    }
                    String strUri = UrlFactory.urlForHtml(mList.get(position).getmStrDetailUrl());
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

    @Override
    public void onDetach() {
        super.onDetach();
    }

    /**
     * 更新数据
     */
    protected void updateData() {
        pageIndex = 1;
        fetchData(pageIndex);
        if(mList != null && mList.size()>0){
            mPtrFrame.setVisibility(View.VISIBLE);
            mPtrFrameEmpty.setVisibility(View.GONE);
            mList.clear();
            mAdapter.notifyDataSetChanged();
            mPtrFrame.refreshComplete();
        }else{
            mPtrFrame.setVisibility(View.GONE);
            mPtrFrameEmpty.setVisibility(View.VISIBLE);
            mPtrFrameEmpty.refreshComplete();
        }
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadingMore() {
        if (mList.size() >= totalCount) {
            Toast.makeText(getActivity(), "没有更多数据", Toast.LENGTH_LONG).show();
            mListView.hideFooterView();
        } else {
            pageIndex++;
            fetchData(pageIndex);
        }
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
                holder.mTvMessageTitle.setTextColor(context.getResources().getColor(R.color.feed_back_color));
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
