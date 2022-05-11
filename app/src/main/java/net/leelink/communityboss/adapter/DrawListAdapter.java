package net.leelink.communityboss.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.leelink.communityboss.R;
import net.leelink.communityboss.bean.DrawBean;

import java.util.List;

public class DrawListAdapter extends RecyclerView.Adapter<DrawListAdapter.ViewHolder> {
    List<DrawBean> list;
    Context context;
    OnOrderListener onOrderListener;

    public DrawListAdapter(List<DrawBean> list, Context context, OnOrderListener onOrderListener) {
        this.list = list;
        this.context = context;
        this.onOrderListener = onOrderListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_draw,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOrderListener.onItemClick(v);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String cardNumber = list.get(position).getCard();
        if(cardNumber.length()>4) {
            cardNumber = cardNumber.substring(cardNumber.length() - 4);
        }
        holder.tv_bank.setText("提现到尾号"+cardNumber);
        holder.tv_time.setText(list.get(position).getStartTime());
        holder.tv_amount.setText("-"+list.get(position).getTxPrice());
        switch (list.get(position).getState()){
            case 1:
                holder.tv_state.setText("待平台审核");
                break;
            case 2:
                holder.tv_state.setText("财务处理中");
                break;
            case 3:
                holder.tv_state.setText("待确认到账");
                break;
            case 4:
                holder.tv_state.setText("已到账");
                break;
            case 5:
                holder.tv_state.setText("未通过");
                break;
        }
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_bank,tv_time,tv_amount,tv_state;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_bank = itemView.findViewById(R.id.tv_bank);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_amount = itemView.findViewById(R.id.tv_amount);
            tv_state = itemView.findViewById(R.id.tv_state);
        }
    }
}
