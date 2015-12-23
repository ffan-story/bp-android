package com.feifan.bp.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.feifan.bp.R;
import com.feifan.bp.base.BaseFragment;

/**
 * 错误提示页
 */
public class ErrorFragment extends BaseFragment {

    private static final String TAG = "ErrorFragment";

    private String mStrErrorMessage = "";
    public static final String EXTRA_KEY_ERROR_MESSAGE = "error_message";
    public static ErrorFragment newInstance() {
        ErrorFragment fragment = new ErrorFragment();
        return fragment;
    }

    public ErrorFragment() {
        // Required empty public constructor
    }

    public static ErrorFragment newInstance(String erorMessage) {
        Bundle args = new Bundle();
        args.putString(EXTRA_KEY_ERROR_MESSAGE, erorMessage);
        ErrorFragment fragment = new ErrorFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStrErrorMessage = getArguments().getString(EXTRA_KEY_ERROR_MESSAGE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_error, container, false);
        TextView mTvErrorMessage = (TextView) v.findViewById(R.id.tv_error_text);
        mTvErrorMessage.setText(mStrErrorMessage);
        return v;
    }


}
