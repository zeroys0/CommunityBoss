package net.leelink.communityboss.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
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
import net.leelink.communityboss.bean.DeleteEvent;
import net.leelink.communityboss.bean.Event;
import net.leelink.communityboss.bean.GoodListBean;
import net.leelink.communityboss.utils.Urls;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
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
private TextView tv_done;
private Button btn_del;
private int type = 0;
List<Integer> idList = new ArrayList<>();
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
        tv_done = findViewById(R.id.tv_done);
        tv_done.setOnClickListener(this);
        btn_del = findViewById(R.id.btn_del);
        btn_del.setOnClickListener(this);
        list_goods.setNestedScrollingEnabled(false);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event event) {
        initlist();
    }


    //获取商品列表
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
                                list = gson.fromJson(jsonArray.toString(), new TypeToken<List<GoodListBean>>(){}.getType());
                                goodListAdapter = new GoodListAdapter(list,ManageListActivity.this,ManageListActivity.this,0);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ManageListActivity.this,LinearLayoutManager.VERTICAL,false);
                                list_goods.setLayoutManager(layoutManager);
                                list_goods.setAdapter(goodListAdapter);
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
            case R.id.tv_done:  //编辑
                if(type == 0) {
                    type = 1;
                    btn_del.setVisibility(View.VISIBLE);
                    btn_del.setText("删除");
                    goodListAdapter.setType(type);
                }else {
                    type = 0;
                    btn_del.setVisibility(View.INVISIBLE);
                    goodListAdapter.setType(type);
                }
                goodListAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_del:  //完成删除
                delete();
                break;
                default:
                    break;
        }
    }


    @Override
    public void onItemClick(View view) {
        int position = list_goods.getChildLayoutPosition(view);
        Intent intent = new Intent(this,ChangeGoodsActivity.class);
        intent.putExtra("commodityId",list.get(position).getCommodityId());
        intent.putExtra("name",list.get(position).getName());
        intent.putExtra("price",list.get(position).getPrice());
        intent.putExtra("details",list.get(position).getDetails());
        intent.putExtra("image",list.get(position).getHeadImage());
        startActivity(intent);


    }

    @Override
    public void onCancelChecked(View view,int position,boolean state) {
        if(state==true){
           idList.add(list.get(position).getCommodityId());
        } else {
            for(int i=0;i<idList.size();i++){
                if(idList.get(i)==list.get(position).getCommodityId()) {
                    idList.remove(i);
                }
            }
        }
    }

    public void delete(){

        String s  = "";
        for(int i=0;i<idList.size();i++) {
            s += idList.get(i).toString()+",";
        }
        s = s.substring(0,s.length()-1);
        Log.d( "delete: ",s);
        OkGo.<String>delete(Urls.COMMODITY+"?appToken="+ CommunityBossApplication.token+"&commodityIds="+s)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            body = body.substring(1,body.length()-1);
                            JSONObject json = new JSONObject(body.replaceAll("\\\\",""));
                            Log.d("用户信息",json.toString());
                            if (json.getInt("ResultCode") == 200) {
                                type = 0;
                                btn_del.setVisibility(View.INVISIBLE);
                                initlist();
                            } else {

                            }
                            Toast.makeText(ManageListActivity.this, json.getString("ResultValue"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
