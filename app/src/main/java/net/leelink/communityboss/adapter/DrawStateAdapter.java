package net.leelink.communityboss.adapter;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.leelink.communityboss.R;

import java.util.List;

public class DrawStateAdapter extends RecyclerView.Adapter<DrawStateAdapter.ViewHolder> {

    Context context;
    List<String> list;
    String startTime;

    public DrawStateAdapter(Context context, List<String> list,String startTime) {
        this.context = context;
        this.list = list;
        this.startTime = startTime;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_state,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if(position == 0){
            holder.up.setVisibility(View.INVISIBLE);
            holder.tv_time.setVisibility(View.VISIBLE);
            holder.tv_time.setText(startTime);
        }
        if(position == list.size()-1) {
            holder.down.setVisibility(View.INVISIBLE);
        }

        holder.tv_state.setText(list.get(position));

    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size() ;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        View up,down;
        TextView tv_state,tv_time;
        public ViewHolder(View itemView) {
            super(itemView);
            up = itemView.findViewById(R.id.up);
            down = itemView.findViewById(R.id.down);
            tv_state = itemView.findViewById(R.id.tv_state);
            tv_time = itemView.findViewById(R.id.tv_time);
        }
    }
}
