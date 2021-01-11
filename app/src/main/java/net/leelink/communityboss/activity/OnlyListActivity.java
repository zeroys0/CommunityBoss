package net.leelink.communityboss.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.Footer.LoadingView;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.R;
import net.leelink.communityboss.adapter.OnOrderListener;
import net.leelink.communityboss.adapter.OrderListAdapter;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.bean.DsTakeOrderRefresh;
import net.leelink.communityboss.bean.OrderBean;
import net.leelink.communityboss.bean.StoreInfo;
import net.leelink.communityboss.fragment.TakeOrderFragment;
import net.leelink.communityboss.utils.Acache;
import net.leelink.communityboss.utils.Urls;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class OnlyListActivity extends BaseActivity implements OnOrderListener {
    private TwinklingRefreshLayout refreshLayout;
    List<OrderBean> list = new ArrayList<>();
    Context context;
    private OrderListAdapter orderListAdapter;
    RecyclerView list_order;
    private RelativeLayout rl_back;
    int page = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_only_list);
        init();
        context = this;
        initList();
    }

    public void init(){
        list_order = findViewById(R.id.list_order);
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initList(){
        //获取订单列表

        OkGo.<String>get(Urls.getInstance().ORDERLIST)
                .params("state",11)
                .params("pageNum",page)
                .params("pageSize",10)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("自提订单列表",json.toString());
                            if (json.getInt("status") == 200) {
                                Gson gson = new Gson();
                                json = json.getJSONObject("data");
                                JSONArray jsonArray = json.getJSONArray("list");
                                List<OrderBean> orderBeanlist = gson.fromJson(jsonArray.toString(),new TypeToken<List<OrderBean>>(){}.getType());
                                list.addAll(orderBeanlist);
                                orderListAdapter = new OrderListAdapter(list,context, OnlyListActivity.this);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
                                list_order.setLayoutManager(layoutManager);
                                list_order.setAdapter(orderListAdapter);
                            } else if(json.getInt("status") == 505) {
                                final SharedPreferences sp = getSharedPreferences("sp", 0);
                                if (!sp.getString("secretKey", "").equals("")) {
                                    OkGo.<String>post(Urls.getInstance().QUICKLOGIN)
                                            .params("telephone", sp.getString("telephone", ""))
                                            .params("secretKey", sp.getString("secretKey", ""))
                                            .params("deviceToken", JPushInterface.getRegistrationID(context))
                                            .tag(this)
                                            .execute(new StringCallback() {
                                                @Override
                                                public void onSuccess(Response<String> response) {
                                                    try {
                                                        String body = response.body();
                                                        JSONObject json = new JSONObject(body);
                                                        Log.d("快速登录", json.toString());
                                                        if (json.getInt("status") == 200) {
                                                            JSONObject jsonObject = json.getJSONObject("data");
                                                            Gson gson = new Gson();
                                                            Acache.get(context).put("storeInfo",jsonObject);
                                                            StoreInfo storeInfo = gson.fromJson(jsonObject.toString(), StoreInfo.class);
                                                            CommunityBossApplication.storeInfo = storeInfo;
                                                            initList();
                                                        } else {
                                                            Toast.makeText(context, "登录失效,请重新登录", Toast.LENGTH_SHORT).show();
                                                            Intent intent4 = new Intent(context, LoginActivity.class);
                                                            SharedPreferences sp = getSharedPreferences("sp",0);
                                                            SharedPreferences.Editor editor = sp.edit();
                                                            editor.remove("secretKey");
                                                            editor.remove("telephone");
                                                            editor.apply();
                                                            intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent4);
                                                            finish();

                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                }
                            }else {
                                Toast.makeText(context, json.getString("message"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void initRefreshLayout(View view) {
        refreshLayout = findViewById(R.id.refreshLayout);
        SinaRefreshView headerView = new SinaRefreshView(this);
        headerView.setTextColor(0xff745D5C);
//        refreshLayout.setHeaderView((new ProgressLayout(getActivity())));
        refreshLayout.setHeaderView(headerView);
        refreshLayout.setBottomView(new LoadingView(this));
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefreshing();
                        list.clear();
                        initList();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishLoadmore();
                        page++;
                        initList();
                        orderListAdapter.update(list);
                    }
                }, 1000);
            }

        });
        // 是否允许开启越界回弹模式
        refreshLayout.setEnableOverScroll(false);
        //禁用掉加载更多效果，即上拉加载更多
        refreshLayout.setEnableLoadmore(true);
        // 是否允许越界时显示刷新控件
        refreshLayout.setOverScrollRefreshShow(true);


    }

    @Override
    public void onItemClick(View view) {
        int position = list_order.getChildLayoutPosition(view);
        Intent intent = new Intent(context, OrderDetailActivity.class);
        intent.putExtra("orderId",list.get(position).getOrderId());
            intent.putExtra("type",5);
        startActivity(intent);
    }

    @Override
    public void onButtonClick(View view, final int position) {
        OkGo.<String>post(Urls.getInstance().ORDER_ONLY+"/"+list.get(position).getOrderId())
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("确认用户自提",json.toString());
                            if (json.getInt("status") == 200) {
                                list.remove(position);
                                EventBus.getDefault().post(new DsTakeOrderRefresh());
                                orderListAdapter.notifyDataSetChanged();
                                Toast.makeText(context, "用户自提完成", Toast.LENGTH_SHORT).show();
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
