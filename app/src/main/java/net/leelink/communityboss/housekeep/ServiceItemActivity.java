package net.leelink.communityboss.housekeep;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.DataSetObserver;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatSpinner;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcw.library.imagepicker.ImagePicker;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.R;
import net.leelink.communityboss.activity.BaseActivity;
import net.leelink.communityboss.adapter.OnCategoryClickListener;
import net.leelink.communityboss.adapter.OnOrderListener;
import net.leelink.communityboss.bean.ServiceBean;
import net.leelink.communityboss.housekeep.adapter.ServiceItemAdapter;
import net.leelink.communityboss.utils.BitmapCompress;
import net.leelink.communityboss.utils.CashierInputFilter;
import net.leelink.communityboss.utils.GlideLoader;
import net.leelink.communityboss.utils.Urls;
import net.leelink.communityboss.view.CategoryPopup;
import net.leelink.communityboss.view.RecycleViewDivider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ServiceItemActivity extends BaseActivity implements OnOrderListener, View.OnClickListener, OnCategoryClickListener {
    private RecyclerView service_list;
    private ServiceItemAdapter serviceItemAdapter;
    private Button btn_add, btn_confirm, btn_cancel;
    private PopupWindow popupWindow, popupWindow2;
    private View popview, popview2;
    private AppCompatSpinner spinner;
    private ImageView img;
    private EditText ed_around, ed_explain, ed_against_ptice, ed_name, ed_price, ed_unit;
    private int state = 1;
    private String id;
    List<String> imagePaths = new ArrayList<>();
    List<ServiceBean> list = new ArrayList<>();
    private static final int REQUEST_SELECT_IMAGES_CODE = 0x01;
    private boolean add;
    private RelativeLayout rl_back;
    private TextView tv_state, tv_up, tv_down;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_item);
        init();
        initData();
        popu_service();
        popu_service2();
    }

    public void init() {
        service_list = findViewById(R.id.service_list);
        btn_add = findViewById(R.id.btn_add);
        btn_add.setOnClickListener(this);
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
    }

    public void initData() {
        OkGo.<String>get(Urls.getInstance().PRODUCT)
                .params("pageNum", 1)
                .params("pageSize", 100)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("获取商品列表", json.toString());
                            if (json.getInt("status") == 200) {
                                json = json.getJSONObject("data");
                                JSONArray jsonArray = json.getJSONArray("list");
                                Gson gson = new Gson();
                                list = gson.fromJson(jsonArray.toString(), new TypeToken<List<ServiceBean>>() {
                                }.getType());
                                serviceItemAdapter = new ServiceItemAdapter(list, ServiceItemActivity.this, ServiceItemActivity.this);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ServiceItemActivity.this, LinearLayoutManager.VERTICAL, false);
                                service_list.addItemDecoration(new RecycleViewDivider(
                                        ServiceItemActivity.this, LinearLayoutManager.VERTICAL, 20, getResources().getColor(R.color.background)));
                                service_list.setLayoutManager(layoutManager);
                                service_list.setAdapter(serviceItemAdapter);
                            } else {

                            }
                            Toast.makeText(ServiceItemActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


    }

    @Override
    public void onItemClick(View view) {

    }

    @Override
    public void onButtonClick(View view, int position) {
        add = false;
        ed_name.setText(list.get(position).getName());
        ed_against_ptice.setText(list.get(position).getDamagePrice() + "");
        ed_around.setText(list.get(position).getAround());
        ed_explain.setText(list.get(position).getRemark());
        ed_price.setText(list.get(position).getUnitPrice() + "");
        ed_unit.setText(list.get(position).getUnit());
        if (list.get(position).getState() == 0) {
            tv_state.setText("下架");
        } else {
            tv_state.setText("上架");
        }
        id = list.get(position).getId();
        btn_confirm.setText("提交");
        Glide.with(this).load(Urls.getInstance().IMG_URL + list.get(position).getImgPath()).into(img);
        popupWindow.showAtLocation(service_list, Gravity.CENTER, 0, 0);
        backgroundAlpha(0.5f);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_add:
                popupWindow.showAtLocation(service_list, Gravity.CENTER, 0, 0);
                backgroundAlpha(0.5f);
                add = true;
                break;
            case R.id.img:
                ImagePicker.getInstance()
                        .setTitle("标题")//设置标题
                        .showCamera(true)//设置是否显示拍照按钮
                        .showImage(true)//设置是否展示图片
                        .showVideo(true)//设置是否展示视频
                        .setSingleType(true)//设置图片视频不能同时选择
                        .setMaxCount(1)//设置最大选择图片数目(默认为1，单选)
//                        .setImagePaths(mImageList)//保存上一次选择图片的状态，如果不需要可以忽略
                        .setImageLoader(new GlideLoader())//设置自定义图片加载器
                        .start(ServiceItemActivity.this, REQUEST_SELECT_IMAGES_CODE);
                break;
            case R.id.btn_confirm:
                if (add) {
                    submit();
                } else {
                    edit();
                }
                break;
            case R.id.btn_cancel:
                popupWindow.dismiss();
                break;
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_state:
                popupWindow2.showAsDropDown(tv_state);
                break;
            case R.id.tv_up:
                state = 1;
                tv_state.setText("上架");
                popupWindow2.dismiss();
                break;
            case R.id.tv_down:
                state = 0;
                tv_state.setText("下架");
                popupWindow2.dismiss();

                break;
        }
    }

    public void submit() {

        if (imagePaths.size() == 0) {
            Toast.makeText(this, "请上传商品图片", Toast.LENGTH_SHORT).show();
            return;
        }
        OkGo.<String>post(Urls.getInstance().PRODUCT)
                .params("around", ed_around.getText().toString().trim())
                .params("damagePrice", ed_against_ptice.getText().toString().trim())
                .params("remark", ed_explain.getText().toString().trim())
                .params("name", ed_name.getText().toString().trim())
                .params("img", new File(imagePaths.get(0)))
                .params("state", state)
                .params("unitPrice", ed_price.getText().toString().trim())
                .params("unit", ed_unit.getText().toString().trim())
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("上架商品", json.toString());
                            if (json.getInt("status") == 200) {
                                popupWindow.dismiss();
                                Toast.makeText(ServiceItemActivity.this, "上传成功", Toast.LENGTH_SHORT).show();
                                initData();
                            } else {

                            }
                            Toast.makeText(ServiceItemActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void edit() {

        HttpParams params = new HttpParams();
        if (imagePaths.size() != 0) {
            params.put("img", new File(imagePaths.get(0)));
            return;
        }
        params.put("around", ed_around.getText().toString().trim());
        params.put("damagePrice", ed_against_ptice.getText().toString().trim());
        params.put("remark", ed_explain.getText().toString().trim());
        params.put("name", ed_name.getText().toString().trim());
        params.put("state", state);
        params.put("unitPrice", ed_price.getText().toString().trim());
        params.put("unit", ed_unit.getText().toString().trim());
        params.put("id", id);
        OkGo.<String>post(Urls.getInstance().PRODUCTIMG)
                .params(params)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("编辑商品", json.toString());
                            if (json.getInt("status") == 200) {
                                popupWindow.dismiss();
                                initData();
                            } else {

                            }
                            Toast.makeText(ServiceItemActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
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
            if (requestCode == REQUEST_SELECT_IMAGES_CODE && resultCode == RESULT_OK) {
                imagePaths = data.getStringArrayListExtra(ImagePicker.EXTRA_SELECT_IMAGES);

                refresh(imagePaths);
            }
        }
    }

    @SuppressLint("WrongConstant")
    private void popu_service() {
        // TODO Auto-generated method stub
        popview = LayoutInflater.from(this).inflate(
                R.layout.popu_service, null);
        btn_cancel = popview.findViewById(R.id.btn_cancel);
        btn_cancel.setOnClickListener(ServiceItemActivity.this);
        btn_confirm = popview.findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(ServiceItemActivity.this);
        ed_around = popview.findViewById(R.id.ed_around);
        ed_explain = popview.findViewById(R.id.ed_explain);
//        spinner = popview.findViewById(R.id.spinner);
        tv_state = popview.findViewById(R.id.tv_state);
        tv_state.setOnClickListener(this);
        ed_against_ptice = popview.findViewById(R.id.ed_against_ptice);
        ed_unit = popview.findViewById(R.id.ed_unit);
        ed_name = popview.findViewById(R.id.ed_name);
        ed_price = popview.findViewById(R.id.ed_price);
        InputFilter[] filters = {new CashierInputFilter()};
        ed_against_ptice.setFilters(filters);
        ed_price.setFilters(filters);
//        List<String> list = new ArrayList<>();
//        list.add("上架");
//        list.add("下架");
        img = popview.findViewById(R.id.img);
        img.setOnClickListener(ServiceItemActivity.this);
//        MySpinnerAdapter spinnerAdapter = new MySpinnerAdapter(list,ServiceItemActivity.this);
//        spinner.setAdapter(spinnerAdapter);
//        spinner.setPopupBackgroundResource(R.color.white);
//        spinner.setBackgroundResource(R.color.white);
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                TextView tv = (TextView) view;
//                tv.setTextColor(getResources().getColor(R.color.blue));
//                tv.setGravity(Gravity.CENTER);
//                if(position==0){
//                    state =1;
//                } else {
//                    state= 0;
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
        popupWindow = new PopupWindow(popview,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setOutsideTouchable(false);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOnDismissListener(new poponDismissListener());
    }

    @SuppressLint("WrongConstant")
    private void popu_service2() {
        // TODO Auto-generated method stub

        LayoutInflater mLayoutInflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        popview2 = mLayoutInflater.from(this).inflate(
                R.layout.popu_service2, null);
        tv_up = popview2.findViewById(R.id.tv_up);
        tv_down = popview2.findViewById(R.id.tv_down);
        tv_up.setOnClickListener(this);
        tv_down.setOnClickListener(this);


        popupWindow2 = new PopupWindow(popview2,
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow2.setFocusable(true);
        popupWindow2.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow2.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow2.setOutsideTouchable(false);
        popupWindow2.setBackgroundDrawable(new BitmapDrawable());
        popupWindow2.setOnDismissListener(new poponDismissListener());
    }


    @SuppressWarnings("ResourceType")
    private static int makeDropDownMeasureSpec(int measureSpec) {
        int mode;
        if (measureSpec == ViewGroup.LayoutParams.WRAP_CONTENT) {
            mode = View.MeasureSpec.UNSPECIFIED;
        } else {
            mode = View.MeasureSpec.EXACTLY;
        }
        return View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.getSize(measureSpec), mode);
    }

    @Override
    public void onclick(View view, int position) {

    }

    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            // Log.v("List_noteTypeActivity:", "我是关闭事件");
            backgroundAlpha(1f);
        }

    }

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

    public void refresh(List<String> list) {
        Log.e("refresh: ", list.size() + "");
        if (list.size() == 0) {
//            imag.setVisibility(View.VISIBLE);
//            upload_img2.setVisibility(View.GONE);
//            upload_img3.setVisibility(View.GONE);
            return;
        } else if (list.size() == 1) {
            Glide.with(this).load(list.get(0)).into(img);

        }
    }
}
