package net.leelink.communityboss.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;

import net.leelink.communityboss.R;
import net.leelink.communityboss.adapter.CategoryAdapter;
import net.leelink.communityboss.adapter.OnCategoryClickListener;
import net.leelink.communityboss.adapter.onCategoryListener;


import java.util.ArrayList;
import java.util.List;

public class CategoryPopup extends PopupWindow implements onCategoryListener {
    private Context context;
    private RecyclerView category_list;
    private CategoryAdapter categoryAdapter;
    List<String> list = new ArrayList<>();
    public CategoryPopup(Context context, OnCategoryClickListener onCategoryClickListener, List<String> list) {
        super(context);
        setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.context =context;
        this.list = list;
        setOutsideTouchable(true);
        setFocusable(true);
        setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View contentView = LayoutInflater.from(context).inflate(R.layout.popu_category,
                null, false);
        setContentView(contentView);
        category_list = contentView.findViewById(R.id.category_list);
        list = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(list,context,onCategoryClickListener);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL,false);
        category_list.setAdapter(categoryAdapter);
        category_list.setLayoutManager(layoutManager);

    }


    @Override
    public void onchange(List<String> list) {
        this.list = list;
        categoryAdapter.update(list);
    }
}
