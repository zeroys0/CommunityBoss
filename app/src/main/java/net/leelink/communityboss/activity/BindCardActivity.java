package net.leelink.communityboss.activity;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.R;
import net.leelink.communityboss.adapter.CardListAdapter;
import net.leelink.communityboss.adapter.OnItemClickListener;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.bean.BankBean;
import net.leelink.communityboss.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BindCardActivity extends BaseActivity implements OnItemClickListener, View.OnClickListener {
    private RelativeLayout rl_back, rl_bank;
    private Button btn_submit;
    private EditText ed_cardId, ed_ID, ed_card_number, ed_phone, ed_code, ed_name, ed_card;
    private TextView getmsmpass_TX, tv_bank;
    int bank_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_card);
        init();
    }

    public void init() {
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        ed_cardId = findViewById(R.id.ed_cardId);
//        ed_ID = findViewById(R.id.ed_ID);
        ed_name = findViewById(R.id.ed_name);
        ed_card = findViewById(R.id.ed_card);
//        ed_card_number = findViewById(R.id.ed_card_number);
        ed_phone = findViewById(R.id.ed_phone);
        tv_bank = findViewById(R.id.tv_bank);
        getmsmpass_TX = findViewById(R.id.getmsmpass_TX);
        getmsmpass_TX.setOnClickListener(this);
        ed_code = findViewById(R.id.ed_code);
        rl_bank = findViewById(R.id.rl_bank);
        rl_bank.setOnClickListener(this);
    }

    @Override
    public void onItemClick(View view) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_back:
                finish();
                break;
            case R.id.btn_submit:   //绑定银行卡
                bind();
                break;
            case R.id.getmsmpass_TX:
                sendSmsCode();
                break;
            case R.id.rl_bank:
                getBank();
                break;
            default:
                break;
        }
    }

    public void bind() {
        OkGo.<String>post(Urls.BINDCARD)
                .params("bankCard", ed_cardId.getText().toString().trim())
                .params("code", ed_code.getText().toString().trim())
                .params("name", ed_name.getText().toString().trim())
                .params("idCard", ed_card.getText().toString().trim())
                .params("telephone", ed_phone.getText().toString().trim())
                .params("bankId",bank_id)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("添加银行卡", json.toString());
                            if (json.getInt("status") == 200) {
                                finish();
                            } else {

                            }
                            Toast.makeText(BindCardActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    public void getBank() {
        OkGo.<String>get(Urls.BANK)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("支持的银行", json.toString());
                            if (json.getInt("status") == 200) {
                                JSONArray jsonArray = json.getJSONArray("data");
                                Gson gson = new Gson();
                                List<BankBean> bank_list = gson.fromJson(jsonArray.toString(), new TypeToken<List<BankBean>>() {
                                }.getType());
                                showBank(bank_list);

                            } else {
                                Toast.makeText(BindCardActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

    // 获取短信验证码的页面显示
    private int time = 60;

    //发送短信验证码
    public void sendSmsCode() {
        if (!ed_phone.getText().toString().trim().equals("")) {
            OkGo.<String>post(Urls.SENDSMSCODE + "?telephone=" + ed_phone.getText().toString().trim())
                    .tag(this)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            try {
                                String body = response.body();
                                JSONObject json = new JSONObject(body);
                                Log.d("获取验证码", json.toString());
                                if (json.getInt("status") == 200) {
                                    if (time == 60) {
                                        new Thread(new BindCardActivity.TimeRun()).start();
                                    } else {
                                        getmsmpass_TX.setEnabled(false);
                                    }
                                } else {
                                    Toast.makeText(BindCardActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

        } else {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
        }
    }


    private class TimeRun implements Runnable {
        @Override
        public void run() {
            while (true) {
                mHandler.sendEmptyMessage(0);
                if (time == 0) {
                    getmsmpass_TX.setOnClickListener(BindCardActivity.this);
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                }
            }
        }

        @SuppressLint("HandlerLeak")
        private Handler mHandler = new Handler() {
            public void handleMessage(Message msg) {
                if (time == 0) {
                    getmsmpass_TX.setText("获取验证码");
                    time = 60;
                } else {
                    getmsmpass_TX.setText((--time) + "秒");
                }
            }
        };
    }

    public void showBank(final List<BankBean> bank) {
        final List<String> bankName = new ArrayList<>();
        for (BankBean bankBean : bank) {
            bankName.add(bankBean.getName());
        }
        //条件选择器
        OptionsPickerView pvOptions = new OptionsPickerBuilder(BindCardActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                if (bank.size() != 0) {
                    tv_bank.setText(bank.get(options1).getName());
                    bank_id = bank.get(options1).getId();
                }
            }
        })
                .setDividerColor(Color.parseColor("#A0A0A0"))
                .setTextColorCenter(Color.parseColor("#333333")) //设置选中项文字颜色
                .setContentTextSize(18)//设置滚轮文字大小
                .setOutSideCancelable(true)//点击外部dismiss default true
                .build();
        pvOptions.setPicker(bankName);
        pvOptions.show();
    }
}
