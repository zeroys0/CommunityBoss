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

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    private Context context;
    private List<OrderBean> list;
    private OnOrderListener onOrderListener;
    public OrderListAdapter(List<OrderBean> list, Context context, OnOrderListener onOrderListener) {
        this.list = list;
        this.context = context;
        this.onOrderListener = onOrderListener;
    }

    public void update(List<OrderBean> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @Override
    public OrderListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderlist_item, parent, false); // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOrderListener.onItemClick(v);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OrderListAdapter.ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.tv_orderid.setText(list.get(position).getOrderId());
        switch (list.get(position).getOrderState()){
            case 2:
                holder.tv_state.setText("未接订单");
                break;
            case 3:
                holder.tv_state.setText("待骑手抢单");
                holder.btn_confirm.setVisibility(View.INVISIBLE);
                break;
            case 4:
                holder.tv_state.setText("待取货");
         //       holder.btn_confirm.setText("订单送出");
                holder.btn_confirm.setVisibility(View.INVISIBLE);
                break;
            case 5:
                holder.tv_state.setText("已送出");
                holder.btn_confirm.setVisibility(View.INVISIBLE);
                break;
            case 6:
                holder.tv_state.setText("待评价");
            case 7:
                holder.tv_state.setText("已完成");
                holder.btn_confirm.setVisibility(View.INVISIBLE);
                break;
            case 11:
                holder.tv_state.setText("用户自提");
                holder.btn_confirm.setVisibility(View.VISIBLE);
                holder.btn_confirm.setText("确认自提");
                break;
            default:
                    break;
        }
        holder.tv_time.setText("预约时间:"+list.get(position).getAppointTime());
        holder.tv_total_price.setText("总价:￥"+list.get(position).getActualPayPrice());
        holder.btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOrderListener.onButtonClick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list ==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_orderid,tv_state,tv_time,tv_total_price;
        Button btn_confirm;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_orderid = itemView.findViewById(R.id.tv_orderid);
            tv_state = itemView.findViewById(R.id.tv_state);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_total_price = itemView.findViewById(R.id.tv_total_price);
            btn_confirm = itemView.findViewById(R.id.btn_confirm);
        }
    }
}
