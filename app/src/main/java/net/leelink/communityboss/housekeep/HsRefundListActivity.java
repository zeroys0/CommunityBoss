package net.leelink.communityboss.housekeep;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.R;
import net.leelink.communityboss.activity.BaseActivity;
import net.leelink.communityboss.adapter.OnCancelListener;
import net.leelink.communityboss.adapter.OnItemClickListener;
import net.leelink.communityboss.adapter.OnOrderListener;
import net.leelink.communityboss.adapter.OrderListAdapter;
import net.leelink.communityboss.adapter.RefundAdapter;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.bean.HsOrderBean;
import net.leelink.communityboss.bean.OrderBean;
import net.leelink.communityboss.fragment.UntakeOrderFragment;
import net.leelink.communityboss.housekeep.adapter.HsOrderAdapter;
import net.leelink.communityboss.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HsRefundListActivity extends BaseActivity implements OnCancelListener {
    private RecyclerView refund_list;
    private List<HsOrderBean> list = new ArrayList<>();
    private RelativeLayout rl_back;
    private HsOrderAdapter hsOrderAdapter;
    JSONArray jsonArray;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_list);
        init();
        initData();
    }

    public void init(){
        refund_list = findViewById(R.id.refund_list);

        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initData(){
        //获取订单列表

        OkGo.<String>get(Urls.getInstance().HS_ORDERLIST)
                .params("state","9,10")
                .params("pageNum",1)
                .params("pageSize",5)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("退款订单",json.toString());
                            if (json.getInt("status") == 200) {
                                Gson gson = new Gson();
                                json = json.getJSONObject("data");
                                jsonArray = json.getJSONArray("list");
                                list = gson.fromJson(jsonArray.toString(),new TypeToken<List<HsOrderBean>>(){}.getType());
                                hsOrderAdapter = new HsOrderAdapter(jsonArray,HsRefundListActivity.this,HsRefundListActivity.this);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(HsRefundListActivity.this,LinearLayoutManager.VERTICAL,false);
                                refund_list.setLayoutManager(layoutManager);
                                refund_list.setAdapter(hsOrderAdapter);
                            } else {
                                Toast.makeText(HsRefundListActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onItemClick(View view) {
        int position = refund_list.getChildLayoutPosition(view);
        Intent intent = new Intent(this, HsOrderDetailActivity.class);
        intent.putExtra("orderId",list.get(position).getOrderId());
        try {
            intent.putExtra("object",jsonArray.getJSONObject(position).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        intent.putExtra("state",9);
        startActivity(intent);
    }

    @Override
    public void onButtonClick(View view, int position) {
        operation(position,1);
    }

    @Override
    public void onCancel(View view, int position) {
        operation(position,0);
    }

    public void operation(final int position , int operation){
        OkGo.<String>post(Urls.getInstance().REFUND+"/"+list.get(position).getOrderId()+"/"+operation)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("确认退款",json.toString());
                            if (json.getInt("status") == 200) {
                                initData();
                            } else {

                            }
                            Toast.makeText(HsRefundListActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
