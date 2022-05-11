package net.leelink.communityboss.housekeep.fragment;

import android.app.job.JobInfo;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lcodecore.tkrefreshlayout.Footer.LoadingView;
import com.lcodecore.tkrefreshlayout.RefreshListenerAdapter;
import com.lcodecore.tkrefreshlayout.TwinklingRefreshLayout;
import com.lcodecore.tkrefreshlayout.header.SinaRefreshView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.R;
import net.leelink.communityboss.activity.LoginActivity;
import net.leelink.communityboss.activity.OrderDetailActivity;
import net.leelink.communityboss.adapter.OnOrderListener;
import net.leelink.communityboss.adapter.OrderListAdapter;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.bean.HsOrderBean;
import net.leelink.communityboss.bean.HsOrderRefresh;
import net.leelink.communityboss.bean.OrderBean;
import net.leelink.communityboss.bean.StoreInfo;
import net.leelink.communityboss.bean.TakeOrderRefresh;
import net.leelink.communityboss.bean.WorkBean;
import net.leelink.communityboss.fragment.BaseFragment;
import net.leelink.communityboss.fragment.TakeOrderFragment;
import net.leelink.communityboss.housekeep.DelegateActivity;
import net.leelink.communityboss.housekeep.HsOrderDetailActivity;
import net.leelink.communityboss.housekeep.adapter.HsOrderAdapter;
import net.leelink.communityboss.housekeep.adapter.WorkOrderAdapter;
import net.leelink.communityboss.utils.Acache;
import net.leelink.communityboss.utils.Urls;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import cn.jpush.android.api.JPushInterface;


