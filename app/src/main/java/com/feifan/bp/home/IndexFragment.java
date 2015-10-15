package com.feifan.bp.home;

import android.app.Activity;
import android.content.Context;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.feifan.bp.BrowserActivity;
import com.feifan.bp.net.UrlFactory;
import com.feifan.bp.util.LogUtil;
import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.R;
import com.feifan.bp.Utils;
import com.feifan.bp.UserProfile;
import com.feifan.bp.base.BaseFragment;
import com.feifan.bp.home.command.Command;
import com.feifan.bp.net.Authority;
import com.feifan.bp.scanner.CodeScannerActivity;

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
    private ImageButton mCodeSearchBtn;

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
        LogUtil.i(TAG, "onCreateView()");
        View v = inflater.inflate(R.layout.fragment_index, container, false);
        v.findViewById(R.id.index_scan).setOnClickListener(this);
        v.findViewById(R.id.index_history).setOnClickListener(this);
//        mCodeEdt = (IconClickableEditText) v.findViewById(R.id.index_search_input);
//        mCodeEdt.setOnIconClickListener(this);
        mCodeEditText = (EditText)v.findViewById(R.id.et_code_edit);
        mCodeSearchBtn = (ImageButton)v.findViewById(R.id.btn_code_search);
        mCodeSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mCodeEditText.getText())) {
                    return;
                }

                if (!Utils.isNetworkAvailable(getActivity())) {
                    Utils.showShortToast(getActivity(), R.string.error_message_text_offline, Gravity.CENTER);
                    return;
                }

                String code = mCodeEditText.getText().toString();

                try {
                    Utils.checkDigitAndLetter(getActivity(), code);
                } catch (Throwable throwable) {
                    Utils.showShortToast(getActivity(), throwable.getMessage());
                    return;
                }
                mCodeEditText.setText("");
                String urlStr = UrlFactory.searchCodeForHtml(getActivity(), code);
                BrowserActivity.startActivity(getActivity(), urlStr);
            }
        });

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
                args.putString(OnFragmentInteractionListener.INTERATION_KEY_TO, "");
                if (Utils.isNetworkAvailable(getActivity())) {
                    String relativeUrl = UserProfile.instance(getActivity()).getPermissionUrl(Authority.HISTORY_ID);
                    String url = UrlFactory.checkHistoryForHtml(getActivity(), relativeUrl);
                    BrowserActivity.startActivity(getActivity(), url);
                } else {
                    Utils.showShortToast(getActivity(), R.string.error_message_text_offline, Gravity.CENTER);
                }
                break;
//                BrowserActivity.startActivity(getActivity(), "http://10.1.171.127:1111/H5App/index.html#/commodity/publish_page");
//                return;
            default:
                return;
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
        public void onBindViewHolder(final IndexViewHolder indexViewHolder, int i) {
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
