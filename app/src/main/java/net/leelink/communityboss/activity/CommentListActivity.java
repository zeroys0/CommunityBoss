package net.leelink.communityboss.activity;

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
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.R;
import net.leelink.communityboss.adapter.CommentListAdapter;
import net.leelink.communityboss.adapter.OnItemClickListener;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.bean.CommentListBean;
import net.leelink.communityboss.utils.RatingBar;
import net.leelink.communityboss.utils.Urls;

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
private String page = "0";
private TextView tv_total_score;
private RatingBar rt_attitude,rt_taste,rt_hygiene,rt_delivery,rt_quality;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_list);
        init();
        initData(page);
    }

    public void init(){
        comment_list = findViewById(R.id.comment_list);

        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        tv_total_score = findViewById(R.id.tv_total_score);
        rt_attitude = findViewById(R.id.rt_attitude);
        rt_attitude.setUntouchable();
        rt_taste = findViewById(R.id.rt_taste);
        rt_taste.setUntouchable();
        rt_hygiene = findViewById(R.id.rt_hygiene);
        rt_hygiene.setUntouchable();
        rt_quality = findViewById(R.id.rt_quality);
        rt_quality.setUntouchable();
        rt_delivery = findViewById(R.id.rt_delivery);
        rt_delivery.setUntouchable();
    }

    public  void initData(String page){
        OkGo.<String>get(Urls.APPRAISELIST+"?model.appToken="+ CommunityBossApplication.token+"&model.orderId="+page)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            body = body.substring(1,body.length()-1);
                            JSONObject json = new JSONObject(body.replaceAll("\\\\",""));
                            Log.d("评价列表",json.toString());
                            if (json.getInt("ResultCode") == 200) {
                                json = json.getJSONObject("ObjectData");
                                Gson gson = new Gson();
                                commentListBean =gson.fromJson(json.toString(),CommentListBean.class);
                                commentListAdapter = new CommentListAdapter(CommentListActivity.this,commentListBean.getUserAppraiseList(),CommentListActivity.this);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(CommentListActivity.this,LinearLayoutManager.VERTICAL,false);
                                comment_list.setLayoutManager(layoutManager);
                                comment_list.setAdapter(commentListAdapter);
                                tv_total_score.setText("综合评分: "+commentListBean.getStoreScore());
                                rt_attitude.setSelectedNumber(getStar(commentListBean.getStoreAttitude()));
                                rt_taste.setSelectedNumber(getStar(commentListBean.getStoreTaste()));
                                rt_hygiene.setSelectedNumber(getStar(commentListBean.getStorePack()));
                                rt_delivery.setSelectedNumber(getStar(commentListBean.getStoreDelivery()));
                                rt_quality.setSelectedNumber(getStar(commentListBean.getStoreQuality()));

                            } else {

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
}
