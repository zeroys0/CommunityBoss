package net.leelink.communityboss.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.leelink.communityboss.R;

import java.util.List;

public class PreOrderAdapter extends RecyclerView.Adapter<PreOrderAdapter.ViewHolder> {
    private Context context;
    private List<String> list;



    public PreOrderAdapter(Context context, List<String> list) {
        this.context =context;
        this.list = list;
    }
    @Override
    public PreOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.pre_order_item, parent, false); // 实例化viewholder
        PreOrderAdapter.ViewHolder viewHolder = new PreOrderAdapter.ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(PreOrderAdapter.ViewHolder holder, int position) {
        holder.tv_name.setText(list.get(position));
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
        }
    }
}
