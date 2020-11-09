package net.leelink.communityboss.housekeep;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.R;
import net.leelink.communityboss.activity.BaseActivity;
import net.leelink.communityboss.bean.DelegateCallBack;
import net.leelink.communityboss.bean.HsOrderBean;
import net.leelink.communityboss.bean.TakeOrderRefresh;
import net.leelink.communityboss.bean.WorkBean;
import net.leelink.communityboss.utils.RatingBar;
import net.leelink.communityboss.utils.Urls;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.view.View.GONE;
import static com.amap.api.maps.model.BitmapDescriptorFactory.getContext;

public class HsOrderDetailActivity extends BaseActivity  implements View.OnClickListener {
    private TextView tv_orderid,tv_time,tv_name,tv_phone,tv_address,tv_good_name,tv_price,tv_total_price,tv_servicer,tv_sex,tv_servphone,tv_service_number,tv_start_time,tv_end_time,tv_userphone,tv_comment,tv_reply;
    private ImageView goods_head,img0,img1,img2,img_head,img_file0,img_file1,img_file2,img_main;
    private Button btn_confirm,order_cancel,order_confirm;
    private LinearLayout ll_title,ll_servicer,ll_sex,ll_phone,ll_number,ll_start_time,ll_end_time,ll_img,ll_button,ll_state,ll_comment,ll_images;
    private RelativeLayout rl_back,rl_img;
    private RatingBar rt_total,rt_pack,rt_taste;
    private EditText ed_reply;
    JSONObject jsonObject = new JSONObject();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_hs_order_detail);
        init();
        initData();
    }

    public void init(){
        EventBus.getDefault().register(this);
        tv_orderid = findViewById(R.id.tv_orderid);
        tv_time = findViewById(R.id.tv_time);
        tv_name = findViewById(R.id.tv_name);
        tv_phone = findViewById(R.id.tv_phone);
        tv_address = findViewById(R.id.tv_address);
        goods_head = findViewById(R.id.goods_head);
        tv_good_name = findViewById(R.id.tv_good_name);
        tv_price = findViewById(R.id.tv_price);
        tv_total_price = findViewById(R.id.tv_total_price);
        btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
        ll_title = findViewById(R.id.ll_title);
        ll_servicer = findViewById(R.id.ll_servicer);
        ll_sex = findViewById(R.id.ll_sex);
        ll_phone = findViewById(R.id.ll_phone);
        ll_number = findViewById(R.id.ll_number);
        ll_start_time = findViewById(R.id.ll_start_time);
        ll_end_time =findViewById(R.id.ll_end_time);
        ll_img = findViewById(R.id.ll_img);
        img0 = findViewById(R.id.img0);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        ll_button = findViewById(R.id.ll_button);
        ll_state = findViewById(R.id.ll_state);
        tv_servicer = findViewById(R.id.tv_servicer);
        tv_sex = findViewById(R.id.tv_sex);
        tv_servphone =findViewById(R.id.tv_servphone);
        tv_service_number = findViewById(R.id.tv_service_number);
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_start_time = findViewById(R.id.tv_start_time);
        tv_end_time = findViewById(R.id.tv_end_time);
        rt_total = findViewById(R.id.rt_total);
        rt_pack = findViewById(R.id.rt_pack);
        rt_taste = findViewById(R.id.rt_taste);
        ll_comment = findViewById(R.id.ll_comment);
        img_head = findViewById(R.id.img_head);
        tv_userphone = findViewById(R.id.tv_userphone);
        tv_comment = findViewById(R.id.tv_comment);
        img_file0 = findViewById(R.id.img_file0);
        img_file1 = findViewById(R.id.img_file1);
        img_file2 = findViewById(R.id.img_file2);
        rl_img = findViewById(R.id.rl_img);
        ll_images = findViewById(R.id.ll_images);
        img_main = findViewById(R.id.img_main);
        tv_reply = findViewById(R.id.tv_reply);
        ed_reply = findViewById(R.id.ed_reply);
        order_cancel = findViewById(R.id.order_cancel);
        order_cancel.setOnClickListener(this);
        order_confirm = findViewById(R.id.order_confirm);
        order_confirm.setOnClickListener(this);

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DelegateCallBack callBack) {
        finish();
    }

    public void initData(){

        try {
            jsonObject=  new JSONObject(getIntent().getStringExtra("object"));
            tv_orderid.setText(jsonObject.getString("orderNo"));
            tv_time.setText(jsonObject.getString("apointTime"));
            tv_name.setText(jsonObject.getString("deliveryName"));
            tv_phone.setText(jsonObject.getString("deliveryPhone"));
            tv_address.setText(jsonObject.getString("receivingAddress"));
            Glide.with(this).load(Urls.IMG_URL+jsonObject.getString("imgPath")).into(goods_head);
            if(jsonObject.has("name")) {
                tv_good_name.setText(jsonObject.getString("name"));
            } else {
                tv_good_name.setText(jsonObject.getString("productName"));
            }

            tv_price.setText("￥"+ jsonObject.getString("unitPrice")+"元/"+jsonObject.getString("unit"));
            switch (jsonObject.getInt("state")){
                case 1:
                    ll_button.setVisibility(View.VISIBLE);
                    btn_confirm.setVisibility(GONE);
                    break;
                case 2:
                    btn_confirm.setText("派工");
                    btn_confirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(HsOrderDetailActivity.this, DelegateActivity.class);
                            try {
                                intent.putExtra("id",jsonObject.getString("productId"));
                                intent.putExtra("orderId",jsonObject.getString("orderId"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            startActivity(intent);

                        }
                    });
                    break;
                case 3:
                case 4:
                    btn_confirm.setVisibility(GONE);
                    btn_confirm.setText("派工");
                    ll_title.setVisibility(View.VISIBLE);
                    ll_servicer.setVisibility(View.VISIBLE);
                    tv_servicer.setText(jsonObject.getString("servicerName"));
                    ll_sex.setVisibility(View.VISIBLE);
                    if(jsonObject.getInt("servicerSex")==0){
                        tv_sex.setText("男");
                    } else {
                        tv_sex.setText("女");
                    }
                    ll_phone.setVisibility(View.VISIBLE);
                    tv_servphone.setText(jsonObject.getString("servicerPhone"));
                    ll_number.setVisibility(View.VISIBLE);
                    if(jsonObject.has("servicerNo")) {
                        tv_service_number.setText(jsonObject.getString("servicerNo"));
                    } else {
                        tv_service_number.setText(jsonObject.getString("user_no"));
                    }
                    break;
                case 5:
                    btn_confirm.setVisibility(GONE);
                    btn_confirm.setText("派工");
                    ll_title.setVisibility(View.VISIBLE);
                    ll_servicer.setVisibility(View.VISIBLE);
                    tv_servicer.setText(jsonObject.getString("servicerName"));
                    ll_sex.setVisibility(View.VISIBLE);
                    if(jsonObject.getInt("servicerSex")==0){
                        tv_sex.setText("男");
                    } else {
                        tv_sex.setText("女");
                    }
                    ll_phone.setVisibility(View.VISIBLE);
                    tv_servphone.setText(jsonObject.getString("servicerPhone"));
                    ll_number.setVisibility(View.VISIBLE);
                    if(jsonObject.has("servicerNo")) {
                        tv_service_number.setText(jsonObject.getString("servicerNo"));
                    } else {
                        tv_service_number.setText(jsonObject.getString("user_no"));
                    }
                    ll_start_time.setVisibility(View.VISIBLE);
                    tv_start_time.setText(jsonObject.getString("start_time"));

                    break;
                case 6:
                case 7:
                    btn_confirm.setVisibility(GONE);
                    btn_confirm.setText("派工");
                    ll_title.setVisibility(View.VISIBLE);
                    ll_servicer.setVisibility(View.VISIBLE);
                    tv_servicer.setText(jsonObject.getString("servicerName"));
                    ll_sex.setVisibility(View.VISIBLE);
                    if(jsonObject.getInt("servicerSex")==0){
                        tv_sex.setText("男");
                    } else {
                        tv_sex.setText("女");
                    }
                    ll_phone.setVisibility(View.VISIBLE);
                    tv_servphone.setText(jsonObject.getString("servicerPhone"));
                    ll_number.setVisibility(View.VISIBLE);
                    if(jsonObject.has("servicerNo")) {
                        tv_service_number.setText(jsonObject.getString("servicerNo"));
                    } else {
                        tv_service_number.setText(jsonObject.getString("user_no"));
                    }
                    ll_start_time.setVisibility(View.VISIBLE);
                    tv_start_time.setText(jsonObject.getString("start_time"));
                    ll_end_time.setVisibility(View.VISIBLE);
                    tv_end_time.setText(jsonObject.getString("end_time"));
                    ll_img .setVisibility(View.VISIBLE);

                    Glide.with(this).load(Urls.IMG_URL+jsonObject.getString("one_file")).into(img0);
                    if(jsonObject.has("two_file")) {
                        Glide.with(this).load(Urls.IMG_URL + jsonObject.getString("two_file")).into(img1);
                    }
                    if(jsonObject.has("three_file")) {
                        Glide.with(this).load(Urls.IMG_URL + jsonObject.getString("three_file")).into(img2);
                    }
                    break;
                case 8:
                    btn_confirm.setVisibility(GONE);
                    btn_confirm.setText("派工");
                    ll_title.setVisibility(View.VISIBLE);
                    ll_servicer.setVisibility(View.VISIBLE);
                    tv_servicer.setText(jsonObject.getString("servicerName"));
                    ll_sex.setVisibility(View.VISIBLE);
                    if(jsonObject.getInt("servicerSex")==0){
                        tv_sex.setText("男");
                    } else {
                        tv_sex.setText("女");
                    }
                    ll_phone.setVisibility(View.VISIBLE);
                    tv_servphone.setText(jsonObject.getString("servicerPhone"));
                    ll_number.setVisibility(View.VISIBLE);
                    if(jsonObject.has("servicerNo")) {
                        tv_service_number.setText(jsonObject.getString("servicerNo"));
                    } else {
                        tv_service_number.setText(jsonObject.getString("user_no"));
                    }
                    ll_start_time.setVisibility(View.VISIBLE);
                    tv_start_time.setText(jsonObject.getString("startTime"));
                    ll_end_time.setVisibility(View.VISIBLE);
                    tv_end_time.setText(jsonObject.getString("endTime"));
                    ll_img .setVisibility(View.VISIBLE);
                    Glide.with(this).load(Urls.IMG_URL+jsonObject.getString("one_file")).into(img0);
                    if(jsonObject.has("two_file")) {
                        Glide.with(this).load(Urls.IMG_URL + jsonObject.getString("two_file")).into(img1);
                    }
                    if(jsonObject.has("three_file")) {
                        Glide.with(this).load(Urls.IMG_URL + jsonObject.getString("three_file")).into(img2);
                    }
                    ll_comment.setVisibility(View.VISIBLE);
                    rt_total.setSelectedNumber(jsonObject.getDouble("total_star"));
                    rt_total.setUntouchable();
                    rt_pack.setSelectedNumber(jsonObject.getDouble("taste_star"));
                    rt_pack.setUntouchable();
                    rt_taste.setSelectedNumber(jsonObject.getDouble("product_star"));
                    rt_taste.setUntouchable();
                    Glide.with(this).load(Urls.IMG_URL + jsonObject.getString("head_img_path")).into(img_head);
                    tv_userphone.setText(jsonObject.getString("orderNo"));
                    tv_comment.setText(jsonObject.getString("content"));
                    if(jsonObject.has("image1_path")) {
                        rl_img.setVisibility(View.VISIBLE);
                        if(jsonObject.has("image2_path")){
                            img_main.setVisibility(GONE);
                            ll_images.setVisibility(View.VISIBLE);
                            Glide.with(this).load(Urls.IMG_URL+jsonObject.getString("image1_path")).into(img_file0);
                            Glide.with(this).load(Urls.IMG_URL+jsonObject.getString("image2_path")).into(img_file1);
                            if(jsonObject.has("image3_path")){
                                Glide.with(this).load(Urls.IMG_URL+jsonObject.getString("image3_path")).into(img_file2);
                            }
                        } else {
                            img_main.setVisibility(View.VISIBLE);
                            Glide.with(this).load(Urls.IMG_URL + jsonObject.getString("image1_path")).into(img_main);
                        }
                    }
                    if(jsonObject.has("reply")){
                        tv_reply.setVisibility(GONE);
                        ed_reply.setText(jsonObject.getString("reply"));
                        ed_reply.setFocusable(false);
                        ed_reply.setFocusableInTouchMode(false);
                    }
                    break;

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:

                break;
            case R.id.order_cancel:
                cancel();
                break;
            case R.id.order_confirm:
                takeOrder();
                break;
                default:
                    break;
        }
    }

    public void takeOrder(){
        try {
            OkGo.<String>post(Urls.HS_ORDERSTATE+"?"+"orderId="+jsonObject.getString("orderId")+"&state="+2)
                    .tag(this)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            try {
                                String body = response.body();
                                JSONObject json = new JSONObject(body);
                                Log.d("确认订单",json.toString());
                                if (json.getInt("status") == 200) {
                                    Toast.makeText(getContext(), "订单已确认,请尽快完成吧~", Toast.LENGTH_SHORT).show();
                                    EventBus.getDefault().post(new TakeOrderRefresh());
                                    ll_button.setVisibility(GONE);
                                } else {
                                    Toast.makeText(getContext(), json.getString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void cancel(){
        try {
            OkGo.<String>post(Urls.HS_ORDERSTATE+"?"+"orderId="+jsonObject.getString("orderId")+"&state="+9)
                    .tag(this)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            try {
                                String body = response.body();
                                JSONObject json = new JSONObject(body);
                                Log.d("确认订单",json.toString());
                                if (json.getInt("status") == 200) {
                                    Toast.makeText(HsOrderDetailActivity.this, "订单已拒绝~", Toast.LENGTH_SHORT).show();
                                    finish();
                                } else {
                                    Toast.makeText(getContext(), json.getString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}