public class HsTakeFragment extends BaseFragment implements OnOrderListener {
    private RecyclerView list_order;
    private List<HsOrderBean> list = new ArrayList<>();
    private TabLayout tablayout;
    private String type = "2";
    private TwinklingRefreshLayout refreshLayout;
    private String orderId = "0";
    private HsOrderAdapter hsOrderAdapter;
    private WorkOrderAdapter workOrderAdapter;
    private List<WorkBean> list_w = new ArrayList<>();
    private JSONArray jsonArray= new JSONArray();
    ProgressBar mProgressBar;
    private Boolean hasNextPage;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_take_order,container,false);
        init(view);
        createProgressBar();


        initRefreshLayout(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            jsonArray = new JSONArray("[]");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initData(type);
    }

    public void init(View view){
        EventBus.getDefault().register(this);
        tablayout = view.findViewById(R.id.tablayout);
        tablayout.addTab(tablayout.newTab().setText("未派工"));
        tablayout.addTab(tablayout.newTab().setText("已派工"));

        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                list.clear();
                list_w.clear();
                try {
                    jsonArray = new JSONArray("[]");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                orderId = "0";
                if(tab.getPosition() == 0){
                    type = "2";
                    initData(type);
                } else {
                    type = "3";
                    initWorkList();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        list_order = view.findViewById(R.id.list_order);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(HsOrderRefresh event) {
        try {
            jsonArray = new JSONArray("[]");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initData(type);
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(TakeOrderRefresh event) {
        try {
            jsonArray = new JSONArray("[]");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        initData(type);
    }

    public void clear(JSONArray jsonArray){
        for (int i = 0, len = jsonArray.length(); i < len; i++) {
            JSONObject obj = null;
            try {
                obj = jsonArray.getJSONObject(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            obj.remove("key");
        }
    }

    public void initData(final String type){
        //获取订单列表
        mProgressBar.setVisibility(View.VISIBLE);
        OkGo.<String>get(Urls.getInstance().HS_ORDERLIST)
                .params("state",type)
                .params("pageNum",1)
                .params("pageSize",10)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        mProgressBar.setVisibility(View.GONE);
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("已接单",json.toString());
                            if (json.getInt("status") == 200) {
                                Gson gson = new Gson();
                                json = json.getJSONObject("data");
                                hasNextPage = json.getBoolean("hasNextPage");
                                JSONArray ja = json.getJSONArray("list");
                                for(int i = 0;i<ja.length();i++) {
                                    jsonArray.put(ja.getJSONObject(i));
                                }
                                List<HsOrderBean> orderBeanlist = gson.fromJson(jsonArray.toString(),new TypeToken<List<HsOrderBean>>(){}.getType());
                                list.addAll(orderBeanlist);
                                hsOrderAdapter = new HsOrderAdapter(jsonArray,getContext(), HsTakeFragment.this);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                                list_order.setLayoutManager(layoutManager);
                                list_order.setAdapter(hsOrderAdapter);
                            } else if(json.getInt("status") == 505){
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
                                                            initData(type);
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
                            }else {
                                Toast.makeText(getContext(), json.getString("message"), Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void initWorkList(){
        mProgressBar.setVisibility(View.VISIBLE);
        OkGo.<String>get(Urls.getInstance().WORKLIST)
                .params("state","1,2")
                .params("pageNum",1)
                .params("pageSize",10)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        mProgressBar.setVisibility(View.GONE);
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("工单列表",json.toString());
                            if (json.getInt("status") == 200) {
                                Gson gson = new Gson();
                                json = json.getJSONObject("data");
                                hasNextPage = json.getBoolean("hasNextPage");
                                JSONArray ja = json.getJSONArray("list");
                                for(int i =0;i<ja.length();i++){
                                    jsonArray.put(ja.getJSONObject(i));
                                }

                                List<WorkBean> workBeanList = gson.fromJson(jsonArray.toString(),new TypeToken<List<WorkBean>>(){}.getType());
                                list_w.addAll(workBeanList);
                                workOrderAdapter = new WorkOrderAdapter(jsonArray,getContext(), HsTakeFragment.this);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);
                                list_order.setLayoutManager(layoutManager);
                                list_order.setAdapter(workOrderAdapter);
                            }else if(json.getInt("status") == 505) {
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
                                                            initWorkList();
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
                            } else {
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
        int position = list_order.getChildLayoutPosition(view);
        Intent intent = new Intent(getContext(), HsOrderDetailActivity.class);

        if(type.equals("2")) {
            intent.putExtra("type",2);
        } else {
            intent.putExtra("type",3);
        }
        try {
            intent.putExtra("object",jsonArray.getJSONObject(position).toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        startActivity(intent);
    }

    @Override
    public void onButtonClick(View view, final int position) {
        int operation;
        if(type .equals( "2")){
            Intent intent = new Intent(getContext(), DelegateActivity.class);
            intent.putExtra("id",list.get(position).getProductId());
            intent.putExtra("orderId",list.get(position).getOrderId());
            startActivity(intent);
        } else  {
            operation = 3;  //确认送达
        }

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
                        orderId = "0";
                        try {
                            jsonArray = new JSONArray("[]");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        if(type.equals("2")) {
                            initData(type);
                        } else {
                            initWorkList();
                        }
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishLoadmore();

                        if(hasNextPage) {
                            if(type.equals("2")) {
                                initData(type);
                            }else {
                                initWorkList();
                            }
                        } else {

                        }
                        hsOrderAdapter.update(jsonArray);
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
    public void handleCallBack(Message msg) {

    }
    private void createProgressBar(){

        //整个Activity布局的最终父布局,参见参考资料
        FrameLayout rootFrameLayout=(FrameLayout) getActivity().findViewById(android.R.id.content);
        FrameLayout.LayoutParams layoutParams=
                new FrameLayout.LayoutParams(FrameLayout.LayoutParams.WRAP_CONTENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        layoutParams.gravity= Gravity.CENTER;
        mProgressBar=new ProgressBar(getContext());
        mProgressBar.setLayoutParams(layoutParams);
        mProgressBar.setVisibility(View.GONE);
        rootFrameLayout.addView(mProgressBar);
    }
}
