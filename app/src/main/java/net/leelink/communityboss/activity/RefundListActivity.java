package net.leelink.communityboss.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import net.leelink.communityboss.R;
import net.leelink.communityboss.adapter.OnItemClickListener;
import net.leelink.communityboss.adapter.OnOrderListener;
import net.leelink.communityboss.adapter.OrderListAdapter;
import net.leelink.communityboss.bean.OrderBean;

import java.util.ArrayList;
import java.util.List;

public class RefundListActivity extends BaseActivity implements OnOrderListener {
private RecyclerView refund_list;
    private OrderListAdapter orderListAdapter;
    private List<OrderBean> list = new ArrayList<>();
    private RelativeLayout rl_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_list);
        init();
    }

    public void init(){
        refund_list = findViewById(R.id.refund_list);
        orderListAdapter = new OrderListAdapter(list,this,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        refund_list.setLayoutManager(layoutManager);
        refund_list.setAdapter(orderListAdapter);
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onItemClick(View view) {

    }

    @Override
    public void onButtonClick(View view, int position) {

    }
}
