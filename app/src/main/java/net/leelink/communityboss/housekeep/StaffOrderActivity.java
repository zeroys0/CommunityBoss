package net.leelink.communityboss.housekeep;

import android.content.Context;
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
import net.leelink.communityboss.adapter.OnOrderListener;
import net.leelink.communityboss.bean.HsOrderBean;
import net.leelink.communityboss.bean.SerWorkBean;
import net.leelink.communityboss.housekeep.adapter.HsOrderAdapter;
import net.leelink.communityboss.housekeep.adapter.SerWorkAdapter;
import net.leelink.communityboss.housekeep.fragment.HsTakeFragment;
import net.leelink.communityboss.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class StaffOrderActivity extends BaseActivity implements View.OnClickListener, OnOrderListener {
    RecyclerView order_list;
    private RelativeLayout rl_back;
    private SerWorkAdapter serWorkAdapter;
    private List<SerWorkBean> list = new ArrayList<>();
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_order);
        init();
        initData();
    }

    public void init() {
        context = this;
        order_list = findViewById(R.id.order_list);
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
    }

    public void initData() {
        OkGo.<String>get(Urls.getInstance().FINDSERWORKBYUSERID)
                .params("userId", getIntent().getStringExtra("userId"))
                .params("pageNum", 1)
                .params("pageSize", 10)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("已分配订单", json.toString());
                            if (json.getInt("status") == 200) {
                                Gson gson = new Gson();
                                json = json.getJSONObject("data");
                                JSONArray jsonArray = json.getJSONArray("list");
                                List<SerWorkBean> orderBeanlist = gson.fromJson(jsonArray.toString(), new TypeToken<List<SerWorkBean>>() {
                                }.getType());
                                list.addAll(orderBeanlist);
                                serWorkAdapter = new SerWorkAdapter(list, context, StaffOrderActivity.this);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
                                order_list.setLayoutManager(layoutManager);
                                order_list.setAdapter(serWorkAdapter);
                            } else {
                                Toast.makeText(context, json.getString("message"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
        }
    }

    @Override
    public void onItemClick(View view) {

    }

    @Override
    public void onButtonClick(View view, int position) {

    }
}
