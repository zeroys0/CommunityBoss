package net.leelink.communityboss.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
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
import net.leelink.communityboss.activity.BoundaryActivity;
import net.leelink.communityboss.activity.ChangePhoneActivity;
import net.leelink.communityboss.activity.CommentListActivity;
import net.leelink.communityboss.activity.IncomeActivity;
import net.leelink.communityboss.activity.InformationActivity;
import net.leelink.communityboss.activity.ManageListActivity;
import net.leelink.communityboss.activity.MyServiceActivity;
import net.leelink.communityboss.activity.RefundListActivity;
import net.leelink.communityboss.activity.SettingActivity;
import net.leelink.communityboss.adapter.GoodListAdapter;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.bean.GoodListBean;
import net.leelink.communityboss.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class MineFragment extends BaseFragment implements View.OnClickListener {
    private RelativeLayout rl_comment,rl_info,rl_goods,rl_income,rl_refund,rl_service,rl_boundary;
    private ImageView img_head,img_change,img_setting;
    private TextView tv_income,tv_order_number,tv_phone,tv_name;
    private TwinklingRefreshLayout refreshLayout;
    @Override
    public void handleCallBack(Message msg) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine,container,false);
        init(view);
        initdata();
        initRefreshLayout(view);
        return view;
    }



    public void init(View view){
        tv_name = view.findViewById(R.id.tv_name);
        rl_comment = view.findViewById(R.id.rl_comment);
        rl_comment.setOnClickListener(this);
        img_head = view.findViewById(R.id.img_head);
        img_head.setOnClickListener(this);
        rl_info = view.findViewById(R.id.rl_info);
        rl_info.setOnClickListener(this);
        rl_goods = view.findViewById(R.id.rl_goods);
        rl_goods.setOnClickListener(this);
        rl_income = view.findViewById(R.id.rl_income);
        rl_income.setOnClickListener(this);
        rl_refund = view.findViewById(R.id.rl_refund);
        rl_refund.setOnClickListener(this);
        rl_service = view.findViewById(R.id.rl_service);
        rl_service.setOnClickListener(this);
        tv_income = view.findViewById(R.id.tv_income);
        tv_order_number = view.findViewById(R.id.tv_order_number);
        tv_phone = view.findViewById(R.id.tv_phone);
        img_change = view.findViewById(R.id.img_change);
        img_change.setOnClickListener(this);
        img_setting = view.findViewById(R.id.img_setting);
        img_setting.setOnClickListener(this);
        rl_boundary = view.findViewById(R.id.rl_boundary);
        rl_boundary.setOnClickListener(this);
    }

    public void initdata(){
        Glide.with(getContext()).load(Urls.IMAGEURL+"Store/"+CommunityBossApplication.storeInfo.getStoreId()+"/Image/"+CommunityBossApplication.storeInfo.getHeadImage()).into(img_head);
        tv_name.setText(CommunityBossApplication.storeInfo.getStoreName());
        tv_phone.setText(CommunityBossApplication.storeInfo.getUsername());

        OkGo.<String>get(Urls.STOREHOME+"?appToken="+ CommunityBossApplication.token)
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
                                tv_income.setText("今日收入: ￥"+json.getJSONObject("ObjectData").getString("Income"));
                                tv_order_number.setText("今日订单: "+json.getJSONObject("ObjectData").getString("OrderNumber")+"单");
                            } else {
                                Toast.makeText(getContext(), json.getString("ResultValue"), Toast.LENGTH_LONG).show();
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
            case R.id.rl_comment:   //用户反馈
                Intent intent = new Intent(getContext(),CommentListActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_info:  //商家信息
                Intent intent1 = new Intent(getContext(), InformationActivity.class);
                startActivity(intent1);
                break;
            case R.id.rl_goods: //商家物品
                Intent intent2 = new Intent(getContext(), ManageListActivity.class);
                startActivity(intent2);
                break;
            case R.id.rl_income:    //收入统计
                Intent intent3 = new Intent(getContext(), IncomeActivity.class);
                startActivity(intent3);
                break;
            case R.id.rl_refund:    //退款订单
                Intent intent4 = new Intent(getContext(), RefundListActivity.class);
                startActivity(intent4);
                break;
            case R.id.rl_service:   //我的客服
                Intent intent5 = new Intent(getContext(), MyServiceActivity.class);
                startActivity(intent5);
                break;
            case R.id.img_change:   //修改电话
                Intent intent6 = new Intent(getContext(), ChangePhoneActivity.class);
                startActivity(intent6);
                break;
            case R.id.img_setting:
                Intent intent7 = new Intent(getContext(), SettingActivity.class);
                startActivity(intent7);
                break;
            case R.id.rl_boundary:
                Intent intent8 = new Intent(getContext(), BoundaryActivity.class);
                startActivity(intent8);
                break;
                default:
                    break;
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
                        initdata();
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishLoadmore();
                    }
                }, 1000);
            }

        });
        // 是否允许开启越界回弹模式
        refreshLayout.setEnableOverScroll(false);
        //禁用掉加载更多效果，即上拉加载更多
        refreshLayout.setEnableLoadmore(false);
        // 是否允许越界时显示刷新控件
        refreshLayout.setOverScrollRefreshShow(true);


    }
}
