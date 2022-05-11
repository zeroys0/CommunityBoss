package net.leelink.communityboss.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.yanzhenjie.recyclerview.OnItemMenuClickListener;
import com.yanzhenjie.recyclerview.SwipeMenu;
import com.yanzhenjie.recyclerview.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.SwipeMenuItem;
import com.yanzhenjie.recyclerview.SwipeRecyclerView;

import net.leelink.communityboss.R;
import net.leelink.communityboss.adapter.CardListAdapter;
import net.leelink.communityboss.adapter.OnCollectListener;
import net.leelink.communityboss.adapter.OnItemClickListener;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.bean.CardBean;
import net.leelink.communityboss.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class WithdrawActivity extends BaseActivity implements OnItemClickListener, View.OnClickListener {
    private SwipeRecyclerView card_list;
    private CardListAdapter cardListAdapter;
    private List<CardBean> list;
    private LinearLayout ll_add;
    private RelativeLayout rl_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);
        init();
        initdata();
        initSlide();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        initdata();
    }

    public void init(){
        card_list = findViewById(R.id.card_list);

        ll_add = findViewById(R.id.ll_add);
        ll_add.setOnClickListener(this);
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
    }

    public void initdata(){
        OkGo.<String>get(Urls.getInstance().BINDCARD)

                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("银行卡列表",json.toString());
                            if (json.getInt("status") == 200) {
                                JSONArray jsonArray  = json.getJSONArray("data");
                                Gson gson = new Gson();
                                list = gson.fromJson(jsonArray.toString(),new TypeToken<List<CardBean>>(){}.getType());
                                cardListAdapter = new CardListAdapter(WithdrawActivity.this,list,WithdrawActivity.this);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(WithdrawActivity.this,LinearLayoutManager.VERTICAL,false);
                                card_list.setLayoutManager(layoutManager);
                                card_list.setAdapter(cardListAdapter);
                            } else {
                                Toast.makeText(WithdrawActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
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
            case R.id.ll_add:   //添加银行卡
                Intent intent = new Intent(this,BindCardActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_back:
                finish();
                break;
                default:
                    break;
        }
    }

    @Override
    public void onItemClick(View view) {
        int position = card_list.getChildLayoutPosition(view);
//        Intent intent = new Intent(this,ConfirmWithdrawActivity.class);
//        startActivity(intent);
        Intent intent = new Intent();
        intent.putExtra("name",list.get(position).getBankName());
        intent.putExtra("number",list.get(position).getBankCard());
        setResult(1,intent);
        finish();

    }

    private void initSlide() {

        // srvWarn.setItemViewSwipeEnabled(true);// 开启滑动删除。默认关闭。
        card_list.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        // 创建菜单：
        SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
            @Override
            public void onCreateMenu(SwipeMenu leftMenu, SwipeMenu rightMenu, int viewType) {
           /* SwipeMenuItem deleteItem = new SwipeMenuItem(mContext);
            // 各种文字和图标属性设置。
            leftMenu.addMenuItem(deleteItem); // 在Item左侧添加一个菜单。*/
                // 2 删除
                SwipeMenuItem deleteItem = new SwipeMenuItem(WithdrawActivity.this);
                deleteItem.setText("删除")
                        .setBackgroundColor(getResources().getColor(R.color.red))
                        .setTextColor(Color.WHITE) // 文字颜色。
                        .setTextSize(15) // 文字大小。
                        .setWidth(200)
                        .setHeight(ViewGroup.LayoutParams.MATCH_PARENT);

                rightMenu.addMenuItem(deleteItem);

                // 注意：哪边不想要菜单，那么不要添加即可。
            }
        };
        // 设置监听器。
        card_list.setSwipeMenuCreator(mSwipeMenuCreator);
        card_list.setOnItemMenuClickListener(new OnItemMenuClickListener() {
            @Override
            public void onItemClick(SwipeMenuBridge menuBridge, int position) {
                // 任何操作必须先关闭菜单，否则可能出现Item菜单打开状态错乱。
                menuBridge.closeMenu();

                // 左侧还是右侧菜单：
                int direction = menuBridge.getDirection();
                // 菜单在Item中的Position：
                int menuPosition = menuBridge.getPosition();
                if (menuPosition == 0) {
//                    int id = list.get(position).getStoreId();
                    bind(position);
                    Toast.makeText(WithdrawActivity.this, "已删除", Toast.LENGTH_SHORT).show();
                }
            }
        });
        card_list.setOnItemClickListener(new com.yanzhenjie.recyclerview.OnItemClickListener() {
            @Override
            public void onItemClick(View itemView, int position) {
//                Intent intent = new Intent(WithdrawActivity.this, ConfirmWithdrawActivity.class);
//                intent.putExtra("card_number", list.get(position).getBankCard());
//                intent.putExtra("balance",getIntent().getStringExtra("balance"));
//                intent.putExtra("bank_name",list.get(position).getBankName());
//                startActivity(intent);
                Intent intent = new Intent();
                intent.putExtra("name",list.get(position).getBankName());
                intent.putExtra("number",list.get(position).getBankCard());
                setResult(1,intent);
                finish();
            }
        });


    }
    public void bind(int position){
        OkGo.<String>delete(Urls.getInstance().BINDCARD+"?appToken="+ CommunityBossApplication.token+"&id="+list.get(position).getBindId())
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            body = body.substring(1,body.length()-1);
                            JSONObject json = new JSONObject(body.replaceAll("\\\\",""));
                            Log.d("删除银行卡",json.toString());
                            if (json.getInt("ResultCode") == 200) {
                               initdata();
                            } else {

                            }
                            Toast.makeText(WithdrawActivity.this, json.getString("ResultValue"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }


}
