package net.leelink.communityboss.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import net.leelink.communityboss.R;
import net.leelink.communityboss.activity.CommentListActivity;
import net.leelink.communityboss.activity.IncomeActivity;
import net.leelink.communityboss.activity.InformationActivity;
import net.leelink.communityboss.activity.ManageListActivity;
import net.leelink.communityboss.activity.MyServiceActivity;
import net.leelink.communityboss.activity.RefundListActivity;

public class MineFragment extends BaseFragment implements View.OnClickListener {
    private RelativeLayout rl_comment,rl_info,rl_goods,rl_income,rl_refund,rl_service;
    private ImageView img_head;
    @Override
    public void handleCallBack(Message msg) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mine,container,false);
        init(view);
        return view;
    }

    public void init(View view){
        rl_comment = view.findViewById(R.id.rl_comment);
        rl_comment.setOnClickListener(this);
        img_head = view.findViewById(R.id.img_head);
        img_head.setOnClickListener(this);
        rl_info = view.findViewById(R.id.rl_info);
        rl_info.setOnClickListener(this);
        rl_goods = view.findViewById(R.id.rl_goods);
        rl_goods.setOnClickListener(this);
        rl_income = view.findViewById(R.id.rl_income);
        rl_income.setOnClickListener(this);
        rl_refund = view.findViewById(R.id.rl_refund);
        rl_refund.setOnClickListener(this);
        rl_service = view.findViewById(R.id.rl_service);
        rl_service.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_comment:   //用户反馈
                Intent intent = new Intent(getContext(),CommentListActivity.class);
                startActivity(intent);
                break;
            case R.id.rl_info:  //商家信息
                Intent intent1 = new Intent(getContext(), InformationActivity.class);
                startActivity(intent1);
                break;
            case R.id.rl_goods: //商家物品
                Intent intent2 = new Intent(getContext(), ManageListActivity.class);
                startActivity(intent2);
                break;
            case R.id.rl_income:    //收入统计
                Intent intent3 = new Intent(getContext(), IncomeActivity.class);
                startActivity(intent3);
                break;
            case R.id.rl_refund:    //退款订单
                Intent intent4 = new Intent(getContext(), RefundListActivity.class);
                startActivity(intent4);
                break;
            case R.id.rl_service:   //我的客服
                Intent intent5 = new Intent(getContext(), MyServiceActivity.class);
                startActivity(intent5);
                break;
                default:
                    break;
        }
    }
}
