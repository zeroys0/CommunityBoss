package net.leelink.communityboss.adapter;

import android.view.View;

public interface OnCollectListener {
     void onItemClick(View view);
    void onCancelChecked(View view,int position,boolean state);
}
