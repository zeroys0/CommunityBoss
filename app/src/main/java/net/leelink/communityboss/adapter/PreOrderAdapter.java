package net.leelink.communityboss.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.leelink.communityboss.R;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.bean.GoodsBean;
import net.leelink.communityboss.bean.OrderDetail;
import net.leelink.communityboss.utils.Urls;

import java.math.BigDecimal;
import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class PreOrderAdapter extends RecyclerView.Adapter<PreOrderAdapter.ViewHolder> {
    private Context context;
    private List<GoodsBean> list;



    public PreOrderAdapter(Context context, List<GoodsBean> list) {
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
        holder.tv_name.setText(list.get(position).getName());
        Glide.with(context).load(Urls.getInstance().IMG_URL+list.get(position).getImageUrl()).into(holder.img_head);
        holder.tv_count.setText("x"+list.get(position).getNumber());
        holder.tv_price.setText("￥"+big2(list.get(position).getPrice()));
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

    private static String big2(double d) {
        BigDecimal d1 = new BigDecimal(Double.toString(d));
        BigDecimal d2 = new BigDecimal(Integer.toString(1));
        // 四舍五入,保留2位小数
        return d1.divide(d2,2,BigDecimal.ROUND_HALF_UP).toString();
    }
}
