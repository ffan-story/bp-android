package com.feifan.bp.refund;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.feifan.bp.CodeScannerActivity;
import com.feifan.bp.R;
import com.feifan.bp.UserProfile;
import com.feifan.bp.base.BaseFragment;

/**
 * Created by congjing 15-11-19.
 */
public class RefundFragment extends BaseFragment implements View.OnClickListener {
    private EditText mEdCode;
    CustomKeyboard mCustomKeyboard;

    public RefundFragment() {
    }

    public static RefundFragment newInstance() {
        Bundle args = new Bundle();
        RefundFragment refundFragment = new RefundFragment();
        refundFragment.setArguments(args);
        return refundFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View contentView = inflater.inflate(R.layout.fragmenr_refund, null);
        mEdCode = (EditText) contentView.findViewById(R.id.et_refund_code_edit);

        contentView.findViewById(R.id.img_refund_scancode).setOnClickListener(this);
        contentView.findViewById(R.id.btn_refund_next).setOnClickListener(this);

        KeyboardView mKeyboardView  = (KeyboardView)contentView.findViewById(R.id.keyboard_view);
        mCustomKeyboard = new CustomKeyboard(getActivity(),mKeyboardView, R.xml.custom_keyboard,mEdCode);
        mCustomKeyboard.registerEditText();
        mCustomKeyboard.showCustomKeyboard(mEdCode);

        mEdCode.addTextChangedListener(mRefundTextWatcher);
        return contentView;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.img_refund_scancode:
                if (!UserProfile.getInstance().isStoreUser()) {
                    Toast.makeText(getActivity(), R.string.error_message_permission_limited, Toast.LENGTH_SHORT).show();
                    return;
                }
                CodeScannerActivity.startActivity(getActivity());
                break;
            case R.id.btn_refund_next:
                break;

        }
    }

    TextWatcher mRefundTextWatcher = new TextWatcher() {
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
                        mEdCode.setText(sb.subSequence(0, sb.length() - 1));
                        index--;
                    }
                } else {
                    if (before == 1) {
                        index--;
                    }
                }
                mEdCode.setText(sb.toString());
                mEdCode.setSelection(index);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
