package net.leelink.communityboss.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MyServiceActivity extends BaseActivity implements OnItemClickListener{
private RecyclerView question_list;
private QuestionAdapter questionAdapter;
private List<QuestionBean> list = new ArrayList<>();
private RelativeLayout rl_back;
private ImageView img_call;
private String phone = "59068580";
private TextView tv_servphone;
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
        tv_servphone = findViewById(R.id.tv_servphone);

        img_call = findViewById(R.id.img_call);
        img_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                Uri data = Uri.parse("tel:" + phone);
                intent.setData(data);
                startActivity(intent);
            }
        });
    }
    public void initData() {
        OkGo.<String>get(Urls.getInstance().FAQ)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("问题列表",json.toString());
                            if (json.getInt("status") == 200) {
                                Gson gson = new Gson();
                                JSONArray jsonArray = json.getJSONArray("data");
//                                phone = json.getJSONObject("ObjectData").getString("ServicePhone");
//                                tv_servphone.setText("客服电话:"+phone);

                                list = gson.fromJson(jsonArray.toString(),new TypeToken<List<QuestionBean>>(){}.getType());
                                questionAdapter = new QuestionAdapter(list,MyServiceActivity.this,MyServiceActivity.this);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MyServiceActivity.this,LinearLayoutManager.VERTICAL,false);
                                question_list.setLayoutManager(layoutManager);
                                question_list.setAdapter(questionAdapter);
                            } else {
                                Toast.makeText(MyServiceActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
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
