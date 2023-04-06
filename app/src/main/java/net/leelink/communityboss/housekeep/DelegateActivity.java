package net.leelink.communityboss.housekeep;

import android.content.Context;
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
import net.leelink.communityboss.adapter.OnOrderListener;
import net.leelink.communityboss.bean.DelegateBean;
import net.leelink.communityboss.bean.DelegateCallBack;
import net.leelink.communityboss.bean.Event;
import net.leelink.communityboss.bean.HsOrderRefresh;
import net.leelink.communityboss.housekeep.adapter.StaffListAdapter;
import net.leelink.communityboss.utils.Urls;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Delayed;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DelegateActivity extends BaseActivity implements OnOrderListener {
    private RecyclerView staff_list;
    private StaffListAdapter staffListAdapter;
    private Context context;
    private int page = 1;
    private RelativeLayout rl_back;
    private List<DelegateBean> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delegate);
        init();
        initData();
    }

    public void init(){

        staff_list = findViewById(R.id.staff_list);
        context = this;
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }

        });
    }



    public void initData(){
        Log.d( "initData: ",getIntent().getStringExtra("id"));
        OkGo.<String>get(Urls.getInstance().WORKSER)
                .params("pangeNum",page)
                .params("pageSize",10)
                .params("productId",getIntent().getStringExtra("id"))
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("可派工人员",json.toString());
                            if (json.getInt("status") == 200) {
                                json = json.getJSONObject("data");
                                JSONArray jsonArray = json.getJSONArray("list");
                                Gson gson = new Gson();
                                list = gson.fromJson(jsonArray.toString(),new TypeToken<List<DelegateBean>>(){}.getType());
                                staffListAdapter = new StaffListAdapter(list,context, DelegateActivity.this);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
                                staff_list.setLayoutManager(layoutManager);
                                staff_list.setAdapter(staffListAdapter);
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
    public void onItemClick(View view) {
        int position = staff_list.getChildLayoutPosition(view);

        Intent intent = new Intent( this,StaffOrderActivity.class);
        intent.putExtra("userId",list.get(position).getUserId());
        startActivity(intent);
    }

    @Override
    public void onButtonClick(View view, int position) {
        OkGo.<String>post(Urls.getInstance().HS_ORDERSTATE+"?"+"orderId="+getIntent().getStringExtra("orderId")+"&userId="+list.get(position).getUserId()+"&state=3")
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("确认订单",json.toString());
                            if (json.getInt("status") == 200) {
                                Toast.makeText(DelegateActivity.this, "派单成功", Toast.LENGTH_SHORT).show();
                            //    EventBus.getDefault().post(new HsOrderRefresh());
                                EventBus.getDefault().post(new DelegateCallBack());
                                finish();
                            } else {
                                Toast.makeText(DelegateActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
