package com.feifan.bp.home;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
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
import android.widget.Toast;

import com.android.volley.Response;
import com.feifan.bp.CodeScannerActivity;
import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.PlatformState;
import com.feifan.bp.PlatformTabActivity;
import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.TransactionFlow.TransFlowTabActivity;
import com.feifan.bp.UserProfile;
import com.feifan.bp.Utils;
import com.feifan.bp.base.BaseFragment;
import com.feifan.bp.browser.BrowserActivity;
import com.feifan.bp.browser.BrowserFragment;
import com.feifan.bp.browser.BrowserTabActivity;
import com.feifan.bp.envir.EnvironmentManager;
import com.feifan.bp.home.check.CheckManageFragment;
import com.feifan.bp.login.AuthListModel.AuthItem;
import com.feifan.bp.logininfo.LoginInfoFragment;
import com.feifan.bp.network.GetRequest;
import com.feifan.bp.network.JsonRequest;
import com.feifan.bp.network.UrlFactory;
import com.feifan.bp.util.LogUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页Fragment
 * Created by xuchunlei on 15/7/2.
 */
public class IndexFragment extends BaseFragment implements View.OnClickListener {

    private static final String TAG = "IndexFragment";
    private static final String STATISTICAL_PATH = "http://sop.sit.ffan.com/H5App/index.html#/statistical";

    private OnFragmentInteractionListener mListener;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;

    // views
//    private IconClickableEditText mCodeEdt;
    private EditText mCodeEditText;
    private boolean mDeleteFlag = true;

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
                if (s == null || s.length() == 0) return;
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < s.length(); i++) {
                    if (i != 4 && i!= 9 && i != 14  && s.charAt(i) == ' ') {
                        continue;
                    } else {
                        sb.append(s.charAt(i));
                        if ((sb.length() == 5 || sb.length() == 10|| sb.length() == 15) && sb.charAt(sb.length() - 1) != ' ') {
                            sb.insert(sb.length() - 1, ' ');
                        }
                    }
                }

                if (!sb.toString().equals(s.toString())) {
                    int index = start + 1;
                    if (sb.charAt(start) == ' ') {
                        if (before == 0) {
                            index++;
                        } else {
                            mCodeEditText.setText(sb.subSequence(0, sb.length() - 1));
                            index--;
                        }
                    } else {
                        if (before == 1) {
                            index--;
                        }
                    }
                    mCodeEditText.setText(sb.toString());
                    mCodeEditText.setSelection(index);
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
        Bundle args = new Bundle();
        args.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM, IndexFragment.class.getName());
        switch (v.getId()) {
            case R.id.index_scan:
//                if (!UserProfile.getInstance().isStoreUser()) {
//                    Toast.makeText(getActivity(), R.string.error_message_permission_limited, Toast.LENGTH_SHORT).show();
//                    return;
//                }
//                args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, CodeScannerActivity.class.getName());
//                break;

                Bundle fragmentArgs = new PlatformTabActivity.ArgsBuilder()
                              .addFragment(SettingsFragment.class.getName(), "设置")
                              .addArgument(SettingsFragment.class.getName(), "count", 1)
                              .addFragment(MessageFragment.class.getName(), "消息 ")
                              .addArgument(MessageFragment.class.getName(), "count", 2)
                              .build();

                Intent intent = PlatformTabActivity.buildIntent(getContext(), "测试中心", fragmentArgs);
                startActivity(intent);
                return;
            case R.id.login_info_icon:
                args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, LoginInfoFragment.class.getName());
                break;

            case R.id.index_history:
                if (Utils.isNetworkAvailable(getActivity())) {
                    String url = UrlFactory.checkHistoryForHtml(UserProfile.getInstance().getHistoryUrl()) + "&status=";
                    args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, BrowserTabActivity.class.getName());
                    args.putString(BrowserTabActivity.EXTRA_KEY_URL, url);
                    args.putStringArray(BrowserTabActivity.EXTRA_KEY_STATUS, getActivity().getResources().getStringArray(R.array.data_type));
                    args.putStringArray(BrowserTabActivity.EXTRA_KEY_TITLES, getActivity().getResources().getStringArray(R.array.tab_title_veri_history_title));
                } else {
                    Utils.showShortToast(getActivity(), R.string.error_message_text_offline, Gravity.CENTER);
                    return;
                }
                break;

            case R.id.index_search_btn:
                if (!UserProfile.getInstance().isStoreUser()) {
                    Toast.makeText(getActivity(), R.string.error_message_permission_limited, Toast.LENGTH_SHORT).show();
                    mCodeEditText.setText("");
                    return;
                }
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
                args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, BrowserActivity.class.getName());
                String urlStr = UrlFactory.searchCodeForHtml(code);
                args.putString(BrowserActivity.EXTRA_KEY_URL, urlStr);
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
            Integer iconRes = EnvironmentManager.getAuthFactory().getAuthFilter().get(item.id);
            if(iconRes == null) {
                return;
            }

            Drawable t = getResources().getDrawable(iconRes);
            t.setBounds(0, 0, mIconSize, mIconSize);
            indexViewHolder.textView.setCompoundDrawables(null, t, null, null);

            indexViewHolder.textView.setText(item.name);
            indexViewHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isAdded()) {
                        if (item.url != null && item.url.length() != 0) {
                            String url = UrlFactory.urlForHtml(item.url);
                            if (Utils.isNetworkAvailable(getContext())) {
                                if (EnvironmentManager.getAuthFactory().getAuthTabTitleRes(item.id) != -1 && EnvironmentManager.getAuthFactory().getAuthTabStatusRes(item.id) != -1) {
                                    String titleName = "";
                                    if (!url.contains("/staff?") && !item.name.equals("员工管理")){
                                        titleName = item.name;
                                    }
                                    BrowserTabActivity.startActivity(getContext(), url + "&status=",
                                            getContext().getResources().getStringArray(EnvironmentManager.getAuthFactory().getAuthTabStatusRes(item.id)),
                                            getContext().getResources().getStringArray(EnvironmentManager.getAuthFactory().getAuthTabTitleRes(item.id)),
                                            titleName);
                                } else {
                                    BrowserActivity.startActivity(getContext(), url);
                                }
                            } else {
                                Utils.showShortToast(getContext(), R.string.error_message_text_offline, Gravity.CENTER);
                            }
                        } else {
                            // TODO notify host activity depends on item.id
                            Bundle args = new Bundle();
                            args.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM, IndexFragment.class.getName());
                            args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, CheckManageFragment.class.getName());
                            mListener.onFragmentInteraction(args);
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
