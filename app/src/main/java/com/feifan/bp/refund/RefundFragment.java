package com.feifan.bp.refund;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.feifan.bp.CodeScannerActivity;
import com.feifan.bp.R;
import com.feifan.bp.UserProfile;
import com.feifan.bp.Utils;
import com.feifan.bp.base.BaseFragment;
import com.feifan.bp.browser.BrowserActivity;
import com.feifan.bp.network.UrlFactory;

/**
 * Created by congjing
 */
public class RefundFragment extends BaseFragment implements View.OnClickListener {
    private EditText mEdRefundCode;
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
        mEdRefundCode = (EditText) contentView.findViewById(R.id.et_refund_code_edit);

        contentView.findViewById(R.id.img_refund_scancode).setOnClickListener(this);
        contentView.findViewById(R.id.btn_refund_next).setOnClickListener(this);

        KeyboardView mKeyboardView  = (KeyboardView)contentView.findViewById(R.id.keyboard_view);
        mCustomKeyboard = new CustomKeyboard(getActivity(),mKeyboardView, R.xml.custom_keyboard,mEdRefundCode);
        mCustomKeyboard.registerEditText();
        mCustomKeyboard.showCustomKeyboard(mEdRefundCode);

        mEdRefundCode.addTextChangedListener(mRefundTextWatcher);
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
                String urlStr = UrlFactory.refundForHtml();
                CodeScannerActivity.startActivityForResult(getActivity(),urlStr);
                break;
            case R.id.btn_refund_next:

                if (!UserProfile.getInstance().isStoreUser()) {
                    Toast.makeText(getActivity(), R.string.error_message_permission_limited, Toast.LENGTH_SHORT).show();
                    mEdRefundCode.setText("");
                    return;
                }
                if (TextUtils.isEmpty(mEdRefundCode.getText().toString().trim())) {
                    Toast.makeText(getActivity(), R.string.order_number_can_not_be_empty, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!Utils.isNetworkAvailable(getActivity())) {
                    Utils.showShortToast(getActivity(), R.string.error_message_text_offline, Gravity.CENTER);
                    return;
                }

                String code = mEdRefundCode.getText().toString().replaceAll(" ", "");
                try {
                    Utils.checkDigitAndLetter(getActivity(), code);
                } catch (Throwable throwable) {
                    Utils.showShortToast(getActivity(), throwable.getMessage());
                    return;
                }

                mEdRefundCode.setText("");
                String url = String.format( UrlFactory.refundForHtml(), code);
                BrowserActivity.startForResultActivity(getActivity(), url);
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
                        mEdRefundCode.setText(sb.subSequence(0, sb.length() - 1));
                        index--;
                    }
                } else {
                    if (before == 1) {
                        index--;
                    }
                }
                mEdRefundCode.setText(sb.toString());
                mEdRefundCode.setSelection(index);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
}
