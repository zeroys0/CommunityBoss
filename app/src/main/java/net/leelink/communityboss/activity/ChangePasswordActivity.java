package net.leelink.communityboss.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.R;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener {
private EditText ed_phone,ed_code,ed_password,ed_confirm_password;
private TextView getmsmpass_TX;
private Button btn_confirm;
    // 获取短信验证码的页面显示
    private int time = 60;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        init();
    }

    public void init(){
        ed_phone  = findViewById(R.id.ed_phone);
        ed_code = findViewById(R.id.ed_code);
        ed_password = findViewById(R.id.ed_password);
        ed_confirm_password = findViewById(R.id.ed_confirm_password);
        getmsmpass_TX = findViewById(R.id.getmsmpass_TX);
        getmsmpass_TX.setOnClickListener(this);
        btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);   
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.getmsmpass_TX:    //获取验证码
                sendSmsCode();
                break;
            case R.id.btn_confirm:      //确认修改密码
                if(ed_password.getText().toString().trim().equals(ed_confirm_password.getText().toString().trim())){
                    resetPassword();
                }else {
                    Toast.makeText(this, "两次密码输入的不一致", Toast.LENGTH_SHORT).show();
                }
                break;
                default:
                    break;
        }
    }

    //修改密码(忘记密码)
    public void resetPassword(){
        OkGo.<String>post(Urls.CHANGEPASSWORD)
                .tag(this)
                .params("telephone", ed_phone.getText().toString().trim())
                .params("smsCode",ed_code.getText().toString().trim())
                .params("password",ed_password.getText().toString().trim())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("忘记密码",json.toString());
                            if (json.getInt("status") == 200) {
                                finish();
                            } else {

                            }
                            Toast.makeText(ChangePasswordActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //发送短信验证码
    public void sendSmsCode(){
        if(!ed_phone.getText().toString().trim().equals("")){
            OkGo.<String>post(Urls.SENDSMSCODE+"?telephone="+ed_phone.getText().toString().trim())
                    .tag(this)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            try {
                                String body = response.body();
                                JSONObject json = new JSONObject(body);
//                                body = body.substring(1,body.length()-1);
//                                JSONObject json = new JSONObject(body.replaceAll("\\\\",""));
                                Log.d("获取验证码",json.toString());
                                if (json.getInt("status") == 200) {
                                    if(time == 60) {
                                        new Thread(new ChangePasswordActivity.TimeRun()).start();
                                    }else {
                                        getmsmpass_TX.setEnabled(false);
                                    }
                                } else {
                                    Toast.makeText(ChangePasswordActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

        } else {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    private class TimeRun implements Runnable {
        @Override
        public void run() {
            while (true) {
                mHandler.sendEmptyMessage(0);
                if (time == 0) {
                    getmsmpass_TX.setOnClickListener(ChangePasswordActivity.this);
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
        }

        @SuppressLint("HandlerLeak")
        private Handler mHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (time == 0) {
                    getmsmpass_TX.setText("获取验证码");
                    time = 60;
                } else {
                    getmsmpass_TX.setText((--time) + "秒");
                }
            }
        };
    }
}
