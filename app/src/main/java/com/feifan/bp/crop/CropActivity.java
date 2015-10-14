package com.feifan.bp.crop;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.Toast;

import com.feifan.bp.net.UrlFactory;
import com.feifan.bp.util.LogUtil;
import com.feifan.bp.R;
import com.feifan.bp.base.BaseActivity;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;

import java.io.File;
import java.io.InputStream;

/**
 * For test.
 */
public class CropActivity extends BaseActivity {
    private static final String TAG = "CropActivity";
    @Override
    protected boolean isShowToolbar() {
        return true;
    }

    private ImageView resultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crop);

        resultView = (ImageView) findViewById(R.id.result_image);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_crop, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_select) {
            resultView.setImageDrawable(null);
            Crop.pickImage(this);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        LogUtil.i(TAG, "onActivityResult() requestCode=" + requestCode + " resultCode=" + resultCode);
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP && resultCode == RESULT_OK) {
            handleCrop(resultCode, result);
        }
    }

    private void beginCrop(Uri source) {
        LogUtil.i(TAG, "beginCrop() source=" + source);
        Uri outputUri = Uri.fromFile(new File(getCacheDir(), "cropped"));
        new Crop(this, source).output(outputUri).withAspect(16, 9).start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        LogUtil.i(TAG, "handleCrop() resultCode=" + resultCode);
        if (resultCode == RESULT_OK) {
            resultView.setImageURI(Crop.getOutput(result));
            uploadPicture(Crop.getOutput(result));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadPicture(Uri uri) {
        try {
            InputStream in = getContentResolver().openInputStream(uri);
            RequestParams params = new RequestParams();
            params.put("image", in);

            String url = UrlFactory.uploadPicture();

            AsyncHttpClient client = new AsyncHttpClient();
            showProgressBar(false);
            client.post(url, params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers,
                                      byte[] responseBody) {
                    hideProgressBar();
                    String result = new String(responseBody);
                    LogUtil.i(TAG, "upload response=" + result);
                    try {
                        if (statusCode == 200) {
                            Toast.makeText(CropActivity.this, "上传成功!", Toast.LENGTH_SHORT)
                                    .show();
                        } else {
                            Toast.makeText(CropActivity.this,
                                    "网络访问异常，错误码：" + statusCode, Toast.LENGTH_SHORT).show();

                        }
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers,
                                      byte[] responseBody, Throwable error) {
                    hideProgressBar();
                    Toast.makeText(CropActivity.this,
                            "网络访问异常，错误码  > " + statusCode, Toast.LENGTH_SHORT).show();

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
