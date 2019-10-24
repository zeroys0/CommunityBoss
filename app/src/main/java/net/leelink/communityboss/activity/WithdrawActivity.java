package net.leelink.communityboss.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.R;
import net.leelink.communityboss.adapter.CardListAdapter;
import net.leelink.communityboss.adapter.OnCollectListener;
import net.leelink.communityboss.adapter.OnItemClickListener;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class WithdrawActivity extends BaseActivity implements OnItemClickListener {
    private RecyclerView card_list;
    private CardListAdapter cardListAdapter;
    private List<String> cards_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        init();
        initdata();
    }


    public void init(){
        card_list = findViewById(R.id.card_list);
        cardListAdapter = new CardListAdapter(this,cards_list,this);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        card_list.setLayoutManager(layoutManager);
        card_list.setAdapter(cardListAdapter);
    }

    public void initdata(){
        OkGo.<String>get(Urls.BANKCARD+"?appToken="+ CommunityBossApplication.token)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            body = body.substring(1,body.length()-1);
                            JSONObject json = new JSONObject(body.replaceAll("\\\\",""));
                            Log.d("修改绑定电话",json.toString());
                            if (json.getInt("ResultCode") == 200) {

                            } else {

                            }
                            Toast.makeText(WithdrawActivity.this, json.getString("ResultValue"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    @Override
    public void onItemClick(View view) {

    }
}
