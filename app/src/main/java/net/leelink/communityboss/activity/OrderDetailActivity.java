package net.leelink.communityboss.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import net.leelink.communityboss.R;
import net.leelink.communityboss.adapter.PreOrderAdapter;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailActivity extends BaseActivity {
private RecyclerView goods_list;
private PreOrderAdapter preOrderAdapter;
private List<String> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);
        init();
    }

    public void init(){
        goods_list = findViewById(R.id.goods_list);
        preOrderAdapter = new PreOrderAdapter(this,list);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        goods_list.setLayoutManager(layoutManager);
        goods_list.setAdapter(preOrderAdapter);
    }
}
