package net.leelink.communityboss.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.model.MyLocationStyle;
import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.R;
import net.leelink.communityboss.adapter.BalanceAdapter;
import net.leelink.communityboss.adapter.IncomeAdapter;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.bean.BalanceBean;
import net.leelink.communityboss.bean.IncomeBean;
import net.leelink.communityboss.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class BalanceActivity extends BaseActivity implements View.OnClickListener {
    RelativeLayout rl_back;
    private TextView tv_type,tv_withdraw,tv_time,tv_out,tv_income,tv_balance;
    private BalanceAdapter balanceAdapter;
    private RecyclerView balance_list;
    private TimePickerView pvTime;
    private SimpleDateFormat sdf;
    private String sort="";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_balance);
        init();
        context = this;
        initPickerView();
    }

    public void init(){
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        tv_type = findViewById(R.id.tv_type);
        tv_type.setOnClickListener(this);
        balance_list = findViewById(R.id.balance_list);
        tv_withdraw = findViewById(R.id.tv_withdraw);
        tv_withdraw.setOnClickListener(this);
        tv_time = findViewById(R.id.tv_time);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");// HH:mm:ss
//获取当前时间
        Date date = new Date(System.currentTimeMillis());
        tv_time.setText(simpleDateFormat.format(date));
        tv_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show();
            }
        });
        tv_out = findViewById(R.id.tv_out);
        tv_income = findViewById(R.id.tv_income);
        tv_balance = findViewById(R.id.tv_balance);

    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        initList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_type:
                backgroundAlpha(0.5f);
                showPopup();
                break;
            case R.id.tv_withdraw:
                Intent intent = new Intent(this,ConfirmWithdrawActivity.class);
                intent.putExtra("balance",tv_balance.getText().toString());
                startActivity(intent);
                break;
        }
    }

    public void initData(){
        OkGo.<String>get(Urls.getInstance().STOREHOME)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("账户余额", json.toString());
                            if (json.getInt("status") == 200) {
                                json = json.getJSONObject("data");
                                CommunityBossApplication.storeInfo.setWallet(json.getDouble("wallet"));
                                tv_balance.setText(CommunityBossApplication.storeInfo.getWallet()+"");
                            } else {
                                Toast.makeText(context, json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void initList(){
        OkGo.<String>get(Urls.getInstance().ACCOUNT)
                .params("date",tv_time.getText().toString())
                .params("sort",sort)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("账户余额", json.toString());
                            if (json.getInt("status") == 200) {
                                json = json.getJSONObject("data");
                                Gson gson = new Gson();
                                BalanceBean balanceBean = gson.fromJson(json.toString(),BalanceBean.class);
                                tv_out.setText("支出￥"+balanceBean.getOutAmount());
                                tv_income.setText("收入￥"+balanceBean.getEntryAmount());
                               // tv_month_amount.setText("月营业额￥"+incomeBean.getMonthAmount());
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
                                balanceAdapter = new BalanceAdapter(balanceBean.getAccountList(),context);
                                balance_list.setLayoutManager(layoutManager);
                                balance_list.setAdapter(balanceAdapter);
                            } else {
                                Toast.makeText(context, json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }


    public void showPopup(){
        View popView = getLayoutInflater().inflate(R.layout.popu_choose_cure, null);
        TextView tv_all = popView.findViewById(R.id.tv_all);
        TextView tv_income = popView.findViewById(R.id.tv_income);
        TextView tv_withdraw = popView.findViewById(R.id.tv_withdraw);

        final PopupWindow pop = new PopupWindow(popView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
        pop.setContentView(popView);
        pop.setOutsideTouchable(true);
        pop.setBackgroundDrawable(new BitmapDrawable());
        pop.setOnDismissListener(new BalanceActivity.poponDismissListener());
        tv_all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                sort = "";
                initList();
            }
        });
        tv_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                sort = "1";
                initList();
            }
        });
        tv_withdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop.dismiss();
                sort = "2";
                initList();
            }
        });

        pop.showAtLocation(rl_back, Gravity.BOTTOM, 0, 0);
    }

    /**
     * 添加新笔记时弹出的popWin关闭的事件，主要是为了将背景透明度改回来
     *
     * @author cg
     */
    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            // TODO Auto-generated method stub
            // Log.v("List_noteTypeActivity:", "我是关闭事件");
            backgroundAlpha(1f);
        }
    }

    /**
     * 设置添加屏幕的背景透明度
     *
     * @param bgAlpha
     */
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha; // 0.0-1.0
        if (bgAlpha == 1) {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//不移除该Flag的话,在有视频的页面上的视频会出现黑屏的bug
        } else {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//此行代码主要是解决在华为手机上半透明效果无效的bug
        }
        getWindow().setAttributes(lp);
    }

    private void initPickerView(){
        sdf = new SimpleDateFormat("yyyy-MM");
        boolean[] type = {true, true, false, false, false, false};
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tv_time.setText(sdf.format(date));
//                myDate = sdf.format(date);
                initList();
            }
        }).setType(type).build();
    }

}
