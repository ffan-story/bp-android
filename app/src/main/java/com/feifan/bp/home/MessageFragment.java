package com.feifan.bp.home;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.toolbox.ImageLoader;
import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.R;
import com.feifan.bp.UserProfile;
import com.feifan.bp.base.BaseFragment;
import com.feifan.bp.util.LogUtil;
import com.feifan.bp.widget.LoadingMoreListView;
import com.feifan.bp.widget.OnLoadingMoreListener;

import java.util.ArrayList;
import java.util.List;

import bp.feifan.com.refresh.PtrClassicFrameLayout;
import bp.feifan.com.refresh.PtrDefaultHandler;
import bp.feifan.com.refresh.PtrFrameLayout;
import bp.feifan.com.refresh.PtrHandler;

public class MessageFragment extends BaseFragment implements OnLoadingMoreListener{
    private static final String TAG = MessageFragment.class.getSimpleName();

//    private ImageLoader mImageLoader;
    private PtrClassicFrameLayout mPtrFrame;


    private int pageIndex = 1;

    LoadingMoreListView mListView;
    private List<MessageModel.MessageData> mList =new ArrayList<MessageModel.MessageData>();
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
        HomeCtrl.messageList( UserProfile.getInstance().getUid()+"", pageIndex+"", new Response.Listener<MessageModel>() {
            @Override
            public void onResponse(MessageModel messageModel) {
                mList = messageModel.getMessageDataList();
            }
        });
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setTitle(R.string.home_message_title_text);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//        mImageLoader = ImageLoaderFactory.create(getContext());

        final View contentView = inflater.inflate(R.layout.refresh_listview, null);

        mListView = (LoadingMoreListView) contentView.findViewById(R.id.rotate_header_list_view);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= 0) {


                }
            }
        });

        mAdapter = new Adapter(getActivity());
        mListView.setAdapter(mAdapter);
        mListView.setOnLoadingMoreListener(this);
        mPtrFrame = (PtrClassicFrameLayout) contentView.findViewById(R.id.rotate_header_list_view_frame);
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setPtrHandler(new PtrHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                updateData();
            }

            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
            }
        });
        // the following are default settings
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        // default is false
        mPtrFrame.setPullToRefresh(false);
        // default is true
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
     * 跟新
     */
    protected void updateData() {
        LogUtil.e(TAG,"UPdate data!!!!!");
        mPtrFrame.refreshComplete();
    }

    /**
     * 加载更多
     */
    @Override
    public void onLoadingMore() {
        // 控制脚布局隐藏
        //mListView.hideFooterView();
    }


    class Adapter extends BaseAdapter {

        private final LayoutInflater mInflater;
        Context context ;
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
                convertView = mInflater.inflate(R.layout.item_message,parent, false);
                holder = ViewHolder.findAndCacheViews(convertView);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }MessageModel.MessageData data = mList.get(position);
            if (null != data.getmStrMessageStatus() && data.getmStrMessageStatus().equals("0")) {
                holder.mImgRedPoint.setVisibility(View.VISIBLE);
                holder.mTvMessageTitle.setTextColor(context.getResources().getColor(R.color.font_color_66));
            } else {
                holder.mImgRedPoint.setVisibility(View.INVISIBLE);
                holder.mTvMessageTitle.setTextColor(context.getResources().getColor(R.color.feed_back_color));
            }
            holder.mTvMessageTitle.setText(data.getmStrMessageTitle());
            holder.mTvMessageTime.setText( data.getmStrMessageTime());
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
