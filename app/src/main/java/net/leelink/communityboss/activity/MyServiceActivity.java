package net.leelink.communityboss.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;


import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.R;
import net.leelink.communityboss.adapter.OnItemClickListener;
import net.leelink.communityboss.adapter.QuestionAdapter;
import net.leelink.communityboss.bean.QuestionBean;
import net.leelink.communityboss.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyServiceActivity extends BaseActivity implements OnItemClickListener{
private RecyclerView question_list;
private QuestionAdapter questionAdapter;
private List<QuestionBean> list = new ArrayList<>();
private RelativeLayout rl_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_service);
        init();
        initData();
    }


    public void init(){
        question_list = findViewById(R.id.question_list);
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void initData() {
        OkGo.<String>get(Urls.STOREFAQ)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            body = body.substring(1,body.length()-1);
                            JSONObject json = new JSONObject(body.replaceAll("\\\\",""));
                            Log.d("问题列表",json.toString());
                            if (json.getInt("ResultCode") == 200) {
                                Gson gson = new Gson();
                                JSONArray jsonArray = json.getJSONArray("ObjectData");
                                list = gson.fromJson(jsonArray.toString(),new TypeToken<List<QuestionBean>>(){}.getType());
                                questionAdapter = new QuestionAdapter(list,MyServiceActivity.this,MyServiceActivity.this);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MyServiceActivity.this,LinearLayoutManager.VERTICAL,false);
                                question_list.setLayoutManager(layoutManager);
                                question_list.setAdapter(questionAdapter);
                            } else {
                                Toast.makeText(MyServiceActivity.this, json.getString("ResultValue"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onItemClick(View view) {
        int position = question_list.getChildLayoutPosition(view);
        Intent intent = new Intent(this,AnswerActivity.class);
        intent.putExtra("answer",list.get(position).getAnswer());
        startActivity(intent);
    }

}
