package net.leelink.communityboss.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.R;
import net.leelink.communityboss.adapter.GoodListAdapter;
import net.leelink.communityboss.adapter.OnCollectListener;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.bean.GoodListBean;
import net.leelink.communityboss.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ManageListActivity extends BaseActivity implements View.OnClickListener, OnCollectListener {
private RelativeLayout rl_back,rl_add;
private RecyclerView list_goods;
private GoodListAdapter goodListAdapter;
private List<GoodListBean> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_list);
        init();
        initlist();
    }
    public void init(){
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        rl_add = findViewById(R.id.rl_add);
        rl_add.setOnClickListener(this);
        list_goods = findViewById(R.id.list_goods);
    }

    public void initlist(){
        OkGo.<String>get(Urls.COMMODITY+"?appToken="+ CommunityBossApplication.token)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            body = body.substring(1,body.length()-1);
                            JSONObject json = new JSONObject(body.replaceAll("\\\\",""));
                            Log.d("商品列表",json.toString());
                            if (json.getInt("ResultCode") == 200) {
                                JSONArray jsonArray = json.getJSONArray("ObjectData");
                                Gson gson = new Gson();
                                list = gson.fromJson(jsonArray.toString(), TypeToken.get(GoodListBean.class).getType());
                                goodListAdapter = new GoodListAdapter(list,ManageListActivity.this,ManageListActivity.this);
                            } else {
                                Toast.makeText(ManageListActivity.this, json.getString("ResultValue"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_add:   //物品管理
                Intent intent =  new Intent(this,ManageGoodsActivity.class);
                startActivity(intent);
                break;
                default:
                    break;
        }
    }

    @Override
    public void onItemClick(View view) {

    }

    @Override
    public void onCancelChecked(View view) {

    }
}
