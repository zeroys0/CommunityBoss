package net.leelink.communityboss.housekeep;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.leelink.communityboss.R;
import net.leelink.communityboss.activity.BaseActivity;
import net.leelink.communityboss.adapter.OnOrderListener;
import net.leelink.communityboss.housekeep.adapter.StaffListAdapter;

import java.util.ArrayList;
import java.util.List;

public class DelegateActivity extends BaseActivity implements OnOrderListener {
    private RecyclerView staff_list;
    private StaffListAdapter staffListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delegate);
        init();
        initData();
    }

    public void init(){
        staff_list = findViewById(R.id.staff_list);

    }

    public void initData(){
        List<String> list = new ArrayList<>();
        list.add("a");
        list.add("a");
        list.add("a");
        staffListAdapter = new StaffListAdapter(list,this,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        staff_list.setLayoutManager(layoutManager);
        staff_list.setAdapter(staffListAdapter);
    }

    @Override
    public void onItemClick(View view) {

    }

    @Override
    public void onButtonClick(View view, int position) {

    }
}
