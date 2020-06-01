package net.leelink.communityboss.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.R;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.utils.BitmapCompress;
import net.leelink.communityboss.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class SettingActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout rl_back, rl_logout, rl_change_password;
    private ImageView img_head, img_change;
    private TextView tv_name, tv_phone;
    private PopupWindow popuPhoneW;
    private View popview;
    private Button btn_album, btn_photograph;
    private Bitmap bitmap = null;
    private File file;
    private ProgressBar mProgressBar;
    private Context mContext = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
        popu_head();
        createProgressBar();
    }

    public void init() {
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        rl_logout = findViewById(R.id.rl_logout);
        rl_logout.setOnClickListener(this);
        img_head = findViewById(R.id.img_head);
        img_head.setOnClickListener(this);
        img_change = findViewById(R.id.img_change);
        img_change.setOnClickListener(this);
        tv_name = findViewById(R.id.tv_name);
        tv_phone = findViewById(R.id.tv_phone);
        if (CommunityBossApplication.storeInfo.getStoreImg() != null) {
            Glide.with(this).load(Urls.IMG_URL + CommunityBossApplication.storeInfo.getStoreImg()).into(img_head);
        }
        tv_name.setText(CommunityBossApplication.storeInfo.getStoreName());
        tv_phone.setText(CommunityBossApplication.storeInfo.getTelephone());
        rl_change_password = findViewById(R.id.rl_change_password);
        rl_change_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_logout:
                logout();
                break;
            case R.id.img_change:       //修改绑定电话
                Intent intent = new Intent(this, ChangePhoneActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_change_password:
                Intent intent1 = new Intent(this, ChangePasswordActivity.class);
                startActivity(intent1);
                break;
            case R.id.img_head:     //修改头像
                popuPhoneW.showAtLocation(img_head, Gravity.CENTER, 0, 0);
                backgroundAlpha(0.5f);
                break;
            case R.id.btn_photograph://拍照
                popuPhoneW.dismiss();
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    //当拒绝了授权后，为提升用户体验，可以以弹窗的方式引导用户到设置中去进行设置
                    new AlertDialog.Builder(this)
                            .setMessage("需要开启权限才能使用此功能")
                            .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    //引导用户到设置中去进行设置
                                    Intent intent = new Intent();
                                    intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                                    intent.setData(Uri.fromParts("package", getPackageName(), null));
                                    startActivity(intent);

                                }
                            }).setNegativeButton("取消", null).create().show();
                } else {
                    Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent2, 2);
                }
                break;
            case R.id.btn_album://相册
                popuPhoneW.dismiss();
                Intent intent2 = new Intent(Intent.ACTION_PICK);
                intent2.setType("image/*");
                startActivityForResult(intent2, 1);
                break;
            default:
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case 1:
                    Uri uri = data.getData();
                    bitmap = BitmapCompress.decodeUriBitmap(SettingActivity.this, uri);
                    img_head.setImageBitmap(bitmap);
                    file = BitmapCompress.compressImage(bitmap);
                    if(file!=null){
                        upLoadImg();
                    }

                    break;
                case 2:
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        bitmap = (Bitmap) bundle.get("data");
                        img_head.setImageBitmap(bitmap);
                        file = BitmapCompress.compressImage(bitmap);
                        if(file!=null){
                            upLoadImg();
                        }
                    }
                    break;

                default:
                    break;
            }
        }
    }

    public void upLoadImg(){
        mProgressBar.setVisibility(View.VISIBLE);
        OkGo.<String>post(Urls.UPLOADIMAGE)
                .params("file",file)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        mProgressBar.setVisibility(View.GONE);
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("修改头像",json.toString());
                            if (json.getInt("status") == 200) {
                                Toast.makeText(SettingActivity.this, "修改头像成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(SettingActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void logout() {
        SharedPreferences sp = getSharedPreferences("sp", 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("AppToken");
        editor.apply();
        Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        OkGo.<String>get(Urls.LOGOUT + "?appToken=" + CommunityBossApplication.token)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            body = body.substring(1, body.length() - 1);
                            JSONObject json = new JSONObject(body.replaceAll("\\\\", ""));
                            Log.d("退出登录", json.toString());
                            if (json.getInt("ResultCode") == 200) {

                                finish();
                            } else {

                            }
                            Toast.makeText(SettingActivity.this, "账号注销,请重新登录", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //获取图片
    @SuppressLint("WrongConstant")
    private void popu_head() {
        // TODO Auto-generated method stub
        popview = LayoutInflater.from(SettingActivity.this).inflate(R.layout.popu_head, null);
        btn_album = (Button) popview.findViewById(R.id.btn_album);
        btn_photograph = (Button) popview.findViewById(R.id.btn_photograph);
        btn_album.setOnClickListener(this);
        btn_photograph.setOnClickListener(this);
        popuPhoneW = new PopupWindow(popview,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popuPhoneW.setFocusable(true);
        popuPhoneW.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popuPhoneW.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popuPhoneW.setOutsideTouchable(true);
        popuPhoneW.setBackgroundDrawable(new BitmapDrawable());
        popuPhoneW.setOnDismissListener(new SettingActivity.poponDismissListener());
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
}
