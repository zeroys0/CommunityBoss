package net.leelink.communityboss.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.R;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IncomeActivity extends BaseActivity implements View.OnClickListener {
private RelativeLayout rl_back,rl_get_money,rl_open_time,rl_close_time;
private TextView tv_income,tv_open_time,tv_close_time,tv_total_income,tv_royalty,tv_order_number;
    private TimePickerView pvTime,pvTime1;
    private SimpleDateFormat sdf,sdf1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        init();
        initdata();
        initPickerView();
        initClose();
    }

    public void init(){
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        rl_get_money = findViewById(R.id.rl_get_money);
        rl_get_money.setOnClickListener(this);
        tv_income = findViewById(R.id.tv_income);
        tv_open_time = findViewById(R.id.tv_open_time);
        tv_close_time = findViewById(R.id.tv_close_time);
        rl_open_time = findViewById(R.id.rl_open_time);
        rl_open_time.setOnClickListener(this);
        rl_close_time = findViewById(R.id.rl_close_time);
        rl_close_time.setOnClickListener(this);
        tv_total_income = findViewById(R.id.tv_total_income);
        tv_royalty = findViewById(R.id.tv_royalty);
        tv_order_number = findViewById(R.id.tv_order_number);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_get_money:     //去提现
                Intent intent = new Intent(this,WithdrawActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_open_time: //查询开始时间
                pvTime.show();
                break;
            case R.id.rl_close_time:    //查询结束时间
                pvTime1.show();
                break;
                default:
                    break;
        }
    }

    public void initdata(){
        OkGo.<String>get(Urls.STOREINCOME+"?appToken="+ CommunityBossApplication.token)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            body = body.substring(1,body.length()-1);
                            JSONObject json = new JSONObject(body.replaceAll("\\\\",""));
                            Log.d("查询收入",json.toString());
                            if (json.getInt("ResultCode") == 200) {
                                JSONObject jsonObject = json.getJSONObject("ObjectData");
                                tv_income.setText(jsonObject.getString("Balance"));
                                tv_order_number.setText(jsonObject.getString("OrderNumber")+"单");
                                tv_total_income.setText("￥"+jsonObject.getString("Income"));
                                tv_royalty.setText("￥"+jsonObject.getString("Royalty"));
                            } else {

                            }
                            Toast.makeText(IncomeActivity.this, json.getString("ResultValue"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    private void initPickerView() {
        boolean[] type = {true, true, true, false, false, false};
        sdf = new SimpleDateFormat("yyyy-MM-dd");
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tv_open_time.setText(sdf.format(date));
            }
        }).setType(type).build();
    }
    private void initClose(){

        boolean[] type = {true, true, true, false, false, false};
        sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        pvTime1 = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                tv_close_time.setText(sdf1.format(date));
            }
        }).setType(type).build();

    }
}
