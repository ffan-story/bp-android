package com.feifan.bp.message;

import android.content.Context;
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
    public MessageAdapter(Context context, ArrayList<MessageModel.MessageData> messageDataList){
        this.messageDataList = messageDataList;
        mContext = context;
    }

    public void notifyData(ArrayList<MessageModel.MessageData> messageDataList){
        this.messageDataList = messageDataList;
        notifyDataSetChanged();
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

        if (null != messageData.mStrMessStatus && messageData.mStrMessStatus.equals(Constants.UNREAD)) {
            mViewHolder.mImgRedDot.setVisibility(View.VISIBLE);
            mViewHolder.mMessSnippet.setTextColor(mContext.getResources().getColor(R.color.font_color_66));
        } else {
            mViewHolder.mImgRedDot.setVisibility(View.INVISIBLE);
            mViewHolder.mMessSnippet.setTextColor(mContext.getResources().getColor(R.color.font_color_99));
        }

        mViewHolder.mMessSnippet.setText(messageData.mStrMessSniper);
        mViewHolder.mMessTime.setText(messageData.mStrMessTime);
        mViewHolder.mMessSender.setText(messageData.mStrMessSender);
        return convertView;
    }

    public static class ViewHolder {
        private ImageView mImgRedDot;
        private TextView mMessSnippet,mMessTime,  mMessSender;
        public ViewHolder(View itemView) {
            mMessSnippet = (TextView)itemView.findViewById(R.id.tv_message_snippet);
            mMessTime = (TextView)itemView.findViewById(R.id.tv_message_time);
            mMessSender = (TextView)itemView.findViewById(R.id.tv_message_sender);
            mImgRedDot=(ImageView)itemView.findViewById(R.id.img_message_red_point);
        }
    }
}
