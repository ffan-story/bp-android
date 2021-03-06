package com.feifan.bp.message;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.feifan.bp.Constants;
import com.feifan.bp.R;

import java.util.ArrayList;


/**
 * 消息adapter
 * Created by apple on 16/3/3.
 */
class MessageAdapter extends BaseAdapter{
    private Context mContext;
    private ArrayList<MessageModel.MessageData> messageDataList;
    private String mStrMessType;
    public MessageAdapter(Context context, ArrayList<MessageModel.MessageData> messageDataList,String messTyle){
        this.messageDataList = messageDataList;
        mContext = context;
        mStrMessType = messTyle;
    }

    public void notifyData(ArrayList<MessageModel.MessageData> messageDataList,String messTyle){
        this.messageDataList = messageDataList;
        notifyDataSetChanged();
        mStrMessType = messTyle;
    }
    @Override
    public int getCount() {
        return messageDataList.size();
    }

    @Override
    public Object getItem(int position) {
        return messageDataList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mViewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_mess,null);
            mViewHolder = new ViewHolder(convertView);
            convertView.setTag(mViewHolder);
        }else{
            mViewHolder = (ViewHolder)convertView.getTag();
        }
        MessageModel.MessageData messageData = messageDataList.get(position);


        if (position == messageDataList.size()-1){
            mViewHolder.mImgLine2.setVisibility(View.GONE);
        }else{
            mViewHolder.mImgLine2.setVisibility(View.VISIBLE);
        }

        if (null != messageData.mStrMessStatus && messageData.mStrMessStatus.equals(Constants.UNREAD)) {
            mViewHolder.mImgRedDot.setVisibility(View.VISIBLE);
            mViewHolder.mMessSnippet.setTextColor(mContext.getResources().getColor(R.color.font_color_66));
        } else {
            mViewHolder.mImgRedDot.setVisibility(View.INVISIBLE);
            mViewHolder.mMessSnippet.setTextColor(mContext.getResources().getColor(R.color.font_color_99));
        }

        mViewHolder.mMessTime.setText(messageData.mStrMessTime);
        if (!TextUtils.isEmpty(mStrMessType) && mStrMessType.equals(MessageFragment.MESS_TYPE_NOTICE)){
            mViewHolder.mMessSnippet.setText(messageData.mStrMessTitle);
            mViewHolder.mMessSender.setText(messageData.mStrMessSender);
        }else{
            mViewHolder.mMessSnippet.setText(messageData.mStrMessSniper);
            mViewHolder.mMessSender.setText(messageData.mStrMessSender);
        }
        if (TextUtils.isEmpty(messageData.mStrMessTag)){
            mViewHolder.mTvTag.setVisibility(View.GONE);
        }else{
            mViewHolder.mTvTag.setVisibility(View.VISIBLE);
            mViewHolder.mTvTag.setText(messageData.mStrMessTag);
        }
        return convertView;
    }

    public static class ViewHolder {
        private ImageView mImgLine2;
        private ImageView mImgRedDot;
        private TextView mMessSnippet,mMessTime,  mMessSender,mTvTag;
        public ViewHolder(View itemView) {
            mMessSnippet = (TextView)itemView.findViewById(R.id.tv_message_snippet);
            mMessTime = (TextView)itemView.findViewById(R.id.tv_message_time);
            mMessSender = (TextView)itemView.findViewById(R.id.tv_message_sender);
            mImgRedDot=(ImageView)itemView.findViewById(R.id.img_message_red_point);
            mTvTag = (TextView)itemView.findViewById(R.id.tv_message_tag);
            mImgLine2 = (ImageView)itemView.findViewById(R.id.line2);
        }
    }
}
