package net.leelink.communityboss.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.R;
import net.leelink.communityboss.adapter.PreOrderAdapter;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.bean.Event;
import net.leelink.communityboss.bean.OrderDetail;
import net.leelink.communityboss.utils.RatingBar;
import net.leelink.communityboss.utils.Urls;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {
private RecyclerView goods_list;
private PreOrderAdapter preOrderAdapter;
private List<OrderDetail.DetailsBean> list = new ArrayList<>();
private TextView tv_state,tv_orderid,tv_time,tv_name,tv_phone,tv_address,tv_remark,tv_total_price,tv_refundRecord,tv_userphone,tv_comment,tv_store_name;
private OrderDetail orderDetail;
private RatingBar rt_attitude,rt_taste,rt_hygiene;
private ImageView img_head;
private LinearLayout ll_comment;
private int type;
private Button btn_confirm;
private RelativeLayout rl_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        init();
        initData();
    }

    public void init(){
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
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        type = getIntent().getIntExtra("type",0);
        if(type == 0) {
            ll_comment.setVisibility(View.GONE);
            tv_state.setText("未接单");
            btn_confirm.setText("确认接单");
        } else if(type ==1) {
            ll_comment.setVisibility(View.GONE);
            tv_state.setText("已接单");
            btn_confirm.setText("确认送出");
        } else if(type ==2) {
            ll_comment.setVisibility(View.GONE);
            tv_state.setText("已出发");
            btn_confirm.setText("确认送达");
        } else if(type ==3) {
            ll_comment.setVisibility(View.GONE);
            tv_state.setText("订单已完成");
            btn_confirm.setVisibility(View.GONE);
        } else if(type ==4){
            ll_comment.setVisibility(View.GONE);
            tv_state.setText("退款订单");
            btn_confirm.setVisibility(View.GONE);
        }
        tv_store_name = findViewById(R.id.tv_store_name);
    }

    public void initData(){
        final String orderId = getIntent().getStringExtra("orderId");
        tv_orderid.setText(orderId);
        OkGo.<String>get(Urls.ORDERDETAILS+"/"+ orderId)
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
                                orderDetail = gson.fromJson(json.toString(),OrderDetail.class);
                                if(!json.isNull("RefundRecord")) {
                                    tv_refundRecord.setVisibility(View.VISIBLE);
                                   tv_refundRecord.setText("退款原因:"+ json.getJSONObject("RefundRecord").getString("Reason"));
                                }
                                String s = json.getString("goodList").trim();
                                JSONArray jsonArray = json.getJSONArray("goodList");
                                Log.e( "onSuccess: ",jsonArray.toString() );
                                tv_time.setText(json.getString("appointTime"));
                                tv_name.setText(json.getString("name"));
                                tv_store_name.setText(CommunityBossApplication.storeInfo.getStoreName());
                                tv_phone.setText(json.getString("telephone"));
                                tv_address.setText(json.getString("address"));
                                tv_remark.setText(json.getString("remark"));
                                preOrderAdapter = new PreOrderAdapter(OrderDetailActivity.this,orderDetail.getDetails());
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(OrderDetailActivity.this,LinearLayoutManager.VERTICAL,false);
                                goods_list.setLayoutManager(layoutManager);
                                goods_list.setAdapter(preOrderAdapter);
                                tv_total_price.setText("￥"+json.getString("actualPayPrice"));
                                if(!json.isNull("Appraise")) {
                                    JSONObject appraise = json.getJSONObject("Appraise");
                                    ll_comment.setVisibility(View.VISIBLE);
                                    initAppraise();
                                    rt_attitude.setSelectedNumber(appraise.getInt("Attitude"));
                                    rt_taste.setSelectedNumber(appraise.getInt("Taste"));
                                    rt_hygiene.setSelectedNumber(appraise.getInt("Hygiene"));
                                    Glide.with(OrderDetailActivity.this).load(Urls.IMAGEHEAD+appraise.getString("UserHeadImage")).into(img_head);
                                    tv_comment.setText(appraise.getString("Message"));
                                    tv_userphone.setText(appraise.getString("Username"));
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
                } else if(type ==1) {   //已接单

                    takeOrder(2);       //确认送出
                } else if(type ==2) {   //已送出

                    takeOrder(3);       //确认送达
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
    }

    public void takeOrder(int operation){
        OkGo.<String>post(Urls.ORDEROPERATION+"?appToken="+ CommunityBossApplication.token)
                .params("orderId",orderDetail.getOrderId())
                .params("operation",operation)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            body = body.substring(1,body.length()-1);
                            JSONObject json = new JSONObject(body.replaceAll("\\\\",""));
                            Log.d("确认订单",json.toString());
                            if (json.getInt("ResultCode") == 200) {
                                Toast.makeText(OrderDetailActivity.this, "操作成功", Toast.LENGTH_SHORT).show();
                                    btn_confirm.setVisibility(View.GONE);
                            } else {
                                Toast.makeText(OrderDetailActivity.this, json.getString("ResultValue"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
