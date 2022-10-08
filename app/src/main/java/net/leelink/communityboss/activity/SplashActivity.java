package net.leelink.communityboss.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.MainActivity;
import net.leelink.communityboss.R;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.utils.Urls;
import net.leelink.communityboss.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;


public class SplashActivity extends BaseActivity implements View.OnClickListener {
    SharedPreferences sp;
    private ImageView img_back;
    private PopupWindow popuPhoneW;
    private View popview;
    TextView tv_cancel, tv_confirm, tv_agreement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        gotoLogin();
     //   checkVersion();
        img_back = findViewById(R.id.img_back);
    }


    @Override
    protected void onResume() {
        super.onResume();
        popu_head();
        sp = getSharedPreferences("sp", 0);
    }

    private void gotoLogin() {
        // TODO Auto-generated method stub
        myHandler.sendEmptyMessageDelayed(0, 1500);
    }

    private void login() {
        // TODO Auto-generated method stub
        myHandler.sendEmptyMessageDelayed(0, 0);
    }

    @SuppressLint("HandlerLeak")
    private Handler myHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            SharedPreferences sp = getSharedPreferences("sp", 0);
            boolean agreement = sp.getBoolean("agreement", false);
            if (agreement) {
                CommunityBossApplication app = (CommunityBossApplication) getApplication();
                app.initSdk();
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                popuPhoneW.showAtLocation(img_back, Gravity.CENTER, 0, 0);
                backgroundAlpha(0.5f);
            }

        }
    };

    //获取图片
    @SuppressLint("WrongConstant")
    private void popu_head() {
        // TODO Auto-generated method stub
        popview = LayoutInflater.from(SplashActivity.this).inflate(R.layout.popu_rule, null);
        popuPhoneW = new PopupWindow(popview,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        tv_cancel = popview.findViewById(R.id.tv_cancel);
        tv_cancel.setOnClickListener(SplashActivity.this);
        tv_confirm = popview.findViewById(R.id.tv_confirm);
        tv_confirm.setOnClickListener(SplashActivity.this);
        tv_agreement = popview.findViewById(R.id.tv_agreement);
        String text1 = getResources().getString(R.string.agreement);
        SpannableString spannableString1 = new SpannableString(text1);
        spannableString1.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashActivity.this, WebActivity.class);
                intent.putExtra("type", "distribution");
                intent.putExtra("url", "https://www.llky.net.cn/store/protocol.html");
                startActivity(intent);
            }
        }, 93, 99, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString1.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SplashActivity.this, WebActivity.class);
                intent.putExtra("type", "distribution");
                intent.putExtra("url", "https://www.llky.net.cn/store/privacyPolicy.html");
                startActivity(intent);

            }
        }, 100, 106, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_agreement.setText(spannableString1);
        tv_agreement.setMovementMethod(LinkMovementMethod.getInstance());
        popuPhoneW.setFocusable(false);
        popuPhoneW.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popuPhoneW.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popuPhoneW.setOutsideTouchable(false);
        popuPhoneW.setBackgroundDrawable(new BitmapDrawable());
        popuPhoneW.setOnDismissListener(new poponDismissListener());
    }

    @Override
    public void onClick(View v) {
        CommunityBossApplication app = (CommunityBossApplication) getApplication();
        switch (v.getId()) {
            case R.id.tv_cancel:    //不同意
                app.exit();
                break;
            case R.id.tv_confirm:   //同意条款
                //是否已登录
                SharedPreferences.Editor editor = sp.edit();
                editor.putBoolean("agreement", true);
                editor.apply();
                login();
                break;
            default:
                break;
        }
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

    public void checkVersion() {
        OkGo.<String>get(Urls.VERSION)
                .tag(this)
                .params("appName", "乐聆商家版")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("获取版本信息", json.toString());
                            if (json.getInt("status") == 200) {
                                json = json.getJSONObject("data");
                                if(Utils.getVersionCode(SplashActivity.this)<json.getInt("version")) {
                                    AllenVersionChecker
                                            .getInstance()
                                            .downloadOnly(
                                                    UIData.create().setDownloadUrl(json.getString("apkUrl"))
                                                            .setTitle("检测到新的版本")
                                                            .setContent("检测到您当前不是最新版本,是否要更新?")
                                            )
                                            .executeMission(SplashActivity.this);
                                }
                            } else {
                                Toast.makeText(SplashActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


    }

}
