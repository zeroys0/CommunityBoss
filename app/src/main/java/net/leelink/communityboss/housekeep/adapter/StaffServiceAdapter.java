package net.leelink.communityboss.housekeep.adapter;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.leelink.communityboss.R;
import net.leelink.communityboss.adapter.OnOrderListener;
import net.leelink.communityboss.bean.ServiceBean;
import net.leelink.communityboss.bean.StaffBean;
import net.leelink.communityboss.utils.Urls;

import java.util.List;

public class StaffServiceAdapter extends RecyclerView.Adapter<StaffServiceAdapter.ViewHolder> {
    private Context context;
    private List<ServiceBean> list;
    private OnOrderListener onOrderListener;
    public StaffServiceAdapter(List<ServiceBean> list, Context context, OnOrderListener onOrderListener) {
        this.list = list;
        this.context = context;
        this.onOrderListener = onOrderListener;

    }

    public void update(List<ServiceBean> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @NonNull
    @Override
    public StaffServiceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.staff_service_item, parent, false); // 实例化viewholder
        StaffServiceAdapter.ViewHolder viewHolder = new StaffServiceAdapter.ViewHolder(v);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOrderListener.onItemClick(v);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull StaffServiceAdapter.ViewHolder holder, int position) {
        holder.tv_name.setText(list.get(position).getName());
        holder.tv_around.setText(list.get(position).getAround());
        Glide.with(context).load(Urls.IMG_URL+list.get(position).getImgPath()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name,tv_around;
        ImageView img;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_around = itemView.findViewById(R.id.tv_around);
            img = itemView.findViewById(R.id.img);
        }
    }
}
