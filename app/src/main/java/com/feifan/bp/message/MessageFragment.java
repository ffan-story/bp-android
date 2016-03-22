package com.feifan.bp.message;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.internal.widget.ViewStubCompat;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.feifan.bp.Constants;
import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.PlatformState;
import com.feifan.bp.R;
import com.feifan.bp.UserProfile;
import com.feifan.bp.Utils;
import com.feifan.bp.base.ProgressFragment;
import com.feifan.bp.browser.BrowserActivity;
import com.feifan.bp.envir.EnvironmentManager;
import com.feifan.bp.home.HomeCtrl;
import com.feifan.bp.home.ReadMessageModel;
import com.feifan.bp.network.UrlFactory;
import com.feifan.bp.network.response.DialogErrorListener;
import com.feifan.bp.widget.LoadingMoreListView;
import com.feifan.bp.widget.OnLoadingMoreListener;

import java.util.ArrayList;

import bp.feifan.com.refresh.PtrClassicFrameLayout;
import bp.feifan.com.refresh.PtrDefaultHandler;
import bp.feifan.com.refresh.PtrFrameLayout;
import bp.feifan.com.refresh.PtrHandler;

/**
 * Created by apple on 16/3/7.
 */
public class MessageFragment extends ProgressFragment implements OnLoadingMoreListener, PtrHandler,View.OnClickListener {
    private PtrClassicFrameLayout mPtrFrame, mPtrFrameEmpty;
    private int pageIndex = 1;
    private int totalCount = 0;
    public static String MESS_TYPE_SYSTEM = "1";
    public static String MESS_TYPE_NOTICE = "2";
    private String mMessType = MESS_TYPE_SYSTEM;

    private LoadingMoreListView mListView;
    private ArrayList<MessageModel.MessageData> mList = new ArrayList<>();
    private MessageAdapter mAdapter;

    private RelativeLayout mRelSystem,mRelNotice;
    private ImageView mImgSystem,mImgNotice;
    private int  mIntNoticeMessCount,mIntSystemMessCount;

    private TextView mTvSystemTitle,mTvNoticeTitle;
    private TextView mTvSystemDot,mTvNoticeDot;

    private OnFragmentInteractionListener mListener;

    /**
     * 首次进入在onResume方法中不刷新数据，只在onRefreshBegin中刷新数据。
     */
    boolean isOnceResume = false;

