package com.feifan.bp.widget;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feifan.bp.R;


public class DialogPhoneLayout extends RelativeLayout{
	Context context;
	private int screenHeight,screenWidth;
	private TextView tvDialogCancel,tvDialogCamera,tvDialogPhone;
	public DialogPhoneLayout(Context context) {
		super(context);
		this.context= context;
		init();
	}
	private void init() {
		DisplayMetrics dm = getResources().getDisplayMetrics();  
		screenHeight = dm.heightPixels;
		screenWidth = dm.widthPixels;
		setLayoutParams(new LayoutParams(screenWidth, screenHeight));
		LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflater.inflate(R.layout.dialog_phone_layout, null);
		addView(v, new LayoutParams(screenWidth, screenHeight));
		tvDialogCancel = (TextView) v.findViewById(R.id.dialog_cancel);
		tvDialogCamera= (TextView) v.findViewById(R.id.dialog_camera);
		tvDialogPhone= (TextView) v.findViewById(R.id.dialog_phone);
	}
	public void setBtnCancelClicklListener(OnClickListener btnCancelListener) {
		tvDialogCancel.setOnClickListener(btnCancelListener);
	}
	public void setLayoutPhoneClicklListener(OnClickListener phoneListener) {
		tvDialogPhone.setOnClickListener(phoneListener);
	}
	public void setLayoutCameraClicklListener(OnClickListener cameraListener) {
        tvDialogCamera.setOnClickListener(cameraListener);
	}
}		
		
		
	
