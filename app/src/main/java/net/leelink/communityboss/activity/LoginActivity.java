package net.leelink.communityboss.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import net.leelink.communityboss.MainActivity;
import net.leelink.communityboss.R;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.bean.StoreInfo;
import net.leelink.communityboss.housekeep.HousekeepApplyActivity;
import net.leelink.communityboss.housekeep.HousekeepMainActivity;
import net.leelink.communityboss.utils.Acache;
import net.leelink.communityboss.utils.Logger;
import net.leelink.communityboss.utils.Urls;


import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Field;

import cn.jpush.android.api.JPushInterface;
import io.reactivex.functions.Consumer;


public class LoginActivity extends BaseActivity implements View.OnClickListener {
private TabLayout tablayout;
private TextView getmsmpass_TX,tv_forgot,tv_register,tv_text,tv_code;
private EditText ed_phone,ed_password,ed_code;
private Button btn_login;
private static int TYPE = 0;    //登录方式 0 验证码登录 1 密码登录
    Context mContext;
    private ProgressBar mProgressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        requestPermissions();
        createProgressBar();
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
        tv_code = findViewById(R.id.tv_code);
        tv_code.setOnClickListener(this);
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
        tv_text = findViewById(R.id.tv_text);
        SpannableString spannableString = new SpannableString("已阅读并同意<<用户协议>>以及<<隐私政策>>");
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(LoginActivity.this,WebActivity.class);
                intent.putExtra("type","distribution");
                intent.putExtra("url","http://www.llky.net.cn/store/protocol.html");
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

                Intent intent = new Intent(LoginActivity.this,WebActivity.class);
                intent.putExtra("type","distribution");
                intent.putExtra("url","http://www.llky.net.cn/store/privacyPolicy.html");
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

    public void initLogin(){
        SharedPreferences sp = getSharedPreferences("sp",0);
        String ip = sp.getString("ip","");
        Urls.IP = ip;
        if(!sp.getString("secretKey","").equals("")) {
            quickLogin();
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
            case R.id.tv_code:      //商户编码
                backgroundAlpha(0.5f);
                showPopup();
                break;

                default:
                    break;
        }
    }

