package net.leelink.communityboss.housekeep.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import net.leelink.communityboss.bean.OrderBean;
import net.leelink.communityboss.fragment.BaseFragment;
import net.leelink.communityboss.fragment.CompleteOrderFragment;
import net.leelink.communityboss.housekeep.adapter.HsOrderAdapter;
import net.leelink.communityboss.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HsCompleteFragment extends BaseFragment implements OnOrderListener {
    private RecyclerView list_order;
    private OrderListAdapter orderListAdapter;
    private List<OrderBean> list = new ArrayList<>();
    private TwinklingRefreshLayout refreshLayout;
    private String orderId = "0";
    private HsOrderAdapter hsOrderAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complete_order,container,false);
        init(view);
        initData(orderId);
        initRefreshLayout(view);
        return view;
    }

    public void init(View view){
        list_order = view.findViewById(R.id.list_order);

    }
    public void initData(String orderId){
        //获取订单列表

        OkGo.<String>get(Urls.ORDERLIST)
                .params("state","6,7")
                .params("pageNum",1)
                .params("pageSize",10)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("已处理",json.toString());
                            if (json.getInt("status") == 200) {
                                Gson gson = new Gson();
                                json = json.getJSONObject("data");
                                JSONArray jsonArray = json.getJSONArray("list");
                                List<OrderBean> orderBeanslist = gson.fromJson(jsonArray.toString(),new TypeToken<List<OrderBean>>(){}.getType());
                                list.addAll(orderBeanslist);
                                hsOrderAdapter = new HsOrderAdapter(list,getContext(), HsCompleteFragment.this);
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

    @Override
    public void onItemClick(View view) {
        int position = list_order.getChildLayoutPosition(view);
        Intent intent = new Intent(getContext(), OrderDetailActivity.class);
        intent.putExtra("orderId",list.get(position).getOrderId());
        intent.putExtra("type",3);
        startActivity(intent);
    }

    @Override
    public void onButtonClick(View view, int position) {

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
                        orderId = "0";
                        initData(orderId);
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
                        initData(orderId);
                        orderListAdapter.update(list);
                        list_order.scrollToPosition(orderListAdapter.getItemCount()-1);
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