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
import net.leelink.communityboss.bean.HsOrderBean;
import net.leelink.communityboss.bean.OrderBean;
import net.leelink.communityboss.fragment.BaseFragment;
import net.leelink.communityboss.fragment.UntakeOrderFragment;
import net.leelink.communityboss.housekeep.HsOrderDetailActivity;
import net.leelink.communityboss.housekeep.adapter.HsOrderAdapter;
import net.leelink.communityboss.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HsUntakeFragment extends BaseFragment implements OnOrderListener {

    private RecyclerView list_order;

    private List<HsOrderBean> list = new ArrayList<>();
    private TwinklingRefreshLayout refreshLayout;
    private int page = 1;
    private  boolean hasNextPage = false;
    private HsOrderAdapter hsOrderAdapter;
    @Override
    public void handleCallBack(Message msg) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_untake_order,container,false);
        init(view);
        initData();
        initRefreshLayout(view);
        return view;
    }
    public void init(View view){
        list_order = view.findViewById(R.id.list_order);
    }


    public void initData(){

        //获取订单列表

        OkGo.<String>get(Urls.HS_ORDERLIST)
                .params("state",1)
                .params("pageNum",page)
                .params("pageSize",10)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("未接订单",json.toString());
                            if (json.getInt("status") == 200) {
                                Gson gson = new Gson();
                                json = json.getJSONObject("data");
                                hasNextPage = json.getBoolean("hasNextPage");
                                JSONArray jsonArray = json.getJSONArray("list");
                                List<HsOrderBean> orderBeanslist = gson.fromJson(jsonArray.toString(),new TypeToken<List<HsOrderBean>>(){}.getType());
                                list.addAll(orderBeanslist);
                                hsOrderAdapter = new HsOrderAdapter(list,getContext(), HsUntakeFragment.this);
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
        Intent intent = new Intent(getContext(), HsOrderDetailActivity.class);
        intent.putExtra("orderDetail",list.get(position));
        intent.putExtra("type",1);
        startActivity(intent);
    }

    //确认接单
    @Override
    public void onButtonClick(View view, final int position) {
        OkGo.<String>post(Urls.HS_ORDERSTATE)
                .params("orderId",list.get(position).getOrderId())
                .params("state",2)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("确认订单",json.toString());
                            if (json.getInt("status") == 200) {
                                list.remove(position);
                                hsOrderAdapter.notifyDataSetChanged();
                                Toast.makeText(getContext(), "订单已确认,请尽快完成吧~", Toast.LENGTH_SHORT).show();
                            } else {

                            }
                            Toast.makeText(getContext(), json.getString("message"), Toast.LENGTH_LONG).show();
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
                        page = 1;

                        list.clear();
                        initData();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishLoadmore();
                        if(hasNextPage){
                            page++;
                            initData();
                        }
                        hsOrderAdapter.update(list);
                        list_order.scrollToPosition(hsOrderAdapter.getItemCount()-1);
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
