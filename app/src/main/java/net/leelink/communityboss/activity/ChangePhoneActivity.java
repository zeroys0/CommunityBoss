package net.leelink.communityboss.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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

public class ChangePhoneActivity extends BaseActivity implements View.OnClickListener {
private RelativeLayout rl_back;
private TextView getmsmpass_TX;
private EditText ed_phone,ed_code;
private Button btn_complete;
    private int time = 60;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone);
        init();
    }

    public void init(){
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        getmsmpass_TX = findViewById(R.id.getmsmpass_TX);
        getmsmpass_TX.setOnClickListener(this);
        ed_phone = findViewById(R.id.ed_phone);
        btn_complete = findViewById(R.id.btn_complete);
        btn_complete.setOnClickListener(this);

        ed_code = findViewById(R.id.ed_code);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_back:
                finish();
                break;
            case  R.id.getmsmpass_TX:   //发送验证码
                send();
                break;
            case R.id.btn_complete:     //修改绑定电话
                changePhone();
                break;
                default:
                    break;
        }
    }

    //修改绑定电话
    public void changePhone(){
        OkGo.<String>post(Urls.PHONENUMBER+"?appToken="+CommunityBossApplication.token)
                .params("phoneNumber", ed_phone.getText().toString().trim())
                .params("smscode",ed_code.getText().toString().trim())
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            body = body.substring(1,body.length()-1);
                            JSONObject json = new JSONObject(body.replaceAll("\\\\",""));
                            Log.d("修改绑定电话",json.toString());
                            if (json.getInt("ResultCode") == 200) {
                                SharedPreferences sp = getSharedPreferences("sp",0);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.remove("AppToken");
                                editor.apply();
                                Intent intent = new Intent(ChangePhoneActivity.this,LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                                Toast.makeText(ChangePhoneActivity.this, "修改成功请重新登录", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(ChangePhoneActivity.this, json.getString("ResultValue"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //发送验证码
    public void send(){
        OkGo.<String>get(Urls.SENDSMSCODE)
                .tag(this)
                .params("phone", ed_phone.getText().toString().trim())
                .params("used",3)
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
                                    new Thread(new ChangePhoneActivity.TimeRun()).start();
                                }else {
                                    getmsmpass_TX.setEnabled(false);
                                }
                            } else {
                                Toast.makeText(ChangePhoneActivity.this, json.getString("ResultValue"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private class TimeRun implements Runnable {
        @Override
        public void run() {
            while (true) {
                mHandler.sendEmptyMessage(0);
                if (time == 0) {
                    getmsmpass_TX.setOnClickListener(ChangePhoneActivity.this);
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
