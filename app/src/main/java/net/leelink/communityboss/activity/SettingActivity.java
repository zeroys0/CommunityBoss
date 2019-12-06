package net.leelink.communityboss.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

public class SettingActivity extends BaseActivity implements View.OnClickListener {
private RelativeLayout rl_back,rl_logout;
private ImageView img_head,img_change;
private TextView tv_name,tv_phone;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        init();
    }

    public void init(){
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        rl_logout = findViewById(R.id.rl_logout);
        rl_logout.setOnClickListener(this);
        img_head = findViewById(R.id.img_head);
        img_change = findViewById(R.id.img_change);
        img_change.setOnClickListener(this);
        tv_name = findViewById(R.id.tv_name);
        tv_phone = findViewById(R.id.tv_phone);
        Glide.with(this).load(Urls.IMAGEURL+"Store/"+CommunityBossApplication.storeInfo.getStoreId()+"/Image/"+CommunityBossApplication.storeInfo.getHeadImage()).into(img_head);
        tv_name.setText(CommunityBossApplication.storeInfo.getStoreName());
        tv_phone.setText(CommunityBossApplication.storeInfo.getUsername());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_logout:
                logout();
                break;
            case R.id.img_change:       //修改绑定电话
                Intent intent = new Intent(this,ChangePhoneActivity.class);
                startActivity(intent);
                break;
                default:
                    break;
        }
    }

    public void logout(){
        SharedPreferences sp = getSharedPreferences("sp",0);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove("AppToken");
        editor.apply();
        Intent intent = new Intent(SettingActivity.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        OkGo.<String>get(Urls.LOGOUT+"?appToken="+ CommunityBossApplication.token)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            body = body.substring(1,body.length()-1);
                            JSONObject json = new JSONObject(body.replaceAll("\\\\",""));
                            Log.d("退出登录",json.toString());
                            if (json.getInt("ResultCode") == 200) {

                                finish();
                            } else {

                            }
                            Toast.makeText(SettingActivity.this, "账号注销,请重新登录", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
}
