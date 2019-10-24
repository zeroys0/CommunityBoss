package net.leelink.communityboss.activity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.R;
import net.leelink.communityboss.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;


public class RegisterActivity extends BaseActivity implements View.OnClickListener {

private TextView getmsmpass_TX;
private EditText ed_phone,ed_code,ed_password,ed_confirm_password;
private Button btn_register;
private int time = 60;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    public void init(){
        getmsmpass_TX = findViewById(R.id.getmsmpass_TX);
        getmsmpass_TX.setOnClickListener(this);
        ed_phone = findViewById(R.id.ed_phone);
        ed_code = findViewById(R.id.ed_code);
        ed_password = findViewById(R.id.ed_password);
        ed_confirm_password = findViewById(R.id.ed_confirm_password);
        btn_register = findViewById(R.id.btn_register);
        btn_register.setOnClickListener(this);
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.getmsmpass_TX:    //获取验证码

                sendSmsCode();
                break;

            case R.id.btn_register: //注册

                regist();
                break;

                default:
                    break;
        }
    }

    //注册
    public void regist(){
       if(!ed_code.getText().toString().trim().equals("")){
            if(ed_password.getText().toString().trim().equals(ed_confirm_password.getText().toString().trim())){
                OkGo.<String>post(Urls.REGISTER)
                        .tag(this)
                        .params("username", ed_phone.getText().toString().trim())
                        .params("password",ed_password.getText().toString().trim())
                        .params("smscode",ed_code.getText().toString().trim())
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {
                                try {
                                    String body = response.body();
                                    body = body.substring(1,body.length()-1);
                                    JSONObject json = new JSONObject(body.replaceAll("\\\\",""));
                                    Log.d("账号注册",json.toString());
                                    if (json.getInt("ResultCode") == 200) {
                                        finish();
                                    } else {
                                        Toast.makeText(RegisterActivity.this, json.getString("ResultValue"), Toast.LENGTH_LONG).show();
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
            } else {
                Toast.makeText(this, "两次密码不一致", Toast.LENGTH_SHORT).show();
            }
       }else {
           Toast.makeText(this, "请输入正确的验证码", Toast.LENGTH_SHORT).show();
       }
    }

    //发送短信验证码
    public void sendSmsCode(){
        if(!ed_phone.getText().toString().trim().equals("")){
            OkGo.<String>get(Urls.SENDSMSCODE)
                    .tag(this)
                    .params("phone", ed_phone.getText().toString().trim())
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            try {
                                String body = response.body();
                                body = body.substring(1,body.length()-1);
                                JSONObject json = new JSONObject(body.replaceAll("\\\\",""));
                                Log.d("获取验证码",json.toString());
                                if (json.getInt("ResultCode") == 200) {
                                    if(time == 60) {
                                        new Thread(new RegisterActivity.TimeRun()).start();
                                    }else {
                                        getmsmpass_TX.setEnabled(false);
                                    }
                                } else {
                                    Toast.makeText(RegisterActivity.this, json.getString("ResultValue"), Toast.LENGTH_LONG).show();
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
                    getmsmpass_TX.setEnabled(true);
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
