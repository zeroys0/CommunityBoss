package net.leelink.communityboss.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.leelink.communityboss.R;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.bean.OrderDetail;
import net.leelink.communityboss.utils.Urls;

import java.util.List;

public class PreOrderAdapter extends RecyclerView.Adapter<PreOrderAdapter.ViewHolder> {
    private Context context;
    private List<OrderDetail.DetailsBean> list;



    public PreOrderAdapter(Context context, List<OrderDetail.DetailsBean> list) {
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
        holder.tv_name.setText(list.get(position).getCommodityName());
        Glide.with(context).load(Urls.IMAGEURL+"/Store/"+ CommunityBossApplication.storeInfo.getStoreId()+"/CommodityImage/"+list.get(position).getCommodityImage()).into(holder.img_head);
        holder.tv_count.setText("x"+list.get(position).getSales());
        holder.tv_price.setText("￥"+list.get(position).getPrice());
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name,tv_count,tv_price;
        ImageView img_head;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.tv_name);
            img_head = itemView.findViewById(R.id.img_head);
            tv_count = itemView.findViewById(R.id.tv_count);
            tv_price = itemView.findViewById(R.id.tv_price);
        }
    }
}