    public static MessageFragment newInstance() {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putString(Constants.EXTRA_KEY_TITLE, Utils.getString(R.string.home_message_title_text));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener)context;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mList !=null  && mList.size()>0 && isOnceResume){
            updateData();
        }
    }

    @Override
    protected View onCreateContentView(ViewStubCompat stub) {
        isOnceResume = false;
        pageIndex = 1;
        mMessType = MESS_TYPE_SYSTEM;

        stub.setLayoutResource(R.layout.fragment_message_main);
        View view = stub.inflate();
        mPtrFrame = (PtrClassicFrameLayout) view.findViewById(R.id.rotate_header_list_view_frame);
        mListView = (LoadingMoreListView) mPtrFrame.findViewById(R.id.rotate_header_list_view);
        mPtrFrameEmpty = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_empty);
        mRelSystem = (RelativeLayout)view.findViewById(R.id.rel_mess_system);
        mRelNotice = (RelativeLayout)view.findViewById(R.id.rel_mess_notice);
        mRelSystem.setOnClickListener(this);
        mRelNotice.setOnClickListener(this);

        mImgSystem = (ImageView)view.findViewById(R.id.img_line_system);
        mImgNotice = (ImageView)view.findViewById(R.id.img_line_notice);

        mTvSystemTitle = (TextView)view.findViewById(R.id.tv_message_system);
        mTvNoticeTitle = (TextView)view.findViewById(R.id.tv_message_notice);

        mTvSystemDot = (TextView)view.findViewById(R.id.tv_dot_system);
        mTvNoticeDot = (TextView)view.findViewById(R.id.tv_dot_notice);

        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mList != null && mList.size() > 0) {
                    if (mList.get(position).mStrMessStatus != null && mList.get(position).mStrMessStatus.equals(Constants.UNREAD)) {
                        setMessageListStatus(mList.get(position).mStrMessId, mList.get(position).mStrMailInboxId, position);
                    }
                    String strUri = UrlFactory.urlForHtml(mList.get(position).mStrDetailUrl);
                    BrowserActivity.startActivity(getActivity(), strUri);
                }
            }
        });

        mAdapter = new MessageAdapter(getActivity(),mList,mMessType);
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
        return view;
    }

    @Override
    protected void requestData() {

    }

    /**
     * 更新数据
     */
    public void updateData() {
        pageIndex = 1;
        if(mList !=null){
            mList.clear();
            mAdapter.notifyDataSetChanged();
            fetchData(pageIndex, mMessType);
        }
    }

    private void getRedDot(){
        HomeCtrl.getUnReadtatus(new Response.Listener<ReadMessageModel>() {
            @Override
            public void onResponse(ReadMessageModel readMessageModel) {
                int refundId = Integer.valueOf(EnvironmentManager.getAuthFactory().getRefundId());
                PlatformState.getInstance().updateUnreadStatus(refundId, readMessageModel.refundCount > 0);
                mIntSystemMessCount = readMessageModel.mIntSystemMessCount;
                mIntNoticeMessCount = readMessageModel.mIntNoticeMessCount;
                setRedDot(mIntSystemMessCount, mIntNoticeMessCount);
                // 更新消息提示
                if (mListener != null) {
                    mListener.onStatusChanged(readMessageModel.messageCount > 0,readMessageModel.messageCount);
                }
            }
        });
    }

    /**
     *
     * @param intSystemMessCount
     * @param intNoticeMessCount
     */
    private void setRedDot(int intSystemMessCount,int intNoticeMessCount){
        if (intSystemMessCount>=100){
            mTvSystemDot.setVisibility(View.VISIBLE);
            mTvSystemDot.setText("…");
        }else if (intSystemMessCount>0){
            mTvSystemDot.setVisibility(View.VISIBLE);
            mTvSystemDot.setText(String.valueOf(intSystemMessCount));
        }else{
            mTvSystemDot.setVisibility(View.INVISIBLE);
        }

        if (intNoticeMessCount>=100){
            mTvNoticeDot.setVisibility(View.VISIBLE);
            mTvNoticeDot.setText("…");
        }else if (intNoticeMessCount>0){
            mTvNoticeDot.setVisibility(View.VISIBLE);
            mTvNoticeDot.setText(String.valueOf(intNoticeMessCount));
        }else{
            mTvNoticeDot.setVisibility(View.INVISIBLE);
        }


    }

    /**
     * 获取列表数据
     */
    public void fetchData (int pageIndex, final String messType){
        if (!isAdded()){
            return;
        }

        setContentShown(true);
        if (!Utils.isNetworkAvailable(getActivity())) {
            setContentEmpty(true, getActivity().getResources().getString(R.string.empty_view_text), getActivity().getResources().getString(R.string.common_retry_text), R.mipmap.empty_ic_timeout, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    fetchData(1, mMessType);
                }
            });
            return;
        }

        MessageCtrl.getMessageCategoryList(UserProfile.getInstance().getUid() + "", pageIndex, messType, new Response.Listener<MessageModel>() {
            @Override
            public void onResponse(MessageModel messageModel) {
                totalCount = messageModel.totalCount;
                if (totalCount <= 0 && mPtrFrameEmpty != null) {
                    setContentEmpty(false);
                    mPtrFrame.setVisibility(View.GONE);
                    mPtrFrameEmpty.setVisibility(View.VISIBLE);
                    return;
                }
                if (messageModel.messageDataList == null) {
                    return;
                }
                setContentEmpty(true);
                if (mList == null || mList.size() <= 0) {
                    mList = new ArrayList<>();
                    mList = messageModel.messageDataList;
                    if (mList != null && mList.size() > 0 && mPtrFrame != null) {
                        setContentEmpty(false);
                        mPtrFrame.setVisibility(View.VISIBLE);
                        mPtrFrameEmpty.setVisibility(View.GONE);
                        mPtrFrame.refreshComplete();
                    } else if (mPtrFrameEmpty != null) {
                        setContentEmpty(false);
                        mPtrFrame.setVisibility(View.GONE);
                        mPtrFrameEmpty.setVisibility(View.VISIBLE);
                        mPtrFrameEmpty.refreshComplete();
                    }
                } else {
                    setContentEmpty(false);
                    for (int i = 0; i < messageModel.messageDataList.size(); i++) {
                        mList.add(messageModel.messageDataList.get(i));
                    }
                }
                mAdapter.notifyData(mList,messType);
                getRedDot();
                isOnceResume = true;
            }
        }, new DialogErrorListener() {
            @Override
            protected void postDisposeError() {
                super.postDisposeError();
                setContentShown(true);
                if (mPtrFrameEmpty != null) {
                    mPtrFrame.refreshComplete();
                } else if (mPtrFrame != null) {
                    mPtrFrame.refreshComplete();
                }
            }
        });

    }

    /**
     * 修改message 状态接口
     * @param userid
     * @param maillnboxid
     * @param position
     */
    private void setMessageListStatus(String userid, String maillnboxid,final int position) {
        HomeCtrl.setMessageStatusRead(userid, maillnboxid, new Response.Listener<MessageStatusModel>() {
            @Override
            public void onResponse(MessageStatusModel messageModel) {
                mList.get(position).mStrMessStatus = Constants.READ;
            }
        }, new DialogErrorListener());
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        return false;
    }

    @Override
    public void onLoadingMore() {
        if (mList.size() >= totalCount) {
            Toast.makeText(getActivity(), getString(R.string.error_no_more_data), Toast.LENGTH_LONG).show();
        } else {
            pageIndex++;
            fetchData(pageIndex, mMessType);
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rel_mess_system:
                mMessType = MESS_TYPE_SYSTEM;
                pageIndex =1;
                mList.clear();
                mAdapter.notifyDataSetChanged();
                mImgNotice.setVisibility(View.INVISIBLE);
                mImgSystem.setVisibility(View.VISIBLE);
                mTvSystemTitle.setTextColor(Color.parseColor("#3d99e9"));
                mTvNoticeTitle.setTextColor(Color.parseColor("#666666"));
                fetchData(pageIndex, mMessType);
                break;
            case R.id.rel_mess_notice:
                mMessType = MESS_TYPE_NOTICE;
                pageIndex =1;
                mList.clear();
                mAdapter.notifyDataSetChanged();
                mImgNotice.setVisibility(View.VISIBLE);
                mImgSystem.setVisibility(View.INVISIBLE);
                mTvNoticeTitle.setTextColor(Color.parseColor("#3d99e9"));
                mTvSystemTitle.setTextColor(Color.parseColor("#666666"));
                fetchData(pageIndex,mMessType);
                break;
        }
    }
}