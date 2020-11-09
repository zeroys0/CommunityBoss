package net.leelink.communityboss.housekeep;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.R;
import net.leelink.communityboss.activity.BaseActivity;
import net.leelink.communityboss.bean.StaffBean;
import net.leelink.communityboss.housekeep.adapter.StaffCheckAdapter;
import net.leelink.communityboss.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class StaffCheckActivity extends BaseActivity implements View.OnClickListener {
private TextView tv_name,tv_sex,tv_birth,tv_nation,tv_phone,tv_id_card,tv_organ,tv_store,tv_skill,tv_experience,tv_educate,tv_remark;
private ImageView img0,img1,img2;
private Button order_cancel,order_confirm,btn_delete;
private RelativeLayout rl_back;
private  StaffBean staffBean;
private LinearLayout ll_button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_check);
        init();
    }

    public void init(){
        staffBean = (StaffBean) getIntent().getSerializableExtra("staff");
        tv_name = findViewById(R.id.tv_name);
        tv_name.setText(staffBean.getName());
        tv_sex = findViewById(R.id.tv_sex);
        if(staffBean.getSex() == 0){
            tv_sex.setText("男");
        } else {
            tv_sex.setText("女");
        }
        tv_birth = findViewById(R.id.tv_birth);
        tv_birth.setText(staffBean.getBirthday());
        tv_nation = findViewById(R.id.tv_nation);
        if(staffBean.getNature()>0) {
            String[] strings = getResources().getStringArray(R.array.nation_list);
            tv_nation.setText(strings[staffBean.getNature() - 1]);
        }
        tv_phone = findViewById(R.id.tv_phone);
        tv_phone.setText(staffBean.getTelephone());
        tv_id_card = findViewById(R.id.tv_id_card);
        tv_id_card.setText(staffBean.getCard());
        tv_organ = findViewById(R.id.tv_organ);
        tv_organ.setText(staffBean.getOrganName());
        tv_store = findViewById(R.id.tv_store);
        tv_store.setText(staffBean.getProviderName());
        tv_skill = findViewById(R.id.tv_skill);
        tv_skill.setText(staffBean.getJzSkill());
        tv_experience = findViewById(R.id.tv_experience);
        tv_experience.setText(staffBean.getJzWorkExp());
        tv_educate = findViewById(R.id.tv_educate);
        tv_educate.setText(staffBean.getJzTrainExp());
        tv_remark = findViewById(R.id.tv_remark);
        tv_remark.setText(staffBean.getRemark());
        ll_button = findViewById(R.id.ll_button);
        img0 = findViewById(R.id.img0);
        if(staffBean.getJzCertificatePathA()!=null){
            Glide.with(this).load(staffBean.getJzCertificatePathA()).into(img0);
        }
        img1 = findViewById(R.id.img1);
        if(staffBean.getJzCertificatePathB()!=null){
            Glide.with(this).load(staffBean.getJzCertificatePathB()).into(img1);
        }
        img2 = findViewById(R.id.img2);
        if(staffBean.getJzCertificatePathC()!=null){
            Glide.with(this).load(staffBean.getJzCertificatePathC()).into(img2);
        }
        order_cancel = findViewById(R.id.order_cancel);
        order_cancel.setOnClickListener(this);
        order_confirm = findViewById(R.id.order_confirm);
        order_confirm.setOnClickListener(this);
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        btn_delete = findViewById(R.id.btn_delete);
        if(getIntent().getStringExtra("action").equals("check")){
            ll_button.setVisibility(View.VISIBLE);
        } else {
            ll_button.setVisibility(View.GONE);
            btn_delete.setVisibility(View.VISIBLE);
            btn_delete.setOnClickListener(this);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_back:
                finish();
                break;
            case R.id.order_cancel:
                vertify(0);
                break;
            case R.id.order_confirm:
                vertify(1);
                break;
            case R.id.btn_delete:
                vertify(0);
                break;
                default:
                    break;
        }
    }
    public void vertify(int state){
        OkGo.<String>post(Urls.VERTIFYSER+"/"+staffBean.getId()+"/"+state)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("审核结果",json.toString());
                            if (json.getInt("status") == 200) {
                                finish();
                            } else {

                            }
                            Toast.makeText(StaffCheckActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }

}
