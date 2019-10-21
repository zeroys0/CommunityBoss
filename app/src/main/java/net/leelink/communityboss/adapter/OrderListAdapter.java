package net.leelink.communityboss.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.leelink.communityboss.R;

import java.util.List;

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.ViewHolder> {

    private Context context;
    private List<String> list;
    private OnItemClickListener onItemClickListener;
    public OrderListAdapter(List<String> list,Context context,OnItemClickListener onItemClickListener) {
        this.list = list;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }
    @Override
    public OrderListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.orderlist_item, parent, false); // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(OrderListAdapter.ViewHolder holder, int position) {
        holder.tv_orderid.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list ==null?0:list.size();
    }

    public class
    ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_orderid;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_orderid = itemView.findViewById(R.id.tv_orderid);
        }
    }
}
