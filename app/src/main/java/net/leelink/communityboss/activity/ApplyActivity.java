package net.leelink.communityboss.activity;

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
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.core.content.ContextCompat;

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
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import net.leelink.communityboss.R;
import net.leelink.communityboss.bean.OrganBean;
import net.leelink.communityboss.bean.StreetBean;
import net.leelink.communityboss.city.CityPicker;
import net.leelink.communityboss.city.Cityinfo;
import net.leelink.communityboss.utils.BitmapCompress;
import net.leelink.communityboss.utils.FileUtil;
import net.leelink.communityboss.utils.Logger;
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
import io.reactivex.functions.Consumer;

public class ApplyActivity extends BaseActivity implements View.OnClickListener {
    private Button btn_submit;
    private EditText ed_name, ed_phone, ed_address, ed_number, ed_name_c, ed_phone_c, ed_server_address, ed_legal_person;
    private RelativeLayout rl_open_time, rl_close_time, rl_back, rl_province, rl_city, rl_local, rl_organ, rl_province_s, rl_city_s, rl_local_s, rl_street;
    private TextView tv_open_time, tv_close_time, tv_province, tv_city, tv_local, tv_organ, tv_province_s, tv_city_s, tv_local_s, tv_street;
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
    String province_id, city_id, local_id, province_id_s, city_id_s, local_id_s, town_id; //街道ID;
    int organ_id, nature;
    int type;
    private boolean ORGAN = true;
    private boolean STORE = false;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
        mContext = this;
        createProgressBar(mContext);
        init();
        initPickerView();
        initClose();
        popu_head();

    }

    public void init() {
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
        tv_province_s = findViewById(R.id.tv_province_s);
        tv_city_s = findViewById(R.id.tv_city_s);
        tv_local_s = findViewById(R.id.tv_local_s);
        ed_number = findViewById(R.id.ed_number);
        ed_name_c = findViewById(R.id.ed_name_c);
        ed_phone_c = findViewById(R.id.ed_phone_c);
        rl_street = findViewById(R.id.rl_street);
        rl_street.setOnClickListener(this);
        tv_street = findViewById(R.id.tv_street);
        ed_server_address = findViewById(R.id.ed_server_address);
        ed_legal_person = findViewById(R.id.ed_legal_person);

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
                requestPermissions();
                type = 0;
                break;

            case R.id.img_publicity:    //上传宣传图片
                requestPermissions();
                type = 1;
                break;
            case R.id.img_license:  //上传执照图片
                requestPermissions();
                type = 2;
                break;

            case R.id.img_permit:   //上传许可证书
                requestPermissions();
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
                if (city_id_s != null) {
                    local(STORE);
                } else {
                    Toast.makeText(this, "请先选择城市", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rl_street:
                if (local_id_s != null) {
                    street();
                } else {
                    Toast.makeText(this, "请先选择区/县", Toast.LENGTH_SHORT).show();
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
                if (ed_phone.getText().toString().trim() != null || !ed_phone.getText().toString().trim().equals("")) {
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
    public void storeInfo() {

        JSONObject json_address = new JSONObject();

        try {
            json_address.put("province", tv_province_s.getText().toString());
            json_address.put("city", tv_city_s.getText().toString());
            json_address.put("countyId", local_id_s);
            json_address.put("county", tv_local_s.getText().toString());
            json_address.put("townId", town_id);
            json_address.put("cityId", city_id_s);
            json_address.put("provinceId", province_id_s);
            json_address.put("town", tv_street.getText().toString());
            json_address.put("address", ed_address.getText().toString());
            json_address.put("fullAddress", tv_province_s.getText().toString() + tv_city_s.getText().toString() + tv_local_s.getText().toString() + tv_street.getText().toString() + ed_address.getText().toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.e("address: ", ed_address.getText().toString().trim());
        Log.e("areaId: ", local_id_s);
        Log.e("businessNo: ", ed_number.getText().toString().trim());
        Log.e("cityId: ", city_id_s);
        Log.e("contact: ", ed_name_c.getText().toString().trim());
        Log.e("contactPhone: ", ed_phone_c.getText().toString().trim());
        Log.e("deviceToken: ", JPushInterface.getRegistrationID(this));
        Log.e("endTime: ", "2000-01-01 " + tv_close_time.getText().toString() + ":00");
        Log.e("startTime: ", "2000-01-01 " + tv_open_time.getText().toString() + ":00");
        Log.e("orderPhone: ", ed_phone.getText().toString().trim());
        Log.e("organId: ", organ_id + "");
        Log.e("provinceId: ", province_id_s);
        Log.e("storeName: ", ed_name.getText().toString().trim());
        Log.e("serverTypeId: ", "1");
        showProgressBar();
        OkGo.<String>post(Urls.getInstance().REGISTER)
                .tag(this)
                .params("address", tv_province_s.getText().toString().trim() + tv_city_s.getText().toString().trim() + tv_local_s.getText().toString().trim() + ed_address.getText().toString().trim())
                .params("areaId", local_id_s)
                .params("businessNo", ed_number.getText().toString().trim())
                .params("cityId", city_id_s)
                .params("contact", ed_name_c.getText().toString().trim())
                .params("contactPhone", ed_phone_c.getText().toString().trim())
                .params("deviceToken", JPushInterface.getRegistrationID(this))
                .params("endTime", "2000-01-01 " + tv_close_time.getText().toString() + ":00")
                .params("startTime", "2000-01-01 " + tv_open_time.getText().toString() + ":00")
                .params("healthfile", file3)
                .params("licensefile", file2)
                .params("orderPhone", ed_phone.getText().toString().trim())
                .params("organId", organ_id)
                .params("provinceId", province_id_s)
                .params("registfile", file0)
                .params("storeFontfile", file1)
                .params("legalPerson", ed_legal_person.getText().toString().trim())
                .params("storeName", ed_name.getText().toString().trim())
                .params("serverTypeId", 1)
                .params("id", getIntent().getStringExtra("id"))
                .params("addressJson", json_address.toString())
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        stopProgressBar();
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("提交商户审核", json.toString());
                            if (json.getInt("status") == 200) {
                                Toast.makeText(ApplyActivity.this, "提交成功,请等待审核", Toast.LENGTH_SHORT).show();
                                finish();
                                ChooseIdentityActivity.instance.finish();
                                Intent intent = new Intent(mContext, ExamineActivity.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(ApplyActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        stopProgressBar();
                        Toast.makeText(mContext, "系统繁忙,请稍后重试", Toast.LENGTH_SHORT).show();
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
        OptionsPickerView pvOptions = new OptionsPickerBuilder(ApplyActivity.this, new OnOptionsSelectListener() {
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
        OptionsPickerView pvOptions = new OptionsPickerBuilder(ApplyActivity.this, new OnOptionsSelectListener() {
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
        OptionsPickerView pvOptions = new OptionsPickerBuilder(ApplyActivity.this, new OnOptionsSelectListener() {
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
        OkGo.<String>get(Urls.IP + "/sh/user/organ")
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
                                Toast.makeText(ApplyActivity.this, json.getString("ResultValue"), Toast.LENGTH_LONG).show();
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
        OptionsPickerView pvOptions = new OptionsPickerBuilder(ApplyActivity.this, new OnOptionsSelectListener() {
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
                    bitmap = BitmapCompress.decodeUriBitmap(ApplyActivity.this, uri);
                    switch (type) {
                        case 0:
                            img_store_head.setImageBitmap(bitmap);
                            file0 = BitmapCompress.compressImage(bitmap,mContext);
                            break;
                        case 1:
                            img_publicity.setImageBitmap(bitmap);
                            file1 = BitmapCompress.compressImage(bitmap,mContext);
                            break;
                        case 2:
                            img_license.setImageBitmap(bitmap);
                            file2 = BitmapCompress.compressImage(bitmap,mContext);
                            break;
                        case 3:
                            img_permit.setImageBitmap(bitmap);
                            file3 = BitmapCompress.compressImage(bitmap,mContext);
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
                                file0 = BitmapCompress.compressImage(bitmap,mContext);
                                break;
                            case 1:
                                img_publicity.setImageBitmap(bitmap);
                                file1 = BitmapCompress.compressImage(bitmap,mContext);
                                break;
                            case 2:
                                img_license.setImageBitmap(bitmap);
                                file2 = BitmapCompress.compressImage(bitmap,mContext);
                                break;
                            case 3:
                                img_permit.setImageBitmap(bitmap);
                                file3 = BitmapCompress.compressImage(bitmap,mContext);
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

    //街道选择
    public void street() {
        OkGo.<String>get(Urls.getInstance().GETTOWN)
                .tag(this)
                .params("id", local_id_s)
                //      .params("deviceToken", JPushInterface.getRegistrationID(LoginActivity.this))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("查看", json.toString());
                            if (json.getInt("status") == 200) {
                                JSONArray jsonArray = json.getJSONArray("data");
                                Gson gson = new Gson();
                                List<StreetBean> list = gson.fromJson(jsonArray.toString(), new TypeToken<List<StreetBean>>() {
                                }.getType());
                                showStreet(list);
                            } else {
                                Toast.makeText(mContext, json.getString("ResultValue"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    //弹出街道列表
    public void showStreet(final List<StreetBean> list) {
        List<String> streetName = new ArrayList<>();
        for (StreetBean streetBean : list) {
            streetName.add(streetBean.getTown());
        }
        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(mContext, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                if (list.size() != 0) {
                    tv_street.setText(list.get(options1).getTown());
                    town_id = list.get(options1).getId();
                }
            }
        })
                .setDividerColor(Color.parseColor("#A0A0A0"))
                .setTextColorCenter(Color.parseColor("#333333")) //设置选中项文字颜色
                .setContentTextSize(18)//设置滚轮文字大小
                .setOutSideCancelable(true)//点击外部dismiss default true
                .build();
        pvOptions.setPicker(streetName);
        pvOptions.show();
    }

    static int index_rx = 0;

    @SuppressLint("CheckResult")
    private void requestPermissions() {

        RxPermissions rxPermission = new RxPermissions(ApplyActivity.this);
        rxPermission.requestEach(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,//写外部存储器
                Manifest.permission.READ_EXTERNAL_STORAGE,//读取外部存储器
                Manifest.permission.CAMERA)//照相机
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            Logger.i("用户已经同意该权限", permission.name + " is granted.");
                            popuPhoneW.showAtLocation(img_store_head, Gravity.CENTER, 0, 0);
                            backgroundAlpha(0.5f);

                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            Logger.i("用户拒绝了该权限,没有选中『不再询问』", permission.name + " is denied. More info should be provided.");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            Logger.i("用户拒绝了该权限,并且选中『不再询问』", permission.name + " is denied.");
                            Toast.makeText(mContext, "您已经拒绝该权限,请在权限管理中开启权限使用本功能", Toast.LENGTH_SHORT).show();
                        }
                        index_rx++;
                        if (index_rx == 3) {

                            index_rx = 0;
                        }
                    }
                });
    }


}
