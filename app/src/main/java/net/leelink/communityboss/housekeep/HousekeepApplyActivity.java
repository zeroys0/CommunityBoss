package net.leelink.communityboss.housekeep;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.R;
import net.leelink.communityboss.activity.ApplyActivity;
import net.leelink.communityboss.activity.BaseActivity;
import net.leelink.communityboss.bean.OrganBean;
import net.leelink.communityboss.city.CityPicker;
import net.leelink.communityboss.city.Cityinfo;
import net.leelink.communityboss.utils.BitmapCompress;
import net.leelink.communityboss.utils.FileUtil;
import net.leelink.communityboss.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class HousekeepApplyActivity extends BaseActivity implements View.OnClickListener {
    private Button btn_submit;
    private EditText ed_name, ed_address, ed_number, ed_name_c, ed_phone_c,ed_server_address,ed_name_l;
    private RelativeLayout  rl_open_time,rl_close_time, rl_back, rl_province, rl_city, rl_local, rl_organ, rl_province_s, rl_city_s, rl_local_s;
    private TextView tv_open_time, tv_close_time, tv_province, tv_city, tv_local, tv_organ, tv_province_s, tv_city_s, tv_local_s;
    private ImageView img_store_head, img_publicity, img_license, img_permit;
    private PopupWindow popuPhoneW;
    private TimePickerView pvTime, pvTime1;
    private SimpleDateFormat sdf, sdf1;
    private Bitmap bitmap = null;
    private File file0, file1, file2, file3;
    private Button btn_album, btn_photograph;
    private View popview;
    List<String> province = new ArrayList<>();
    List<String> city = new ArrayList<>();
    List<String> local = new ArrayList<>();
    private List<Cityinfo> province_list = new ArrayList<Cityinfo>();
    private HashMap<String, List<Cityinfo>> city_map = new HashMap<String, List<Cityinfo>>();
    private HashMap<String, List<Cityinfo>> couny_map = new HashMap<String, List<Cityinfo>>();
    String province_id, city_id, local_id, province_id_s, city_id_s, local_id_s;
    int organ_id, nature;
    int type;
    private boolean ORGAN = true;
    private boolean STORE = false;
    ProgressBar mProgressBar;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_housekeep_apply);
        init();
        initPickerView();
        initClose();
        popu_head();
        createProgressBar();
    }

    public void init() {
        btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        ed_name = findViewById(R.id.ed_name);
        ed_address = findViewById(R.id.ed_address);
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
        rl_province = findViewById(R.id.rl_province);
        rl_province.setOnClickListener(this);
        rl_city = findViewById(R.id.rl_city);
        rl_city.setOnClickListener(this);
        rl_local = findViewById(R.id.rl_local);
        rl_local.setOnClickListener(this);
        rl_organ = findViewById(R.id.rl_organ);
        rl_organ.setOnClickListener(this);
        tv_province = findViewById(R.id.tv_province);
        tv_city = findViewById(R.id.tv_city);
        tv_local = findViewById(R.id.tv_local);
        tv_organ = findViewById(R.id.tv_organ);
        rl_province_s = findViewById(R.id.rl_province_s);
        rl_province_s.setOnClickListener(this);
        rl_city_s = findViewById(R.id.rl_city_s);
        rl_city_s.setOnClickListener(this);
        rl_local_s = findViewById(R.id.rl_local_s);
        rl_local_s.setOnClickListener(this);
        rl_open_time = findViewById(R.id.rl_open_time);
        rl_open_time.setOnClickListener(this);
        rl_close_time = findViewById(R.id.rl_close_time);
        rl_close_time.setOnClickListener(this);
        tv_open_time = findViewById(R.id.tv_open_time);
        tv_close_time = findViewById(R.id.tv_close_time);
        tv_province_s = findViewById(R.id.tv_province_s);
        tv_city_s = findViewById(R.id.tv_city_s);
        tv_local_s = findViewById(R.id.tv_local_s);
        ed_number = findViewById(R.id.ed_number);
        ed_name_c = findViewById(R.id.ed_name_c);
        ed_phone_c = findViewById(R.id.ed_phone_c);
        ed_server_address = findViewById(R.id.ed_server_address);
        ed_name_l = findViewById(R.id.ed_name_l);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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
            case R.id.rl_province:
                province(ORGAN);
                break;
            case R.id.rl_city:
                if (province_id != null) {
                    city(ORGAN);
                } else {
                    Toast.makeText(this, "请先选择省份", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rl_local:
                if (city_id != null) {
                    local(ORGAN);
                } else {
                    Toast.makeText(this, "请先选择城市", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rl_organ:
                if (local_id != null) {
                    organ();
                } else {
                    Toast.makeText(this, "请先选择地区", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rl_province_s:

                province(STORE);
                break;
            case R.id.rl_city_s:
                if (province_id_s != null) {
                    city(STORE);
                } else {
                    Toast.makeText(this, "请先选择省份", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rl_local_s:
                if(city_id_s != null) {
                    local(STORE);
                }else {
                    Toast.makeText(this, "请先选择城市", Toast.LENGTH_SHORT).show();
                }
                break;

            default:
                break;
        }
    }

    //提交审核
    public void submit() {
        if (ed_address.getText().toString().trim() != null || !ed_address.getText().toString().trim().equals("")) {
            if (ed_name.getText().toString().trim() != null || !ed_name.getText().toString().trim().equals("")) {
                    if (tv_open_time.getText().toString() != null || !tv_open_time.getText().toString().equals("")) {
                        if (tv_close_time.getText().toString() != null || !tv_close_time.getText().toString().equals("")) {
                            if (file0 != null) {
                                if (file1 != null) {
                                    if (file2 != null) {
                                        if (file3 != null) {
                                            storeInfo();

                                        } else {
                                            Toast.makeText(this, "请上传食品流通许可", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Toast.makeText(this, "请上传营业执照", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Toast.makeText(this, "请上传宣传图片", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(this, "请上传商家头像", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "请选择闭店时间", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(this, "请选择开店时间", Toast.LENGTH_SHORT).show();
                    }

            } else {
                Toast.makeText(this, "名称不能为空", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "请输入正确的地址", Toast.LENGTH_SHORT).show();
        }
    }


    //修改商户信息
    public void storeInfo() {
        Log.e( "address: ",ed_address.getText().toString().trim() );
        Log.e( "areaId: ",local_id_s );
        Log.e( "businessNo: ", ed_number.getText().toString().trim() );
        Log.e( "cityId: ",city_id_s );
        Log.e( "contact: ",ed_name_c.getText().toString().trim() );
        Log.e( "orderPhone: ",ed_phone_c.getText().toString().trim() );
        Log.e( "deviceToken: ", JPushInterface.getRegistrationID(this) );
        Log.e( "endTime: ","2000-01-01 "+tv_close_time.getText().toString()+":00");
        Log.e( "startTime: ","2000-01-01 "+tv_open_time.getText().toString()+":00" );
        Log.e( "organId: ",organ_id+"" );
        Log.e( "provinceId: ",province_id_s );
        Log.e( "storeName: ", ed_name.getText().toString().trim() );
        Log.e( "serverTypeId: ","2" );
        Log.e( "id: ",getIntent().getStringExtra("id") );
        Log.e( "legalPerson: ",ed_name_l.getText().toString().trim() );

        mProgressBar.setVisibility(View.VISIBLE);
        OkGo.<String>post(Urls.REGISTER)
                .tag(this)
                .params("address", ed_address.getText().toString().trim())
                .params("areaId", local_id_s)
                .params("businessNo", ed_number.getText().toString().trim())
                .params("cityId", city_id_s)
                .params("contact", ed_name_c.getText().toString().trim())
                .params("orderPhone", ed_phone_c.getText().toString().trim())
                .params("deviceToken", JPushInterface.getRegistrationID(this))
                .params("endTime", "2000-01-01 "+tv_close_time.getText().toString()+":00")
                .params("startTime", "2000-01-01 "+tv_open_time.getText().toString()+":00")
                .params("healthfile", file1)
                .params("licensefile", file0)
                .params("organId", organ_id)
                .params("provinceId", province_id_s)
                .params("registfile", file3)
                .params("storeFontfile", file2)
                .params("storeName", ed_name.getText().toString().trim())
                .params("serverTypeId",2)
                .params("serverAddress",ed_server_address.getText().toString().trim())
                .params("legalPerson",ed_name_l.getText().toString().trim())
                .params("id",getIntent().getStringExtra("id"))

                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("修改商户信息", json.toString());
                            if (json.getInt("status") == 200) {
                                mProgressBar.setVisibility(View.GONE);
                                Toast.makeText(HousekeepApplyActivity.this, "提交成功,请等待审核", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(HousekeepApplyActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


    CityPicker.JSONParser parser = new CityPicker.JSONParser();

    //选择省份
    public void province(final boolean organ) {
        province.clear();

        String area_str = FileUtil.readAssets(this, "area.json");
        province_list = parser.getJSONParserResult(area_str, "area0");
        city_map = parser.getJSONParserResultArray(area_str, "area1");
        couny_map = parser.getJSONParserResultArray(area_str, "area2");
        for (Cityinfo cityinfo : province_list) {
            province.add(cityinfo.getCity_name());
        }
        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(HousekeepApplyActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                if (organ) {
                    tv_province.setText(province.get(options1));
                    province_id = province_list.get(options1).getId();
                } else {
                    tv_province_s.setText(province.get(options1));
                    province_id_s = province_list.get(options1).getId();

                }
            }
        })
                .setDividerColor(Color.parseColor("#A0A0A0"))
                .setTextColorCenter(Color.parseColor("#333333")) //设置选中项文字颜色
                .setContentTextSize(18)//设置滚轮文字大小
                .setOutSideCancelable(true)//点击外部dismiss default true
                .build();
        pvOptions.setPicker(province);
        pvOptions.show();
    }

    //城市选择
    public void city(final boolean organ) {
        city.clear();
        final List<Cityinfo> cityinfoList;
        if (organ) {
            cityinfoList = city_map.get(province_id);
        } else {
            cityinfoList = city_map.get(province_id_s);
        }
        for (Cityinfo cityinfo : cityinfoList) {
            city.add(cityinfo.getCity_name());
        }
        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(HousekeepApplyActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                if (organ) {
                    tv_city.setText(city.get(options1));
                    city_id = cityinfoList.get(options1).getId();
                } else {
                    tv_city_s.setText(city.get(options1));
                    city_id_s = cityinfoList.get(options1).getId();
                }
            }
        })
                .setDividerColor(Color.parseColor("#A0A0A0"))
                .setTextColorCenter(Color.parseColor("#333333")) //设置选中项文字颜色
                .setContentTextSize(18)//设置滚轮文字大小
                .setOutSideCancelable(true)//点击外部dismiss default true
                .build();
        pvOptions.setPicker(city);
        pvOptions.show();
    }

    //地区选择
    public void local(final boolean organ) {
        local.clear();
        final List<Cityinfo> cityinfoList;
        if (organ) {
            cityinfoList = couny_map.get(city_id);
        } else {
            cityinfoList = couny_map.get(city_id_s);
        }
        for (Cityinfo cityinfo : cityinfoList) {
            local.add(cityinfo.getCity_name());
        }
        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(HousekeepApplyActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                if (organ) {
                    tv_local.setText(local.get(options1));
                    local_id = cityinfoList.get(options1).getId();
                } else {
                    tv_local_s.setText(local.get(options1));
                    local_id_s = cityinfoList.get(options1).getId();
                }
            }
        })
                .setDividerColor(Color.parseColor("#A0A0A0"))
                .setTextColorCenter(Color.parseColor("#333333")) //设置选中项文字颜色
                .setContentTextSize(18)//设置滚轮文字大小
                .setOutSideCancelable(true)//点击外部dismiss default true
                .build();
        pvOptions.setPicker(local);
        pvOptions.show();
    }

    //选择地区机构
    public void organ() {
        OkGo.<String>get("http://221.238.204.114:8888/sh/user/organ")
                .tag(this)
                .params("areaId", local_id)
                //      .params("deviceToken", JPushInterface.getRegistrationID(LoginActivity.this))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("查询机构", json.toString());
                            if (json.getInt("status") == 200) {
                                JSONArray jsonArray = json.getJSONArray("data");
                                Gson gson = new Gson();
                                List<OrganBean> list = gson.fromJson(jsonArray.toString(), new TypeToken<List<OrganBean>>() {
                                }.getType());
                                showOrgan(list);
                            } else {
                                Toast.makeText(HousekeepApplyActivity.this, json.getString("ResultValue"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //弹出机构列表
    public void showOrgan(final List<OrganBean> list) {
        List<String> organName = new ArrayList<>();
        for (OrganBean organBean : list) {
            organName.add(organBean.getOrganName());
        }
        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(HousekeepApplyActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                if (list.size() != 0) {
                    tv_organ.setText(list.get(options1).getOrganName());
                    organ_id = list.get(options1).getId();
                }
            }
        })
                .setDividerColor(Color.parseColor("#A0A0A0"))
                .setTextColorCenter(Color.parseColor("#333333")) //设置选中项文字颜色
                .setContentTextSize(18)//设置滚轮文字大小
                .setOutSideCancelable(true)//点击外部dismiss default true
                .build();
        pvOptions.setPicker(organName);
        pvOptions.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            switch (requestCode) {
                case 1:
                    Uri uri = data.getData();
                    bitmap = BitmapCompress.decodeUriBitmap(HousekeepApplyActivity.this, uri);
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
                case 2:
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
        popview = LayoutInflater.from(HousekeepApplyActivity.this).inflate(R.layout.popu_head, null);
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
        popuPhoneW.setOnDismissListener(new HousekeepApplyActivity.poponDismissListener());
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
