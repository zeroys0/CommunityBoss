package net.leelink.communityboss.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;


import net.leelink.communityboss.R;

import java.util.List;

public class CategoryAdapter extends  RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    List<String> list;
    Context context;
    OnCategoryClickListener onCategoryClickListener;

    public void update(List<String> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public CategoryAdapter(List<String> list, Context context, OnCategoryClickListener onCategoryClickListener){
        this.list = list;
        this.context = context;
        this.onCategoryClickListener = onCategoryClickListener;
    }
    @Override
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_item, parent, false); // 实例化viewholder
        CategoryAdapter.ViewHolder viewHolder = new CategoryAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.ViewHolder holder, final int position) {
        holder.tv_question.setText(list.get(position));
        holder.rl_content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCategoryClickListener.onclick(v,position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_question;
        RelativeLayout rl_content;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_question = itemView.findViewById(R.id.tv_question);
            rl_content = itemView.findViewById(R.id.rl_content);
        }
    }
}
