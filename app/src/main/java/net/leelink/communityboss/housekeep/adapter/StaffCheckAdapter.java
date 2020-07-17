package net.leelink.communityboss.housekeep.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.leelink.communityboss.R;
import net.leelink.communityboss.adapter.OnOrderListener;
import net.leelink.communityboss.bean.StaffBean;

import org.w3c.dom.Text;

import java.util.List;

public class StaffCheckAdapter extends RecyclerView.Adapter<StaffCheckAdapter.ViewHolder> {

    private Context context;
    private List<StaffBean> list;
    private OnOrderListener onOrderListener;
    private int type;
    public StaffCheckAdapter(List<StaffBean> list, Context context, OnOrderListener onOrderListener,int type) {
        this.list = list;
        this.context = context;
        this.onOrderListener = onOrderListener;
        this.type = type;
    }

    public void update(List<StaffBean> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @Override
    public StaffCheckAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.staff_check_item, parent, false); // 实例化viewholder
        StaffCheckAdapter.ViewHolder viewHolder = new StaffCheckAdapter.ViewHolder(v);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOrderListener.onItemClick(v);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StaffCheckAdapter.ViewHolder holder, final int position) {
        holder.tv_staff_name.setText(list.get(position).getName());
        switch (list.get(position).getSex()){
            case 0:
                holder.tv_sex.setText("男");
                break;
            case 1:
                holder.tv_sex.setText("女");
                break;
        }
        holder.tv_phone.setText(list.get(position).getTelephone());
        holder.tv_remark.setText(list.get(position).getRemark());
        if(type ==1){
            holder.btn_confirm.setText("员工管理");
        }
        if(type ==2){
            holder.btn_confirm.setText("添加服务");
            holder.btn_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOrderListener.onButtonClick(v,position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_staff_name,tv_sex,tv_phone,tv_remark;
        Button btn_confirm;
        public ViewHolder(View itemView) {
            super(itemView);
            tv_staff_name = itemView.findViewById(R.id.tv_staff_name);
            tv_sex = itemView.findViewById(R.id.tv_sex);
            tv_phone = itemView.findViewById(R.id.tv_phone);
            tv_remark = itemView.findViewById(R.id.tv_remark);
            btn_confirm = itemView.findViewById(R.id.btn_confirm);
        }
    }
}
