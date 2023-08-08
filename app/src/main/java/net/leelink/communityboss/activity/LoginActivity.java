package net.leelink.communityboss.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.MainActivity;
import net.leelink.communityboss.R;
import net.leelink.communityboss.adapter.OnOrderListener;
import net.leelink.communityboss.adapter.UserNameAdapter;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.bean.StoreInfo;
import net.leelink.communityboss.housekeep.HousekeepApplyActivity;
import net.leelink.communityboss.housekeep.HousekeepMainActivity;
import net.leelink.communityboss.utils.Acache;
import net.leelink.communityboss.utils.Urls;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.jpush.android.api.JPushInterface;


public class LoginActivity extends BaseActivity implements View.OnClickListener, OnOrderListener {
    private TextView getmsmpass_TX, tv_forgot, tv_register, tv_text, tv_code, tv_tab;
    private EditText ed_phone, ed_password, ed_code;
    private Button btn_login;
    private ImageView img_user_name, img_eye, img_logo;
    private int loginType = 1;      //登录方式 0验证码登录 1账号密码登录
    Context mContext;
    private ProgressBar mProgressBar;
    private CheckBox cb_agree;
    private RecyclerView user_list;
    PopupWindow pop;
    boolean visible = false;
    private Context context;
    private PopupWindow popuPhoneW;
    private View popview;
    TextView tv_cancel, tv_confirm, tv_agreement;
    SharedPreferences sp;
    boolean first = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sp = getSharedPreferences("sp", 0);
        //requestPermissions();
        createProgressBar();
        init();
        initLogin();
        context = this;
        img_logo = findViewById(R.id.img_logo);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    public void init() {
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
        tv_code = findViewById(R.id.tv_code);
        tv_code.setOnClickListener(this);
        img_user_name = findViewById(R.id.img_user_name);
        img_user_name.setOnClickListener(this);
        tv_tab = findViewById(R.id.tv_tab);
        tv_tab.setOnClickListener(this);
        img_eye = findViewById(R.id.img_eye);
        img_eye.setOnClickListener(this);
        cb_agree = findViewById(R.id.cb_agree);
        cb_agree.setOnClickListener(this);

        tv_text = findViewById(R.id.tv_text);
        SpannableString spannableString = new SpannableString("已阅读并同意<<用户协议>>以及<<隐私政策>>");
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(LoginActivity.this, WebActivity.class);
                intent.putExtra("type", "distribution");
                intent.putExtra("url", "https://www.llky.net.cn/store/protocol.html");
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.blue)); //设置颜色
            }
        }, 6, 14, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                Intent intent = new Intent(LoginActivity.this, WebActivity.class);
                intent.putExtra("type", "distribution");
                intent.putExtra("url", "https://www.llky.net.cn/store/privacyPolicy.html");
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.blue)); //设置颜色
            }
        }, 16, 24, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_text.append(spannableString);
        tv_text.setMovementMethod(LinkMovementMethod.getInstance());  //很重要，点击无效就是由于没有设置这个引起

    }

    public void initLogin() {
        SharedPreferences sp = getSharedPreferences("sp", 0);
        String ip = sp.getString("ip", "");
        Urls.IP = ip;
        if (ip.equals("")) {
            getCode("000002");
        } else {
            first = false;
        }

        if (!sp.getString("secretKey", "").equals("")) {
            quickLogin();
        }


    }

    // 获取短信验证码的页面显示
    private int time = 60;


    @Override
    public void onClick(View v) {
        CommunityBossApplication app = (CommunityBossApplication) getApplication();
        switch (v.getId()) {
            case R.id.getmsmpass_TX:    //获取验证码
                sendSmsCode();
                break;
            case R.id.tv_register:  //注册
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_login:    //登录
                if (cb_agree.isChecked()) {
                    if (loginType == 0) {
                        loginByCode();
                    } else {
                        login();
                    }
                } else {
                    Toast.makeText(context, "请认真阅读并同意用户协议", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.tv_forgot:    //忘记密码
                Intent intent1 = new Intent(this, ChangePasswordActivity.class);
                startActivity(intent1);
                break;
            case R.id.tv_code:      //商户编码
                backgroundAlpha(0.5f);
                showPopup();
                break;
            case R.id.img_user_name:    //过往登录列表
                backgroundAlpha(0.5f);
                showPopup1();
                break;
            case R.id.tv_tab:
                if (loginType == 1) {
                    getmsmpass_TX.setVisibility(View.VISIBLE);
                    tv_forgot.setVisibility(View.INVISIBLE);
                    ed_phone.setHint("请输入手机号");
                    ed_password.setVisibility(View.GONE);
                    ed_code.setVisibility(View.VISIBLE);
                    img_eye.setVisibility(View.GONE);
                    tv_tab.setText("密码登录");
                    loginType = 0;
                } else {
                    getmsmpass_TX.setVisibility(View.GONE);
                    tv_forgot.setVisibility(View.VISIBLE);
                    ed_password.setVisibility(View.VISIBLE);
                    ed_code.setVisibility(View.GONE);
                    img_eye.setVisibility(View.VISIBLE);
                    ed_phone.setHint("请输入手机号");
                    tv_tab.setText("验证码登录");
                    loginType = 1;
                }
                break;
            case R.id.img_eye:
                if (visible) {
                    ed_password.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    img_eye.setImageResource(R.drawable.img_eye_close);
                    visible = false;
                } else {
                    ed_password.setInputType(InputType.TYPE_CLASS_TEXT);
                    img_eye.setImageResource(R.drawable.img_eye);
                    visible = true;
                }
                break;

            default:
                break;
        }
    }

    //短信验证码登录
    public void loginByCode() {
        if (Urls.IP.equals("")) {
            Toast.makeText(mContext, "请输入商户编码", Toast.LENGTH_SHORT).show();
            return;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        OkGo.<String>post(Urls.getInstance().LOGINBYCODE)
                .tag(this)
                .params("telephone", ed_phone.getText().toString().trim())
                .params("code", ed_code.getText().toString().trim())
                .params("deviceToken", JPushInterface.getRegistrationID(LoginActivity.this))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        mProgressBar.setVisibility(View.GONE);
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("验证码登录", json.toString());
                            if (json.getInt("status") == 200) {
                                //   CommunityBossApplication.token = json.getString("AppToken");
                                saveUsername(ed_phone.getText().toString().trim());
                                JSONObject jsonObject = json.getJSONObject("data");

                                if (jsonObject.getInt("serverTypeId") == 100) {
                                    Intent intent = new Intent(LoginActivity.this, ChooseIdentityActivity.class);
                                    intent.putExtra("id", jsonObject.getString("id"));
                                    startActivity(intent);
                                } else if (jsonObject.getInt("serverTypeId") == 1) {
                                    if (jsonObject.getInt("vertifyState") == 1) {
                                        Intent intent = new Intent(LoginActivity.this, ExamineActivity.class);
                                        startActivity(intent);
                                    } else if (jsonObject.getInt("vertifyState") == 2) {
                                        SharedPreferences sp = getSharedPreferences("sp", 0);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("secretKey", jsonObject.getString("secretKey"));
                                        editor.putString("telephone", ed_phone.getText().toString().trim());
                                        editor.apply();
                                        Gson gson = new Gson();
                                        Acache.get(LoginActivity.this).put("storeInfo", jsonObject);
                                        StoreInfo storeInfo = gson.fromJson(jsonObject.toString(), StoreInfo.class);
                                        CommunityBossApplication.storeInfo = storeInfo;
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                } else if (jsonObject.getInt("serverTypeId") == 2) {
                                    if (jsonObject.getInt("vertifyState") == 1) {
                                        Intent intent = new Intent(LoginActivity.this, ExamineActivity.class);
                                        startActivity(intent);
                                    } else if (jsonObject.getInt("vertifyState") == 2) {
                                        SharedPreferences sp = getSharedPreferences("sp", 0);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("secretKey", jsonObject.getString("secretKey"));
                                        editor.putString("telephone", ed_phone.getText().toString().trim());
                                        editor.apply();
                                        Gson gson = new Gson();
                                        Acache.get(LoginActivity.this).put("storeInfo", jsonObject);
                                        StoreInfo storeInfo = gson.fromJson(jsonObject.toString(), StoreInfo.class);
                                        CommunityBossApplication.storeInfo = storeInfo;
                                        Intent intent = new Intent(LoginActivity.this, HousekeepMainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    }
                                }

                            } else {
                                Toast.makeText(LoginActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //账号密码登录
    public void login() {
        if (Urls.IP.equals("")) {
            Toast.makeText(mContext, "请输入商户编码", Toast.LENGTH_SHORT).show();
            return;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        Log.e("login: ", Urls.IP);
        Log.e("login: ", Urls.getInstance().LOGIN);
        Log.e("login: ", JPushInterface.getRegistrationID(LoginActivity.this));
        OkGo.<String>post(Urls.getInstance().LOGIN)
                .tag(this)
                .params("telephone", ed_phone.getText().toString().trim())
                .params("password", ed_password.getText().toString().trim())
                .params("deviceToken", JPushInterface.getRegistrationID(LoginActivity.this))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        mProgressBar.setVisibility(View.GONE);
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("用户名密码登录", json.toString());
                            if (json.getInt("status") == 200) {
                                JSONObject jsonObject = json.getJSONObject("data");
                                saveUsername(ed_phone.getText().toString().trim());
                                if (jsonObject.getInt("serverTypeId") == 100) {
                                    Intent intent = new Intent(LoginActivity.this, ChooseIdentityActivity.class);
                                    intent.putExtra("id", jsonObject.getString("id"));
                                    startActivity(intent);
                                } else if (jsonObject.getInt("serverTypeId") == 1) {
                                    if (jsonObject.getInt("vertifyState") == 1) {
                                        Intent intent = new Intent(LoginActivity.this, ExamineActivity.class);
                                        startActivity(intent);
                                    } else if (jsonObject.getInt("vertifyState") == 2) {
                                        SharedPreferences sp = getSharedPreferences("sp", 0);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("secretKey", jsonObject.getString("secretKey"));
                                        editor.putString("telephone", ed_phone.getText().toString().trim());
                                        editor.apply();
                                        Gson gson = new Gson();
                                        Acache.get(LoginActivity.this).put("storeInfo", jsonObject);
                                        StoreInfo storeInfo = gson.fromJson(jsonObject.toString(), StoreInfo.class);
                                        CommunityBossApplication.storeInfo = storeInfo;
                                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else if (jsonObject.getInt("vertifyState") == 3) {
                                        Intent intent = new Intent(LoginActivity.this, ApplyActivity.class);
                                        intent.putExtra("id", jsonObject.getString("id"));
                                        startActivity(intent);
                                    }
                                } else if (jsonObject.getInt("serverTypeId") == 2) {
                                    if (jsonObject.getInt("vertifyState") == 1) {
                                        Intent intent = new Intent(LoginActivity.this, ExamineActivity.class);
                                        startActivity(intent);
                                    } else if (jsonObject.getInt("vertifyState") == 2) {
                                        SharedPreferences sp = getSharedPreferences("sp", 0);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("secretKey", jsonObject.getString("secretKey"));
                                        editor.putString("telephone", ed_phone.getText().toString().trim());
                                        editor.apply();
                                        Gson gson = new Gson();
                                        Acache.get(LoginActivity.this).put("storeInfo", jsonObject);
                                        StoreInfo storeInfo = gson.fromJson(jsonObject.toString(), StoreInfo.class);
                                        CommunityBossApplication.storeInfo = storeInfo;
                                        Intent intent = new Intent(LoginActivity.this, HousekeepMainActivity.class);
                                        startActivity(intent);
                                        finish();
                                    } else if (jsonObject.getInt("vertifyState") == 3) {
                                        Intent intent = new Intent(LoginActivity.this, HousekeepApplyActivity.class);
                                        intent.putExtra("id", jsonObject.getString("id"));
                                        startActivity(intent);
                                    }
                                }


                            } else {
                                Toast.makeText(LoginActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void quickLogin() {
        if (Urls.IP.equals("")) {
            Toast.makeText(mContext, "请输入商户编码", Toast.LENGTH_SHORT).show();
            return;
        }
        final SharedPreferences sp = getSharedPreferences("sp", 0);
        if (!sp.getString("secretKey", "").equals("")) {
            OkGo.<String>post(Urls.getInstance().QUICKLOGIN)
                    .params("telephone", sp.getString("telephone", ""))
                    .params("secretKey", sp.getString("secretKey", ""))
                    .params("deviceToken", JPushInterface.getRegistrationID(this))
                    .tag(this)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            try {
                                String body = response.body();
                                JSONObject json = new JSONObject(body);
                                Log.d("快速登录", json.toString());
                                if (json.getInt("status") == 200) {
                                    JSONObject jsonObject = json.getJSONObject("data");

                                    if (jsonObject.getInt("serverTypeId") == 100) {
                                        Intent intent = new Intent(LoginActivity.this, ChooseIdentityActivity.class);
                                        intent.putExtra("id", jsonObject.getString("id"));
                                        startActivity(intent);
                                    } else if (jsonObject.getInt("serverTypeId") == 1) {
                                        if (jsonObject.getInt("vertifyState") == 1) {
                                            Intent intent = new Intent(LoginActivity.this, ExamineActivity.class);
                                            startActivity(intent);
                                        } else if (jsonObject.getInt("vertifyState") == 2) {
                                            Gson gson = new Gson();
                                            Acache.get(LoginActivity.this).put("storeInfo", jsonObject);
                                            StoreInfo storeInfo = gson.fromJson(jsonObject.toString(), StoreInfo.class);
                                            CommunityBossApplication.storeInfo = storeInfo;
                                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                                            startActivity(intent);
                                        }
                                    } else if (jsonObject.getInt("serverTypeId") == 2) {
                                        if (jsonObject.getInt("vertifyState") == 1) {
                                            Intent intent = new Intent(LoginActivity.this, ExamineActivity.class);
                                            startActivity(intent);
                                        } else if (jsonObject.getInt("vertifyState") == 2) {
                                            Gson gson = new Gson();
                                            Acache.get(LoginActivity.this).put("storeInfo", jsonObject);
                                            StoreInfo storeInfo = gson.fromJson(jsonObject.toString(), StoreInfo.class);
                                            CommunityBossApplication.storeInfo = storeInfo;
                                            Intent intent = new Intent(LoginActivity.this, HousekeepMainActivity.class);
                                            startActivity(intent);
                                        }
                                    }
                                    finish();
                                } else {

                                    Toast.makeText(LoginActivity.this, "登录过期,请重新登录", Toast.LENGTH_LONG).show();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    //发送短信验证码
    public void sendSmsCode() {
        if (Urls.IP.equals("")) {
            Toast.makeText(mContext, "请输入商户编码", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!ed_phone.getText().toString().trim().equals("")) {
            mProgressBar.setVisibility(View.VISIBLE);
            OkGo.<String>post(Urls.getInstance().SENDSMSCODE + "?telephone=" + ed_phone.getText().toString().trim())
                    .tag(this)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            mProgressBar.setVisibility(View.GONE);
                            try {
                                String body = response.body();
                                JSONObject json = new JSONObject(body);
                                Log.d("获取验证码", json.toString());
                                if (json.getInt("status") == 200) {
                                    if (time == 60) {
                                        new Thread(new LoginActivity.TimeRun()).start();
                                    } else {
                                        getmsmpass_TX.setEnabled(false);
                                    }
                                } else {
                                    Toast.makeText(LoginActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
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

    private void createProgressBar() {
        mContext = this;
        //整个Activity布局的最终父布局,参见参考资料
        FrameLayout rootFrameLayout = (FrameLayout) findViewById(android.R.id.content);
        FrameLayout.LayoutParams layoutParams =
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity = Gravity.CENTER;
        mProgressBar = new ProgressBar(mContext);
        mProgressBar.setLayoutParams(layoutParams);
        mProgressBar.setVisibility(View.GONE);
        rootFrameLayout.addView(mProgressBar);
    }

    @Override
    public void onItemClick(View view) {
        int position = user_list.getChildLayoutPosition(view);
        SharedPreferences sp = getSharedPreferences("sp", 0);
        String user_list = sp.getString("user_name", "");
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(user_list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        try {
            ed_phone.setText(jsonArray.getJSONObject(position).getString("user_name"));
            pop.dismiss();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onButtonClick(View view, int position) {

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


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                if (hideInputMethod(this, v)) {
                    return true; //隐藏键盘时，其他控件不响应点击事件==》注释则不拦截点击事件
                }
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    public static boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] leftTop = {0, 0};
            v.getLocationInWindow(leftTop);
            int left = leftTop[0], top = leftTop[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 保留点击EditText的事件
                return false;
            } else {
                return true;
            }
        }
        return false;
    }

    public static Boolean hideInputMethod(Context context, View v) {
        InputMethodManager imm = (InputMethodManager) context
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            return imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
        }
        return false;
    }


    @SuppressLint("WrongConstant")
    public void showPopup() {
        View popview = LayoutInflater.from(LoginActivity.this).inflate(R.layout.pop_partner_code, null);
        final EditText ed_name = popview.findViewById(R.id.ed_name);
        Button btn_confirm = popview.findViewById(R.id.btn_confirm);
        final PopupWindow popuPhoneW = new PopupWindow(popview,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popuPhoneW.setFocusable(true);
        popuPhoneW.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popuPhoneW.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popuPhoneW.setOutsideTouchable(true);
        popuPhoneW.setBackgroundDrawable(new BitmapDrawable());
        popuPhoneW.setOnDismissListener(new LoginActivity.poponDismissListener());
        popuPhoneW.showAtLocation(tv_code, Gravity.CENTER, 0, 0);
        SharedPreferences sp = getSharedPreferences("sp", 0);
        ed_name.setText(sp.getString("code", ""));
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ed_name.getText().toString().equals("")) {
                    String s = ed_name.getText().toString().trim();
                    getCode(s);
                    popuPhoneW.dismiss();
                }
            }
        });
    }

    public void showPopup1() {
        View popView = getLayoutInflater().inflate(R.layout.popu_choose_user, null);

        user_list = popView.findViewById(R.id.user_list);

        SharedPreferences sp = getSharedPreferences("sp", 0);
        String userList = sp.getString("user_name", "");
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(userList);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        TextView tv_cancel = popView.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
            }
        });

        UserNameAdapter userNameAdapter = new UserNameAdapter(jsonArray, LoginActivity.this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, RecyclerView.VERTICAL, false);
        user_list.setAdapter(userNameAdapter);
        user_list.setLayoutManager(layoutManager);
        pop = new PopupWindow(popView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pop.setContentView(popView);
        pop.setOutsideTouchable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setOnDismissListener(new LoginActivity.poponDismissListener());

        pop.showAtLocation(ed_phone, Gravity.BOTTOM, 0, 150);
    }


    /**
     * 根据商户编码获取地址
     */
    public void getCode(final String code) {

        OkGo.<String>get(Urls.PARTNER_CODE + code)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("根据商户编码获取url", json.toString());
                            if (json.getInt("status") == 200) {
                                if (!json.isNull("data")) {
                                    json = json.getJSONObject("data");
                                    SharedPreferences sp = getSharedPreferences("sp", 0);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("ip", json.getString("apiUrl"));
                                    editor.putString("h5_ip", json.getString("h5Url"));
                                    editor.putString("ws", json.getString("websocketUrl"));
                                    editor.putString("c_ip", json.getString("clientInfoUrl"));
                                    Urls.IP = json.getString("apiUrl");
                                    Urls.H5_IP = json.getString("h5Url");
                                    editor.putString("code", code);
                                    if (first) {
                                        first = false;
                                    } else {
                                        Toast.makeText(LoginActivity.this, "切换商户成功", Toast.LENGTH_SHORT).show();
                                    }
                                    Log.e("login: ", Urls.IP);
                                    editor.apply();
                                } else {
                                    Toast.makeText(mContext, "商户编码错误", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(LoginActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {

                        super.onError(response);
                        Toast.makeText(LoginActivity.this, "网络不给力啊", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        if (bgAlpha == 1) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        getWindow().setAttributes(lp);
    }

    /**
     * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     *
     * @author cg
     */
    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            // Log.v("List_noteTypeActivity:", "我是关闭事件");
            backgroundAlpha(1f);
        }
    }

    public void saveUsername(String user_name) {
        SharedPreferences sp = getSharedPreferences("sp", 0);
        String user_list = sp.getString("user_name", "");
        JSONArray jsonArray = null;
        try {
            jsonArray = new JSONArray(user_list);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (jsonArray != null) {
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    if (jsonObject.getString("user_name").equals(user_name)) {
                        return;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        } else {
            jsonArray = new JSONArray();
        }
        JSONObject json = new JSONObject();
        try {
            json.put("user_name", user_name);
            jsonArray.put(json);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("user_name", jsonArray.toString());
        editor.apply();

    }

}
