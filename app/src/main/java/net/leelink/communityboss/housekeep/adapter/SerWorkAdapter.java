package net.leelink.communityboss.housekeep.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.leelink.communityboss.R;
import net.leelink.communityboss.adapter.OnOrderListener;
import net.leelink.communityboss.bean.HsOrderBean;
import net.leelink.communityboss.bean.SerWorkBean;

import org.w3c.dom.Text;

import java.util.List;

public class SerWorkAdapter extends RecyclerView.Adapter<SerWorkAdapter.ViewHolder>{
    private Context context;
    private List<SerWorkBean> list;
    private OnOrderListener onOrderListener;
    public SerWorkAdapter(List<SerWorkBean> list, Context context, OnOrderListener onOrderListener) {
        this.list = list;
        this.context = context;
        this.onOrderListener = onOrderListener;
    }
    @NonNull
    @Override
    public SerWorkAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ser_work_item, parent, false); // 实例化viewholder
        SerWorkAdapter.ViewHolder viewHolder = new SerWorkAdapter.ViewHolder(v);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOrderListener.onItemClick(v);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SerWorkAdapter.ViewHolder holder, int position) {
        holder.order_no.setText(list.get(position).getWorkOrderNo());
        holder.tv_apoint_time.setText(list.get(position).getAppointTime());
        holder.tv_address.setText(list.get(position).getReceivingAddress());
        holder.tv_order_id.setText(list.get(position).getWorkId());

    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView order_no,tv_apoint_time,tv_address,tv_order_id;
        public ViewHolder(View itemView) {
            super(itemView);
            order_no = itemView.findViewById(R.id.order_no);
            tv_apoint_time = itemView.findViewById(R.id.tv_apoint_time);
            tv_address = itemView.findViewById(R.id.tv_address);
            tv_order_id = itemView.findViewById(R.id.tv_order_id);
        }
    }
}
