package net.leelink.communityboss.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.R;
import net.leelink.communityboss.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

public class ApplyActivity extends BaseActivity implements View.OnClickListener {
private Button btn_submit;
private EditText ed_name,ed_phone,ed_address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apply);
        init();
    }

    public void init(){
        btn_submit = findViewById(R.id.btn_submit);
        btn_submit.setOnClickListener(this);
        ed_name = findViewById(R.id.ed_name);
        ed_phone = findViewById(R.id.ed_phone);
        ed_address = findViewById(R.id.ed_address);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_submit:   //提交审核
                storeInfo();
                break;
                default:
                    break;
        }
    }

    //修改商户信息
    public void storeInfo(){
        OkGo.<String>post(Urls.STOREINFO+"?appToken=c672daaa99414db2a906f6c5a008f195")
                .tag(this)
                .params("address", ed_address.getText().toString().trim())
                .params("name",ed_name.getText().toString().trim())
                .params("phoneNumber",ed_phone.getText().toString().trim())
                .params("time","10:00-20:00")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            body = body.substring(1,body.length()-1);
                            JSONObject json = new JSONObject(body.replaceAll("\\\\",""));
                            Log.d("修改商户信息",json.toString());
                            if (json.getInt("ResultCode") == 200) {

                            } else {
                                Toast.makeText(ApplyActivity.this, json.getString("ResultValue"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
