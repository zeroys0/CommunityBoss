package net.leelink.communityboss.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import net.leelink.communityboss.R;
import net.leelink.communityboss.housekeep.HousekeepApplyActivity;

public class ChooseIdentityActivity extends BaseActivity implements View.OnClickListener {
private RelativeLayout rl_distribution,rl_housekeeping;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_identity);
        init();
    }

    public void init(){
        rl_distribution = findViewById(R.id.rl_distribution);
        rl_distribution.setOnClickListener(this);
        rl_housekeeping = findViewById(R.id.rl_housekeeping);
        rl_housekeeping.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_distribution:
                Intent intent = new Intent(this,ApplyActivity.class);
                intent.putExtra("id",getIntent().getStringExtra("id"));
                startActivity(intent);
                break;
            case R.id.rl_housekeeping:
                Intent intent1 = new Intent(this, HousekeepApplyActivity.class);
                intent1.putExtra("id",getIntent().getStringExtra("id"));
                startActivity(intent1);
                break;
                default:
                    break;
        }
    }
}
