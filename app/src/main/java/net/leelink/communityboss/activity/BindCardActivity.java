package net.leelink.communityboss.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import net.leelink.communityboss.adapter.CardListAdapter;
import net.leelink.communityboss.adapter.OnItemClickListener;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class BindCardActivity extends BaseActivity implements OnItemClickListener , View.OnClickListener {
private RelativeLayout rl_back;
private Button btn_submit;
private EditText ed_name,ed_ID,ed_bank,ed_card_number,ed_phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_card);
        init();
    }

    public void init(){
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        ed_name = findViewById(R.id.ed_name);
        ed_ID = findViewById(R.id.ed_ID);
        ed_bank = findViewById(R.id.ed_bank);
        ed_card_number = findViewById(R.id.ed_card_number);
        ed_phone = findViewById(R.id.ed_phone);
    }

    @Override
    public void onItemClick(View view) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_back:
                finish();
                break;
            case R.id.btn_submit:   //绑定银行卡
                bind();
                break;

                default:
                    break;
        }
    }

    public void bind(){
        OkGo.<String>post(Urls.BANKCARD+"?appToken="+ CommunityBossApplication.token)
                .params("userName",ed_name.getText().toString().trim())
                .params("idNumber",ed_ID.getText().toString().trim())
                .params("bank",ed_bank.getText().toString().trim())
                .params("cardNumber",ed_card_number.getText().toString().trim())
                .params("phoneNumber",ed_phone.getText().toString().trim())
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            body = body.substring(1,body.length()-1);
                            JSONObject json = new JSONObject(body.replaceAll("\\\\",""));
                            Log.d("添加银行卡",json.toString());
                            if (json.getInt("ResultCode") == 200) {
                                finish();
                            } else {

                            }
                            Toast.makeText(BindCardActivity.this, json.getString("ResultValue"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
