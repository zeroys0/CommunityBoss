package net.leelink.communityboss.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import net.leelink.communityboss.R;


public class AnswerActivity extends BaseActivity {
private RelativeLayout rl_back;
private TextView tv_content;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_answer);
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
        tv_content = findViewById(R.id.tv_content);
        tv_content.setText("    "+ getIntent().getStringExtra("answer"));
    }
}
