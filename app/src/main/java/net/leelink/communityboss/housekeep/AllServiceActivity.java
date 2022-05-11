package net.leelink.communityboss.housekeep;

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
import net.leelink.communityboss.bean.ServiceBean;
import net.leelink.communityboss.housekeep.adapter.StaffServiceAdapter;
import net.leelink.communityboss.utils.Urls;
import net.leelink.communityboss.view.RecycleViewDivider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class AllServiceActivity extends BaseActivity implements OnOrderListener {
private RelativeLayout rl_back;
    private List<ServiceBean> list = new ArrayList<>();
    private StaffServiceAdapter staffServiceAdapter;
    private int page = 1;
    private RecyclerView service_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_service);
        init();
        initData();
    }

    public void init(){
        rl_back= findViewById(R.id.rl_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        service_list = findViewById(R.id.service_list);
    }

    public void initData(){
        OkGo.<String>get(Urls.getInstance().FINDSERBYUSERID)
                .params("pageNum",page)
                .params("pageSize",10)
                .params("userId",getIntent().getStringExtra("id"))
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("未分配项目",json.toString());
                            if (json.getInt("status") == 200) {
                                json = json.getJSONObject("data");
                                JSONArray jsonArray = json.getJSONArray("list");
                                Gson gson = new Gson();
                                list = gson.fromJson(jsonArray.toString(),new TypeToken<List<ServiceBean>>(){}.getType());
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(AllServiceActivity.this,LinearLayoutManager.VERTICAL,false);
                                service_list.addItemDecoration(new RecycleViewDivider(
                                        AllServiceActivity.this, LinearLayoutManager.VERTICAL, 20, getResources().getColor(R.color.background)));
                                staffServiceAdapter = new StaffServiceAdapter(list,AllServiceActivity.this,AllServiceActivity.this);
                                service_list.setLayoutManager(layoutManager);
                                service_list.setAdapter(staffServiceAdapter);
                            } else {
                                Toast.makeText(AllServiceActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onItemClick(View view) {
        int position = service_list.getChildLayoutPosition(view);
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("shUserId",getIntent().getStringExtra("id"));
            jsonObject.put("providerProductId",list.get(position).getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        OkGo.<String>post(Urls.getInstance().SERPRODUCT)
                .upJson(jsonObject)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("添加服务",json.toString());
                            if (json.getInt("status") == 200) {
                                Toast.makeText(AllServiceActivity.this, "添加成功", Toast.LENGTH_LONG).show();
                                finish();
                            } else {
                                Toast.makeText(AllServiceActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    @Override
    public void onButtonClick(View view, int position) {

    }
}
