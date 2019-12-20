package net.leelink.communityboss.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
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
import java.text.SimpleDateFormat;
import java.util.Date;

public class ApplyActivity extends BaseActivity implements View.OnClickListener {
private Button btn_submit;
private EditText ed_name,ed_phone,ed_address;
private RelativeLayout rl_open_time,rl_close_time,rl_back;
private TextView tv_open_time,tv_close_time;
private ImageView img_store_head,img_publicity,img_license,img_permit;
    private PopupWindow popuPhoneW;
    private TimePickerView pvTime,pvTime1;
    private SimpleDateFormat sdf,sdf1;
    private Bitmap bitmap = null;
    private File file0,file1,file2,file3;
    private Button btn_album, btn_photograph;
    private View popview;
    int type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
        init();
        initPickerView();
        initClose();
        popu_head();
    }

    public void init(){
        btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        ed_name = findViewById(R.id.ed_name);
        ed_phone = findViewById(R.id.ed_phone);
        ed_address = findViewById(R.id.ed_address);
        rl_open_time = findViewById(R.id.rl_open_time);
        rl_open_time.setOnClickListener(this);
        rl_close_time = findViewById(R.id.rl_close_time);
        rl_close_time.setOnClickListener(this);
        tv_open_time = findViewById(R.id.tv_open_time);
        tv_close_time = findViewById(R.id.tv_close_time);
        img_store_head = findViewById(R.id.img_store_head);
        img_store_head.setOnClickListener(this);
        img_publicity = findViewById(R.id.img_publicity);
        img_publicity.setOnClickListener(this);
        img_license = findViewById(R.id.img_license);
        img_license.setOnClickListener(this);
        img_permit = findViewById(R.id.img_permit);
        img_permit.setOnClickListener(this);
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:   //提交审核
                submit();
                break;
            case R.id.rl_open_time: //开店时间
                pvTime.show();
                break;
            case R.id.rl_close_time:    //关店时间
                pvTime1.show();
                break;
            case R.id.img_store_head:   //上传商店头像
                popuPhoneW.showAtLocation(img_store_head, Gravity.CENTER, 0, 0);
                backgroundAlpha(0.5f);
                type = 0;
                break;

            case R.id.img_publicity:    //上传宣传图片
                popuPhoneW.showAtLocation(img_store_head, Gravity.CENTER, 0, 0);
                backgroundAlpha(0.5f);
                type = 1;
                break;
            case R.id.img_license:  //上传执照图片
                popuPhoneW.showAtLocation(img_store_head, Gravity.CENTER, 0, 0);
                backgroundAlpha(0.5f);
                type = 2;
                break;

            case R.id.img_permit:   //上传许可证书
                popuPhoneW.showAtLocation(img_store_head, Gravity.CENTER, 0, 0);
                backgroundAlpha(0.5f);
                type = 3;
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
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                startActivityForResult(intent1, 1);
                break;
            case R.id.rl_back:
                finish();
                break;
                default:
                    break;
        }
    }

    //提交审核
    public void submit(){
        if(ed_address.getText().toString().trim()!=null || !ed_address.getText().toString().trim().equals("")) {
            if (ed_name.getText().toString().trim()!=null || !ed_name.getText().toString().trim().equals("")) {
                if (ed_phone.getText().toString().trim()!=null || !ed_phone.getText().toString().trim().equals("")) {
                    if (tv_open_time.getText().toString()!=null || !tv_open_time.getText().toString().equals("")){
                        if (tv_close_time.getText().toString()!=null || !tv_close_time.getText().toString().equals("")){
                            if(file0!=null){
                                if(file1!=null){
                                    if(file2!=null){
                                        if(file3!=null){
                                            storeInfo();

                                        }else {
                                            Toast.makeText(this, "请上传食品流通许可", Toast.LENGTH_SHORT).show();
                                        }
                                    }else {
                                        Toast.makeText(this, "请上传营业执照", Toast.LENGTH_SHORT).show();
                                    }
                                }else {
                                    Toast.makeText(this, "请上传宣传图片", Toast.LENGTH_SHORT).show();
                                }
                            }else {
                                Toast.makeText(this, "请上传商家头像", Toast.LENGTH_SHORT).show();
                            }
                        }else {
                            Toast.makeText(this, "请选择闭店时间", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        Toast.makeText(this, "请选择开店时间", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(this, "电话号码不能为空", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(this, "名称不能为空", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "请输入正确的地址", Toast.LENGTH_SHORT).show();
        }
    }


    //修改商户信息
    public void storeInfo(){
        OkGo.<String>post(Urls.STOREINFO+"?appToken="+CommunityBossApplication.token)
                .tag(this)
                .params("address", ed_address.getText().toString().trim())
                .params("name",ed_name.getText().toString().trim())
                .params("phoneNumber",ed_phone.getText().toString().trim())
                .params("time",tv_open_time.getText().toString()+"-"+tv_close_time.getText().toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            body = body.substring(1,body.length()-1);
                            JSONObject json = new JSONObject(body.replaceAll("\\\\",""));
                            Log.d("修改商户信息",json.toString());
                            if (json.getInt("ResultCode") == 200) {
                                updateImage();
                            } else {
                                Toast.makeText(ApplyActivity.this, json.getString("ResultValue"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //上传图片
    public void updateImage(){
        OkGo.<String>post(Urls.UPLOADIMAGE+"?appToken="+ CommunityBossApplication.token+"&type=1")
                .tag(this)
                .params("file", file0)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            body = body.substring(1,body.length()-1);
                            JSONObject json = new JSONObject(body.replaceAll("\\\\",""));
                            Log.d("上传图片",json.toString());
                            if (json.getInt("ResultCode") == 200) {

                            } else {
                                Toast.makeText(ApplyActivity.this, json.getString("ResultValue"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        OkGo.<String>post(Urls.UPLOADIMAGE+"?appToken="+ CommunityBossApplication.token+"&type=2")
                .tag(this)
                .params("file", file1)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            body = body.substring(1,body.length()-1);
                            JSONObject json = new JSONObject(body.replaceAll("\\\\",""));
                            Log.d("上传图片",json.toString());
                            if (json.getInt("ResultCode") == 200) {
                            } else {
                                Toast.makeText(ApplyActivity.this, json.getString("ResultValue"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        OkGo.<String>post(Urls.UPLOADIMAGE+"?appToken="+ CommunityBossApplication.token+"&type=3")
                .tag(this)
                .params("file", file2)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            body = body.substring(1,body.length()-1);
                            JSONObject json = new JSONObject(body.replaceAll("\\\\",""));
                            Log.d("上传图片",json.toString());
                            if (json.getInt("ResultCode") == 200) {
                            } else {
                                Toast.makeText(ApplyActivity.this, json.getString("ResultValue"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
        OkGo.<String>post(Urls.UPLOADIMAGE+"?appToken="+ CommunityBossApplication.token+"&type=4")
                .tag(this)
                .params("file", file3)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            body = body.substring(1,body.length()-1);
                            JSONObject json = new JSONObject(body.replaceAll("\\\\",""));
                            Log.d("上传图片",json.toString());
                            if (json.getInt("ResultCode") == 200) {
                                Toast.makeText(ApplyActivity.this, "信息提交成功,请等待审核", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ApplyActivity.this,ExamineActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(ApplyActivity.this, json.getString("ResultValue"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case 1:
                    Uri uri = data.getData();
                    bitmap = BitmapCompress.decodeUriBitmap(ApplyActivity.this, uri);
                    switch (type){
                        case 0:
                            img_store_head.setImageBitmap(bitmap);
                            file0 = BitmapCompress.compressImage(bitmap);
                            break;
                        case 1:
                            img_publicity.setImageBitmap(bitmap);
                            file1 = BitmapCompress.compressImage(bitmap);
                            break;
                        case 2:
                            img_license.setImageBitmap(bitmap);
                            file2 = BitmapCompress.compressImage(bitmap);
                            break;
                        case 3:
                            img_permit.setImageBitmap(bitmap);
                            file3 = BitmapCompress.compressImage(bitmap);
                            break;
                        default:
                            break;
                    }
                    break;
                case 2:
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        bitmap = (Bitmap) bundle.get("data");
                        switch (type){
                            case 0:
                                img_store_head.setImageBitmap(bitmap);
                                file0 = BitmapCompress.compressImage(bitmap);
                                break;
                            case 1:
                                img_publicity.setImageBitmap(bitmap);
                                file1 = BitmapCompress.compressImage(bitmap);
                                break;
                            case 2:
                                img_license.setImageBitmap(bitmap);
                                file2 = BitmapCompress.compressImage(bitmap);
                                break;
                            case 3:
                                img_permit.setImageBitmap(bitmap);
                                file3 = BitmapCompress.compressImage(bitmap);
                                break;
                            default:
                                break;
                        }
                    }
                    break;

                default:
                    break;
            }
        }
    }


    //获取图片
    @SuppressLint("WrongConstant")
    private void popu_head() {
        // TODO Auto-generated method stub
        popview = LayoutInflater.from(ApplyActivity.this).inflate(R.layout.popu_head, null);
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
        popuPhoneW.setOnDismissListener(new poponDismissListener());
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

    private void initPickerView() {
        boolean[] type = {false, false, false, true, true, false};
        sdf = new SimpleDateFormat("HH:mm");
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tv_open_time.setText(sdf.format(date));
            }
        }).setType(type).build();
    }
    private void initClose(){

        boolean[] type = {false, false, false, true, true, false};
        sdf1 = new SimpleDateFormat("HH:mm");
        pvTime1 = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tv_close_time.setText(sdf1.format(date));
            }
        }).setType(type).build();

    }
}
