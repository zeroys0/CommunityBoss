package net.leelink.communityboss.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;


import net.leelink.communityboss.R;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.utils.LoadDialog;
import net.leelink.communityboss.utils.Urls;

import org.json.JSONException;
import org.json.JSONObject;

public class NowUserActivity extends BaseActivity implements View.OnClickListener {
    Context context;
    private RelativeLayout rl_back;
    private RelativeLayout rl_logout;
    private TextView tv_phoneNumber;
    private String elderlyId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_user);
        context = this;
        init();
        initData();
    }

    public void init(){
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        rl_logout = findViewById(R.id.rl_logout);
        rl_logout.setOnClickListener(this);
        tv_phoneNumber = findViewById(R.id.tv_phoneNumber);
    }

    public void initData(){
        LoadDialog.start(context);
        OkGo.<String>get(Urls.getInstance().STOREHOME)
                .tag(this)
                .headers("token", CommunityBossApplication.token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        LoadDialog.stop();
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("个人中心", json.toString());
                            if (json.getInt("status") == 200) {
                                json = json.getJSONObject("data");
                                String t = json.getString("telephone");
                                StringBuilder tel = new StringBuilder();
                                tel.append(t);
                                tel.setCharAt(3,'*');
                                tel.setCharAt(4,'*');
                                tel.setCharAt(5,'*');
                                tel.setCharAt(6,'*');
                                tv_phoneNumber.setText(tel);
                                elderlyId = json.getString("id");
                                SharedPreferences sp = getSharedPreferences("sp",0);
                                SharedPreferences.Editor editor = sp.edit();
                                editor.putString("elderlyId",elderlyId);
                                editor.apply();
                            } else if (json.getInt("status") == 505) {

                            } else {
                                Toast.makeText(context, json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        LoadDialog.stop();
                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.rl_back:
                finish();
                break;
                case  R.id.rl_logout:
                    Intent intent = new Intent(this, LogoutActivity.class);
                    intent.putExtra("telephone",tv_phoneNumber.getText().toString());
                    intent.putExtra("elderlyId",elderlyId);
                    startActivity(intent);
            break;
        }

    }
}