package net.leelink.communityboss.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.leelink.communityboss.R;
import net.leelink.communityboss.adapter.CardListAdapter;
import net.leelink.communityboss.adapter.OnItemClickListener;

import java.util.List;

public class BindCardActivity extends BaseActivity implements OnItemClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_card);
        init();
    }

    public void init(){

    }

    @Override
    public void onItemClick(View view) {

    }
}
