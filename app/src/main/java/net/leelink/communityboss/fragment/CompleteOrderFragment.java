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

public class CompleteOrderFragment extends BaseFragment implements OnOrderListener {
    private RecyclerView list_order;
    private OrderListAdapter orderListAdapter;
    private List<OrderBean> list = new ArrayList<>();
    @Override
    public void handleCallBack(Message msg) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complete_order,container,false);
        init(view);
        initData();
        return view;
    }

    public void init(View view){
        list_order = view.findViewById(R.id.list_order);
        orderListAdapter = new OrderListAdapter(list,getContext(),this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        list_order.setLayoutManager(layoutManager);
        list_order.setAdapter(orderListAdapter);
    }

    public void initData(){
        //获取订单列表

        OkGo.<String>get(Urls.ORDERLIST+"?appToken="+ CommunityBossApplication.token+"&type=5,6"+"&orderId="+0)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            body = body.substring(1,body.length()-1);
                            JSONObject json = new JSONObject(body.replaceAll("\\\\",""));
                            Log.d("已处理",json.toString());
                            if (json.getInt("ResultCode") == 200) {
                                Gson gson = new Gson();
                                JSONArray jsonArray = json.getJSONArray("ObjectData");
                                list = gson.fromJson(jsonArray.toString(),new TypeToken<List<OrderBean>>(){}.getType());
                                orderListAdapter = new OrderListAdapter(list,getContext(),CompleteOrderFragment.this);
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
        Intent intent = new Intent(getContext(), OrderDetailActivity.class);
        startActivity(intent);
    }

    @Override
    public void onButtonClick(View view, int position) {

    }
}
