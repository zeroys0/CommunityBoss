package net.leelink.communityboss.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
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
import net.leelink.communityboss.activity.LoginActivity;
import net.leelink.communityboss.activity.OnlyListActivity;
import net.leelink.communityboss.activity.OrderDetailActivity;
import net.leelink.communityboss.adapter.OnItemClickListener;
import net.leelink.communityboss.adapter.OnOrderListener;
import net.leelink.communityboss.adapter.OrderListAdapter;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.bean.DsTakeOrderRefresh;
import net.leelink.communityboss.bean.OrderBean;
import net.leelink.communityboss.bean.StoreInfo;
import net.leelink.communityboss.bean.TakeOrderRefresh;
import net.leelink.communityboss.utils.Acache;
import net.leelink.communityboss.utils.Urls;
import net.leelink.communityboss.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.jpush.android.api.JPushInterface;

public class TakeOrderFragment extends  BaseFragment implements OnOrderListener {
    private RecyclerView list_order;
    private OrderListAdapter orderListAdapter;
    private List<OrderBean> list = new ArrayList<>();
    private TabLayout tablayout;
    private String type = "3,4";
    private TwinklingRefreshLayout refreshLayout;
    private String orderId = "0";
    private TextView tv_only;
    @Override
    public void handleCallBack(Message msg) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_take_order,container,false);
        init(view);
        initData(type);
        initRefreshLayout(view);
        return view;
    }

    public void init(View view){
        EventBus.getDefault().register(this);
        tablayout = view.findViewById(R.id.tablayout);
        tablayout.addTab(tablayout.newTab().setText("未派送"));
        tablayout.addTab(tablayout.newTab().setText("已派送"));

        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                list.clear();
                orderId = "0";
                if(tab.getPosition() == 0){
                    type = "3,4";
                    initData(type);
                } else {
                    type = "5";
                    initData(type);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        list_order = view.findViewById(R.id.list_order);
        tv_only= view.findViewById(R.id.tv_only);
        tv_only.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), OnlyListActivity.class);
                startActivity(intent);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DsTakeOrderRefresh event) {
        list.clear();
        initData(type);
    }

    public void initData(final String type){
        //获取订单列表

        OkGo.<String>get(Urls.getInstance().ORDERLIST)
                .params("state",type)
                .params("pageNum",1)
                .params("pageSize",10)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("已接单",json.toString());
                            if (json.getInt("status") == 200) {
                                Gson gson = new Gson();
                                json = json.getJSONObject("data");
                                JSONArray jsonArray = json.getJSONArray("list");
                                List<OrderBean> orderBeanlist = gson.fromJson(jsonArray.toString(),new TypeToken<List<OrderBean>>(){}.getType());
                                list.addAll(orderBeanlist);
                                orderListAdapter = new OrderListAdapter(list,getContext(),TakeOrderFragment.this);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                                list_order.setLayoutManager(layoutManager);
                                list_order.setAdapter(orderListAdapter);
                            } else if(json.getInt("status") == 505) {
                                final SharedPreferences sp = getActivity().getSharedPreferences("sp", 0);
                                if (!sp.getString("secretKey", "").equals("")) {
                                    OkGo.<String>post(Urls.getInstance().QUICKLOGIN)
                                            .params("telephone", sp.getString("telephone", ""))
                                            .params("secretKey", sp.getString("secretKey", ""))
                                            .params("deviceToken", JPushInterface.getRegistrationID(getContext()))
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
                                                            Acache.get(getContext()).put("storeInfo",jsonObject);
                                                            StoreInfo storeInfo = gson.fromJson(jsonObject.toString(), StoreInfo.class);
                                                            CommunityBossApplication.storeInfo = storeInfo;
                                                            initData(type);
                                                        } else {
                                                            Toast.makeText(getContext(), "登录失效,请重新登录", Toast.LENGTH_SHORT).show();
                                                            Intent intent4 = new Intent(getContext(), LoginActivity.class);
                                                            SharedPreferences sp = getActivity().getSharedPreferences("sp",0);
                                                            SharedPreferences.Editor editor = sp.edit();
                                                            editor.remove("secretKey");
                                                            editor.remove("telephone");
                                                            editor.apply();
                                                            intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                            startActivity(intent4);
                                                            getActivity().finish();

                                                        }
                                                    } catch (JSONException e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                            });
                                }
                            }else {
                                Toast.makeText(getContext(), json.getString("message"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onItemClick(View view) {
        if(Utils.isFastClick()){
            return;
        }
        int position = list_order.getChildLayoutPosition(view);
        Intent intent = new Intent(getContext(), OrderDetailActivity.class);
        intent.putExtra("orderId",list.get(position).getOrderId());
        if(type.equals("3,4")) {
            intent.putExtra("type",1);
        } else {
            intent.putExtra("type",2);
        }
        startActivity(intent);
    }

    @Override
    public void onButtonClick(View view, final int position) {
        if(Utils.isFastClick()){
            return;
        }
        int operation;
        if(type .equals( "3,4")){
            operation = 2;  //开始派送
        } else  {
            operation = 3;  //确认送达
        }
        OkGo.<String>post(Urls.getInstance().ORDEROPERATION+"?appToken="+ CommunityBossApplication.token)
                .params("orderId",list.get(position).getOrderId())
                .params("operation",operation)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("确认订单",json.toString());
                            if (json.getInt("ResultCode") == 200) {
                                list.remove(position);
                                orderListAdapter.notifyDataSetChanged();
                            } else {

                            }
                            Toast.makeText(getContext(), json.getString("ResultValue"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void initRefreshLayout(View view) {
        refreshLayout = (TwinklingRefreshLayout) view.findViewById(R.id.refreshLayout);
        SinaRefreshView headerView = new SinaRefreshView(getContext());
        headerView.setTextColor(0xff745D5C);
//        refreshLayout.setHeaderView((new ProgressLayout(getActivity())));
        refreshLayout.setHeaderView(headerView);
        refreshLayout.setBottomView(new LoadingView(getContext()));
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefreshing();
                        list.clear();
                        initData(type);
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishLoadmore();
                        initData(type);
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
}
