package com.feifan.bp.home;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.base.BaseFragment;
import com.feifan.bp.home.command.OrderManagementCmd;
import com.feifan.bp.home.command.RefundCmd;
import com.feifan.bp.home.command.StaffManagementCmd;
import com.feifan.bp.home.command.StatisticReportCmd;
import com.feifan.bp.net.NetUtils;
import com.feifan.bp.scanner.CodeScannerActivity;
import com.feifan.bp.widget.IconClickableEditText;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页Fragment
 * Created by xuchunlei on 15/7/2.
 */
public class IndexFragment extends BaseFragment implements View.OnClickListener,
        IconClickableEditText.OnIconClickListener {

    private OnFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;

    // views
    private IconClickableEditText mCodeEdt;

    public static IndexFragment newInstance() {
        IndexFragment fragment = new IndexFragment();
        return fragment;
    }

    public IndexFragment() {
        // Required empty public constructor
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setTitle(R.string.app_name);
    }

    @Override
    public void onResume() {
        super.onResume();
        getToolbar().setVisibility(View.GONE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_index, container, false);
        v.findViewById(R.id.index_scan).setOnClickListener(this);
        v.findViewById(R.id.index_history).setOnClickListener(this);
        mCodeEdt = (IconClickableEditText) v.findViewById(R.id.index_search_input);
        mCodeEdt.setOnIconClickListener(this);

        List<FunctionModel> dataList = new ArrayList<>();
        dataList.add(new FunctionModel(new OrderManagementCmd(getActivity()),
                getString(R.string.index_order_text), R.mipmap.index_ic_order));
        dataList.add(new FunctionModel(new StatisticReportCmd(getActivity()),
                getString(R.string.index_report_text), R.mipmap.index_ic_report));
        dataList.add(new FunctionModel(new StaffManagementCmd(getActivity()),
                getString(R.string.index_staff_text), R.mipmap.index_ic_staff));
        dataList.add(new FunctionModel(new RefundCmd(getActivity()),
                getString(R.string.index_refund_text), R.mipmap.index_ic_refund));

        mRecyclerView = (RecyclerView) v.findViewById(R.id.rv_function_container);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(
                2, StaggeredGridLayoutManager.VERTICAL));
        mRecyclerView.setAdapter(new IndexAdapter(dataList));
        return v;
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
    public void onClick(View v) {
        Bundle args = new Bundle();
        args.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM, IndexFragment.class.getName());
        switch (v.getId()) {
            case R.id.index_scan:
                args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, CodeScannerActivity.class.getName());
                break;
            case R.id.index_history:
                args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO,
                        NetUtils.getUrlFactory().checkHistoryForHtml(getActivity()));
                break;
            default:
                return;
        }
        mListener.onFragmentInteraction(args);

    }

    @Override
    public void onRightClicked(View v) {

        if (TextUtils.isEmpty(mCodeEdt.getText())) {
            return;
        }

        if (!Utils.isNetworkAvailable(getActivity())) {
            Utils.showShortToast(getActivity(), R.string.error_message_text_offline, Gravity.CENTER);
            return;
        }

        String code = mCodeEdt.getText().toString();


        try {
            Utils.checkDigitAndLetter(getActivity(), code);
        } catch (Throwable throwable) {
            Utils.showShortToast(getActivity(), throwable.getMessage());
            return;
        }


        Bundle args = new Bundle();
        switch (v.getId()) {
            case R.id.index_search_input:
                args.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM,
                        IndexFragment.class.getName());
                args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO,
                        NetUtils.getUrlFactory().searchCodeForHtml(getActivity(), code));
                break;
            default:
                break;
        }
        mListener.onFragmentInteraction(args);
    }

    class IndexAdapter extends RecyclerView.Adapter<IndexAdapter.IndexViewHolder> {

        private List<FunctionModel> mFunctionList = new ArrayList<>();

        public IndexAdapter(List<FunctionModel> list) {
            mFunctionList.addAll(list);
        }

        @Override
        public int getItemCount() {
            return mFunctionList.size();
        }

        @Override
        public IndexViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getActivity()).
                    inflate(R.layout.item_index_function, viewGroup, false);
            IndexViewHolder holder = new IndexViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(IndexViewHolder indexViewHolder, int i) {
            final FunctionModel f = mFunctionList.get(i);

            Drawable t = getResources().getDrawable(f.getResId());
            t.setBounds(0, 0, t.getMinimumWidth(), t.getMinimumHeight());

            indexViewHolder.textView.setCompoundDrawables(null, t, null, null);
            indexViewHolder.textView.setText(f.getText());
            indexViewHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    f.getCommand().handle();
                }
            });
        }

        class IndexViewHolder extends RecyclerView.ViewHolder {
            private TextView textView;
            private View layout;

            public IndexViewHolder(View itemView) {
                super(itemView);
                layout = itemView.findViewById(R.id.tv_function_item);
                textView = (TextView) itemView.findViewById(R.id.text);
            }
        }
    }
}
