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
import net.leelink.communityboss.adapter.OrderListAdapter;
import net.leelink.communityboss.bean.HsOrderBean;
import net.leelink.communityboss.bean.OrderBean;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class HsOrderAdapter extends RecyclerView.Adapter<HsOrderAdapter.ViewHolder> {

    private Context context;
//    private List<HsOrderBean> list;
    private JSONArray jsonArray;
    private OnOrderListener onOrderListener;

    public HsOrderAdapter(JSONArray jsonArray, Context context, OnOrderListener onOrderListener) {
        this.jsonArray = jsonArray;
        this.context = context;
        this.onOrderListener = onOrderListener;
    }

    public void update(JSONArray jsonArray){
        this.jsonArray = jsonArray;
        notifyDataSetChanged();
    }
    @Override
    public HsOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.hs_orderlist_item, parent, false); // 实例化viewholder
        HsOrderAdapter.ViewHolder viewHolder = new HsOrderAdapter.ViewHolder(v);
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onOrderListener.onItemClick(v);
            }
        });
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(HsOrderAdapter.ViewHolder holder, final int position) {
        try {
            JSONObject jsonObject = jsonArray.getJSONObject(position);
            holder.order_no.setText(jsonObject.getString("orderNo"));
            holder.tv_apoint_time.setText(jsonObject.getString("apointTime"));
            holder.tv_service.setText(jsonObject.getString("name"));
            holder.tv_price.setText(jsonObject.getString("unitPrice")+"元/"+jsonObject.getString("unit"));
            switch (jsonObject.getInt("state")){
                case 1:
                    holder.tv_state.setText("待确认");
                    holder.btn_confirm.setText("确认订单");
                    break;
                case 2:
                    holder.tv_state.setText("待派工");
                    holder.btn_confirm.setText("派工");
                    break;
                case 3:
                    holder.tv_state.setText("已派工");
                    holder.btn_confirm.setVisibility(View.GONE);
                    break;
                case 4:
                    holder.tv_state.setText("待确认价格");
                    holder.btn_confirm.setVisibility(View.GONE);
                    break;
                case 5:
                    holder.tv_state.setText("服务中");
                    holder.btn_confirm.setVisibility(View.GONE);
                    break;
                case 6:
                    holder.btn_confirm.setVisibility(View.GONE);
                    break;
                case 7:
                    holder.tv_state.setText("待评价");
                    holder.btn_confirm.setVisibility(View.GONE);
                    break;
                case 8:
                    holder.tv_state.setText("服务完成");
                    holder.btn_confirm.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
            holder.btn_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onOrderListener.onButtonClick(v,position);
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return jsonArray==null?0:jsonArray.length();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView order_no,tv_apoint_time,tv_service,tv_price,tv_state;
        Button btn_confirm;
        public ViewHolder(View itemView) {
            super(itemView);
            order_no = itemView.findViewById(R.id.order_no);
            tv_apoint_time = itemView.findViewById(R.id.tv_apoint_time);
            tv_service = itemView.findViewById(R.id.tv_service);
            tv_price = itemView.findViewById(R.id.tv_price);
            tv_state = itemView.findViewById(R.id.tv_state);
            btn_confirm = itemView.findViewById(R.id.btn_confirm);
        }
    }
}
