package net.leelink.communityboss.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import net.leelink.communityboss.R;

public class ExamineActivity extends BaseActivity {
private RelativeLayout rl_back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_examine);
        init();
    }

    public void init(){
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}
