package net.leelink.communityboss.housekeep;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.leelink.communityboss.R;
import net.leelink.communityboss.activity.BaseActivity;
import net.leelink.communityboss.bean.HsOrderBean;
import net.leelink.communityboss.bean.WorkBean;
import net.leelink.communityboss.utils.Urls;

public class HsOrderDetailActivity extends BaseActivity {
    private TextView tv_orderid,tv_time,tv_name,tv_phone,tv_address,tv_good_name,tv_price,tv_total_price,tv_servicer,tv_sex,tv_servphone,tv_service_number;
    private ImageView goods_head,img0,img1,img2;
    private Button btn_confirm;
    private LinearLayout ll_title,ll_servicer,ll_sex,ll_phone,ll_number,ll_start_time,ll_end_time,ll_img,ll_button,ll_state;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hs_order_detail);
        init();
        initData();
    }

    public void init(){
        tv_orderid = findViewById(R.id.tv_orderid);
        tv_time = findViewById(R.id.tv_time);
        tv_name = findViewById(R.id.tv_name);
        tv_phone = findViewById(R.id.tv_phone);
        tv_address = findViewById(R.id.tv_address);
        goods_head = findViewById(R.id.goods_head);
        tv_good_name = findViewById(R.id.tv_good_name);
        tv_price = findViewById(R.id.tv_price);
        tv_total_price = findViewById(R.id.tv_total_price);
        btn_confirm = findViewById(R.id.btn_confirm);
        ll_title = findViewById(R.id.ll_title);
        ll_servicer = findViewById(R.id.ll_servicer);
        ll_sex = findViewById(R.id.ll_sex);
        ll_phone = findViewById(R.id.ll_phone);
        ll_number = findViewById(R.id.ll_number);
        ll_start_time = findViewById(R.id.ll_start_time);
        ll_end_time =findViewById(R.id.ll_end_time);
        ll_img = findViewById(R.id.ll_img);
        img0 = findViewById(R.id.img0);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        ll_button = findViewById(R.id.ll_button);
        ll_state = findViewById(R.id.ll_state);
        tv_servicer = findViewById(R.id.tv_servicer);
        tv_sex = findViewById(R.id.tv_sex);
        tv_servphone =findViewById(R.id.tv_servphone);
        tv_service_number = findViewById(R.id.tv_service_number);
    }

    public void initData(){
        int type = getIntent().getIntExtra("type",0);
        HsOrderBean hsOrderBean ;
        WorkBean workBean;
        switch (type){
            case 1:
                hsOrderBean = (HsOrderBean) getIntent().getSerializableExtra("orderDetail");
                tv_orderid.setText(hsOrderBean.getOrderNo());
                tv_time.setText(hsOrderBean.getApointTime());
                tv_name.setText(hsOrderBean.getDeliveryName());
                tv_phone.setText(hsOrderBean.getDeliveryPhone());
                tv_address.setText(hsOrderBean.getReceivingAddress());
                Glide.with(this).load(Urls.IMG_URL+hsOrderBean.getImgPath()).into(goods_head);
                tv_good_name.setText(hsOrderBean.getName());
                tv_price.setText("￥"+ hsOrderBean.getUnitPrice()+"/"+hsOrderBean.getUnit());
                break;
            case 2:
                hsOrderBean = (HsOrderBean) getIntent().getSerializableExtra("orderDetail");
                tv_orderid.setText(hsOrderBean.getOrderNo());
                tv_time.setText(hsOrderBean.getApointTime());
                tv_name.setText(hsOrderBean.getDeliveryName());
                tv_phone.setText(hsOrderBean.getDeliveryPhone());
                tv_address.setText(hsOrderBean.getReceivingAddress());
                Glide.with(this).load(Urls.IMG_URL+hsOrderBean.getImgPath()).into(goods_head);
                tv_good_name.setText(hsOrderBean.getName());
                tv_price.setText("￥"+ hsOrderBean.getUnitPrice()+"/"+hsOrderBean.getUnit());
                btn_confirm.setText("派工");
                break;
            case 3:
                workBean = (WorkBean) getIntent().getSerializableExtra("orderDetail");
                btn_confirm.setVisibility(View.GONE);
                tv_orderid.setText(workBean.getOrderNo());
                tv_time.setText(workBean.getApointTime());
                tv_name.setText(workBean.getDeliveryName());
                tv_phone.setText(workBean.getDeliveryPhone());
                tv_address.setText(workBean.getReceivingAddress());
                Glide.with(this).load(Urls.IMG_URL+workBean.getImgPath()).into(goods_head);
                tv_good_name.setText(workBean.getProductName());
                tv_price.setText("￥"+ workBean.getUnitPrice()+"/"+workBean.getUnit());
                btn_confirm.setText("派工");
                ll_title.setVisibility(View.VISIBLE);
                ll_servicer.setVisibility(View.VISIBLE);
                tv_servicer.setText(workBean.getServicerName());
                ll_sex.setVisibility(View.VISIBLE);
                if(workBean.getServicerSex()==0){
                    tv_sex.setText("男");
                } else {
                    tv_sex.setText("女");
                }
                ll_phone.setVisibility(View.VISIBLE);
                tv_servphone.setText(workBean.getServicerPhone());
                ll_number.setVisibility(View.VISIBLE);
                tv_service_number.setText(workBean.getServicerNo());
                break;
        }
    }
}
