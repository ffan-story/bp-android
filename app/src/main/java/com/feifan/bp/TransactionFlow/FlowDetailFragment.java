package com.feifan.bp.transactionflow;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.transactionflow.FlashSummaryModel.FlashSummaryDetailModel;

import java.util.ArrayList;

/**
 * Created by Frank on 15/11/7.
 */
public class FlowDetailFragment extends Fragment {
    private static final String PAGE_NUM = "pageNum";
    private static final String TRADE_DATA_LIST = "tradeDataList";
    private int mPageNum;
    private ArrayList<FlashSummaryDetailModel> mTradeDataList;

    public static FlowDetailFragment newInstance(int pageNum,ArrayList<FlashSummaryDetailModel> tradeDataList) {
        FlowDetailFragment fragment = new FlowDetailFragment();
        Bundle args = new Bundle();
        args.putInt(PAGE_NUM, pageNum);
        args.putParcelableArrayList(TRADE_DATA_LIST,tradeDataList);
        fragment.setArguments(args);
        return fragment;
    }

    public FlowDetailFragment() {
    }

    public void setmTradeDataList(ArrayList<FlashSummaryDetailModel> mTradeDataList) {
        this.mTradeDataList = mTradeDataList;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPageNum = getArguments().getInt(PAGE_NUM);
            mTradeDataList = getArguments().getParcelableArrayList(TRADE_DATA_LIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_flow_detail, container, false);

        TextView title1 = (TextView) v.findViewById(R.id.item_1).findViewById(R.id.title);
        TextView title2 = (TextView) v.findViewById(R.id.item_2).findViewById(R.id.title);
        TextView title3 = (TextView) v.findViewById(R.id.item_3).findViewById(R.id.title);
        TextView title4 = (TextView) v.findViewById(R.id.item_4).findViewById(R.id.title);

        TextView content1= (TextView) v.findViewById(R.id.item_1).findViewById(R.id.content);
        TextView content2 = (TextView) v.findViewById(R.id.item_2).findViewById(R.id.content);
        TextView content3 = (TextView) v.findViewById(R.id.item_3).findViewById(R.id.content);
        TextView content4 = (TextView) v.findViewById(R.id.item_4).findViewById(R.id.content);
        switch (mPageNum){
            case 0:
                title1.setText(mTradeDataList.get(0).getCountName());
                content1.setText(mTradeDataList.get(0).getCountValue());

                title2.setText(mTradeDataList.get(0).getAmountName());
                content2.setText(Utils.formatMoney(mTradeDataList.get(0).getAmountValue(),2));

                title3.setText(mTradeDataList.get(1).getCountName());
                content3.setText(mTradeDataList.get(1).getCountValue());

                title4.setText(mTradeDataList.get(1).getAmountName());
                content4.setText(Utils.formatMoney(mTradeDataList.get(1).getAmountValue(),2));

                break;
            case 1:
                title1.setText(mTradeDataList.get(2).getCountName());
                content1.setText(mTradeDataList.get(2).getCountValue());

                title2.setText(mTradeDataList.get(2).getAmountName());
                content2.setText(Utils.formatMoney(mTradeDataList.get(2).getAmountValue(),2));

                title3.setText(mTradeDataList.get(3).getCountName());
                content3.setText(mTradeDataList.get(3).getCountValue());

                title4.setText(mTradeDataList.get(3).getAmountName());
                content4.setText(Utils.formatMoney(mTradeDataList.get(3).getAmountValue(),2));

                break;
        }
        return v;
    }
}
