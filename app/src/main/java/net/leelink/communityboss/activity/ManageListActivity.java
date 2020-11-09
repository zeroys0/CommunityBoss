package net.leelink.communityboss.activity;

import android.content.Intent;
import android.os.Handler;
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
import com.lcodecore.tkrefreshlayout.Footer.LoadingView;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;
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
List<String> idList = new ArrayList<>();
private int page = 1;
private boolean hasNextPage = false;
    private TwinklingRefreshLayout refreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_list);
        init();
        initlist();
        initRefreshLayout();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void init(){
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        rl_add = findViewById(R.id.rl_add);
        rl_add.setOnClickListener(this);
        list_goods = findViewById(R.id.list_goods);
        list_goods.setNestedScrollingEnabled(true);
        tv_done = findViewById(R.id.tv_done);
        tv_done.setOnClickListener(this);
        btn_del = findViewById(R.id.btn_del);
        btn_del.setOnClickListener(this);
        list_goods.setNestedScrollingEnabled(false);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Event event) {
        list.clear();
        initlist();
    }


    //获取商品列表
    public void initlist(){
        OkGo.<String>get(Urls.COMMODITY)
                .tag(this)
                .params("pageNum",page)
                .params("pageSize",10)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("商品列表",json.toString());
                            if (json.getInt("status") == 200) {
                                json = json.getJSONObject("data");
                                hasNextPage = json.getBoolean("hasNextPage");
                                JSONArray jsonArray = json.getJSONArray("list");
                                Gson gson = new Gson();
                                List<GoodListBean> goodListBeans = new ArrayList<>();
                                goodListBeans = gson.fromJson(jsonArray.toString(), new TypeToken<List<GoodListBean>>(){}.getType());
                                list.addAll(goodListBeans);
                                goodListAdapter = new GoodListAdapter(list,ManageListActivity.this,ManageListActivity.this,0);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(ManageListActivity.this,LinearLayoutManager.VERTICAL,false);
                                list_goods.setLayoutManager(layoutManager);
                                list_goods.setAdapter(goodListAdapter);
                            } else {
                                Toast.makeText(ManageListActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
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
        intent.putExtra("Id",list.get(position).getId());
        intent.putExtra("name",list.get(position).getName());
        intent.putExtra("price",list.get(position).getUnitPrice());
        intent.putExtra("details",list.get(position).getRemark());
        intent.putExtra("image",list.get(position).getProductImgPath());
        intent.putExtra("state",list.get(position).getState());
        startActivity(intent);


    }

    @Override
    public void onCancelChecked(View view,int position,boolean state) {
        if(state==true){
           idList.add(list.get(position).getId());
        } else {
            for(int i=0;i<idList.size();i++){
                if(idList.get(i)==list.get(position).getId()) {
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
        OkGo.<String>delete(Urls.COMMODITY)
                .params("productIdList",s)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("删除商品",json.toString());
                            if (json.getInt("status") == 200) {
                                type = 0;
                                btn_del.setVisibility(View.INVISIBLE);
                                list.clear();
                                initlist();
                            } else {

                            }
                            Toast.makeText(ManageListActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    public void initRefreshLayout() {
        refreshLayout = (TwinklingRefreshLayout) findViewById(R.id.refreshLayout);
        SinaRefreshView headerView = new SinaRefreshView(this);
        headerView.setTextColor(0xff745D5C);
//        refreshLayout.setHeaderView((new ProgressLayout(getActivity())));
        refreshLayout.setHeaderView(headerView);
        refreshLayout.setBottomView(new LoadingView(this));
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefreshing();
                        page = 1;
                        list.clear();
                        initlist();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishLoadmore();
                        if(hasNextPage){
                            page++;
                            initlist();
                        }

                    }
                }, 1000);
            }

        });
        // 是否允许开启越界回弹模式
        refreshLayout.setEnableOverScroll(false);
        //禁用掉加载更多效果，即上拉加载更多
        refreshLayout.setEnableLoadmore(true);
        // 是否允许越界时显示刷新控件
        refreshLayout.setOverScrollRefreshShow(true);


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
