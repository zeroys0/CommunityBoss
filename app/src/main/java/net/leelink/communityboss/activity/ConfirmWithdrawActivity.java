package net.leelink.communityboss.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.R;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

public class ConfirmWithdrawActivity extends BaseActivity implements View.OnClickListener {
private RelativeLayout rl_back;
private EditText ed_amount;
private Button btn_confirm;
private TextView tv_allin,tv_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_withdraw);
        init();
    }

    public void init(){
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        tv_name = findViewById(R.id.tv_name);
        tv_name.setText(getIntent().getStringExtra("bank_name"));
        ed_amount = findViewById(R.id.ed_amount);
        ed_amount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //删除“.”后面超过2位后的数据
                if (s.toString().contains(".")) {
                    if (s.length() - 1 - s.toString().indexOf(".") > 2) {
                        s = s.toString().subSequence(0,
                                s.toString().indexOf(".") + 3);
                        ed_amount.setText(s);
                        ed_amount.setSelection(s.length()); //光标移到最后
                    }
                }
                //如果"."在起始位置,则起始位置自动补0
                if (s.toString().trim().substring(0).equals(".")) {
                    s = "0" + s;
                    ed_amount.setText(s);
                    ed_amount.setSelection(2);
                }

                //如果起始位置为0,且第二位跟的不是".",则无法后续输入
                if (s.toString().startsWith("0")
                        && s.toString().trim().length() > 1) {
                    if (!s.toString().substring(1, 2).equals(".")) {
                        ed_amount.setText(s.subSequence(0, 1));
                        ed_amount.setSelection(1);
                        return;
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
        tv_allin = findViewById(R.id.tv_allin);
        tv_allin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_back:
                finish();
                break;
            case R.id.btn_confirm:
                if(ed_amount.getText().toString().trim().equals("")) {
                    Toast.makeText(this, "提现的金额不能为空", Toast.LENGTH_SHORT).show();
                } else {
                    withdraw();
                }
                break;
            case R.id.tv_allin:
                ed_amount.setText(getIntent().getStringExtra("balance"));
                break;
                default:
                    break;
        }
    }

    public void withdraw(){
        float f = Float.valueOf(ed_amount.getText().toString().trim());
        final int amount = (int)(f*100);
        OkGo.<String>post(Urls.TXAOUNT)
                .params("card",getIntent().getStringExtra("card_number"))
                .params("bigDecimal",ed_amount.getText().toString().trim())
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("提现",json.toString()+"     "+ amount);
                            if (json.getInt("status") == 200) {
                                finish();
                            } else {

                            }
                            Toast.makeText(ConfirmWithdrawActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
