package net.leelink.communityboss.activity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.R;
import net.leelink.communityboss.adapter.IncomeAdapter;
import net.leelink.communityboss.bean.IncomeBean;
import net.leelink.communityboss.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class IncomeListActicity extends BaseActivity {
    RelativeLayout rl_back;
    private RecyclerView income_list;
    private IncomeAdapter incomeAdapter;
    private TextView tv_time,tv_month_amount;
    private TimePickerView pvTime;
    private SimpleDateFormat sdf;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_list_acticity);
        init();
        context = this;
        initList();
        initPickerView();
    }

    public void init(){
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        income_list = findViewById(R.id.income_list);
        tv_time = findViewById(R.id.tv_time);
        tv_month_amount = findViewById(R.id.tv_month_amount);
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
    }

    public void initList(){

        OkGo.<String>get(Urls.getInstance().STORE_INCOME)
                .params("date",tv_time.getText().toString())
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("收入统计", json.toString());
                            if (json.getInt("status") == 200) {
                                json = json.getJSONObject("data");
                                Gson gson = new Gson();
                                IncomeBean incomeBean = gson.fromJson(json.toString(),IncomeBean.class);
                                tv_month_amount.setText("月营业额￥"+incomeBean.getMonthAmount());
                                incomeAdapter = new IncomeAdapter(incomeBean.getStoreInComeVo(),context);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
                                income_list.setLayoutManager(layoutManager);
                                income_list.setAdapter(incomeAdapter);
                            } else {
                                Toast.makeText(IncomeListActicity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
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
