package net.leelink.communityboss.activity;

import android.content.Context;
import android.content.Intent;
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
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.R;
import net.leelink.communityboss.adapter.DrawListAdapter;
import net.leelink.communityboss.adapter.IncomeAdapter;
import net.leelink.communityboss.adapter.OnOrderListener;
import net.leelink.communityboss.bean.DrawBean;
import net.leelink.communityboss.bean.IncomeBean;
import net.leelink.communityboss.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class DrawHistoryActivity extends BaseActivity implements View.OnClickListener, OnOrderListener {
    private RecyclerView draw_list;
    private RelativeLayout rl_back;
    private Context context;
    private DrawListAdapter drawListAdapter;
    private TextView tv_time;
    private TimePickerView pvTime;
    private List<DrawBean> list;
    private SimpleDateFormat sdf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_history);
        init();
        context = this;
        initList();
        initPickerView();
    }

    public void init(){
        draw_list = findViewById(R.id.draw_list);
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
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
    }

    public void initList(){
        OkGo.<String>get(Urls.getInstance().STORE_TX)
                .params("date",tv_time.getText().toString())
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("提现记录", json.toString());
                            if (json.getInt("status") == 200) {
                                json = json.getJSONObject("data");
                                Gson gson = new Gson();
                                JSONArray jsonArray = json.getJSONArray("storeTxVoList");
                                list= gson.fromJson(jsonArray.toString(),new TypeToken<List<DrawBean>>(){}.getType());
                                drawListAdapter = new DrawListAdapter(list,context,DrawHistoryActivity.this);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false);
                                draw_list.setLayoutManager(layoutManager);
                                draw_list.setAdapter(drawListAdapter);

                            } else {
                                Toast.makeText(DrawHistoryActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
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
            case R.id.rl_back:
                finish();
                break;
        }
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

    @Override
    public void onItemClick(View view) {
        int position = draw_list.getChildLayoutPosition(view);
        Intent intent = new Intent(this,DrawDetailActivity.class);
        intent.putExtra("state",list.get(position).getState());
        intent.putExtra("time",list.get(position).getStartTime());
        intent.putExtra("number",list.get(position).getCard());

        startActivity(intent);
    }

    @Override
    public void onButtonClick(View view, int position) {

    }
}
