package net.leelink.communityboss.activity;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;


import net.leelink.communityboss.R;
import net.leelink.communityboss.adapter.OnItemClickListener;
import net.leelink.communityboss.adapter.QuestionAdapter;

import java.util.ArrayList;
import java.util.List;

public class MyServiceActivity extends BaseActivity implements OnItemClickListener{
private RecyclerView question_list;
private QuestionAdapter questionAdapter;
private List<String> list = new ArrayList<>();
private RelativeLayout rl_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_service);
        init();
    }


    public void init(){
        question_list = findViewById(R.id.question_list);
        list.add("我怎么修改密码");
        list.add("我能充值吗");
        list.add("如何投诉");
        questionAdapter = new QuestionAdapter(list,this,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        question_list.setLayoutManager(layoutManager);
        question_list.setAdapter(questionAdapter);
        rl_back = findViewById(R.id.rl_back);

        //返回
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
}
