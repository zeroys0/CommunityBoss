package net.leelink.communityboss.housekeep.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.Footer.LoadingView;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.MainActivity;
import net.leelink.communityboss.R;
import net.leelink.communityboss.activity.ChooseIdentityActivity;
import net.leelink.communityboss.activity.ExamineActivity;
import net.leelink.communityboss.activity.LoginActivity;
import net.leelink.communityboss.activity.OrderDetailActivity;
import net.leelink.communityboss.adapter.OnOrderListener;
import net.leelink.communityboss.adapter.OrderListAdapter;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.bean.HsOrderBean;
import net.leelink.communityboss.bean.OrderBean;
import net.leelink.communityboss.bean.StoreInfo;
import net.leelink.communityboss.bean.TakeOrderRefresh;
import net.leelink.communityboss.fragment.BaseFragment;
import net.leelink.communityboss.fragment.UntakeOrderFragment;
import net.leelink.communityboss.housekeep.HousekeepMainActivity;
import net.leelink.communityboss.housekeep.HsOrderDetailActivity;
import net.leelink.communityboss.housekeep.adapter.HsOrderAdapter;
import net.leelink.communityboss.utils.Acache;
import net.leelink.communityboss.utils.Urls;
import net.leelink.communityboss.utils.Utils;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.jpush.android.api.JPushInterface;

public class HsUntakeFragment extends BaseFragment implements OnOrderListener {

    private RecyclerView list_order;

    private List<HsOrderBean> list = new ArrayList<>();
    private TwinklingRefreshLayout refreshLayout;
    private int page = 1;
    private  boolean hasNextPage = false;
    private HsOrderAdapter hsOrderAdapter;
    private JSONArray jsonArray= new JSONArray();

    @Override
    public void handleCallBack(Message msg) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_untake_order,container,false);
        init(view);

        initRefreshLayout(view);
        return view;
    }
    public void init(View view){
        list_order = view.findViewById(R.id.list_order);
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            jsonArray = new JSONArray("[]");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initData();
    }

    public void initData(){

        //获取订单列表

        OkGo.<String>get(Urls.getInstance().HS_ORDERLIST)
                .params("state",1)
                .params("pageNum",page)
                .params("pageSize",10)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("未接订单",json.toString());
                            if (json.getInt("status") == 200) {
                                Gson gson = new Gson();
                                json = json.getJSONObject("data");
                                hasNextPage = json.getBoolean("hasNextPage");
                                JSONArray ja = json.getJSONArray("list");
                                for(int i = 0;i<ja.length();i++) {
                                    jsonArray.put(ja.getJSONObject(i));
                                }
                                List<HsOrderBean> orderBeanslist = gson.fromJson(jsonArray.toString(),new TypeToken<List<HsOrderBean>>(){}.getType());
                                list.addAll(orderBeanslist);
                                hsOrderAdapter = new HsOrderAdapter(jsonArray,getContext(), HsUntakeFragment.this);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                                list_order.setLayoutManager(layoutManager);
                                list_order.setAdapter(hsOrderAdapter);
                            } else if(json.getInt("status") == 505) {
                                quickLogin();
                            }
                            else {
                                Toast.makeText(getContext(), json.getString("message"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }

    @Override
    public void onItemClick(View view) {
        if(Utils.isFastClick()){
            return;
        }
        int position = list_order.getChildLayoutPosition(view);
        Intent intent = new Intent(getContext(), HsOrderDetailActivity.class);
        try {
            intent.putExtra("object",jsonArray.getJSONObject(position).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        intent.putExtra("type",1);
        startActivity(intent);
    }

    public void quickLogin() {
        final SharedPreferences sp = getActivity().getSharedPreferences("sp", 0);
        if (!sp.getString("secretKey", "").equals("")) {
            OkGo.<String>post(Urls.getInstance().QUICKLOGIN)
                    .params("telephone", sp.getString("telephone", ""))
                    .params("secretKey", sp.getString("secretKey", ""))
                    .params("deviceToken", JPushInterface.getRegistrationID(getContext()))
                    .tag(this)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            try {
                                String body = response.body();
                                JSONObject json = new JSONObject(body);
                                Log.d("快速登录", json.toString());
                                if (json.getInt("status") == 200) {
                                    JSONObject jsonObject = json.getJSONObject("data");
                                            Gson gson = new Gson();
                                            Acache.get(getContext()).put("storeInfo",jsonObject);
                                            StoreInfo storeInfo = gson.fromJson(jsonObject.toString(), StoreInfo.class);
                                            CommunityBossApplication.storeInfo = storeInfo;
                                            initData();
                                } else {
                                    Toast.makeText(getContext(), "登录失效,请重新登录", Toast.LENGTH_SHORT).show();
                                    Intent intent4 = new Intent(getContext(), LoginActivity.class);
                                    SharedPreferences sp = getActivity().getSharedPreferences("sp",0);
                                    SharedPreferences.Editor editor = sp.edit();
                                    editor.remove("secretKey");
                                    editor.remove("telephone");
                                    editor.apply();
                                    intent4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(intent4);
                                    getActivity().finish();

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    //确认接单
    @Override
    public void onButtonClick(View view, final int position) {
        if(Utils.isFastClick()){
            return;
        }
        Log.d( "确认订单: ",list.get(position).getOrderId());
        OkGo.<String>post(Urls.getInstance().HS_ORDERSTATE+"?"+"orderId="+list.get(position).getOrderId()+"&state="+2)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("确认订单",json.toString());
                            if (json.getInt("status") == 200) {
                                jsonArray.remove(position);
                                list.remove(position);
                                hsOrderAdapter.notifyDataSetChanged();
                                Toast.makeText(getContext(), "订单已确认,请尽快完成吧~", Toast.LENGTH_SHORT).show();
                                EventBus.getDefault().post(new TakeOrderRefresh());
                            } else {
                                Toast.makeText(getContext(), json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void initRefreshLayout(View view) {
        refreshLayout = (TwinklingRefreshLayout) view.findViewById(R.id.refreshLayout);
        SinaRefreshView headerView = new SinaRefreshView(getContext());
        headerView.setTextColor(0xff745D5C);
//        refreshLayout.setHeaderView((new ProgressLayout(getActivity())));
        refreshLayout.setHeaderView(headerView);
        refreshLayout.setBottomView(new LoadingView(getContext()));
        refreshLayout.setOnRefreshListener(new RefreshListenerAdapter() {
            @Override
            public void onRefresh(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishRefreshing();
                        page = 1;
                        try {
                            jsonArray = new JSONArray("[]");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        initData();
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
                            initData();
                        }
                        hsOrderAdapter.update(jsonArray);
                        list_order.scrollToPosition(hsOrderAdapter.getItemCount()-1);
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


}
