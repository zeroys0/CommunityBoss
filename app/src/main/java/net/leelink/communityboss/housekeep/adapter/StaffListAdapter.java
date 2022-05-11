package net.leelink.communityboss.housekeep.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import net.leelink.communityboss.R;
import net.leelink.communityboss.adapter.OnOrderListener;
import net.leelink.communityboss.adapter.OrderListAdapter;
import net.leelink.communityboss.bean.DelegateBean;
import net.leelink.communityboss.bean.OrderBean;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public class StaffListAdapter extends RecyclerView.Adapter<StaffListAdapter.ViewHolder> {
    private Context context;
    private List<DelegateBean> list;
    private OnOrderListener onOrderListener;
    public StaffListAdapter(List<DelegateBean> list, Context context, OnOrderListener onOrderListener) {
        this.list = list;
        this.context = context;
        this.onOrderListener = onOrderListener;
    }

    public void update(List<DelegateBean> list){
        this.list = list;
        notifyDataSetChanged();
    }
    @Override
    public StaffListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.staff_list_item, parent, false); // 实例化viewholder
        StaffListAdapter.ViewHolder viewHolder = new StaffListAdapter.ViewHolder(v);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOrderListener.onItemClick(v);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(StaffListAdapter.ViewHolder holder, final int position) {
        holder.btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOrderListener.onButtonClick(v,position);
            }
        });
        holder.tv_staff_name.setText(list.get(position).getName());
        if(list.get(position).getSex()==0){
            holder.tv_sex.setText("男");
        } else {
            holder.tv_sex.setText("女");
        }
        holder.tv_phone.setText(list.get(position).getTelephone());
        holder.tv_work_no.setText(list.get(position).getUserNo());
    }

    @Override
    public int getItemCount() {
        return list==null?0:list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        Button btn_confirm;
        TextView tv_staff_name,tv_sex,tv_phone,tv_work_no;
        public ViewHolder(View itemView) {
            super(itemView);
            btn_confirm = itemView.findViewById(R.id.btn_confirm);
            tv_staff_name = itemView.findViewById(R.id.tv_staff_name);
            tv_sex = itemView.findViewById(R.id.tv_sex);
            tv_phone = itemView.findViewById(R.id.tv_phone);
            tv_work_no = itemView.findViewById(R.id.tv_work_no);
        }
    }
}
