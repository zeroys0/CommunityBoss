package net.leelink.communityboss.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import net.leelink.communityboss.R;
import net.leelink.communityboss.bean.CommentListBean;
import net.leelink.communityboss.utils.Urls;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class CommentListAdapter extends RecyclerView.Adapter<CommentListAdapter.ViewHolder> {
    private Context context;
    private JSONArray jsonArray;
    private OnItemClickListener onItemClickListener;

    public CommentListAdapter (Context context, JSONArray jsonArray, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.jsonArray = jsonArray;

        this.onItemClickListener = onItemClickListener;
    }

    public void update(JSONArray jsonArray){
        this.jsonArray = jsonArray;
        notifyDataSetChanged();
    }
    @Override
    public CommentListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.comment_item , parent, false); // 实例化viewholder
        CommentListAdapter.ViewHolder viewHolder = new CommentListAdapter.ViewHolder(v);
        
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CommentListAdapter.ViewHolder holder, int position) {
        try {
            JSONObject json = jsonArray.getJSONObject(position);
            Glide.with(context).load(Urls.IMG_URL+json.getString("elderlyImgPath")).into(holder.img_head);
            holder.tv_orderId.setText("订单编号:"+json.getString("id"));
            holder.tv_phone.setText(json.getString("telephone"));
            holder.tv_detail.setText(json.getString("content"));
            if(json.getDouble("customTotalStar")>0.5) {
                holder.img_star0.setVisibility(View.VISIBLE);
            }
            if(json.getDouble("customTotalStar")>1.5) {
                holder.img_star1.setVisibility(View.VISIBLE);
            }
            if(json.getDouble("customTotalStar")>2.5) {
                holder.img_star2.setVisibility(View.VISIBLE);
            }
            if(json.getDouble("customTotalStar")>3.5) {
                holder.img_star3.setVisibility(View.VISIBLE);
            }
            if(json.getDouble("customTotalStar")>4.5) {
                holder.img_star4.setVisibility(View.VISIBLE);
            }
            if(json.has("image1_path")){
                holder.rl_img.setVisibility(View.VISIBLE);
                if(json.has("image2_path")){
                    holder.ll_images.setVisibility(View.VISIBLE);
                    Glide.with(context).load(Urls.IMG_URL+json.getString("image1_path")).into(holder.img0);
                    Glide.with(context).load(Urls.IMG_URL+json.getString("image2_path")).into(holder.img1);
                    if(json.has("image3_path")){
                        Glide.with(context).load(Urls.IMG_URL+json.getString("image3_path")).into(holder.img2);
                    }
                } else {
                    holder.img_main.setVisibility(View.VISIBLE);
                    Glide.with(context).load(Urls.IMG_URL+json.getString("image1_path")).into(holder.img_main);
                }
            }
            if(json.has("reply")){
                holder.tv_reply.setVisibility(View.VISIBLE);
                holder.tv_reply.setText(json.getString("reply"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return jsonArray==null?0:jsonArray.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img_head,img_star0,img_star1,img_star2,img_star3,img_star4,img_main,img0,img1,img2;
        TextView tv_orderId,tv_phone,tv_detail,tv_reply;
        LinearLayout ll_images;
        RelativeLayout rl_img;
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
            img_main = itemView.findViewById(R.id.img_main);
            ll_images = itemView.findViewById(R.id.ll_images);
            img0 = itemView.findViewById(R.id.img0);
            img1 = itemView.findViewById(R.id.img1);
            img2 = itemView.findViewById(R.id.img2);
            rl_img = itemView.findViewById(R.id.rl_img);
            tv_reply = itemView.findViewById(R.id.tv_reply);
        }
    }
}