    //短信验证码登录
    public void loginByCode(){
        if(Urls.IP.equals("")) {
            Toast.makeText(mContext, "请输入商户编码", Toast.LENGTH_SHORT).show();
            return;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        OkGo.<String>post(Urls.getInstance().LOGINBYCODE)
                .tag(this)
                .params("telephone", ed_phone.getText().toString().trim())
                .params("code",ed_code.getText().toString().trim())
                .params("deviceToken", JPushInterface.getRegistrationID(LoginActivity.this))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        mProgressBar.setVisibility(View.GONE);
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("验证码登录",json.toString());
                            if (json.getInt("status") == 200) {
                             //   CommunityBossApplication.token = json.getString("AppToken");

                                JSONObject jsonObject = json.getJSONObject("data");

                                if(jsonObject.getInt("serverTypeId")==100) {
                                    Intent intent = new Intent(LoginActivity.this, ChooseIdentityActivity.class);
                                    intent.putExtra("id",jsonObject.getString("id"));
                                    startActivity(intent);
                                }else if(jsonObject.getInt("serverTypeId")==1) {
                                    if(jsonObject.getInt("vertifyState") == 1) {
                                        Intent intent = new Intent(LoginActivity.this, ExamineActivity.class);
                                        startActivity(intent);
                                    }else if(jsonObject.getInt("vertifyState") ==2 ){
                                        SharedPreferences sp = getSharedPreferences("sp",0);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("secretKey",jsonObject.getString("secretKey"));
                                        editor.putString("telephone",ed_phone.getText().toString().trim());
                                        editor.apply();
                                        Gson gson = new Gson();
                                        Acache.get(LoginActivity.this).put("storeInfo",jsonObject);
                                        StoreInfo storeInfo = gson.fromJson(jsonObject.toString(), StoreInfo.class);
                                        CommunityBossApplication.storeInfo = storeInfo;
                                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                        startActivity(intent);
                                    }
                                } else if(jsonObject.getInt("serverTypeId")==2) {
                                    if(jsonObject.getInt("vertifyState") == 1) {
                                        Intent intent = new Intent(LoginActivity.this, ExamineActivity.class);
                                        startActivity(intent);
                                    }else if(jsonObject.getInt("vertifyState") ==2 ){
                                        SharedPreferences sp = getSharedPreferences("sp",0);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("secretKey",jsonObject.getString("secretKey"));
                                        editor.putString("telephone",ed_phone.getText().toString().trim());
                                        editor.apply();
                                        Gson gson = new Gson();
                                        Acache.get(LoginActivity.this).put("storeInfo",jsonObject);
                                        StoreInfo storeInfo = gson.fromJson(jsonObject.toString(), StoreInfo.class);
                                        CommunityBossApplication.storeInfo = storeInfo;
                                        Intent intent = new Intent(LoginActivity.this,HousekeepMainActivity.class);
                                        startActivity(intent);
                                    }
                                }
                                finish();
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
    public void login(){
        if(Urls.IP.equals("")) {
            Toast.makeText(mContext, "请输入商户编码", Toast.LENGTH_SHORT).show();
            return;
        }
        mProgressBar.setVisibility(View.VISIBLE);
        Log.e( "login: ",Urls.IP );
        Log.e( "login: ",Urls.getInstance().LOGIN);
        OkGo.<String>post(Urls.getInstance().LOGIN)
                .tag(this)
                .params("telephone", ed_phone.getText().toString().trim())
                .params("password",ed_password.getText().toString().trim())
                .params("deviceToken",JPushInterface.getRegistrationID(LoginActivity.this))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        mProgressBar.setVisibility(View.GONE);
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("用户名密码登录",json.toString());
                            if (json.getInt("status") == 200) {
                                JSONObject jsonObject = json.getJSONObject("data");

                                if(jsonObject.getInt("serverTypeId")==100) {
                                    Intent intent = new Intent(LoginActivity.this, ChooseIdentityActivity.class);
                                    intent.putExtra("id",jsonObject.getString("id"));
                                    startActivity(intent);
                                }else if(jsonObject.getInt("serverTypeId")==1) {
                                    if(jsonObject.getInt("vertifyState") == 1) {
                                        Intent intent = new Intent(LoginActivity.this, ExamineActivity.class);
                                        startActivity(intent);
                                    }else if(jsonObject.getInt("vertifyState") ==2 ){
                                        SharedPreferences sp = getSharedPreferences("sp",0);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("secretKey",jsonObject.getString("secretKey"));
                                        editor.putString("telephone",ed_phone.getText().toString().trim());
                                        editor.apply();
                                        Gson gson = new Gson();
                                        Acache.get(LoginActivity.this).put("storeInfo",jsonObject);
                                        StoreInfo storeInfo = gson.fromJson(jsonObject.toString(), StoreInfo.class);
                                        CommunityBossApplication.storeInfo = storeInfo;
                                        Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                        startActivity(intent);
                                    } else if(jsonObject.getInt("vertifyState") == 3) {
                                        Intent intent = new Intent(LoginActivity.this, ApplyActivity.class);
                                        intent.putExtra("id",jsonObject.getString("id"));
                                        startActivity(intent);
                                    }
                                } else if(jsonObject.getInt("serverTypeId")==2) {
                                    if(jsonObject.getInt("vertifyState") == 1) {
                                        Intent intent = new Intent(LoginActivity.this, ExamineActivity.class);
                                        startActivity(intent);
                                    }else if(jsonObject.getInt("vertifyState") ==2 ){
                                        SharedPreferences sp = getSharedPreferences("sp",0);
                                        SharedPreferences.Editor editor = sp.edit();
                                        editor.putString("secretKey",jsonObject.getString("secretKey"));
                                        editor.putString("telephone",ed_phone.getText().toString().trim());
                                        editor.apply();
                                        Gson gson = new Gson();
                                        Acache.get(LoginActivity.this).put("storeInfo",jsonObject);
                                        StoreInfo storeInfo = gson.fromJson(jsonObject.toString(), StoreInfo.class);
                                        CommunityBossApplication.storeInfo = storeInfo;
                                        Intent intent = new Intent(LoginActivity.this,HousekeepMainActivity.class);
                                        startActivity(intent);
                                    } else if(jsonObject.getInt("vertifyState") == 3) {
                                        Intent intent = new Intent(LoginActivity.this, HousekeepApplyActivity.class);
                                        intent.putExtra("id",jsonObject.getString("id"));
                                        startActivity(intent);
                                    }
                                }
                                finish();

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
        if(Urls.IP.equals("")) {
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

                                    if(jsonObject.getInt("serverTypeId")==100) {
                                        Intent intent = new Intent(LoginActivity.this, ChooseIdentityActivity.class);
                                        intent.putExtra("id",jsonObject.getString("id"));
                                        startActivity(intent);
                                    }else if(jsonObject.getInt("serverTypeId")==1) {
                                        if(jsonObject.getInt("vertifyState") == 1) {
                                            Intent intent = new Intent(LoginActivity.this, ExamineActivity.class);
                                            startActivity(intent);
                                        }else if(jsonObject.getInt("vertifyState") ==2 ){
                                            Gson gson = new Gson();
                                            Acache.get(LoginActivity.this).put("storeInfo",jsonObject);
                                            StoreInfo storeInfo = gson.fromJson(jsonObject.toString(), StoreInfo.class);
                                            CommunityBossApplication.storeInfo = storeInfo;
                                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                                            startActivity(intent);
                                        }
                                    } else if(jsonObject.getInt("serverTypeId")==2) {
                                        if(jsonObject.getInt("vertifyState") == 1) {
                                            Intent intent = new Intent(LoginActivity.this, ExamineActivity.class);
                                            startActivity(intent);
                                        }else if(jsonObject.getInt("vertifyState") ==2 ){
                                            Gson gson = new Gson();
                                            Acache.get(LoginActivity.this).put("storeInfo",jsonObject);
                                            StoreInfo storeInfo = gson.fromJson(jsonObject.toString(), StoreInfo.class);
                                            CommunityBossApplication.storeInfo = storeInfo;
                                            Intent intent = new Intent(LoginActivity.this,HousekeepMainActivity.class);
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
    public void sendSmsCode(){
        if(Urls.IP.equals("")) {
            Toast.makeText(mContext, "请输入商户编码", Toast.LENGTH_SHORT).show();
            return;
        }
        if(!ed_phone.getText().toString().trim().equals("")){
            mProgressBar.setVisibility(View.VISIBLE);
            OkGo.<String>post(Urls.getInstance().SENDSMSCODE+"?telephone="+ed_phone.getText().toString().trim())
                    .tag(this)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            mProgressBar.setVisibility(View.GONE);
                            try {
                                String body = response.body();
                                JSONObject json = new JSONObject(body);
                                Log.d("获取验证码",json.toString());
                                if (json.getInt("status") == 200) {
                                    if(time == 60) {
                                        new Thread(new LoginActivity.TimeRun()).start();
                                    }else {
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

    private void createProgressBar(){
        mContext=this;
        //整个Activity布局的最终父布局,参见参考资料
        FrameLayout rootFrameLayout=(FrameLayout) findViewById(android.R.id.content);
        FrameLayout.LayoutParams layoutParams=
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity= Gravity.CENTER;
        mProgressBar=new ProgressBar(mContext);
        mProgressBar.setLayoutParams(layoutParams);
        mProgressBar.setVisibility(View.GONE);
        rootFrameLayout.addView(mProgressBar);
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

    static int index_rx = 0;
    @SuppressLint("CheckResult")
    private void requestPermissions() {
        RxPermissions rxPermission = new RxPermissions(LoginActivity.this);
        rxPermission.requestEach(android.Manifest.permission.ACCESS_FINE_LOCATION,//获取位置
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,//写外部存储器
                android.Manifest.permission.READ_EXTERNAL_STORAGE,//读取外部存储器
//                        Manifest.permission.READ_CALENDAR,//读取日历
//                        Manifest.permission.READ_CALL_LOG,//看电话记录
//                Manifest.permission.READ_CONTACTS,//读取通讯录
//                        Manifest.permission.READ_PHONE_STATE,//读取手机状态
//                        Manifest.permission.READ_SMS,//读取信息 　
//                          Manifest.permission.SEND_SMS,//发信息
//                Manifest.permission.CALL_PHONE,//打电话
                Manifest.permission.CAMERA)//照相机
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            Logger.i("用户已经同意该权限", permission.name + " is granted.");
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            Logger.i("用户拒绝了该权限,没有选中『不再询问』", permission.name + " is denied. More info should be provided.");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            Logger.i("用户拒绝了该权限,并且选中『不再询问』", permission.name + " is denied.");
                        }
                        index_rx++;
                        if (index_rx == 4) {
                            index_rx = 0;
                        }

                    }
                });

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (isShouldHideInput(v, ev)) {
                if(hideInputMethod(this, v)) {
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
            int[] leftTop = { 0, 0 };
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
    public void showPopup(){
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
        SharedPreferences sp = getSharedPreferences("sp",0);
        ed_name.setText(sp.getString("code",""));
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!ed_name.getText().toString().equals("")) {
                    String s = ed_name.getText().toString().trim();
                    getCode(s);
                    popuPhoneW.dismiss();
                }
            }
        });
    }


    /**
     * 根据商户编码获取地址
     */
    public void getCode(final String code){

        OkGo.<String>get(Urls.PARTNER_CODE+code)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {

                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("根据商户编码获取url", json.toString());
                            if (json.getInt("status") == 200) {
                                if(!json.isNull("data")) {
                                    json = json.getJSONObject("data");
                                    SharedPreferences sp = getSharedPreferences("sp", 0);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.putString("ip", json.getString("apiUrl"));
                                    editor.putString("h5_ip", json.getString("h5Url"));
                                    editor.putString("ws", json.getString("websocketUrl"));
                                    editor.putString("c_ip", json.getString("clientInfoUrl"));
                                    Urls.IP = json.getString("apiUrl");
                                    Urls.H5_IP = json.getString("h5Url");
                                    editor.putString("code",code);
                                    Toast.makeText(LoginActivity.this, "切换商户成功", Toast.LENGTH_SHORT).show();
                                    Log.e( "login: ",Urls.IP );
                                    editor.apply();
                                }else {
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

}
