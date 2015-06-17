package com.feifan.bp.login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.toolbox.Volley;
import com.feifan.bp.R;
import com.feifan.bp.net.API;
import com.feifan.bp.net.http.HttpStack;
import com.feifan.bp.net.http.HttpUrlConnectionStack;
import com.feifan.bp.net.http.Request;
import com.feifan.bp.net.http.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.SoftReference;

/**
 * Created by maning on 15/6/15.
 */
public class LoginActivity extends FragmentActivity implements View.OnClickListener {

    private EditText mEditTextPhone;
    private EditText mEditTextPassword;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mEditTextPhone = (EditText) findViewById(R.id.et_phone);
        mEditTextPassword = (EditText) findViewById(R.id.et_password);

        findViewById(R.id.btn_login).setOnClickListener(this);
        mEditTextPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

//    private void setupActionBar() {
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayShowCustomEnabled(true);
//        actionBar.setCustomView(R.layout.actionbar_login);
//    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_login:
                String name = mEditTextPhone.getText().toString();
                String password = mEditTextPassword.getText().toString();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(password)) {
                    Toast.makeText(this, getString(R.string.login_info_not_enough), Toast.LENGTH_SHORT).show();
                    break;
                }
                new LoginTask(this).execute(name, password);
                break;
            default:
                // Do nothing.
                break;
        }
    }

    static class LoginTask extends AsyncTask<String, Void, Response> {
        private Context mAppContext;
        private SoftReference<Context> mContextRef;

        public LoginTask(Context context) {
            mAppContext = context.getApplicationContext();
            mContextRef = new SoftReference<Context>(context);
        }

        @Override
        protected Response doInBackground(String... params) {
            String userName = params[0];
            String password = params[1];

            Request request = new Request() {
                @Override
                public void process(Response response) {

                }
            };
            request.setMethod(HttpStack.METHOD.POST);
            request.setUrl(API.Login.LOGIN);
            request.putParams("userName", userName);
            request.putParams("password", password);

            HttpUrlConnectionStack stack = new HttpUrlConnectionStack();
            Response response = stack.connect(request);

            //TODO: We need handle net work response here.
            request.process(response);
            return response;
        }

        @Override
        protected void onPostExecute(Response response) {
            Log.d("LoginActivity", "response=" + response + " status=" + response.getStatus());
            Context context = mContextRef.get();
            if (response != null && response.getStatus() == 200) {

                try {
                    String result = response.getResult();
                    JSONObject j = new JSONObject(result);
                    String status = j.optString("status");
                    if ("200".equals(status)) {

                        if (context != null) {
//                            MainTabActivity.startActivity(context);
                            if (context instanceof Activity) {
                                ((Activity) context).finish();
                            }
                        }
                    } else {
                        Toast.makeText(mAppContext, j.optString("msg"), Toast.LENGTH_SHORT).show();
                    }
                    return;
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
            Toast.makeText(mAppContext, mAppContext.getString(R.string.login_error_network), Toast.LENGTH_SHORT).show();
        }
    }


}
