package net.leelink.communityboss.housekeep;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.R;
import net.leelink.communityboss.activity.BaseActivity;
import net.leelink.communityboss.activity.LoginActivity;
import net.leelink.communityboss.adapter.OnOrderListener;
import net.leelink.communityboss.bean.StaffBean;
import net.leelink.communityboss.housekeep.adapter.StaffCheckAdapter;
import net.leelink.communityboss.housekeep.adapter.StaffListAdapter;
import net.leelink.communityboss.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static net.leelink.communityboss.activity.LoginActivity.setIndicator;

public class StaffManageActivity extends BaseActivity implements OnOrderListener {
private TabLayout tablayout;
private RecyclerView staff_list;
private StaffCheckAdapter staffCheckAdapter;
private Context context;
private List<StaffBean> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_manage);
        init();
        initData();

    }

    public void init(){
        context = this;
        tablayout = findViewById(R.id.tablayout);
        tablayout.addTab(tablayout.newTab().setText("员工审核"));
        tablayout.addTab(tablayout.newTab().setText("员工管理"));
        tablayout.addTab(tablayout.newTab().setText("添加服务"));
        tablayout.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(tablayout, 36, 36);
            }
        });

        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                initData();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        staff_list = findViewById(R.id.list);
    }

    public void initData(){
        OkGo.<String>get(Urls.VERTIFYSER)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("员工管理",json.toString());
                            if (json.getInt("status") == 200) {
                                JSONArray jsonArray = json.getJSONArray("data");
                                Gson gson = new Gson();
                                list = gson.fromJson(jsonArray.toString(),new TypeToken<List<StaffBean>>(){}.getType());
                                staffCheckAdapter = new StaffCheckAdapter(list,context,StaffManageActivity.this);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
                                staff_list.setLayoutManager(layoutManager);
                                staff_list.setAdapter(staffCheckAdapter);
                            } else {
                                Toast.makeText(StaffManageActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
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
        Intent intent = new Intent(this,StaffCheckActivity.class);
        intent.putExtra("staff",list.get(position) );
        startActivity(intent);
    }

    @Override
    public void onButtonClick(View view, int position) {

    }
}
