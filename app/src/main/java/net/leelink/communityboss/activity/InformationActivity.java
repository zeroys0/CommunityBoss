package net.leelink.communityboss.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.R;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

public class InformationActivity extends BaseActivity implements View.OnClickListener {
private RelativeLayout rl_back;
private TextView tv_done,tv_time;
private EditText ed_name,ed_phone,ed_address;
private ImageView img_store_head,img_publicity,img_license,img_permit;
@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        init();
        initData();
    }

    public void init(){
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        tv_done = findViewById(R.id.tv_done);
        tv_done.setOnClickListener(this);
        ed_name = findViewById(R.id.ed_name);
        ed_phone = findViewById(R.id.ed_phone);
        ed_address = findViewById(R.id.ed_address);
        tv_time = findViewById(R.id.tv_time);
        tv_time.setOnClickListener(this);
        img_store_head = findViewById(R.id.img_store_head);
        img_store_head.setOnClickListener(this);
        img_publicity = findViewById(R.id.img_publicity);
        img_publicity.setOnClickListener(this);
        img_license = findViewById(R.id.img_license);
        img_license.setOnClickListener(this);
        img_permit = findViewById(R.id.img_permit);
        img_permit.setOnClickListener(this);
    }

    public void initData(){
        ed_name.setText(CommunityBossApplication.storeInfo.getStoreName());
        ed_phone.setText(CommunityBossApplication.storeInfo.getPhoneNumber());
        ed_address.setText(CommunityBossApplication.storeInfo.getAddress());
        tv_time.setText(CommunityBossApplication.storeInfo.getBusinessHours());
        Glide.with(this).load(Urls.IMAGEURL+"/Store/"+CommunityBossApplication.storeInfo.getStoreId()+"/Image/"+CommunityBossApplication.storeInfo.getHeadImage()).into(img_store_head);
        Glide.with(this).load(Urls.IMAGEURL+"/Store/"+CommunityBossApplication.storeInfo.getStoreId()+"/Image/"+CommunityBossApplication.storeInfo.getPropagandaImage()).into(img_publicity);
        Glide.with(this).load(Urls.IMAGEURL+"/Store/"+CommunityBossApplication.storeInfo.getStoreId()+"/Image/"+CommunityBossApplication.storeInfo.getBusinessLicense()).into(img_license);
        Glide.with(this).load(Urls.IMAGEURL+"/Store/"+CommunityBossApplication.storeInfo.getStoreId()+"/Image/"+CommunityBossApplication.storeInfo.getFoodLicence()).into(img_permit);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_back:
                finish();
                break;
            case R.id.tv_done:  //编辑
                edit();
                break;
                default:
                    break;
        }
    }

    //编辑资料
    public void edit(){
        OkGo.<String>post(Urls.STOREINFO+"?appToken="+CommunityBossApplication.token)
                .tag(this)
                .params("address", ed_address.getText().toString().trim())
                .params("name",ed_name.getText().toString().trim())
                .params("phoneNumber",ed_phone.getText().toString().trim())
                .params("time",tv_time.getText().toString())
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
                                Toast.makeText(InformationActivity.this, json.getString("ResultValue"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
