package net.leelink.communityboss.fragment;

import android.content.Intent;
import android.os.Bundle;
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
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.R;
import net.leelink.communityboss.activity.OrderDetailActivity;
import net.leelink.communityboss.adapter.OnItemClickListener;
import net.leelink.communityboss.adapter.OnOrderListener;
import net.leelink.communityboss.adapter.OrderListAdapter;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.bean.OrderBean;
import net.leelink.communityboss.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static net.leelink.communityboss.activity.LoginActivity.setIndicator;

public class TakeOrderFragment extends  BaseFragment implements OnOrderListener {
    private RecyclerView list_order;
    private OrderListAdapter orderListAdapter;
    private List<OrderBean> list = new ArrayList<>();
    private TabLayout tablayout;
    private int type = 3;
    @Override
    public void handleCallBack(Message msg) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_take_order,container,false);
        init(view);
        initData();
        return view;
    }

    public void init(View view){
        tablayout = view.findViewById(R.id.tablayout);
        tablayout.addTab(tablayout.newTab().setText("未派送"));
        tablayout.addTab(tablayout.newTab().setText("已派送"));
        tablayout.post(new Runnable() {
            @Override
            public void run() {
                setIndicator(tablayout, 36, 36);
            }
        });
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0){
                    type = 3;
                    initData();
                } else {
                    type = 4;
                    initData();
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

    public void initData(){
        //获取订单列表

        OkGo.<String>get(Urls.ORDERLIST+"?appToken="+ CommunityBossApplication.token+"&type="+type+"&orderId="+0)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            body = body.substring(1,body.length()-1);
                            JSONObject json = new JSONObject(body.replaceAll("\\\\",""));
                            Log.d("已接单",json.toString());
                            if (json.getInt("ResultCode") == 200) {
                                Gson gson = new Gson();
                                JSONArray jsonArray = json.getJSONArray("ObjectData");
                                list = gson.fromJson(jsonArray.toString(),new TypeToken<List<OrderBean>>(){}.getType());
                                orderListAdapter = new OrderListAdapter(list,getContext(),TakeOrderFragment.this);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                                list_order.setLayoutManager(layoutManager);
                                list_order.setAdapter(orderListAdapter);
                            } else {

                            }
                            Toast.makeText(getContext(), json.getString("ResultValue"), Toast.LENGTH_LONG).show();
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
        if(type==3) {
            intent.putExtra("type",1);
        } else {
            intent.putExtra("type",2);
        }
        startActivity(intent);
    }

    @Override
    public void onButtonClick(View view, final int position) {
        int operation;
        if(type == 3){
            operation = 2;  //开始派送
        } else  {
            operation = 3;  //确认送达
        }
        OkGo.<String>post(Urls.ORDEROPERATION+"?appToken="+ CommunityBossApplication.token)
                .params("orderId",list.get(position).getOrderId())
                .params("operation",operation)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            body = body.substring(1,body.length()-1);
                            JSONObject json = new JSONObject(body.replaceAll("\\\\",""));
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
}
