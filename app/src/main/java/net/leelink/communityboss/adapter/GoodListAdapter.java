package net.leelink.communityboss.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import net.leelink.communityboss.R;

import java.util.List;

public class GoodListAdapter extends RecyclerView.Adapter<GoodListAdapter.ViewHolder> {
    private List<String> list ;
    private Context context;
    private OnCollectListener onCollectListener;
    private int type = 0;

    public GoodListAdapter(List<String> list,Context context,OnCollectListener onCollectListener,int type){
        this.list = list;
        this.context = context;
        this.onCollectListener = onCollectListener;
        this.type = type;
    }
    @Override
    public GoodListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.goods_item, parent, false); // 实例化viewholder
        GoodListAdapter.ViewHolder viewHolder = new GoodListAdapter.ViewHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(GoodListAdapter.ViewHolder holder, int position) {
        if(type == 1) {
            holder.rl_check.setVisibility(View.VISIBLE);
        } else {
            holder.rl_check.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rl_check;
        public ViewHolder(View itemView) {
            super(itemView);
            rl_check = itemView.findViewById(R.id.rl_check);
        }
    }
}
