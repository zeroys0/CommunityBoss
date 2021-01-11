package net.leelink.communityboss.adapter;

import android.content.Context;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.leelink.communityboss.R;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.bean.GoodListBean;
import net.leelink.communityboss.utils.Urls;

import java.util.List;

public class GoodListAdapter extends RecyclerView.Adapter<GoodListAdapter.ViewHolder> {
    private List<GoodListBean> list ;
    private Context context;
    private OnCollectListener onCollectListener;
    private int type = 0;

    public GoodListAdapter(List<GoodListBean> list,Context context,OnCollectListener onCollectListener,int type){
        this.list = list;
        this.context = context;
        this.onCollectListener = onCollectListener;
        this.type = type;
    }
    @Override
    public GoodListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.goods_item, parent, false); // 实例化viewholder
        GoodListAdapter.ViewHolder viewHolder = new GoodListAdapter.ViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCollectListener.onItemClick(v);
            }
        });
        return viewHolder;
    }

    public void setType(int type){
        this.type = type;
    }

    @Override
    public void onBindViewHolder(final GoodListAdapter.ViewHolder holder, final int position) {
        if(type == 1) {
            holder.rl_check.setVisibility(View.VISIBLE);
        } else {
            holder.rl_check.setVisibility(View.GONE);
        }
        holder.tv_name.setText(list.get(position).getName());
        holder.tv_price.setText("￥"+list.get(position).getUnitPrice());
        holder.tv_detail.setText(list.get(position).getRemark());
        Glide.with(context).load(Urls.getInstance().IMG_URL+list.get(position).getProductImgPath()).into(holder.img_head);
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.checkBox.isChecked()) {
                    onCollectListener.onCancelChecked(v, position,true);
                }else {
                    onCollectListener.onCancelChecked(v, position,false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        RelativeLayout rl_check;
        CheckBox checkBox;
        ImageView img_head;
        TextView tv_name,tv_price,tv_detail;
        public ViewHolder(View itemView) {
            super(itemView);
            rl_check = itemView.findViewById(R.id.rl_check);
            img_head = itemView.findViewById(R.id.img_head);
            tv_name = itemView.findViewById(R.id.tv_name);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_detail = itemView.findViewById(R.id.tv_detail);
            checkBox = itemView.findViewById(R.id.checkbox);

        }
    }
}
