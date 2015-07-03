package com.feifan.bp.password;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.feifan.bp.OnFragmentInteractionListener;
import com.feifan.bp.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ForgetPasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ForgetPasswordFragment extends Fragment implements View.OnClickListener {
 
   
    private OnFragmentInteractionListener mListener;
    private EditText mPhoneNum;
    private EditText mSmsCode;
    private TextView mTvCode;
    private Button mBtnConfirm;
    
    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.     
     * @return A new instance of fragment ForgetPasswordFragment.
     */ 
    public static ForgetPasswordFragment newInstance() {
        ForgetPasswordFragment fragment = new ForgetPasswordFragment(); 
        return fragment;
    }

    public ForgetPasswordFragment() {
        
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); 
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {  
         View rootView=inflater.inflate(R.layout.fragment_forget_password, container, false);
         mPhoneNum=(EditText)rootView.findViewById(R.id.et_phone_num);
         mSmsCode=(EditText)rootView.findViewById(R.id.sms_code);
         mTvCode=(TextView)rootView.findViewById(R.id.get_sms_code);
         mBtnConfirm=(Button)rootView.findViewById(R.id.btn_confirm); 
         return rootView;
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
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_confirm:
                String phone=mPhoneNum.getText().toString();
                String smsCode=mSmsCode.getText().toString();
                
                
                break;
            case R.id.get_sms_code:
                
                break;
        }
    }
}
