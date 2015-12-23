package com.feifan.bp.home;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.feifan.bp.PlatformTopbarActivity;
import com.feifan.bp.R;
import com.feifan.bp.base.BaseFragment;
import com.feifan.bp.flashevent.FlashEventGoodsSettingDetailFragment;

/**
 * 错误提示页
 */
public class ErrorFragment extends BaseFragment implements  View.OnClickListener{

    private static final String TAG = "ErrorFragment";

    private String mStrErrorMessage = "",mStrErrorMessageNextLine ="";

    /**
     * 图片id
     */
    private int mIntResourceId = 0;

    private String mStrBntText;
    private int mIntBntType;

    public static final String EXTRA_KEY_ERROR_MIPMAP_ID = "mipmap_id";
    public static final String EXTRA_KEY_ERROR_MESSAGE = "error_message";
    public static final String EXTRA_KEY_ERROR_MESSAGE_NEXT_LINE = "error_message_next_line";
    public static final String EXTRA_KEY_ERROR_BTN_TEXT = "bnt_text";
    public static final String EXTRA_KEY_ERROR_BTN_TEXT_TYPE= "bnt_text_type";

    /**
     * 区分bnt 事件
     */
    public static final int  EXTRA_KEY_ERROR_BTN_LISTENER_TO_GOODS_MANAGE= 1;


    public static ErrorFragment newInstance() {
        ErrorFragment fragment = new ErrorFragment();
        return fragment;
    }

    public ErrorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mIntResourceId = getArguments().getInt(EXTRA_KEY_ERROR_MIPMAP_ID);
        mStrErrorMessage = getArguments().getString(EXTRA_KEY_ERROR_MESSAGE);
        mStrErrorMessageNextLine= getArguments().getString(EXTRA_KEY_ERROR_MESSAGE_NEXT_LINE);
        mStrBntText = getArguments().getString(EXTRA_KEY_ERROR_BTN_TEXT);
        mIntBntType = getArguments().getInt(EXTRA_KEY_ERROR_BTN_TEXT_TYPE);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_error, container, false);
        TextView mTvErrorMessage = (TextView) v.findViewById(R.id.tv_error_text);
        TextView mTvErrorMessageNextLine = (TextView) v.findViewById(R.id.tv_error_text_next_line);

        Button mBtnError =  (Button)v.findViewById(R.id.btn_error);

        if (mIntResourceId != 0){
            Drawable drawable = getResources().getDrawable(mIntResourceId);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            mTvErrorMessage.setCompoundDrawables(null,drawable,null,null);
        }

        mTvErrorMessage.setText(mStrErrorMessage);
        if (!TextUtils.isEmpty(mStrErrorMessageNextLine)){
            mTvErrorMessageNextLine.setText(mStrErrorMessageNextLine);
            mTvErrorMessageNextLine.setVisibility(View.VISIBLE);
        }else{
            mTvErrorMessageNextLine.setVisibility(View.GONE);
        }

        if (!TextUtils.isEmpty(mStrBntText)){
            mBtnError.setVisibility(View.VISIBLE);
            mBtnError.setText(mStrBntText);
            mBtnError.setOnClickListener(this);
        }else{
            mBtnError.setVisibility(View.GONE);
        }

        return v;
    }




    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.btn_error){
            switch (mIntBntType){
                case EXTRA_KEY_ERROR_BTN_LISTENER_TO_GOODS_MANAGE://商品管理
                    getActivity().finish();
                    PlatformTopbarActivity.startActivityForResult(getActivity(), FlashEventGoodsSettingDetailFragment.class.getName(), "设置详情");
                    break;
                default:
                    break;

            }
        }
    }


//    /**
//     *
//     *
//     * bnt+图片+文字
//     * @param errorMessage  提示信息
//     * @param strBtnText
//     * @param intBntListenerType
//     * @return
//     */
//    public static ErrorFragment newInstance(int drawableId,String errorMessage,String strBtnText,int intBntListenerType) {
//        Bundle args = new Bundle();
//        args.putInt(EXTRA_KEY_ERROR_MIPMAP_ID, drawableId);
//        args.putString(EXTRA_KEY_ERROR_MESSAGE, errorMessage);
//        args.putString(EXTRA_KEY_ERROR_BTN_TEXT, strBtnText);
//        args.putInt(EXTRA_KEY_ERROR_BTN_TEXT_TYPE, intBntListenerType);
//        ErrorFragment fragment = new ErrorFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//
//    /**
//     * 哭脸+提示文案（1行，字体大小多行可加/n 换行符）
//     * @param erorMessage
//     * @return
//     */
//    public static ErrorFragment newInstance(String erorMessage) {
//        Bundle args = new Bundle();
//        args.putString(EXTRA_KEY_ERROR_MESSAGE, erorMessage);
//        ErrorFragment fragment = new ErrorFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    /**
//     * 图片+提示文案（两行，文字大小不一样 NextLine文字小）
//     * @param errorMessage
//     * @return
//     */
//    public static ErrorFragment newInstance(int drawableId,String errorMessage,String errorMessageNextLine) {
//        Bundle args = new Bundle();
//        args.putInt(EXTRA_KEY_ERROR_MIPMAP_ID, drawableId);
//        args.putString(EXTRA_KEY_ERROR_MESSAGE, errorMessage);
//        args.putString(EXTRA_KEY_ERROR_MESSAGE_NEXT_LINE, errorMessageNextLine);
//        ErrorFragment fragment = new ErrorFragment();
//        fragment.setArguments(args);
//        return fragment;
//    }
}
