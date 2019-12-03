package net.leelink.communityboss.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.MainActivity;
import net.leelink.communityboss.R;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.bean.StoreInfo;
import net.leelink.communityboss.utils.Acache;
import net.leelink.communityboss.utils.Urls;


import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Field;

import cn.jpush.android.api.JPushInterface;


public class LoginActivity extends BaseActivity implements View.OnClickListener {
private TabLayout tablayout;
private TextView getmsmpass_TX,tv_forgot,tv_register;
private EditText ed_phone,ed_password,ed_code;
private Button btn_login;
private static int TYPE = 0;    //登录方式 0 验证码登录 1 密码登录
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        initLogin();
    }


    public void init(){
        getmsmpass_TX = findViewById(R.id.getmsmpass_TX);
        getmsmpass_TX.setOnClickListener(this);
        ed_phone = findViewById(R.id.ed_phone);
        ed_password = findViewById(R.id.ed_password);
        ed_code = findViewById(R.id.ed_code);
        tv_forgot = findViewById(R.id.tv_forgot);
        tv_forgot.setOnClickListener(this);
        tv_register = findViewById(R.id.tv_register);
        tv_register.setOnClickListener(this);
        btn_login = findViewById(R.id.btn_login);
        btn_login.setOnClickListener(this);
        tablayout = findViewById(R.id.tablayout);
        tablayout.addTab(tablayout.newTab().setText("手机验证码登录"));
        tablayout.addTab(tablayout.newTab().setText("账号密码登录"));
        tablayout.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(tablayout, 32, 32);
            }
        });
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()==0) {
                    getmsmpass_TX.setVisibility(View.VISIBLE);
                    tv_forgot.setVisibility(View.INVISIBLE);
                    ed_code.setVisibility(View.VISIBLE);
                    ed_password.setVisibility(View.GONE);
                    ed_phone.setHint("请输入手机号");
                    TYPE = 0;
                } else {
                    getmsmpass_TX.setVisibility(View.GONE);
                    tv_forgot.setVisibility(View.VISIBLE);
                    ed_code.setVisibility(View.GONE);
                    ed_password.setVisibility(View.VISIBLE);
                    ed_phone.setHint("请输入账号");
                    ed_password.setHint("请输入密码");
                    TYPE = 1;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    public void initLogin(){
        SharedPreferences sp = getSharedPreferences("sp",0);
        if(sp.getString("AppToken","")==null ||sp.getString("AppToken","").equals("") ) {

        } else {
            CommunityBossApplication.token = sp.getString("AppToken","");
            JSONObject jsonObject = Acache.get(this).getAsJSONObject("storeInfo");
            Gson gson = new Gson();
            CommunityBossApplication.storeInfo = gson.fromJson(jsonObject.toString(),StoreInfo.class);
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }


    }

    // 获取短信验证码的页面显示
    private int time = 60;


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.getmsmpass_TX:    //获取验证码
                sendSmsCode();
                break;
            case R.id.tv_register:  //注册
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_login:    //登录
                if(TYPE==0) {
                    loginByCode();
                } else {
                    login();
                }
                break;
            case R.id.tv_forgot:    //忘记密码
                Intent intent1 = new Intent(this,ChangePasswordActivity.class);
                startActivity(intent1);
                break;

                default:
                    break;
        }
    }

    //短信验证码登录
    public void loginByCode(){
        OkGo.<String>post(Urls.LOGINBYCODE)
                .tag(this)
                .params("username", ed_phone.getText().toString().trim())
                .params("smscode",ed_password.getText().toString().trim())
                .params("deviceToken", JPushInterface.getRegistrationID(LoginActivity.this))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            body = body.substring(1,body.length()-1);
                            JSONObject json = new JSONObject(body.replaceAll("\\\\",""));
                            Log.d("验证码登录",json.toString());
                            if (json.getInt("ResultCode") == 200) {
                                CommunityBossApplication.token = json.getString("AppToken");
                                SharedPreferences sp = getSharedPreferences("sp",0);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("AppToken",json.getString("AppToken"));
                                editor.apply();
                                Gson gson = new Gson();
                                JSONObject jsonObject = json.getJSONObject("StoreInfo");
                                Acache.get(LoginActivity.this).put("storeInfo",jsonObject);
                                if(jsonObject.getInt("StoreState")==0) {
                                    Intent intent = new Intent(LoginActivity.this, ApplyActivity.class);
                                    startActivity(intent);
                                } else {
                                    StoreInfo storeInfo = gson.fromJson(jsonObject.toString(), StoreInfo.class);
                                    CommunityBossApplication.storeInfo = storeInfo;
                                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(intent);
                                }
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, json.getString("ResultValue"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //账号密码登录
    public void login(){
        OkGo.<String>post(Urls.LOGIN)
                .tag(this)
                .params("username", ed_phone.getText().toString().trim())
                .params("password",ed_password.getText().toString().trim())
                .params("deviceToken",JPushInterface.getRegistrationID(LoginActivity.this))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            body = body.substring(1,body.length()-1);
                            JSONObject json = new JSONObject(body.replaceAll("\\\\",""));
                            Log.d("用户名密码登录",json.toString());
                            if (json.getInt("ResultCode") == 200) {
                                CommunityBossApplication.token = json.getString("AppToken");
                                SharedPreferences sp = getSharedPreferences("sp",0);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("AppToken",json.getString("AppToken"));
                                editor.apply();
                                Gson gson = new Gson();
                                JSONObject jsonObject = json.getJSONObject("StoreInfo");
                                Acache.get(LoginActivity.this).put("storeInfo",jsonObject);
                                if(jsonObject.getInt("StoreState")==0) {
                                    Intent intent = new Intent(LoginActivity.this, ApplyActivity.class);
                                    startActivity(intent);
                                }  else if(jsonObject.getInt("StoreState")==1) {
                                    Intent intent = new Intent(LoginActivity.this, ExamineActivity.class);
                                    startActivity(intent);
                                }  else {
                                    StoreInfo storeInfo = gson.fromJson(jsonObject.toString(), StoreInfo.class);
                                    CommunityBossApplication.storeInfo = storeInfo;
                                    Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                    startActivity(intent);
                                    finish();
                                }

                            } else {
                                Toast.makeText(LoginActivity.this, json.getString("ResultValue"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //发送短信验证码
    public void sendSmsCode(){
        if(!ed_phone.getText().toString().trim().equals("")){
            OkGo.<String>get(Urls.SENDSMSCODE)
                    .tag(this)
                    .params("phone", ed_phone.getText().toString().trim())
                    .params("used",1)
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
                                        new Thread(new LoginActivity.TimeRun()).start();
                                    }else {
                                        getmsmpass_TX.setEnabled(false);
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, json.getString("ResultValue"), Toast.LENGTH_LONG).show();
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
                    getmsmpass_TX.setOnClickListener(LoginActivity.this);
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

    public static void setIndicator(TabLayout tabs, int leftDip, int rightDip) {
        Class<?> tabLayout = tabs.getClass();
        Field tabStrip = null;
        try {
            tabStrip = tabLayout.getDeclaredField("mTabStrip");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        tabStrip.setAccessible(true);
        LinearLayout llTab = null;
        try {
            llTab = (LinearLayout) tabStrip.get(tabs);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        int left = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, leftDip, Resources.getSystem().getDisplayMetrics());
        int right = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, rightDip, Resources.getSystem().getDisplayMetrics());

        for (int i = 0; i < llTab.getChildCount(); i++) {
            View child = llTab.getChildAt(i);
            child.setPadding(0, 0, 0, 0);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
            params.leftMargin = left;
            params.rightMargin = right;
            child.setLayoutParams(params);
            child.invalidate();
        }
    }
}
