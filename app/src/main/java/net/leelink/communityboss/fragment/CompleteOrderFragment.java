package net.leelink.communityboss.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.leelink.communityboss.R;
import net.leelink.communityboss.activity.OrderDetailActivity;
import net.leelink.communityboss.adapter.OnItemClickListener;
import net.leelink.communityboss.adapter.OrderListAdapter;

import java.util.ArrayList;
import java.util.List;

public class CompleteOrderFragment extends BaseFragment implements OnItemClickListener {
    private RecyclerView list_order;
    private OrderListAdapter orderListAdapter;
    private List<String> list = new ArrayList<>();
    @Override
    public void handleCallBack(Message msg) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_complete_order,container,false);
        init(view);
        return view;
    }

    public void init(View view){
        list_order = view.findViewById(R.id.list_order);
        orderListAdapter = new OrderListAdapter(list,getContext(),this);
        list.add("已完成单");
        list.add("吼山路订单");
        list.add("完成单2");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
        list_order.setLayoutManager(layoutManager);
        list_order.setAdapter(orderListAdapter);
    }

    @Override
    public void onItemClick(View view) {
        Intent intent = new Intent(getContext(), OrderDetailActivity.class);
        startActivity(intent);
    }
}
