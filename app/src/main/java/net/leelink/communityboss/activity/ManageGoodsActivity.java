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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.R;
import net.leelink.communityboss.adapter.CommentListAdapter;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.bean.Event;
import net.leelink.communityboss.utils.Acache;
import net.leelink.communityboss.utils.BitmapCompress;
import net.leelink.communityboss.utils.LoadDialog;
import net.leelink.communityboss.utils.Urls;
import net.leelink.communityboss.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

public class ManageGoodsActivity extends BaseActivity implements View.OnClickListener {
    private RelativeLayout rl_back;
    private ImageView img_head,img_del;
    private File file;
    private View popview;
    private PopupWindow popupWindow;
    private Button btn_album,btn_photograph,btn_upload;
    private EditText ed_name,ed_price,ed_detail;
    private Bitmap bitmap;
    private int state =1;
    private AppCompatRadioButton cb_up,cb_down;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_goods);
        init();
        popu_head();
    }

    public void init(){
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        img_head = findViewById(R.id.img_head);
        img_head.setOnClickListener(this);
        ed_name = findViewById(R.id.ed_name);
        ed_price = findViewById(R.id.ed_price);
        ed_price.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //删除“.”后面超过2位后的数据
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        ed_price.setText(s);
                        ed_price.setSelection(s.length()); //光标移到最后
                    }
                }
                //如果"."在起始位置,则起始位置自动补0
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    ed_price.setText(s);
                    ed_price.setSelection(2);
                }

                //如果起始位置为0,且第二位跟的不是".",则无法后续输入
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        ed_price.setText(s.subSequence(0, 1));
                        ed_price.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        ed_detail = findViewById(R.id.ed_detail);
        btn_upload = findViewById(R.id.btn_upload);
        btn_upload.setOnClickListener(this);
        img_del = findViewById(R.id.img_del);
        img_del.setOnClickListener(this);
        cb_up = findViewById(R.id.cb_up);
        cb_down = findViewById(R.id.cb_down);
        cb_up.setChecked(true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_back:
                finish();
                break;
            case R.id.img_head: //修改商品头像
                popupWindow.showAtLocation(img_head, Gravity.CENTER, 0, 0);
                backgroundAlpha(0.5f);
                break;
            case R.id.btn_upload:   //提交
                if(file!=null) {
                    commit();
                }  else {
                    Toast.makeText(this, "请上传商品图片", Toast.LENGTH_SHORT).show();
                }

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
            case R.id.img_del:  //删除
                
                break;
            default:
                break;
        }
    }

    public void commit(){
        if(cb_down.isChecked())
        {
            state = 0;
        } else if(cb_up.isChecked()){
            state = 1;
        }
        JSONObject json = Acache.get(this).getAsJSONObject("storeInfo");
        Log.e( "commit: ",json.toString() );
        String id="";
        try {
            id = json.getString("id");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        LoadDialog.start(this);
        OkGo.<String>post(Urls.COMMODITY)
                .tag(this)
                .params("remark", ed_detail.getText().toString().trim())
                .params("name",ed_name.getText().toString().trim())
                .params("unitPrice",ed_price.getText().toString().trim())
                .params("productFile",file)
                .params("shStoreId",id)
                .params("state",state)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        LoadDialog.stop();
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("添加商品",json.toString());
                            if (json.getInt("status") == 200) {
                                finish();
                                EventBus.getDefault().post(new Event());
                            } else {

                            }
                            Toast.makeText(ManageGoodsActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        LoadDialog.stop();
                        Toast.makeText(ManageGoodsActivity.this, "网络连接超时,请检查网络", Toast.LENGTH_SHORT).show();
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
                    bitmap = BitmapCompress.decodeUriBitmap(ManageGoodsActivity.this, uri);
                    img_head.setImageBitmap(bitmap);
                    file =  BitmapCompress.compressImage(bitmap);
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


    //获取图片
    @SuppressLint("WrongConstant")
    private void popu_head() {
        // TODO Auto-generated method stub
        popview = LayoutInflater.from(ManageGoodsActivity.this).inflate(R.layout.popu_head, null);
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
        popupWindow.setOnDismissListener(new ManageGoodsActivity.poponDismissListener());
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

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                View view =getCurrentFocus();
                Utils.hideKeyboard(ev, view, this);
                break;
            default:
                break;
        }
        return super.dispatchTouchEvent(ev);

    }

}
