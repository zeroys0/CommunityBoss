package net.leelink.communityboss.adapter;

import android.view.View;

public interface OnCancelListener extends OnOrderListener{
    void onCancel(View view,int position);
}
