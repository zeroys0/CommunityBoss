package net.leelink.communityboss.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import net.leelink.communityboss.R;

public class ManageListActivity extends BaseActivity implements View.OnClickListener {
private RelativeLayout rl_back,rl_add;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_list);
        init();
    }
    public void init(){
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        rl_add = findViewById(R.id.rl_add);
        rl_add.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_back:
                finish();
                break;
            case R.id.rl_add:   //物品管理
                Intent intent =  new Intent(this,ManageGoodsActivity.class);
                startActivity(intent);
                break;
                default:
                    break;
        }
    }
}
