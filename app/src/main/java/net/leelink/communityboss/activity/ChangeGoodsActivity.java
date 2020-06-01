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
import android.provider.MediaStore;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatRadioButton;
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
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.Target;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.R;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.bean.DeleteEvent;
import net.leelink.communityboss.bean.Event;
import net.leelink.communityboss.utils.Acache;
import net.leelink.communityboss.utils.BitmapCompress;
import net.leelink.communityboss.utils.Urls;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.concurrent.ExecutionException;

public class ChangeGoodsActivity extends BaseActivity implements View.OnClickListener {

    private RelativeLayout rl_back;
    private ImageView img_head, img_del;
    private File file;
    private View popview;
    private PopupWindow popupWindow;
    private Button btn_album, btn_photograph, btn_upload;
    private EditText ed_name, ed_price, ed_detail;
    private Bitmap bitmap;

    private int commodityId;
    private String url;
    private int state =1;
    private AppCompatRadioButton cb_up,cb_down;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_goods);
        init();
        popu_head();
    }

    public void init() {
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        img_head = findViewById(R.id.img_head);
        img_head.setOnClickListener(this);
        ed_name = findViewById(R.id.ed_name);
        ed_price = findViewById(R.id.ed_price);
        ed_detail = findViewById(R.id.ed_detail);
        btn_upload = findViewById(R.id.btn_upload);
        btn_upload.setOnClickListener(this);
        img_del = findViewById(R.id.img_del);
        img_del.setOnClickListener(this);


        commodityId = getIntent().getIntExtra("commodityId", 0);
        ed_name.setText(getIntent().getStringExtra("name"));
        double price = getIntent().getDoubleExtra("price", 0);
        ed_price.setText(Double.toString(price));
        ed_detail.setText(getIntent().getStringExtra("details"));
        url = getIntent().getStringExtra("image");
        state = getIntent().getIntExtra("state",0);
        cb_up = findViewById(R.id.cb_up);
        cb_down = findViewById(R.id.cb_down);
        if(state == 0){

            cb_down.setChecked(true);
        }
        if(state ==1) {
            cb_up.setChecked(true);
        }

        Glide.with(ChangeGoodsActivity.this).load(Urls.IMG_URL + url).into(img_head);

        //将网络地址转化为file
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Bitmap bitmap = Glide.with(ChangeGoodsActivity.this).load(Urls.IMG_URL + url).asBitmap().into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL).get();
                    file = BitmapCompress.compressImage(bitmap);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.img_head: //修改商品头像
                popupWindow.showAtLocation(img_head, Gravity.CENTER, 0, 0);
                backgroundAlpha(0.5f);
                break;
            case R.id.btn_upload:   //提交
                commit();
                break;
            case R.id.btn_photograph://拍照
                popupWindow.dismiss();
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
                popupWindow.dismiss();
                Intent intent1 = new Intent(Intent.ACTION_PICK);
                intent1.setType("image/*");
                startActivityForResult(intent1, 1);
                break;
            case R.id.img_del:  //删除商品
                delete();
                break;



            default:
                break;
        }
    }

    public void commit() {
        if(cb_down.isChecked()){
            state = 0;
        }
        else if(cb_up.isChecked()){
            state = 1;
        }
        JSONObject json = Acache.get(this).getAsJSONObject("storeInfo");
        String id="";
        Log.e( "commit: ",json.toString() );
        try {
            id = json.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.e( "state: ",state+"" );
        OkGo.<String>post(Urls.COMMODITYIMG )
                .tag(this)
                .params("id",getIntent().getStringExtra("Id"))
                .params("remark", ed_detail.getText().toString().trim())
                .params("name", ed_name.getText().toString().trim())
                .params("unitPrice", ed_price.getText().toString().trim())
                .params("productFile", file)
                .params("shStoreId",id)
                .params("state",state)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("编辑商品", json.toString());
                            if (json.getInt("status") == 200) {
                                finish();
                            } else {

                            }
                            Toast.makeText(ChangeGoodsActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
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
                    bitmap = BitmapCompress.decodeUriBitmap(ChangeGoodsActivity.this, uri);
                    img_head.setImageBitmap(bitmap);
                    file = BitmapCompress.compressImage(bitmap);
                    break;
                case 2:
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        bitmap = (Bitmap) bundle.get("data");
                        img_head.setImageBitmap(bitmap);
                        file = BitmapCompress.compressImage(bitmap);
                    }
                    break;

                default:
                    break;
            }
        }
    }

    public void delete() {

        OkGo.<String>delete(Urls.COMMODITY + "?appToken=" + CommunityBossApplication.token + "&commodityIds=" + commodityId)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            body = body.substring(1, body.length() - 1);
                            JSONObject json = new JSONObject(body.replaceAll("\\\\", ""));
                            Log.d("用户信息", json.toString());
                            if (json.getInt("ResultCode") == 200) {
                                EventBus.getDefault().post(new Event());
                                finish();
                            } else {

                            }
                            Toast.makeText(ChangeGoodsActivity.this, json.getString("ResultValue"), Toast.LENGTH_LONG).show();
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
        popview = LayoutInflater.from(ChangeGoodsActivity.this).inflate(R.layout.popu_head, null);
        btn_album = (Button) popview.findViewById(R.id.btn_album);
        btn_photograph = (Button) popview.findViewById(R.id.btn_photograph);
        btn_album.setOnClickListener(this);
        btn_photograph.setOnClickListener(this);
        popupWindow = new PopupWindow(popview,
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        popupWindow.setFocusable(true);
        popupWindow.setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        popupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOnDismissListener(new ChangeGoodsActivity.poponDismissListener());
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
