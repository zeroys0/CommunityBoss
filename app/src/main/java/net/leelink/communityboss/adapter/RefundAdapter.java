package net.leelink.communityboss.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.leelink.communityboss.R;
import net.leelink.communityboss.bean.OrderBean;

import java.util.List;

public class RefundAdapter extends OrderListAdapter {
    private Context context;
    private List<OrderBean> list;
    private OnCancelListener onCancelListener;
    public RefundAdapter(List<OrderBean> list, Context context, OnCancelListener onCancelListener) {
        super(list, context, onCancelListener);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.refund_item, parent, false); // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelListener.onItemClick(v);
            }
        });
        return viewHolder;
    }


    public void onBindViewHolder(RefundAdapter.ViewHolder holder, final int position) {
        holder.tv_orderid.setText(list.get(position).getOrderId());
        holder.tv_time.setText("预约时间:"+list.get(position).getDeliveryTime());
        holder.tv_total_price.setText("总价:￥"+list.get(position).getTotalPrice());
        holder.btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelListener.onButtonClick(v,position);
            }
        });
        holder.btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancelListener.onCancel(v,position);
            }
        });
    }

    public class ViewHolder extends OrderListAdapter.ViewHolder {

        Button btn_cancel;
        public ViewHolder(View itemView) {
            super(itemView);
            btn_cancel = itemView.findViewById(R.id.btn_refuse);
        }
    }
}
