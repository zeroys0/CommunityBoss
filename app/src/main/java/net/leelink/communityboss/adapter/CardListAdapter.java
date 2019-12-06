package net.leelink.communityboss.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.leelink.communityboss.R;
import net.leelink.communityboss.bean.CardBean;

import java.util.List;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {

    private Context context;
    private List<CardBean> list;
    private OnItemClickListener onItemClickListener;

    public CardListAdapter (Context context,List<CardBean> list,OnItemClickListener onItemClickListener) {
        this.context = context;
        this.list = list;
        this.onItemClickListener = onItemClickListener;
    }
    @Override
    public CardListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardlist_item , parent, false); // 实例化viewholder
        CardListAdapter.ViewHolder viewHolder = new CardListAdapter.ViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CardListAdapter.ViewHolder holder, int position) {
        holder.tv_bank.setText(list.get(position).getBank());
        String number = list.get(position).getCardNumber();
        String head = number.substring(0,4);
        String end = number.substring(number.length()-4);
        int count =  number.length() - 8;
        String card_no =head;
        for(int i =0;i<count;i++){
            card_no += "*";
        }
        card_no += end;
        holder.tv_card_number.setText(card_no);
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_bank,tv_card_number;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_bank = itemView.findViewById(R.id.tv_bank);
            tv_card_number = itemView.findViewById(R.id.tv_card_number);
        }
    }
}
