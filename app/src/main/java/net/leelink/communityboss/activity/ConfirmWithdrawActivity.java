package net.leelink.communityboss.activity;

import android.content.Intent;
import android.support.annotation.Nullable;
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
private RelativeLayout rl_back,rl_default,rl_card;
private EditText ed_amount;
private Button btn_confirm;
private TextView tv_allin,tv_name,tv_draw_history,tv_balance,tv_card_number;
private String cardNumber;
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
        tv_card_number = findViewById(R.id.tv_card_number);

     //   tv_name.setText(getIntent().getStringExtra("bank_name"));
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
        tv_draw_history = findViewById(R.id.tv_draw_history);
        tv_draw_history.setOnClickListener(this);
        rl_default = findViewById(R.id.rl_default);
        rl_default.setOnClickListener(this);
        rl_card = findViewById(R.id.rl_card);
        rl_card.setOnClickListener(this);
        tv_balance = findViewById(R.id.tv_balance);
        tv_balance.setText("可提现余额"+getIntent().getStringExtra("balance")+"元");
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
            case R.id.tv_draw_history:
                Intent intent = new Intent(this,DrawHistoryActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_default:
            case R.id.rl_card:
                Intent intent1 = new Intent(this,WithdrawActivity.class);
                startActivityForResult(intent1,1);
                break;
                default:
                    break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            if(requestCode ==1) {
                rl_default.setVisibility(View.GONE);
                rl_card.setVisibility(View.VISIBLE);
                tv_name.setText(data.getStringExtra("name"));
                cardNumber = data.getStringExtra("number");
                if(cardNumber.length()>4){
                    String number = cardNumber.substring(cardNumber.length()-4);
                    tv_card_number.setText("尾号"+number);
                } else {
                    tv_card_number.setText("尾号"+cardNumber);
                }
            }
        }
    }

    public void withdraw(){
        if(cardNumber==null){
            Toast.makeText(this, "请选择银行卡", Toast.LENGTH_SHORT).show();
            return;
        }
        float f = Float.valueOf(ed_amount.getText().toString().trim());
        final int amount = (int)(f*100);
        OkGo.<String>post(Urls.getInstance().TXAOUNT)
                .params("card",cardNumber)
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
                                Toast.makeText(ConfirmWithdrawActivity.this, "提现成功", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(ConfirmWithdrawActivity.this, "提现失败", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
