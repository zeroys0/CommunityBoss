package net.leelink.communityboss.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.R;
import net.leelink.communityboss.adapter.DrawStateAdapter;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DrawDetailActivity extends BaseActivity {
    private RelativeLayout rl_back;
    private TextView tv_balance,tv_text;
    private Context context;
    private DrawStateAdapter drawStateAdapter;
    private List<String> list = new ArrayList<>();
    private RecyclerView income_state;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_detail);
        init();
        context = this;
        initData();
        initView();
    }

    public void init() {
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_balance = findViewById(R.id.tv_balance);
        income_state = findViewById(R.id.income_state);
        tv_text = findViewById(R.id.tv_text);
    }

    public void initView() {
        int state = getIntent().getIntExtra("state", 1);
        String cardNumber = getIntent().getStringExtra("number");
        if(cardNumber.length()>4) {
            cardNumber = cardNumber.substring(cardNumber.length() - 4);
        }
        tv_text.setText("提现到尾号"+cardNumber);
        list.add("发起提现");
        list.add("平台审核中");
        switch (state) {
            case 1:
                break;
            case 2:
                list.add("财务处理中");
                break;
            case 3:
                list.add("财务处理中");
                list.add("银行处理中");
                break;
            case 4:
                list.add("财务处理中");
                list.add("银行处理中");
                list.add("确认到账");
                break;
            case 5:
                list.add("审核未通过-已退还余额");
                break;
            case 6:
                list.add("财务处理中");
                list.add("银行处理中");
                list.add("确认未到账-已退还余额");
                break;
        }
        DrawStateAdapter drawStateAdapter = new DrawStateAdapter(this,list,getIntent().getStringExtra("time"));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        income_state.setAdapter(drawStateAdapter);
        income_state.setLayoutManager(layoutManager);
    }


    public void initData() {
        OkGo.<String>get(Urls.getInstance().STOREHOME)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("账户余额", json.toString());
                            if (json.getInt("status") == 200) {
                                json = json.getJSONObject("data");
                                CommunityBossApplication.storeInfo.setWallet(json.getDouble("wallet"));
                                tv_balance.setText(CommunityBossApplication.storeInfo.getWallet() + "");
                            } else {
                                Toast.makeText(context, json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
