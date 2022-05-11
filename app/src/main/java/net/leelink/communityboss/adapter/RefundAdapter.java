package net.leelink.communityboss.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.leelink.communityboss.R;
import net.leelink.communityboss.bean.OrderBean;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class RefundAdapter extends RecyclerView.Adapter<RefundAdapter.ViewHolder> {
    private Context context;
    private List<OrderBean> list;
    private OnCancelListener onCancelListener;
    public RefundAdapter(List<OrderBean> list, Context context, OnCancelListener onCancelListener) {
        this.context = context;
        this.list = list;
        this.onCancelListener = onCancelListener;
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

    @Override
    public void onBindViewHolder(RefundAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.tv_orderid.setText(list.get(position).getOrderId());
        holder.tv_time.setText("预约时间:"+list.get(position).getAppointTime());
        holder.tv_total_price.setText("总价:￥"+list.get(position).getActualPayPrice());
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
        if(list.get(position).getOrderState()==8) {
            holder.tv_state.setText("退款申请中");
            holder.btn_confirm.setVisibility(View.VISIBLE);
            holder.btn_cancel.setVisibility(View.VISIBLE);
        } else if(list.get(position).getOrderState()==9){
            holder.tv_state.setText("退款中");
            holder.btn_confirm.setVisibility(View.GONE);
            holder.btn_cancel.setVisibility(View.GONE);
        } else if(list.get(position).getOrderState()==10){
            holder.tv_state.setText("退款完成");
            holder.btn_confirm.setVisibility(View.GONE);
            holder.btn_cancel.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_orderid,tv_time,tv_total_price,tv_state;
        Button btn_confirm;
        Button btn_cancel;
        public ViewHolder(View itemView) {
            super(itemView);
            btn_cancel = itemView.findViewById(R.id.btn_refuse);
            tv_orderid = itemView.findViewById(R.id.tv_orderid);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_total_price = itemView.findViewById(R.id.tv_total_price);
            btn_confirm = itemView.findViewById(R.id.btn_confirm);
            tv_state  = itemView.findViewById(R.id.tv_state);
        }
    }
}
