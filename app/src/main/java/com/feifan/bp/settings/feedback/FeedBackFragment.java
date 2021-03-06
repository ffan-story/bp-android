package com.feifan.bp.settings.feedback;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.ClipboardManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.feifan.bp.Constants;
import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.R;
import com.feifan.bp.UserProfile;
import com.feifan.bp.Utils;
import com.feifan.bp.base.ui.BaseFragment;
import com.feifan.bp.base.network.BaseModel;

/**
 * Created by tianjun on 2015-10-27.
 */
public class FeedBackFragment extends BaseFragment implements View.OnClickListener {
    private OnFragmentInteractionListener mListener;
    private boolean mIsConfirmEnable = true;
    private EditText mEtFeedBack;
    private TextView mFeedBackPrompt;

    public FeedBackFragment() {
        Bundle args = new Bundle();
        args.putString(Constants.EXTRA_KEY_TITLE, Utils.getString(R.string.feed_back));
        setArguments(args);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_feedback, container, false);
        mFeedBackPrompt = (TextView) v.findViewById(R.id.feedback_prompt);
        mEtFeedBack = (EditText) v.findViewById(R.id.feedback_content);
        mEtFeedBack.addTextChangedListener(new TextWatcher() {
            private CharSequence temp;//监听前的文本
            private int editStart;//光标开始位置
            private int editEnd;//光标结束位置
            private final int charMaxNum = 300;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                temp = s;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mFeedBackPrompt.setText(getString(R.string.feed_back_input_strings, charMaxNum - s.length()));
            }

            @Override
            public void afterTextChanged(Editable s) {
                /** 得到光标开始和结束位置 ,超过最大数后记录刚超出的数字索引进行控制 */
                editStart = mEtFeedBack.getSelectionStart();
                editEnd = mEtFeedBack.getSelectionEnd();
                if (temp.length() > charMaxNum) {
                    Utils.showShortToastSafely(R.string.error_message_feed_back_length);
                    if(getClipboard().trim().length()> 0){
                        s.delete(editStart - getClipboard().trim().length(), editEnd);
                    }else {
                        s.delete(editStart - 1, editEnd);
                    }
                    int tempSelection = editStart;
                    mEtFeedBack.setText(s);
                    mEtFeedBack.setSelection(tempSelection);
                }
            }
        });
        mIsConfirmEnable = true;
        v.findViewById(R.id.feedback_submit).setOnClickListener(this);
        return v;
    }

    public String getClipboard() {
        ClipboardManager clipboard = (ClipboardManager)getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        return clipboard.getText().toString();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.feedback_submit:
                if (!mIsConfirmEnable) {
                    return;
                }
                if (TextUtils.isEmpty(mEtFeedBack.getText().toString().trim())) {
                    Utils.showShortToastSafely(R.string.error_message_feed_back_length_empty);
                    return;
                }
                if (mEtFeedBack.getText().toString().length() > Constants.FEED_BACK_MAX_lENGTH) {
                    Utils.showShortToastSafely(R.string.error_message_feed_back_length);
                    return;
                }
                if(!Utils.isNetworkAvailable()){
                    Utils.showShortToastSafely(R.string.error_message_text_offline);
                    return;
                }
                UserProfile manager = UserProfile.getInstance();
                int uid = manager.getUid();
                String description = mEtFeedBack.getText().toString();
                mIsConfirmEnable = false;
                showProgressBar(true);
                FeedBackCtrl.submitFeedBack(String.valueOf(uid), description,
                        new Response.Listener<BaseModel>() {
                            public void onResponse(BaseModel feedBackModel) {
                                if (feedBackModel.status == Constants.RESPONSE_CODE_SUCCESS) {
                                    Utils.showLongToastSafely(R.string.feed_back_success_prompt);
                                    if (mListener != null) {
                                        Bundle b = new Bundle();
                                        b.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM,
                                                FeedBackFragment.class.getName());
                                        b.putInt(OnFragmentInteractionListener.INTERATION_KEY_TYPE,
                                                OnFragmentInteractionListener.TYPE_NAVI_CLICK);
                                        mListener.onFragmentInteraction(b);
                                    }
                                } else {
                                    Utils.showShortToastSafely(feedBackModel.msg);
                                }
                                hideProgressBar();
                            }
                        });
                break;
        }
    }

    public static FeedBackFragment newInstance() {
        FeedBackFragment fragment = new FeedBackFragment();
        return fragment;
    }

    @Override
    protected void setupToolbar(Toolbar toolbar) {
        super.setupToolbar(toolbar);
        toolbar.setTitle(R.string.feed_back);
        toolbar.setNavigationIcon(R.mipmap.ic_left_arrow);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null) {
                    Bundle b = new Bundle();
                    b.putString(OnFragmentInteractionListener.INTERATION_KEY_FROM,  FeedBackFragment.class.getName());
                    b.putInt(OnFragmentInteractionListener.INTERATION_KEY_TYPE,  OnFragmentInteractionListener.TYPE_NAVI_CLICK);
                    mListener.onFragmentInteraction(b);
                }
            }
        });
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
}
