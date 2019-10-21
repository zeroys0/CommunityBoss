package net.leelink.communityboss.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import net.leelink.communityboss.R;
import net.leelink.communityboss.adapter.CardListAdapter;
import net.leelink.communityboss.adapter.OnCollectListener;
import net.leelink.communityboss.adapter.OnItemClickListener;

import java.util.List;

public class WithdrawActivity extends BaseActivity implements OnItemClickListener {
    private RecyclerView card_list;
    private CardListAdapter cardListAdapter;
    private List<String> cards_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        init();
    }


    public void init(){
        card_list = findViewById(R.id.card_list);
        cardListAdapter = new CardListAdapter(this,cards_list,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        card_list.setLayoutManager(layoutManager);
        card_list.setAdapter(cardListAdapter);
    }

    @Override
    public void onItemClick(View view) {

    }
}
