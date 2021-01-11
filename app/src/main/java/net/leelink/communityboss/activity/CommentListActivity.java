package net.leelink.communityboss.activity;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
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

import net.leelink.communityboss.R;
import net.leelink.communityboss.adapter.CommentListAdapter;
import net.leelink.communityboss.adapter.OnItemClickListener;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.bean.CommentListBean;
import net.leelink.communityboss.bean.MyInfoBean;
import net.leelink.communityboss.utils.RatingBar;
import net.leelink.communityboss.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

import javax.xml.transform.Templates;

public class CommentListActivity extends BaseActivity implements OnItemClickListener , View.OnClickListener {
private RecyclerView comment_list;
private CommentListAdapter commentListAdapter;
private CommentListBean commentListBean;
private RelativeLayout rl_back;
private String orderId = "0";
private TextView tv_total_score,tv_total,tv_pack,tv_taste;
private RatingBar rt_total,rt_taste,rt_pack,rt_quality;
private TwinklingRefreshLayout refreshLayout;
private List<CommentListBean> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);
        init();
        initData(orderId);
        initRefreshLayout();
    }

    public void init(){
        comment_list = findViewById(R.id.comment_list);
        comment_list.setNestedScrollingEnabled(false);
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        tv_total_score = findViewById(R.id.tv_total_score);
        rt_total = findViewById(R.id.rt_total);
        rt_total.setUntouchable();
        rt_taste = findViewById(R.id.rt_taste);
        rt_taste.setUntouchable();
        rt_pack = findViewById(R.id.rt_pack);
        rt_pack.setUntouchable();
        MyInfoBean  myInfoBean = (MyInfoBean) getIntent().getSerializableExtra("myInfo");
        rt_total.setSelectedNumber(myInfoBean.getTotal_star());
        rt_pack.setSelectedNumber(myInfoBean.getProduct_star());
        rt_taste.setSelectedNumber(myInfoBean.getTaste_star());
        tv_total = findViewById(R.id.tv_total);
        tv_total.setText(myInfoBean.getTotal_star()+"");
        tv_total_score = findViewById(R.id.tv_total_score);
        tv_total_score.setText("综合评分"+myInfoBean.getTotal_star());
        tv_pack = findViewById(R.id.tv_pack);
        tv_pack.setText(myInfoBean.getProduct_star()+"");
        tv_taste = findViewById(R.id.tv_taste);
        tv_taste.setText(myInfoBean.getTaste_star()+"");
    }

    public  void initData(String orderId){
        OkGo.<String>get(Urls.getInstance().APPRAISELIST)
                .params("pageNum",1)
                .params("pageSize",10)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("评价列表",json.toString());
                            if (json.getInt("status") == 200) {
                                    json =  json.getJSONObject("data");
                                    JSONArray jsonArray = json.getJSONArray("list");
                                    commentListAdapter = new CommentListAdapter(CommentListActivity.this, jsonArray, CommentListActivity.this);
                                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CommentListActivity.this, LinearLayoutManager.VERTICAL, false);
                                    comment_list.setLayoutManager(layoutManager);
                                    comment_list.setAdapter(commentListAdapter);
                            } else {
                                Toast.makeText(CommentListActivity.this, json.getString("message"), Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onItemClick(View view) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_back:
                finish();
                break;
                default:
                    break;
        }
    }

    public int getStar(double score){
        if(score>4.5){
            return 5;
        }
        if(score>3.5){
            return 4;
        }
        if(score>2.5){
            return 3;
        }
        if(score>1.5){
            return 2;
        }
        if(score>0.5){
            return 1;
        }
        return 0;
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
                        list.clear();
                        orderId = "0";
                        initData(orderId);
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore(final TwinklingRefreshLayout refreshLayout) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        refreshLayout.finishLoadmore();
                        initData(orderId);
                     //   commentListAdapter.update(j);
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
