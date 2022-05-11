package net.leelink.communityboss.activity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.core.content.ContextCompat;
import android.os.Bundle;
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
import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
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

public class InformationActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout rl_back;
    private TextView tv_done, tv_close_time, tv_address, tv_province, tv_city, tv_local, tv_organ,tv_town,tv_name_c, tv_open_time, ed_businessNo, tv_file_name0, tv_file_name1, tv_file_name2, tv_file_name3,tv_legal_person,tv_organ_city,tv_organ_area;
    private EditText ed_name, ed_phone, ed_phone_c;
    private ImageView img_store_head, img_publicity, img_license, img_permit;
    private RelativeLayout rl_open_time, rl_close_time;
    private Bitmap bitmap = null;
    private File file0, file1, file2, file3;
    private Button btn_album, btn_photograph;
    private View popview;
    int type;
    private PopupWindow popuPhoneW;
    private TimePickerView pvTime, pvTime1;
    private SimpleDateFormat sdf, sdf1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        init();
        initData();
        popu_head();
        initPickerView();
        initClose();

    }

    public void init() {
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        tv_done = findViewById(R.id.tv_done);
        tv_done.setOnClickListener(this);
        ed_name = findViewById(R.id.ed_name);
        ed_phone = findViewById(R.id.ed_phone);
        tv_address = findViewById(R.id.tv_address);
        tv_close_time = findViewById(R.id.tv_close_time);
        tv_province = findViewById(R.id.tv_province);
        img_store_head = findViewById(R.id.img_store_head);
        img_store_head.setOnClickListener(this);
        img_publicity = findViewById(R.id.img_publicity);
        img_publicity.setOnClickListener(this);
        img_license = findViewById(R.id.img_license);
        img_license.setOnClickListener(this);
        img_permit = findViewById(R.id.img_permit);
        img_permit.setOnClickListener(this);
        tv_city = findViewById(R.id.tv_city);
        tv_local = findViewById(R.id.tv_local);
        tv_town = findViewById(R.id.tv_town);
        tv_organ = findViewById(R.id.tv_organ);
        tv_name_c = findViewById(R.id.tv_name_c);
        ed_phone_c = findViewById(R.id.ed_phone_c);
        ed_businessNo = findViewById(R.id.ed_businessNo);
        tv_open_time = findViewById(R.id.tv_open_time);
        rl_open_time = findViewById(R.id.rl_open_time);
        rl_open_time.setOnClickListener(this);
        rl_close_time = findViewById(R.id.rl_close_time);
        rl_close_time.setOnClickListener(this);
        tv_file_name0 = findViewById(R.id.tv_file_name0);
        tv_file_name1 = findViewById(R.id.tv_file_name1);
        tv_file_name2 = findViewById(R.id.tv_file_name2);
        tv_file_name3 = findViewById(R.id.tv_file_name3);
        tv_town = findViewById(R.id.tv_town);
        tv_legal_person = findViewById(R.id.tv_legal_person);

    }

    public void initData() {

        tv_close_time.setText(CommunityBossApplication.storeInfo.getEndTime());
        tv_name_c.setText(CommunityBossApplication.storeInfo.getContact());
        ed_phone_c.setText(CommunityBossApplication.storeInfo.getContactPhone());
        ed_businessNo.setText(CommunityBossApplication.storeInfo.getBusinessNo());
        if (getIntent().getStringExtra("type").equals("housekeep")) {
            tv_file_name0.setText("营业执照");
            tv_file_name1.setText("组织机构代码");
            tv_file_name2.setText("税务登记证");
            tv_file_name3.setText("门店头像");
        }
        if (CommunityBossApplication.storeInfo.getLicensePath()!= null) {
            Glide.with(this).load(Urls.getInstance().IMG_URL + CommunityBossApplication.storeInfo.getLicensePath()).into(img_license);
        }
        if (CommunityBossApplication.storeInfo.getFoodHealthPath()!= null) {
            Glide.with(this).load(Urls.getInstance().IMG_URL + CommunityBossApplication.storeInfo.getFoodHealthPath()).into(img_permit);
        }

        OkGo.<String>get(Urls.getInstance().STOREHOME)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("获取个人信息", json.toString());
                            if (json.getInt("status") == 200) {
                                json = json.getJSONObject("data");
                                ed_name.setText(json.getString("store_name"));
                                ed_phone.setText(json.getString("order_phone"));
                                String address = json.getString("address_json");
                                tv_legal_person.setText(json.getString("legal_person"));
                                JSONObject jsonObject = new JSONObject(address) ;
                                tv_province.setText(jsonObject.getString("province"));
                                tv_city.setText(jsonObject.getString("city"));
                                tv_local.setText(jsonObject.getString("county"));
                                tv_town.setText(jsonObject.getString("town"));
                                tv_address.setText(jsonObject.getString("address"));
                                String start_time = json.getString("start_time");
                                start_time = start_time.substring(11,16);
                                tv_open_time.setText(start_time);

//                                SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.sssZ", Locale.US);
//                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                                sf.setTimeZone(TimeZone.getTimeZone("UTC"));
//                                Date date = null;
//                                String dateTime = "";
//                                try {
//                                    date = sf.parse(start_time);
//                                    dateTime = sdf.format(date);
//
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//                                dateTime = dateTime.substring(11,16);
//                                tv_open_time.setText(dateTime);
                                String close_time = json.getString("end_time");
                                close_time = close_time.substring(11,16);
                                tv_close_time.setText(close_time);
//                                Date date_close = null;
//                                String dateTime_close = "";
//                                try {
//                                    date_close = sf.parse(close_time);
//                                    dateTime_close = sdf.format(date_close);
//                                } catch (ParseException e) {
//                                    e.printStackTrace();
//                                }
//                                dateTime_close= dateTime_close.substring(11,16);
//                                tv_close_time.setText(dateTime_close);
                                Glide.with(InformationActivity.this).load(Urls.getInstance().IMG_URL + json.getString("regist_path")).into(img_store_head);
                                Glide.with(InformationActivity.this).load(Urls.getInstance().IMG_URL + json.getString("store_font_path")).into(img_publicity);
                                Glide.with(InformationActivity.this).load(Urls.getInstance().IMG_URL + json.getString("license_path")).into(img_license);
                                Glide.with(InformationActivity.this).load(Urls.getInstance().IMG_URL + json.getString("food_health_path")).into(img_permit);


                            } else {
                                Toast.makeText(InformationActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_done:  //编辑
                edit();
                // updateImage();
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
//                popuPhoneW.showAtLocation(img_store_head, Gravity.CENTER, 0, 0);
//                backgroundAlpha(0.5f);
//                type = 2;
                break;

            case R.id.img_permit:   //上传许可证书
//                popuPhoneW.showAtLocation(img_store_head, Gravity.CENTER, 0, 0);
//                backgroundAlpha(0.5f);
//                type = 3;
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
            case R.id.rl_open_time:     //开店时间
                pvTime.show();
                break;
            case R.id.rl_close_time:    //闭店时间
                pvTime1.show();
                break;
            default:
                break;
        }
    }

    //编辑资料
    public void edit() {
        HttpParams params = new HttpParams();

        if (file0 != null) {
            params.put("registfile", file0);
        }
        if (file1 != null) {
            params.put("storeFontfile", file1);
        }
        params.put("storeName", ed_name.getText().toString().trim());
        params.put("orderPhone", ed_phone.getText().toString().trim());
  //      params.put("contactPhone",ed_phone_c.getText().toString().trim());
        Log.e("startTime: ", "2000-01-01 " + tv_open_time.getText().toString() + ":00");
        params.put("startTime", "2000-01-01 " + tv_open_time.getText().toString() + ":00");
        Log.e("endTime: ", "2000-01-01 " + tv_close_time.getText().toString() + ":00");
        params.put("endTime", "2000-01-01 " + tv_close_time.getText().toString() + ":00");
        params.put("id", CommunityBossApplication.storeInfo.getId());
        OkGo.<String>post(Urls.getInstance().UPDATESTOREINGO)
                .tag(this)
                .params(params)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("修改商户信息", json.toString());
                            if (json.getInt("status") == 200) {
                                Toast.makeText(InformationActivity.this, "信息提交成功", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(InformationActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //上传图片
    public void updateImage() {

        if (file0 != null) {
            OkGo.<String>post(Urls.getInstance().UPLOADIMAGE + "?appToken=" + CommunityBossApplication.token + "&type=1")
                    .tag(this)
                    .params("file", file0)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            try {
                                String body = response.body();
                                body = body.substring(1, body.length() - 1);
                                JSONObject json = new JSONObject(body.replaceAll("\\\\", ""));
                                Log.d("上传图片", json.toString());
                                if (json.getInt("ResultCode") == 200) {
                                    Toast.makeText(InformationActivity.this, "信息已提交审核,请重新登录", Toast.LENGTH_SHORT).show();
                                    SharedPreferences sp = getSharedPreferences("sp", 0);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.remove("AppToken");
                                    editor.apply();
                                    Intent intent = new Intent(InformationActivity.this, LoginActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(InformationActivity.this, json.getString("ResultValue"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
        if (file1 != null) {
            OkGo.<String>post(Urls.getInstance().UPLOADIMAGE + "?appToken=" + CommunityBossApplication.token + "&type=2")
                    .tag(this)
                    .params("file", file1)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            try {
                                String body = response.body();
                                body = body.substring(1, body.length() - 1);
                                JSONObject json = new JSONObject(body.replaceAll("\\\\", ""));
                                Log.d("上传图片", json.toString());
                                if (json.getInt("ResultCode") == 200) {
                                } else {
                                    Toast.makeText(InformationActivity.this, json.getString("ResultValue"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
        if (file2 != null) {
            OkGo.<String>post(Urls.getInstance().UPLOADIMAGE + "?appToken=" + CommunityBossApplication.token + "&type=3")
                    .tag(this)
                    .params("file", file2)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            try {
                                String body = response.body();
                                body = body.substring(1, body.length() - 1);
                                JSONObject json = new JSONObject(body.replaceAll("\\\\", ""));
                                Log.d("上传图片", json.toString());
                                if (json.getInt("ResultCode") == 200) {
                                } else {
                                    Toast.makeText(InformationActivity.this, json.getString("ResultValue"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
        if (file3 != null) {
            OkGo.<String>post(Urls.getInstance().UPLOADIMAGE + "?appToken=" + CommunityBossApplication.token + "&type=4")
                    .tag(this)
                    .params("file", file3)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            try {
                                String body = response.body();
                                body = body.substring(1, body.length() - 1);
                                JSONObject json = new JSONObject(body.replaceAll("\\\\", ""));
                                Log.d("上传图片", json.toString());
                                if (json.getInt("ResultCode") == 200) {
                                } else {
                                    Toast.makeText(InformationActivity.this, json.getString("ResultValue"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case 1:     //相册
                    Uri uri = data.getData();
                    bitmap = BitmapCompress.decodeUriBitmap(InformationActivity.this, uri);
                    switch (type) {
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
                case 2:     //拍照
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        bitmap = (Bitmap) bundle.get("data");
                        switch (type) {
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
        popview = LayoutInflater.from(InformationActivity.this).inflate(R.layout.popu_head, null);
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
        popuPhoneW.setOnDismissListener(new InformationActivity.poponDismissListener());
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

    private void initClose() {

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
