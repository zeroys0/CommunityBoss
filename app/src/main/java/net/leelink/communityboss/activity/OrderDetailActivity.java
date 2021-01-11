package net.leelink.communityboss.activity;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.R;
import net.leelink.communityboss.adapter.PreOrderAdapter;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.bean.DsTakeOrderRefresh;
import net.leelink.communityboss.bean.Event;
import net.leelink.communityboss.bean.GoodsBean;
import net.leelink.communityboss.bean.OrderDetail;
import net.leelink.communityboss.bean.TakeOrderRefresh;
import net.leelink.communityboss.utils.RatingBar;
import net.leelink.communityboss.utils.Urls;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {
private RecyclerView goods_list;
private PreOrderAdapter preOrderAdapter;
private List<OrderDetail.DetailsBean> list = new ArrayList<>();
private TextView tv_state,tv_orderid,tv_time,tv_name,tv_phone,tv_address,tv_remark,tv_total_price,tv_refundRecord,tv_userphone,tv_comment,tv_store_name,tv_reply;
private String orderId;
private RatingBar rt_attitude,rt_taste,rt_hygiene;
private EditText ed_reply;
private ImageView img_head,img_main,img0,img1,img2;
private LinearLayout ll_comment,ll_images,ll_connect,ll_adress;
private int type;
private Button btn_confirm;
private RelativeLayout rl_back,rl_img;
private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        setContentView(R.layout.activity_order_detail);
        init();
        initData();
    }

    public void init(){
        context = this;
        goods_list = findViewById(R.id.goods_list);
        tv_state = findViewById(R.id.tv_state);
        tv_orderid = findViewById(R.id.tv_orderid);
        tv_time = findViewById(R.id.tv_time);
        tv_name = findViewById(R.id.tv_name);
        tv_phone = findViewById(R.id.tv_phone);
        tv_address = findViewById(R.id.tv_address);
        tv_remark = findViewById(R.id.tv_remark);
        tv_total_price = findViewById(R.id.tv_total_price);
        ll_comment = findViewById(R.id.ll_comment);
        tv_refundRecord = findViewById(R.id.tv_refundRecord);
        btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
        ll_connect = findViewById(R.id.ll_connect);
        ll_adress = findViewById(R.id.ll_adress);
        ll_connect = findViewById(R.id.ll_connect);
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        type = getIntent().getIntExtra("type",0);
        if(type == 0) {
            ll_comment.setVisibility(View.GONE);
            tv_state.setText("未接单");
            btn_confirm.setVisibility(View.VISIBLE);
            btn_confirm.setText("确认接单");
        } else if(type ==1) {
            ll_comment.setVisibility(View.GONE);
            tv_state.setText("已接单");

        } else if(type ==2) {
            ll_comment.setVisibility(View.GONE);
            tv_state.setText("已出发");

        } else if(type ==3) {
            ll_comment.setVisibility(View.GONE);
            tv_state.setText("订单已完成");

        } else if(type ==4){
            ll_comment.setVisibility(View.GONE);
            tv_state.setText("退款订单");
        } else  if(type ==5) {
            ll_comment.setVisibility(View.GONE);
            tv_state.setText("已出发");
            ll_adress.setVisibility(View.GONE);
            ll_connect.setVisibility(View.GONE);
        }
        tv_store_name = findViewById(R.id.tv_store_name);
    }

    public void initData(){
        final String orderId = getIntent().getStringExtra("orderId");
        tv_orderid.setText(orderId);
        OkGo.<String>get(Urls.getInstance().ORDERDETAILS+"/"+ orderId)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("订单详情",json.toString());
                            if (json.getInt("status") == 200) {
                                json = json.getJSONObject("data");
                                Gson gson = new Gson();

                                if(!json.isNull("RefundRecord")) {
                                    tv_refundRecord.setVisibility(View.VISIBLE);
                                   tv_refundRecord.setText("退款原因:"+ json.getJSONObject("RefundRecord").getString("Reason"));
                                }
                                String s = json.getString("goodList").trim();
                                JSONArray jsonArray = new JSONArray(json.getString("goodList"));
                                Log.e( "onSuccess: ",jsonArray.toString() );
                                tv_time.setText(json.getString("appointTime"));
                                if(json.has("name")) {
                                    tv_name.setText(json.getString("name"));
                                }
                                tv_store_name.setText(CommunityBossApplication.storeInfo.getStoreName());
                                if(json.has("telephone")) {
                                    tv_phone.setText(json.getString("telephone"));
                                }
                                tv_address.setText(json.getString("address"));
                                if(json.has("remark")) {
                                    tv_remark.setText(json.getString("remark"));
                                }
                                List<GoodsBean> list = gson.fromJson(jsonArray.toString(),new TypeToken<List<GoodsBean>>(){}.getType());
                                preOrderAdapter = new PreOrderAdapter(OrderDetailActivity.this,list);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(OrderDetailActivity.this,LinearLayoutManager.VERTICAL,false);
                                goods_list.setLayoutManager(layoutManager);
                                goods_list.setAdapter(preOrderAdapter);
                                goods_list.setNestedScrollingEnabled(false);
                                tv_total_price.setText("￥"+json.getString("actualPayPrice"));
                                if(json.getInt("orderState")==7) {
                                    ll_comment.setVisibility(View.VISIBLE);
                                    initAppraise();
                                    rt_attitude.setSelectedNumber(json.getInt("total_star"));
                                    rt_taste.setSelectedNumber(json.getInt("product_star"));
                                    rt_hygiene.setSelectedNumber(json.getInt("taste_star"));
                                    Glide.with(OrderDetailActivity.this).load(Urls.getInstance().IMG_URL+json.getString("head_img_path")).into(img_head);
                                    tv_comment.setText(json.getString("feed_bank_content"));
                                    tv_userphone.setText(json.getString("telephone"));
                                    if(json.has("image1_path")) {
                                        rl_img.setVisibility(View.VISIBLE);
                                        if(json.has("image2_path")){
                                            img_main.setVisibility(View.GONE);
                                            ll_images.setVisibility(View.VISIBLE);
                                            Glide.with(context).load(Urls.getInstance().IMG_URL+json.getString("image1_path")).into(img0);
                                            Glide.with(context).load(Urls.getInstance().IMG_URL+json.getString("image2_path")).into(img1);
                                            if(json.has("image3_path")){
                                                Glide.with(context).load(Urls.getInstance().IMG_URL+json.getString("image3_path")).into(img2);
                                            }
                                        } else {
                                            img_main.setVisibility(View.VISIBLE);
                                            Glide.with(context).load(Urls.getInstance().IMG_URL + json.getString("image1_path")).into(img_main);
                                        }
                                    }
                                    if(json.has("reply")){
                                        tv_reply.setVisibility(View.GONE);
                                        ed_reply.setText(json.getString("reply"));
                                        ed_reply.setFocusable(false);
                                        ed_reply.setFocusableInTouchMode(false);
                                    }
                                }

                            } else {
                                Toast.makeText(OrderDetailActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }



    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_confirm:
                if(type == 0) { //未接单
                    takeOrder(1);       //确认接单
                }
                break;
            case R.id.rl_back:
                finish();
                break;

                default:
                    break;
        }
    }

    public void initAppraise(){
        rt_attitude = findViewById(R.id.rt_attitude);
        rt_attitude.setUntouchable();
        rt_taste = findViewById(R.id.rt_taste);
        rt_taste.setUntouchable();
        rt_hygiene = findViewById(R.id.rt_hygiene);
        rt_hygiene.setUntouchable();
        img_head = findViewById(R.id.img_head);
        tv_userphone = findViewById(R.id.tv_userphone);
        tv_comment = findViewById(R.id.tv_comment);
        img_main = findViewById(R.id.img_main);
        rl_img = findViewById(R.id.rl_img);
        ll_images = findViewById(R.id.ll_images);
        img0 = findViewById(R.id.img0);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        ed_reply = findViewById(R.id.ed_reply);
        tv_reply = findViewById(R.id.tv_reply);
        tv_reply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reply();
            }
        });
    }

    public void takeOrder(int operation){

        OkGo.<String>post(Urls.getInstance().ORDERSTATE+"?orderId="+tv_orderid.getText().toString().trim()+"&state=3")
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("确认订单",json.toString());
                            if (json.getInt("status") == 200) {
                                int position = getIntent().getIntExtra("position",0);
                                EventBus.getDefault().post(new DsTakeOrderRefresh(position));
                                btn_confirm.setVisibility(View.GONE);
                                Toast.makeText(OrderDetailActivity.this, "订单已确认,请尽快完成吧~", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(OrderDetailActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void reply(){
        OkGo.<String>get(Urls.getInstance().REPLYAPPRAISE)
                .params("orderId",tv_orderid.getText().toString().trim())
                .params("replyContent",ed_reply.getText().toString().trim())
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("回复评价",json.toString());
                            if (json.getInt("status") == 200) {
                                Toast.makeText(OrderDetailActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                                tv_reply.setVisibility(View.GONE);
                                ed_reply.setFocusable(false);
                                ed_reply.setFocusableInTouchMode(false);
                            } else {
                                Toast.makeText(OrderDetailActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
