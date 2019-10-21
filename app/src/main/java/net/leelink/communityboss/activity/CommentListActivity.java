package net.leelink.communityboss.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import net.leelink.communityboss.R;
import net.leelink.communityboss.adapter.CommentListAdapter;
import net.leelink.communityboss.adapter.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class CommentListActivity extends BaseActivity implements OnItemClickListener , View.OnClickListener {
private RecyclerView comment_list;
private CommentListAdapter commentListAdapter;
private List<String> list = new ArrayList<>();
private RelativeLayout rl_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);
        init();
    }

    public void init(){
        list.add("服务非常周到,以后还会来");
        comment_list = findViewById(R.id.comment_list);
        commentListAdapter = new CommentListAdapter(this,list,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        comment_list.setLayoutManager(layoutManager);
        comment_list.setAdapter(commentListAdapter);
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
    }

    @Override
    public void onItemClick(View view) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_back:
                finish();
                break;
                default:
                    break;
        }
    }
}
