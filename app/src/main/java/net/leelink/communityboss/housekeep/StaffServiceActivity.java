package net.leelink.communityboss.housekeep;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import net.leelink.communityboss.R;
import net.leelink.communityboss.activity.BaseActivity;
import net.leelink.communityboss.activity.ConfirmWithdrawActivity;
import net.leelink.communityboss.activity.WithdrawActivity;
import net.leelink.communityboss.adapter.OnOrderListener;
import net.leelink.communityboss.bean.ServiceBean;
import net.leelink.communityboss.bean.StaffBean;
import net.leelink.communityboss.housekeep.adapter.StaffCheckAdapter;
import net.leelink.communityboss.housekeep.adapter.StaffServiceAdapter;
import net.leelink.communityboss.housekeep.fragment.StaffServiceFragment;
import net.leelink.communityboss.utils.Urls;
import net.leelink.communityboss.view.RecycleViewDivider;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class StaffServiceActivity extends BaseActivity implements OnOrderListener {
private SwipeRecyclerView service_list;
private Context context;
private int page = 1;
private List<ServiceBean> list = new ArrayList<>();
private StaffServiceAdapter staffServiceAdapter;
private ImageView img_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_service);
        init();
        initSlide();

    }

    public void init(){
        context = this;
        service_list = findViewById(R.id.service_list);
        img_add = findViewById(R.id.img_add);
        img_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context,AllServiceActivity.class);
                intent.putExtra("id",getIntent().getStringExtra("id"));
                startActivity(intent);
            }
        });
        service_list.addItemDecoration(new RecycleViewDivider(
                StaffServiceActivity.this, LinearLayoutManager.VERTICAL, 20, getResources().getColor(R.color.background)));

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    public void initData(){
        OkGo.<String>get(Urls.getInstance().FINDSERALLBYUSERID)
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
                            Log.d("已分配项目",json.toString());
                            if (json.getInt("status") == 200) {
                                json = json.getJSONObject("data");
                                JSONArray jsonArray = json.getJSONArray("list");
                                Gson gson = new Gson();
                                list = gson.fromJson(jsonArray.toString(),new TypeToken<List<ServiceBean>>(){}.getType());
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(StaffServiceActivity.this,LinearLayoutManager.VERTICAL,false);

                                staffServiceAdapter = new StaffServiceAdapter(list,StaffServiceActivity.this,StaffServiceActivity.this);
                                service_list.setLayoutManager(layoutManager);
                                service_list.setAdapter(staffServiceAdapter);
                            } else {
                                Toast.makeText(context, json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initSlide() {

        // srvWarn.setItemViewSwipeEnabled(true);// 开启滑动删除。默认关闭。
        service_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // 创建菜单：
        SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int viewType) {
           /* SwipeMenuItem deleteItem = new SwipeMenuItem(mContext);
            // 各种文字和图标属性设置。
            leftMenu.addMenuItem(deleteItem); // 在Item左侧添加一个菜单。*/
                // 2 删除
                SwipeMenuItem deleteItem = new SwipeMenuItem(StaffServiceActivity.this);
                deleteItem.setText("删除")
                        .setBackgroundColor(getResources().getColor(R.color.red))
                        .setTextColor(Color.WHITE) // 文字颜色。
                        .setTextSize(15) // 文字大小。
                        .setWidth(200)
                        .setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

                rightMenu.addMenuItem(deleteItem);

                // 注意：哪边不想要菜单，那么不要添加即可。
            }
        };
        // 设置监听器。
        service_list.setSwipeMenuCreator(mSwipeMenuCreator);
        service_list.setOnItemMenuClickListener(new OnItemMenuClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge, int position) {
                // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
                menuBridge.closeMenu();

                // 左侧还是右侧菜单：
                int direction = menuBridge.getDirection();
                // 菜单在Item中的Position：
                int menuPosition = menuBridge.getPosition();
                if (menuPosition == 0) {
//                    int id = list.get(position).getStoreId();
                    delete(position);
                }

            }
        });
        service_list.setOnItemClickListener(new com.yanzhenjie.recyclerview.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {

            }
        });


    }

    @Override
    public void onItemClick(View view) {

    }

    @Override
    public void onButtonClick(View view, int position) {

    }

    public void delete(final int position){
        OkGo.<String>delete(Urls.getInstance().SERPRODUCT+"/"+getIntent().getStringExtra("id")+"/"+list.get(position).getId())
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("删除项目",json.toString());
                            if (json.getInt("status") == 200) {
                                Toast.makeText(context, "删除成功", Toast.LENGTH_SHORT).show();
                                list.remove(position);
                                staffServiceAdapter.update(list);
                            } else {
                                Toast.makeText(context, json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
