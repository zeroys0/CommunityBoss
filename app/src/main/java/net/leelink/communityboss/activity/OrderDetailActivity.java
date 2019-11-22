package net.leelink.communityboss.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.R;
import net.leelink.communityboss.adapter.PreOrderAdapter;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.bean.Event;
import net.leelink.communityboss.bean.OrderDetail;
import net.leelink.communityboss.utils.Urls;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends BaseActivity implements View.OnClickListener {
private RecyclerView goods_list;
private PreOrderAdapter preOrderAdapter;
private List<OrderDetail.DetailsBean> list = new ArrayList<>();
private TextView tv_state,tv_orderid,tv_time,tv_name,tv_phone,tv_address,tv_remark,tv_total_price;
private OrderDetail orderDetail;
private LinearLayout ll_comment;
private int type;
private Button btn_confirm;

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
        btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
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
        }
    }

    public void initData(){
        final String orderId = getIntent().getStringExtra("orderId");
        tv_orderid.setText(orderId);
        OkGo.<String>get(Urls.ORDERDETAILS+"?appToken="+ CommunityBossApplication.token)
                .params("orderId",orderId)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            body = body.substring(1,body.length()-1);
                            JSONObject json = new JSONObject(body.replaceAll("\\\\",""));
                            Log.d("订单详情",json.toString());
                            if (json.getInt("ResultCode") == 200) {
                                json = json.getJSONObject("ObjectData");
                                Gson gson = new Gson();
                                orderDetail = gson.fromJson(json.toString(),OrderDetail.class);
                                tv_name.setText(orderDetail.getDeliveryName());
                                tv_time.setText(orderDetail.getDeliveryTime());
                                tv_phone.setText(orderDetail.getDeliveryPhone());
                                tv_address.setText(orderDetail.getDeliveryAddress());
                                tv_remark.setText(orderDetail.getRemarks());
                                preOrderAdapter = new PreOrderAdapter(OrderDetailActivity.this,orderDetail.getDetails());
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(OrderDetailActivity.this,LinearLayoutManager.VERTICAL,false);
                                goods_list.setLayoutManager(layoutManager);
                                goods_list.setAdapter(preOrderAdapter);
                                tv_total_price.setText("￥"+orderDetail.getTotalPrice());

                            } else {
                                Toast.makeText(OrderDetailActivity.this, json.getString("ResultValue"), Toast.LENGTH_LONG).show();
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

                    takeOrder(1);
                } else if(type ==1) {   //已接单

                    takeOrder(2);
                } else if(type ==2) {   //已送出

                    takeOrder(3);
                }
                break;

                default:
                    break;
        }
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
