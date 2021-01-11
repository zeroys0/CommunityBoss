package net.leelink.communityboss.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.leelink.communityboss.R;
import net.leelink.communityboss.bean.IncomeBean;

import java.util.List;

public class IncomeAdapter extends RecyclerView.Adapter<IncomeAdapter.ViewHolder> {
    List<IncomeBean.StoreInComeVoBean> list;
    Context context;

    public IncomeAdapter(List<IncomeBean.StoreInComeVoBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_income,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_order_no.setText(list.get(position).getOrderNo());
        holder.tv_time.setText(list.get(position).getCreateTime());
        holder.tv_amount.setText("+"+list.get(position).getAmount());
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_order_no,tv_time,tv_amount;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_order_no = itemView.findViewById(R.id.tv_order_no);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_amount = itemView.findViewById(R.id.tv_amount);
        }
    }
}
