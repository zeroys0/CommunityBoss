package net.leelink.communityboss.housekeep.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import net.leelink.communityboss.activity.OrderDetailActivity;
import net.leelink.communityboss.adapter.OnOrderListener;
import net.leelink.communityboss.adapter.OrderListAdapter;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.bean.HsOrderBean;
import net.leelink.communityboss.bean.OrderBean;
import net.leelink.communityboss.bean.WorkBean;
import net.leelink.communityboss.fragment.BaseFragment;
import net.leelink.communityboss.fragment.TakeOrderFragment;
import net.leelink.communityboss.housekeep.DelegateActivity;
import net.leelink.communityboss.housekeep.HsOrderDetailActivity;
import net.leelink.communityboss.housekeep.adapter.HsOrderAdapter;
import net.leelink.communityboss.housekeep.adapter.WorkOrderAdapter;
import net.leelink.communityboss.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static net.leelink.communityboss.activity.LoginActivity.setIndicator;

public class HsTakeFragment extends BaseFragment implements OnOrderListener {
    private RecyclerView list_order;
    private List<HsOrderBean> list = new ArrayList<>();
    private TabLayout tablayout;
    private String type = "2";
    private TwinklingRefreshLayout refreshLayout;
    private String orderId = "0";
    private HsOrderAdapter hsOrderAdapter;
    private WorkOrderAdapter workOrderAdapter;
    private List<WorkBean> list_w = new ArrayList<>();
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_take_order,container,false);
        init(view);
        initData(type,orderId);
        initRefreshLayout(view);
        return view;
    }
    public void init(View view){
        tablayout = view.findViewById(R.id.tablayout);
        tablayout.addTab(tablayout.newTab().setText("未派工"));
        tablayout.addTab(tablayout.newTab().setText("已派工"));
        tablayout.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(tablayout, 36, 36);
            }
        });

        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                list.clear();
                list_w.clear();
                orderId = "0";
                if(tab.getPosition() == 0){
                    type = "2";
                    initData(type,orderId);
                } else {
//                    type = "3,4";
//                    initData(type,orderId);
                    type = "3";
                    initWorkList();
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
    }

    public void initData(String type,String orderId){
        //获取订单列表

        OkGo.<String>get(Urls.HS_ORDERLIST)
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
                                List<HsOrderBean> orderBeanlist = gson.fromJson(jsonArray.toString(),new TypeToken<List<HsOrderBean>>(){}.getType());
                                list.addAll(orderBeanlist);
                                hsOrderAdapter = new HsOrderAdapter(list,getContext(), HsTakeFragment.this);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                                list_order.setLayoutManager(layoutManager);
                                list_order.setAdapter(hsOrderAdapter);
                            } else {
                                Toast.makeText(getContext(), json.getString("message"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void initWorkList(){
        OkGo.<String>get(Urls.WORKLIST)
                .params("state","1,2,3")
                .params("pageNum",1)
                .params("pageSize",10)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("工单列表",json.toString());
                            if (json.getInt("status") == 200) {
                                Gson gson = new Gson();
                                json = json.getJSONObject("data");
                                JSONArray jsonArray = json.getJSONArray("list");
                                List<WorkBean> workBeanList = gson.fromJson(jsonArray.toString(),new TypeToken<List<WorkBean>>(){}.getType());
                                list_w.addAll(workBeanList);
                                workOrderAdapter = new WorkOrderAdapter(list_w,getContext(), HsTakeFragment.this);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                                list_order.setLayoutManager(layoutManager);
                                list_order.setAdapter(workOrderAdapter);
                            } else {
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
        int position = list_order.getChildLayoutPosition(view);
        Intent intent = new Intent(getContext(), HsOrderDetailActivity.class);

        if(type.equals("2")) {
            intent.putExtra("type",2);
            intent.putExtra("orderId",list.get(position).getOrderId());
            intent.putExtra("orderDetail",list.get(position));
        } else {
            intent.putExtra("type",3);
            intent.putExtra("orderDetail",list_w.get(position));

        }
        startActivity(intent);
    }

    @Override
    public void onButtonClick(View view, final int position) {
        int operation;
        if(type .equals( "2")){
            Intent intent = new Intent(getContext(), DelegateActivity.class);
            intent.putExtra("id",list.get(position).getProductId());
            intent.putExtra("orderId",list.get(position).getOrderId());
            startActivity(intent);
        } else  {
            operation = 3;  //确认送达
        }

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
                        orderId = "0";
                        list.clear();
                        initData(type,orderId);
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishLoadmore();
                        orderId = list.get(list.size()-1).getOrderId();
                        initData(type,orderId);
                        hsOrderAdapter.update(list);
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
    public void handleCallBack(Message msg) {

    }
}
