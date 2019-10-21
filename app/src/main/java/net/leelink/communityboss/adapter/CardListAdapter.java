package net.leelink.communityboss.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import net.leelink.communityboss.R;

import java.util.List;

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.ViewHolder> {

    private Context context;
    private List<String> list;
    private OnItemClickListener onItemClickListener;

    public CardListAdapter (Context context,List<String> list,OnItemClickListener onItemClickListener) {
        this.context = context;
        this.list = list;
        this.onItemClickListener = onItemClickListener;
    }
    @Override
    public CardListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardlist_item , parent, false); // 实例化viewholder
        CardListAdapter.ViewHolder viewHolder = new CardListAdapter.ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CardListAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View itemView) {
            super(itemView);
        }
    }
}
