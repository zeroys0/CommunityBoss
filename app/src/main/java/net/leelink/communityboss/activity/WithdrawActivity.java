package net.leelink.communityboss.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.R;
import net.leelink.communityboss.adapter.CardListAdapter;
import net.leelink.communityboss.adapter.OnCollectListener;
import net.leelink.communityboss.adapter.OnItemClickListener;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.bean.CardBean;
import net.leelink.communityboss.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class WithdrawActivity extends BaseActivity implements OnItemClickListener, View.OnClickListener {
    private RecyclerView card_list;
    private CardListAdapter cardListAdapter;
    private List<CardBean> list;
    private LinearLayout ll_add;
    private RelativeLayout rl_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        init();
        initdata();
    }


    public void init(){
        card_list = findViewById(R.id.card_list);

        ll_add = findViewById(R.id.ll_add);
        ll_add.setOnClickListener(this);
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
    }

    public void initdata(){
        OkGo.<String>get(Urls.BANKCARD+"?appToken="+ CommunityBossApplication.token)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            body = body.substring(1,body.length()-1);
                            JSONObject json = new JSONObject(body.replaceAll("\\\\",""));
                            Log.d("银行卡列表",json.toString());
                            if (json.getInt("ResultCode") == 200) {
                                JSONArray jsonArray  = json.getJSONArray("ObjectData");
                                Gson gson = new Gson();
                                list = gson.fromJson(jsonArray.toString(),new TypeToken<List<CardBean>>(){}.getType());
                                cardListAdapter = new CardListAdapter(WithdrawActivity.this,list,WithdrawActivity.this);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(WithdrawActivity.this,LinearLayoutManager.VERTICAL,false);
                                card_list.setLayoutManager(layoutManager);
                                card_list.setAdapter(cardListAdapter);
                            } else {

                            }
                            Toast.makeText(WithdrawActivity.this, json.getString("ResultValue"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ll_add:   //添加银行卡
                Intent intent = new Intent(this,BindCardActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_back:
                finish();
                break;
                default:
                    break;
        }
    }

    @Override
    public void onItemClick(View view) {
        int position = card_list.getChildLayoutPosition(view);
        Intent intent = new Intent(this,ConfirmWithdrawActivity.class);
        startActivity(intent);
        intent.putExtra("card_number", list.get(position).getCardNumber());

    }


}
