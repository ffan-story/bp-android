package com.feifan.bp.home;

import android.app.Activity;
import android.content.Context;
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
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.Response;
import com.feifan.bp.PlatformState;
import com.feifan.bp.browser.BrowserActivityNew;
import com.feifan.bp.browser.TabLayoutActivity;
import com.feifan.bp.login.Authority;
import com.feifan.bp.logininfo.LoginInfoFragment;
import com.feifan.bp.login.UserModel;
import com.feifan.bp.net.UrlFactory;
import com.feifan.bp.network.GetRequest;
import com.feifan.bp.network.JsonRequest;
import com.feifan.bp.util.LogUtil;
import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.UserProfile;
import com.feifan.bp.base.BaseFragment;
import com.feifan.bp.home.command.Command;
import com.feifan.bp.CodeScannerActivity;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
        getShopData();
        mCodeEditText = (EditText)v.findViewById(R.id.et_code_edit);
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
                }  else if (count == 0) { // 删除字符
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

        List<FunctionModel> dataList = new ArrayList<>();
        List<String> list = UserProfile.instance(getActivity()).getPermissionList();
        LogUtil.i(TAG, "auth list=" + list);
        for (String id : Authority.AUTH_LIST) {
            LogUtil.i(TAG, "id=" + id);
            if (list.contains(id)) {
                LogUtil.i(TAG, "auth id=" + id);
                try {
                    Authority.Auth a = Authority.AUTH_MAP.get(id);
                    String url = UserProfile.instance(getActivity()).getPermissionUrl(id);
                    LogUtil.i(TAG, "auth url=" + url);
                    Constructor constructor = a.clazz.getConstructor(Context.class, String.class);
                    Command c = (Command) constructor.newInstance(getActivity(), url);
                    dataList.add(new FunctionModel(c, getString(a.titleResId), id, a.iconResId));
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (java.lang.InstantiationException e) {
                    e.printStackTrace();
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }

            }
        }

        mRecyclerView = (RecyclerView) v.findViewById(R.id.rv_function_container);
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(
                3, StaggeredGridLayoutManager.VERTICAL));
//        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(getActivity()));
        mAdapter = new IndexAdapter(dataList);
        mRecyclerView.setAdapter(mAdapter);

        return v;
    }

    /**
     * 获取门店数据
     */
    private void getShopData(){

//        String merchantId = UserProfile.instance(getActivity()).getAuthRangeId();
//        由于种种特殊原因,目前商户ID用测试数据
        String merchantId = "2052506";
        String url = UrlFactory.getShopListUrl();
        LogUtil.i(TAG, "Url = " + url);
        LogUtil.i(TAG, "merchantId = " + merchantId);

        JsonRequest<StoreModel> request = new GetRequest.Builder<StoreModel>(url+"/v1/cdaservice/stores/"+merchantId)
                .build()
                .targetClass(StoreModel.class)
                .listener(new Response.Listener<StoreModel>() {
                    @Override
                    public void onResponse(StoreModel storeModel) {
                        UserProfile userProfile = UserProfile.instance(getActivity());
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

    private String arryUrl[] ;


    @Override
    public void onClick(View v) {
        Bundle args = new Bundle();
        args.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM, IndexFragment.class.getName());
        switch (v.getId()) {
            case R.id.index_scan:
                args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, CodeScannerActivity.class.getName());
                break;
            case R.id.index_history:
                args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, "");
                if (Utils.isNetworkAvailable(getActivity())) {
                    String relativeUrl = UserProfile.instance(getActivity()).getPermissionUrl(Authority.HISTORY_ID);
                    String url = UrlFactory.checkHistoryForHtml(getActivity(), relativeUrl);
                   // BrowserActivity.startActivity(getActivity(), url);
                    arryUrl = new String[]{url,url};
                    Intent intent = new Intent(getActivity(), TabLayoutActivity.class);
                    intent.putExtra(TabLayoutActivity.EXTRA_KEY_URLS,arryUrl);
                    intent.putExtra(TabLayoutActivity.EXTRA_KEY_TITLES, getActivity().getResources().getStringArray(R.array.tab_title_veri_history));
                    getActivity().startActivity(intent);

                } else {
                    Utils.showShortToast(getActivity(), R.string.error_message_text_offline, Gravity.CENTER);
                }
                break;
            case R.id.login_info_icon:
                args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, LoginInfoFragment.class.getName());
                break;
            case R.id.index_search_btn:
                if (TextUtils.isEmpty(mCodeEditText.getText())) {
                    return;
                }

                if (!Utils.isNetworkAvailable(getActivity())) {
                    Utils.showShortToast(getActivity(), R.string.error_message_text_offline, Gravity.CENTER);
                    return;
                }

                String code = mCodeEditText.getText().toString().replaceAll(" ","");
                LogUtil.i(TAG, "Input code is " + code);
                try {
                    Utils.checkDigitAndLetter(getActivity(), code);
                } catch (Throwable throwable) {
                    Utils.showShortToast(getActivity(), throwable.getMessage());
                    return;
                }
                mCodeEditText.setText("");
                args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, BrowserActivityNew.class.getName());
                String urlStr = UrlFactory.searchCodeForHtml(getActivity(), code);
                args.putString(BrowserActivityNew.EXTRA_KEY_URL, urlStr);
                break;
            default:
                return;
        }
        mListener.onFragmentInteraction(args);

    }

    class IndexAdapter extends RecyclerView.Adapter<IndexAdapter.IndexViewHolder> {

        private List<FunctionModel> mFunctionList = new ArrayList<>();
        private final int mIconSize;
        {
            mIconSize = getResources().getDrawable(R.mipmap.index_ic_order).getMinimumHeight();
        }

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
        public void onBindViewHolder(final IndexViewHolder indexViewHolder, int i) {
            final FunctionModel f = mFunctionList.get(i);

            Drawable t = getResources().getDrawable(f.getResId());
            t.setBounds(0, 0, mIconSize, mIconSize);

            indexViewHolder.textView.setCompoundDrawables(null, t, null, null);
            indexViewHolder.textView.setText(f.getText());
            indexViewHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    f.getCommand().handle();

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
