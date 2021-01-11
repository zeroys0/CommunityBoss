package net.leelink.communityboss.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.leelink.communityboss.R;
import net.leelink.communityboss.bean.BalanceBean;

import java.util.List;

public class BalanceAdapter extends RecyclerView.Adapter<BalanceAdapter.ViewHolder> {
    List<BalanceBean.AccountListBean> list;
    Context context;

    public BalanceAdapter(List<BalanceBean.AccountListBean> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_balance,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv_type.setText(list.get(position).getTitle());
        holder.tv_time.setText(list.get(position).getCreateTime());
        if(list.get(position).getAccountState().equals("1")) {
            holder.tv_amount.setText("+"+list.get(position).getAmount());
            holder.tv_amount.setTextColor(context.getResources().getColor(R.color.text_green));
        } else {
            holder.tv_amount.setText("-"+list.get(position).getAmount());
            holder.tv_amount.setTextColor(context.getResources().getColor(R.color.text_red));
        }
        holder.tv_balance.setText("余额"+list.get(position).getHistoryAmount());

    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_type,tv_time,tv_amount,tv_balance;
        public ViewHolder(View itemView) {

            super(itemView);
            tv_type = itemView.findViewById(R.id.tv_type);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_balance = itemView.findViewById(R.id.tv_balance);
        }
    }
}
