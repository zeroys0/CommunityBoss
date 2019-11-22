package net.leelink.communityboss.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import net.leelink.communityboss.adapter.OnCancelListener;
import net.leelink.communityboss.adapter.OnItemClickListener;
import net.leelink.communityboss.adapter.OnOrderListener;
import net.leelink.communityboss.adapter.OrderListAdapter;
import net.leelink.communityboss.adapter.RefundAdapter;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.bean.OrderBean;
import net.leelink.communityboss.fragment.UntakeOrderFragment;
import net.leelink.communityboss.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class RefundListActivity extends BaseActivity implements OnCancelListener {
private RecyclerView refund_list;
    private RefundAdapter refundAdapter;
    private List<OrderBean> list = new ArrayList<>();
    private RelativeLayout rl_back;
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

        OkGo.<String>get(Urls.ORDERLIST+"?appToken="+ CommunityBossApplication.token+"&type=7,8,9"+"&orderId="+0)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            body = body.substring(1,body.length()-1);
                            JSONObject json = new JSONObject(body.replaceAll("\\\\",""));
                            Log.d("退款订单",json.toString());
                            if (json.getInt("ResultCode") == 200) {
                                Gson gson = new Gson();
                                JSONArray jsonArray = json.getJSONArray("ObjectData");
                                list = gson.fromJson(jsonArray.toString(),new TypeToken<List<OrderBean>>(){}.getType());
                                refundAdapter = new RefundAdapter(list,RefundListActivity.this,RefundListActivity.this);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(RefundListActivity.this,LinearLayoutManager.VERTICAL,false);
                                refund_list.setLayoutManager(layoutManager);
                                refund_list.setAdapter(refundAdapter);
                            } else {

                            }
                            Toast.makeText(RefundListActivity.this, json.getString("ResultValue"), Toast.LENGTH_LONG).show();
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
        Toast.makeText(this, "那我挺牛逼的了", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCancel(View view, int position) {
        Toast.makeText(this, "那我挺牛逼的了", Toast.LENGTH_SHORT).show();
    }
}
