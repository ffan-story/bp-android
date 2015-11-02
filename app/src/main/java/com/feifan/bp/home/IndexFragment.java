package com.feifan.bp.home;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Response;
import com.feifan.bp.CodeScannerActivity;
import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.PlatformState;
import com.feifan.bp.R;
import com.feifan.bp.UserProfile;
import com.feifan.bp.Utils;
import com.feifan.bp.base.BaseFragment;
import com.feifan.bp.browser.BrowserActivityNew;
import com.feifan.bp.browser.BrowserTabActivity;
import com.feifan.bp.envir.EnvironmentManager;
import com.feifan.bp.login.AuthListModel.AuthItem;
import com.feifan.bp.logininfo.LoginInfoFragment;
import com.feifan.bp.network.UrlFactory;
import com.feifan.bp.network.GetRequest;
import com.feifan.bp.network.JsonRequest;
import com.feifan.bp.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页Fragment
 * Created by xuchunlei on 15/7/2.
 */
public class IndexFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "IndexFragment";

    private OnFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    // views
//    private IconClickableEditText mCodeEdt;
    private EditText mCodeEditText;

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
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_index, container, false);
        v.findViewById(R.id.index_scan).setOnClickListener(this);
        v.findViewById(R.id.index_history).setOnClickListener(this);
        v.findViewById(R.id.login_info_icon).setOnClickListener(this);
