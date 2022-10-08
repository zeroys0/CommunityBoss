package net.leelink.communityboss.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;


import net.leelink.communityboss.R;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.utils.LoadDialog;
import net.leelink.communityboss.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfirmLogoutActivity extends BaseActivity implements View.OnClickListener {
    Context context;
    private RelativeLayout rl_back;
    private TextView tv_stop, tv_logout;
    private int time = 10;
    String elderlyId;
    SharedPreferences sp;
    private ImageView img_warning;
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_logout);
        context = this;
        init();
    }

    public void init() {
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        tv_stop = findViewById(R.id.tv_stop);
        tv_stop.setOnClickListener(this);
        elderlyId = getIntent().getStringExtra("edlerlyId");
        sp = getSharedPreferences("sp", 0);
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    mHandler.sendEmptyMessage(0);
                    if (time == 0) {

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
                        tv_stop.setText("正在注销");
                        try {
                            logout();
                        } catch (Exception e) {

                        }
                        time = 10;
                    } else {
                        tv_stop.setText("点击停止注销(" + (--time) + "s)");
                    }
                }
            };
        });
        thread.start();
        img_warning = findViewById(R.id.img_warning);
        tv_logout = findViewById(R.id.tv_logout);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_stop:
                finish();
                break;
        }
    }

    public void logout() {
        LoadDialog.start(context);
        String elderlyId = sp.getString("elderlyId", "abccc");
        OkGo.<String>put(Urls.getInstance().CANCELACCOUNT + "/"+elderlyId)
                .tag(this)
                .headers("token", CommunityBossApplication.token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        LoadDialog.stop();
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("注销账户", json.toString());
                            if (json.getInt("status") == 200) {
                                img_warning.setImageResource(R.drawable.img_logout_sucess);
                                tv_stop.setVisibility(View.GONE);
                                tv_logout.setText("账号注销完成");
                                rl_back.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        unlogin();
                                    }
                                });
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        unlogin();
                                    }
                                }, 2000);
                            } else if (json.getInt("status") == 505) {

                            } else {
                                Toast.makeText(context, json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        LoadDialog.stop();
                    }
                });

    }

    /**
     * 退出登录
     */
    public void unlogin() {
        Intent intent4 = new Intent(ConfirmLogoutActivity.this, LoginActivity.class);
        SharedPreferences sp = getSharedPreferences("sp", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("secretKey");
        editor.remove("telephone");
        editor.remove("clientId");
        editor.apply();
        intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent4);
        finish();
    }

//    private class TimeRun implements Runnable {
//        @Override
//        public void run() {
//            while (true) {
//                mHandler.sendEmptyMessage(0);
//                if (time == 0) {
//
//                    break;
//                }
//                try {
//                    Thread.sleep(1000);
//                } catch (InterruptedException e) {
//
//                }
//            }
//        }
//
//        @SuppressLint("HandlerLeak")
//        private Handler mHandler = new Handler() {
//            public void handleMessage(Message msg) {
//                if (time == 0) {
//                    tv_stop.setText("正在注销");
//                    logout();
//                    time = 10;
//                } else {
//                    tv_stop.setText("点击停止注销("+(--time) + "s)");
//                }
//            }
//        };
//    }

    @Override
    protected void onStop() {

        if (!thread.isInterrupted()) {

            try {
                thread.interrupt();
            } catch (Exception e) {
                Toast.makeText(context, "进程阻止失败", Toast.LENGTH_SHORT).show();
            }

        }
        super.onStop();
    }
}