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
import net.leelink.communityboss.bean.CommentListBean;
import net.leelink.communityboss.utils.Urls;

import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {
    private Context context;
    private List<CommentListBean.UserAppraiseListBean> list;
    private OnItemClickListener onItemClickListener;

    public CommentListAdapter (Context context,List<CommentListBean.UserAppraiseListBean> list,OnItemClickListener onItemClickListener) {
        this.context = context;
        this.list = list;
        this.onItemClickListener = onItemClickListener;
    }
    @Override
    public CommentListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item , parent, false); // 实例化viewholder
        CommentListAdapter.ViewHolder viewHolder = new CommentListAdapter.ViewHolder(v);
        
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CommentListAdapter.ViewHolder holder, int position) {
        Glide.with(context).load(Urls.IMAGEHEAD+list.get(position).getUserHeadImage()).into(holder.img_head);
        holder.tv_orderId.setText("订单编号:"+list.get(position).getOrderId());
        holder.tv_phone.setText(list.get(position).getUsername());
        holder.tv_detail.setText(list.get(position).getUserMessage());
        if(list.get(position).getUserScore()>0.5) {
            holder.img_star0.setVisibility(View.VISIBLE);
        }
        if(list.get(position).getUserScore()>1.5) {
            holder.img_star1.setVisibility(View.VISIBLE);
        }
        if(list.get(position).getUserScore()>2.5) {
            holder.img_star2.setVisibility(View.VISIBLE);
        }
        if(list.get(position).getUserScore()>3.5) {
            holder.img_star3.setVisibility(View.VISIBLE);
        }
        if(list.get(position).getUserScore()>4.5) {
            holder.img_star4.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_head,img_star0,img_star1,img_star2,img_star3,img_star4;
        TextView tv_orderId,tv_phone,tv_detail;
        public ViewHolder(View itemView) {
            super(itemView);
            img_head = itemView.findViewById(R.id.img_head);
            tv_orderId = itemView.findViewById(R.id.tv_orderId);
            tv_phone = itemView.findViewById(R.id.tv_phone);
            tv_detail = itemView.findViewById(R.id.tv_detail);
            img_star0 = itemView.findViewById(R.id.img_star0);
            img_star1 = itemView.findViewById(R.id.img_star1);
            img_star2 = itemView.findViewById(R.id.img_star2);
            img_star3 = itemView.findViewById(R.id.img_star3);
            img_star4 = itemView.findViewById(R.id.img_star4);
        }
    }
}
