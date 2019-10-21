package net.leelink.communityboss.activity;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import net.leelink.communityboss.R;

public class ChangePasswordActivity extends BaseActivity implements View.OnClickListener {
private EditText ed_phone,ed_code,ed_password,ed_confirm_password;
private TextView getmsmpass_TX;
private Button btn_confirm;
    // 获取短信验证码的页面显示
    private int time = 120;
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
                if(time ==120) {
                    new Thread(new ChangePasswordActivity.TimeRun()).start();
                }
                break;
            case R.id.btn_confirm:      //确认修改密码
                break;
                default:
                    break;
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
                    time = 120;
                } else {
                    getmsmpass_TX.setText((--time) + "秒");
                }
            }
        };
    }
}
