package net.leelink.communityboss.fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.R;
import net.leelink.communityboss.activity.BalanceActivity;
import net.leelink.communityboss.activity.BoundaryActivity;
import net.leelink.communityboss.activity.ChangePhoneActivity;
import net.leelink.communityboss.activity.CommentListActivity;
import net.leelink.communityboss.activity.IncomeActivity;
import net.leelink.communityboss.activity.IncomeListActicity;
import net.leelink.communityboss.activity.InformationActivity;
import net.leelink.communityboss.activity.LoginActivity;
import net.leelink.communityboss.activity.ManageListActivity;
import net.leelink.communityboss.activity.MyServiceActivity;
import net.leelink.communityboss.activity.RefundListActivity;
import net.leelink.communityboss.activity.SettingActivity;
import net.leelink.communityboss.activity.WebActivity;
import net.leelink.communityboss.adapter.GoodListAdapter;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.bean.GoodListBean;
import net.leelink.communityboss.bean.MyInfoBean;
import net.leelink.communityboss.bean.StoreInfo;
import net.leelink.communityboss.utils.Acache;
import net.leelink.communityboss.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import cn.jpush.android.api.JPushInterface;

public class MineFragment extends BaseFragment implements View.OnClickListener {
    private RelativeLayout rl_comment, rl_info, rl_goods, rl_income, rl_refund, rl_service, rl_boundary, rl_balance;
    private ImageView img_head, img_change, img_setting, img_state;
    private TextView tv_income, tv_order_number, tv_phone, tv_name, tv_text;
    private TwinklingRefreshLayout refreshLayout;
    private MyInfoBean myInfoBean;
    private boolean open;

    @Override
    public void handleCallBack(Message msg) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine, container, false);
        init(view);
        initdata();
        initRefreshLayout(view);
        return view;
    }


    public void init(View view) {
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
        tv_income = view.findViewById(R.id.tv_total_income);
        tv_order_number = view.findViewById(R.id.tv_order_number);
        tv_phone = view.findViewById(R.id.tv_phone);
        tv_phone.setOnClickListener(this);
        img_setting = view.findViewById(R.id.img_setting);
        img_setting.setOnClickListener(this);
        rl_boundary = view.findViewById(R.id.rl_boundary);
        rl_boundary.setOnClickListener(this);
        rl_balance = view.findViewById(R.id.rl_balance);
        rl_balance.setOnClickListener(this);
        img_state = view.findViewById(R.id.img_state);
        img_state.setOnClickListener(this);
        tv_text = view.findViewById(R.id.tv_text);
        SpannableString spannableString = new SpannableString("阅读<<用户协议>>以及<<隐私政策>>");
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(getContext(), WebActivity.class);
                intent.putExtra("type", "distribution");
                intent.putExtra("url", "https://www.llky.net.cn/store/protocol.html");
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.blue)); //设置颜色
            }
        }, 2, 10, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                Intent intent = new Intent(getContext(), WebActivity.class);
                intent.putExtra("type", "distribution");
                intent.putExtra("url", "https://www.llky.net.cn/store/privacyPolicy.html");
                startActivity(intent);
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setColor(getResources().getColor(R.color.blue)); //设置颜色
            }
        }, 12, 20, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tv_text.append(spannableString);
        tv_text.setMovementMethod(LinkMovementMethod.getInstance());  //很重要，点击无效就是由于没有设置这个引起
    }

    public void initdata() {
        if (CommunityBossApplication.storeInfo.getStoreImg() != null) {
            Glide.with(this).load(Urls.getInstance().IMG_URL + CommunityBossApplication.storeInfo.getStoreImg()).into(img_head);
        }
        tv_name.setText(CommunityBossApplication.storeInfo.getStoreName());
        tv_phone.setText(CommunityBossApplication.storeInfo.getOrderPhone());

        OkGo.<String>get(Urls.getInstance().STOREHOME)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("获取个人信息", json.toString());
                            if (json.getInt("status") == 200) {
                                json = json.getJSONObject("data");
                                Gson gson = new Gson();
                                myInfoBean = gson.fromJson(json.toString(), MyInfoBean.class);
                                tv_income.setText(json.getString("todayAmount"));
                                tv_order_number.setText(json.getString("todayOrderNum"));
                                if (json.getBoolean("open")) {
                                    open = true;
                                    img_state.setImageResource(R.mipmap.img_state_open);
                                } else {
                                    open = false;
                                    img_state.setImageResource(R.mipmap.img_state_rest);
                                }
                            } else if (json.getInt("status") == 505) {
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
                                                            Acache.get(getContext()).put("storeInfo", jsonObject);
                                                            StoreInfo storeInfo = gson.fromJson(jsonObject.toString(), StoreInfo.class);
                                                            CommunityBossApplication.storeInfo = storeInfo;
                                                            initdata();
                                                        } else {
                                                            Toast.makeText(getContext(), "登录失效,请重新登录", Toast.LENGTH_SHORT).show();
                                                            Intent intent4 = new Intent(getContext(), LoginActivity.class);
                                                            SharedPreferences sp = getActivity().getSharedPreferences("sp", 0);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_comment:   //用户反馈
                Intent intent = new Intent(getContext(), CommentListActivity.class);
                intent.putExtra("myInfo", myInfoBean);

                startActivity(intent);
                break;
            case R.id.rl_info:  //商家信息
                Intent intent1 = new Intent(getContext(), InformationActivity.class);
                intent1.putExtra("type", "distribution");
                startActivity(intent1);
                break;
            case R.id.rl_goods: //商家物品
                Intent intent2 = new Intent(getContext(), ManageListActivity.class);
                startActivity(intent2);
                break;
            case R.id.rl_income:    //收入统计
                Intent intent3 = new Intent(getContext(), IncomeListActicity.class);
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
            case R.id.tv_phone:   //修改电话
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
            case R.id.rl_balance:   //账户余额
                Intent intent9 = new Intent(getContext(), BalanceActivity.class);
                startActivity(intent9);
                break;
            case R.id.img_state:
                changeState();
                break;
            default:
                break;
        }
    }

    public void changeState() {

        HttpParams httpParams = new HttpParams();
        final int state;
        if (open) {
            state = 0;
        } else {
            state = 1;
        }
        httpParams.put("state", state);
        httpParams.put("id", CommunityBossApplication.storeInfo.getId());
        OkGo.<String>post(Urls.getInstance().UPDATESTOREINGO)
                .tag(this)
                .params(httpParams)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("修改营业状态", json.toString());
                            if (json.getInt("status") == 200) {
                                if (state == 0) {
                                    open = false;
                                    img_state.setImageResource(R.mipmap.img_state_rest);
                                } else {
                                    open = true;
                                    img_state.setImageResource(R.mipmap.img_state_open);
                                }
                                Toast.makeText(getContext(), "修改成功", Toast.LENGTH_SHORT).show();

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
