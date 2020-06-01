package net.leelink.communityboss.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import net.leelink.communityboss.R;
import net.leelink.communityboss.bean.QuestionBean;

import java.util.List;

public class QuestionAdapter extends   RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    private List<QuestionBean> list;
    private Context context;
    private OnItemClickListener onItemClickListener;

    public QuestionAdapter(List<QuestionBean> list, Context context, OnItemClickListener onItemClickListener){
        this.list = list;
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }
    @Override
    public QuestionAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.question_list_item, parent, false); // 实例化viewholder
        QuestionAdapter.ViewHolder viewHolder = new QuestionAdapter.ViewHolder(v);
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickListener.onItemClick(v);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(QuestionAdapter.ViewHolder holder, int position) {
        holder.tv_question.setText(list.get(position).getQuestion());
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_question;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_question = itemView.findViewById(R.id.tv_question);
        }
    }
}