//        mCodeEdt = (IconClickableEditText) v.findViewById(R.id.index_search_input);
//        mCodeEdt.setOnIconClickListener(this);
        if (UserProfile.getInstance().getAuthRangeType().equals("merchant")) {
            getShopData();
        }
        mCodeEditText = (EditText) v.findViewById(R.id.et_code_edit);
        mCodeEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 1) { // 输入字符
                    if ((s.length() + 1) % 5 == 0) {
                        mCodeEditText.setText(s + " ");
                        mCodeEditText.setSelection(s.length() + 1);
                    }
                } else if (count == 0) { // 删除字符
                    // 自动删除空格
                    if (s.length() > 0 && s.length() % 5 == 0) {
                        mCodeEditText.setText(s.subSequence(0, s.length() - 1));
                        mCodeEditText.setSelection(s.length() - 1);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        v.findViewById(R.id.index_search_btn).setOnClickListener(this);

        List<AuthItem> list = UserProfile.getInstance().getAuthList();
        LogUtil.i(TAG, "auth list=" + list);

        mRecyclerView = (RecyclerView) v.findViewById(R.id.rv_function_container);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(
                3, StaggeredGridLayoutManager.VERTICAL));
//        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity()));
        mAdapter = new IndexAdapter(list);
        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

    /**
     * 获取门店数据
     */
    private void getShopData() {

//        由于种种特殊原因,目前商户ID用测试数据
//        String merchantId = "2052506";
        String merchantId = UserProfile.getInstance().getAuthRangeId();
        String url = UrlFactory.getShopListUrl();
        LogUtil.i(TAG, "Url = " + url);
        LogUtil.i(TAG, "merchantId = " + merchantId);

        JsonRequest<StoreModel> request = new GetRequest.Builder<StoreModel>(url + merchantId)
                .build()
                .targetClass(StoreModel.class)
                .listener(new Response.Listener<StoreModel>() {
                    @Override
                    public void onResponse(StoreModel storeModel) {
                        UserProfile userProfile = UserProfile.getInstance();
                        userProfile.setStoreList(storeModel.getStoreList());
                    }
                });
        PlatformState.getInstance().getRequestQueue().add(request);
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
//        if(v.getId() == R.id.index_history){//验证历史
//            if (Utils.isNetworkAvailable(getActivity())) {
//                String relativeUrl = UserProfile.getInstance().getHistoryUrl();
//                String url = UrlFactory.checkHistoryForHtml(relativeUrl)+"&status=";
//                BrowserTabActivity.startActivity(getActivity(), url, getActivity().getResources().getStringArray(R.array.data_type),
//                        getActivity().getResources().getStringArray(R.array.tab_title_veri_history_title));
//            }else{
//                Utils.showShortToast(getActivity(), R.string.error_message_text_offline, Gravity.CENTER);
//            }
//            return;
//        }

        Bundle args = new Bundle();
        args.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM, IndexFragment.class.getName());
        switch (v.getId()) {
            case R.id.index_scan:
                args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, CodeScannerActivity.class.getName());
                break;

            case R.id.login_info_icon:
                args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, LoginInfoFragment.class.getName());
                break;

            case R.id.index_history:
                if (Utils.isNetworkAvailable(getActivity())) {
                    String relativeUrl = UserProfile.getInstance().getHistoryUrl();
                    String url = UrlFactory.checkHistoryForHtml(relativeUrl)+"&status=";
//                    args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, BrowserTabActivity.class.getName());
//                    args.putString(BrowserTabActivity.EXTRA_KEY_URL, url);
//                    args.putStringArray(BrowserTabActivity.EXTRA_KEY_STATUS, getActivity().getResources().getStringArray(R.array.data_type));
//                    args.putStringArray(BrowserTabActivity.EXTRA_KEY_TITLES,  getActivity().getResources().getStringArray(R.array.tab_title_veri_history_title));

                    args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, BrowserActivityNew.class.getName());
                    args.putString(BrowserActivityNew.EXTRA_KEY_URL, url);
                }else{
                    Utils.showShortToast(getActivity(), R.string.error_message_text_offline, Gravity.CENTER);
                }
                break;

            case R.id.index_history:
                if (Utils.isNetworkAvailable(getActivity())) {
                    String relativeUrl = UserProfile.getInstance().getHistoryUrl();
                    String url = UrlFactory.checkHistoryForHtml(relativeUrl)+"&status=";
//                    args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, BrowserTabActivity.class.getName());
//                    args.putString(BrowserTabActivity.EXTRA_KEY_URL, url);
//                    args.putStringArray(BrowserTabActivity.EXTRA_KEY_STATUS, getActivity().getResources().getStringArray(R.array.data_type));
//                    args.putStringArray(BrowserTabActivity.EXTRA_KEY_TITLES,  getActivity().getResources().getStringArray(R.array.tab_title_veri_history_title));

                    args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, BrowserActivityNew.class.getName());
                    args.putString(BrowserActivityNew.EXTRA_KEY_URL, url);
                }else{
                    Utils.showShortToast(getActivity(), R.string.error_message_text_offline, Gravity.CENTER);
                }
                break;

            case R.id.index_search_btn:
                if (TextUtils.isEmpty(mCodeEditText.getText())) {
                    return;
                }

                if (!Utils.isNetworkAvailable(getActivity())) {
                    Utils.showShortToast(getActivity(), R.string.error_message_text_offline, Gravity.CENTER);
                    return;
                }

                String code = mCodeEditText.getText().toString().replaceAll(" ", "");
                LogUtil.i(TAG, "Input code is " + code);
                try {
                    Utils.checkDigitAndLetter(getActivity(), code);
                } catch (Throwable throwable) {
                    Utils.showShortToast(getActivity(), throwable.getMessage());
                    return;
                }
                mCodeEditText.setText("");
                args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, BrowserActivityNew.class.getName());
                String urlStr = UrlFactory.searchCodeForHtml(code);
                args.putString(BrowserActivityNew.EXTRA_KEY_URL, urlStr);
                break;

            default:
                return;
        }
        mListener.onFragmentInteraction(args);

    }

    class IndexAdapter extends RecyclerView.Adapter<IndexAdapter.IndexViewHolder> {

        private List<AuthItem> mList = new ArrayList<>();
        private final int mIconSize;

        {
            mIconSize = getResources().getDrawable(R.mipmap.index_ic_order).getMinimumHeight();
        }

        public IndexAdapter(List<AuthItem> list) {
            mList.addAll(list);
        }


        @Override
        public int getItemCount() {
            return mList.size();
        }

        @Override
        public IndexViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(getActivity()).
                    inflate(R.layout.item_index_function, viewGroup, false);
            IndexViewHolder holder = new IndexViewHolder(view);
            return holder;
        }

        @Override
        public void onBindViewHolder(final IndexViewHolder indexViewHolder, int i) {
            final AuthItem item = mList.get(i);

            Drawable t = getResources().getDrawable(item.icon);
            t.setBounds(0, 0, mIconSize, mIconSize);

            indexViewHolder.textView.setCompoundDrawables(null, t, null, null);
            indexViewHolder.textView.setText(item.name);
            indexViewHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String url = UrlFactory.urlForHtml(item.url);
                    if(isAdded()) {
                        if (Utils.isNetworkAvailable(getContext())) {
                            if (EnvironmentManager.getAuthFactory().getAuthTabTitleRes(item.id) != -1 && EnvironmentManager.getAuthFactory().getAuthTabStatusRes(item.id) != -1){
                                BrowserTabActivity.startActivity(getContext(),url+"&status=",
                                        getContext().getResources().getStringArray(EnvironmentManager.getAuthFactory().getAuthTabStatusRes(item.id)),
                                        getContext().getResources().getStringArray(EnvironmentManager.getAuthFactory().getAuthTabTitleRes(item.id)));
                        }else{
                            BrowserActivityNew.startActivity(getContext(),url);
                        }
                    } else {
                        Utils.showShortToast(getContext(), R.string.error_message_text_offline, Gravity.CENTER);
                    }
                }
                }
            });
            indexViewHolder.redDotView.setVisibility(View.GONE);
//            if (Authority.REFUND_ID.equals(f.getId())) {
//                RefundCountRequest.Params params = BaseRequest.newParams(RefundCountRequest.Params.class);
//                params.setStoreId(AccountManager.instance(getActivity()).getAuthRangeId());
//                params.setRefundStatus("MER_REVIEW");
//                HttpEngine.Builder builder = HttpEngine.Builder.newInstance(getActivity());
//                builder.setRequest(new RefundCountRequest(params,
//                        new BaseRequestProcessListener<RefundCountModel>(getActivity(), false) {
//                            @Override
//                            public void onResponse(RefundCountModel refundCountModel) {
//                                if (refundCountModel.getCount() > 0) {
//                                    indexViewHolder.redDotView.setVisibility(View.VISIBLE);
//                                }
//                            }
//                        })).build().start();
//
//            }
        }

        class IndexViewHolder extends RecyclerView.ViewHolder {
            private TextView textView;
            private View layout;
            private View redDotView;

            public IndexViewHolder(View itemView) {
                super(itemView);
                layout = itemView.findViewById(R.id.tv_function_item);
                textView = (TextView) itemView.findViewById(R.id.text);
                redDotView = itemView.findViewById(R.id.iv_red_dot);
            }
        }
    }
}
