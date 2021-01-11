package net.leelink.communityboss.housekeep.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.leelink.communityboss.R;
import net.leelink.communityboss.adapter.OnOrderListener;
import net.leelink.communityboss.bean.ServiceBean;
import net.leelink.communityboss.utils.Urls;

import java.util.List;

public class ServiceItemAdapter extends RecyclerView.Adapter<ServiceItemAdapter.ViewHolder> {
    private Context context;
    private List<ServiceBean> list;
    private OnOrderListener onOrderListener;
    public ServiceItemAdapter(List<ServiceBean> list, Context context, OnOrderListener onOrderListener) {
        this.list = list;
        this.context = context;
        this.onOrderListener = onOrderListener;
    }

    public void update(List<ServiceBean> list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public ServiceItemAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.service_item, parent, false); // 实例化viewholder
        ServiceItemAdapter.ViewHolder viewHolder = new ServiceItemAdapter.ViewHolder(v);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOrderListener.onItemClick(v);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ServiceItemAdapter.ViewHolder holder, final int position) {
        holder.rl_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOrderListener.onButtonClick(v,position);
            }
        });
        holder.tv_service_name.setText(list.get(position).getName());
        holder.tv_service_price.setText(list.get(position).getUnitPrice()+"/"+list.get(position).getUnit());
        holder.tv_service.setText(list.get(position).getAround());
        holder.tv_explain.setText(list.get(position).getRemark());
        holder.tv_against_price.setText(list.get(position).getDamagePrice()+"");
        if(list.get(position).getState()==0) {
            holder.tv_status.setText("下架");
        }else {
            holder.tv_status.setText("上架");
        }

        Glide.with(context).load(Urls.getInstance().IMG_URL+list.get(position).getImgPath()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rl_edit;
        TextView tv_service_name,tv_service_price,tv_service,tv_explain,tv_against_price,tv_status;
        ImageView img;
        public ViewHolder(View itemView) {
            super(itemView);
            rl_edit = itemView.findViewById(R.id.rl_edit);
            tv_service_name = itemView.findViewById(R.id.tv_service_name);
            tv_service_price = itemView.findViewById(R.id.tv_service_price);
            tv_service = itemView.findViewById(R.id.tv_service);
            tv_explain = itemView.findViewById(R.id.tv_explain);
            tv_against_price = itemView.findViewById(R.id.tv_against_price);
            tv_status = itemView.findViewById(R.id.tv_status);
            img = itemView.findViewById(R.id.img);
        }
    }
}
