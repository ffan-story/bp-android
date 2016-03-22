package bp.feifan.com.codescanner;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.feifan.scan.base.ScannerView;
import com.feifan.scan.common.toolbox.IScanStack;

public class CodeScannerFragment extends Fragment  implements IScanStack.onScanCallBack {
    private static final String TAG = CodeScannerFragment.class.getSimpleName();
    public static final String ACTION_EXCLUDE_PICTURE =
            "com.wanda.sdk.zxing.CaptureActivity.ACTION_EXCLUDE_PICTURE";
    public static final String SCAN_CODE_TIP = "scan_code_tip";
    public static final String IS_INVISIBLE_TIP_ICON = "is_invisible_tip_icon";
    /**
     * Note:the size is the type of SP
     */
    public static final String SCAN_CODE_TIP_TEXT_SIZE = "scan_code_tip_text_size";

    public static final int REQUEST_CODE = 1;
    private ScannerView mScannerView;
    private Button mLoadPictureButton;
    private Button mLightButton;
    private TextView scanCodeTipTextView;
    private ProgressDialog mDecodeDialog;
    public static final String CHECK_TYPE = "check_all_orientation";

    private View view;
    private LinearLayout mTipView;
    private ImageView mTipIconView;
    private RelativeLayout mBottom;
    private boolean isFlashOpen = false;
    private CaptureActivityOfResult captureActivityOfResult;
    private boolean isVisibleTipIcon;

    public void setCaptureActivityOfResult(
            CaptureActivityOfResult captureActivityOfResult) {
        this.captureActivityOfResult = captureActivityOfResult;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // 初始化 CameraManager
        Window window = getActivity().getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        window.setFormat(PixelFormat.TRANSLUCENT);
        view = inflater.inflate(R.layout.scan_code_view, container, false);
        initBund();
        findView(view);
        initScanView();
        return view;
    }

    private void findView(View view){
        if(view != null){
            mScannerView = (ScannerView) view.findViewById(R.id.scan_view);
            mLoadPictureButton = (Button) view
                    .findViewById(R.id.select_code_from_album);

            mLoadPictureButton.setVisibility(View.VISIBLE);
            mLoadPictureButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View arg0) {
                    Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                    i.setType("image/*");
                    try {
                        startActivityForResult(i, REQUEST_CODE);
                    } catch (ActivityNotFoundException e) {
                    }
                }
            });
            mLightButton = (Button) view.findViewById(R.id.open_light);
            mLightButton.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                   if(mScannerView != null){
                       mScannerView.setFlash(!isFlashOpen);
                       isFlashOpen = !isFlashOpen;
                   }
                }
            });
            mTipView = (LinearLayout) view.findViewById(R.id.tip_view);
            mTipIconView = (ImageView) view.findViewById(R.id.tip_icon);
            scanCodeTipTextView = (TextView) view
                    .findViewById(R.id.scan_code_my_code_tip_in_discount_tv);
            mBottom = (RelativeLayout) view.findViewById(R.id.bottom);
        }
    }

    private void initBund(){
        Bundle bundle = getArguments();
        if (null != bundle) {
            String tip = bundle.getString(SCAN_CODE_TIP);
            boolean needExcludePicture = bundle
                    .getBoolean(ACTION_EXCLUDE_PICTURE);
            if (null != tip) {
                scanCodeTipTextView.setText(tip);
            }
            int textSize = bundle.getInt(SCAN_CODE_TIP_TEXT_SIZE);
            if (textSize > 0) {
                scanCodeTipTextView.setTextSize(textSize);
            }
            if (needExcludePicture) {
                mBottom.setVisibility(View.INVISIBLE);
            }
            isVisibleTipIcon = bundle.getBoolean(IS_INVISIBLE_TIP_ICON);
            if (isVisibleTipIcon) {
                mTipIconView.setVisibility(View.GONE);
            }
        }
    }

    private void initScanView(){
        if(mScannerView != null) {
//            mScannerView.setScanStack(new ZXingStack());
            mScannerView.setScanCallBck(this);
            mScannerView.startCamera();
        }
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            final Uri uri = data.getData();
            if (uri == null) {
                return;
            }
            showDecodeDialog();
            new Thread() {
                public void run() {
                    startDecode(uri);
                }
            }.start();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void startDecode(Uri uri) {
        if(uri != null){
            Bitmap bitmap = decodeUriAsBitmap(uri);
            if(bitmap != null){
                mScannerView.scanBitMap(bitmap);
            }
        }
    }

    private Bitmap decodeUriAsBitmap(Uri uri) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(uri));
        } catch (Exception e) {
            //当图片过大的时候溢出
            e.printStackTrace();
            return null;
        }
        return bitmap;
    }

    private void showDecodeDialog() {
        if (isAdded() && mDecodeDialog == null) {
            mDecodeDialog = new ProgressDialog(getActivity());
            mDecodeDialog.setTitle(R.string.scan_cade_dialog_title);
            mDecodeDialog.setMessage(getString(R.string.dialog_message));
        }
        mDecodeDialog.show();
    }

    @Override
    public void onScanSucess(String result, String format) {
        if(!TextUtils.isEmpty(result) && !TextUtils.isEmpty(format)){
            captureActivityOfResult.getScanCodeResult(result,
                    System.currentTimeMillis(), format);
        }else {
            captureActivityOfResult.getScanCodeResult(null, 0, null);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if(mScannerView != null){
            mScannerView.stopCamera();
        }
    }
}
