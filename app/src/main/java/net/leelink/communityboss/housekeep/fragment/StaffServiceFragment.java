package net.leelink.communityboss.housekeep.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.R;
import net.leelink.communityboss.adapter.OnOrderListener;
import net.leelink.communityboss.bean.StaffBean;
import net.leelink.communityboss.fragment.BaseFragment;
import net.leelink.communityboss.housekeep.AllServiceActivity;
import net.leelink.communityboss.housekeep.StaffServiceActivity;
import net.leelink.communityboss.housekeep.adapter.StaffCheckAdapter;
import net.leelink.communityboss.utils.Urls;
import net.leelink.communityboss.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class StaffServiceFragment extends BaseFragment implements OnOrderListener {

    private RecyclerView staff_list;
    private List<StaffBean> list = new ArrayList<>();
    private StaffCheckAdapter staffCheckAdapter;
    private int page = 1;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff_check,container,false);
        init(view);


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    public void init(View view){
        staff_list = view.findViewById(R.id.staff_list);

    }

    public void initData(){
        OkGo.<String>get(Urls.getInstance().SERPRODUCT)
                .params("pageNum",page)
                .params("pageSize",10)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("服务人员列表",json.toString());
                            if (json.getInt("status") == 200) {
                                json = json.getJSONObject("data");
                                JSONArray jsonArray = json.getJSONArray("list");
                                Gson gson = new Gson();
                                list = gson.fromJson(jsonArray.toString(),new TypeToken<List<StaffBean>>(){}.getType());
                                staffCheckAdapter = new StaffCheckAdapter(list,getContext(), StaffServiceFragment.this,2);
                                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false);

                                staff_list.setLayoutManager(layoutManager);
                                staff_list.setAdapter(staffCheckAdapter);
                            } else {
                                Toast.makeText(getContext(), json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
    }
    @Override
    public void onItemClick(View view) {
        if(Utils.isFastClick()){
            return;
        }
        int position = staff_list.getChildLayoutPosition(view);
        Intent intent = new Intent(getContext(), StaffServiceActivity.class);
        intent.putExtra("id",list.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void onButtonClick(View view, int position) {
        if(Utils.isFastClick()){
            return;
        }
        Intent intent = new Intent(getContext(), AllServiceActivity.class);
        intent.putExtra("id",list.get(position).getId());
        startActivity(intent);
    }

    @Override
    public void handleCallBack(Message msg) {

    }
}
