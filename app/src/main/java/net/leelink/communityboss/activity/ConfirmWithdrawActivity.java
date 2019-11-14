package net.leelink.communityboss.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_withdraw);
        init();
    }

    public void init(){
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        ed_amount = findViewById(R.id.ed_amount);
        btn_confirm = findViewById(R.id.btn_confirm);
        btn_confirm.setOnClickListener(this);
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
                default:
                    break;
        }
    }

    public void withdraw(){
        float f = Float.valueOf(ed_amount.getText().toString().trim());
        int amount = (int)f*100;
        OkGo.<String>post(Urls.CASHOUT+"?appToken="+ CommunityBossApplication.token)
                .params("cardId",getIntent().getStringExtra("card_number"))
                .params("amount",amount)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            body = body.substring(1,body.length()-1);
                            JSONObject json = new JSONObject(body.replaceAll("\\\\",""));
                            Log.d("提现",json.toString());
                            if (json.getInt("ResultCode") == 200) {
                                finish();
                            } else {

                            }
                            Toast.makeText(ConfirmWithdrawActivity.this, json.getString("ResultValue"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
