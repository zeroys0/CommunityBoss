package net.leelink.communityboss.housekeep;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.google.android.material.tabs.TabLayout;

import net.leelink.communityboss.R;
import net.leelink.communityboss.activity.BaseActivity;
import net.leelink.communityboss.adapter.OnOrderListener;
import net.leelink.communityboss.housekeep.fragment.StaffCheckFragment;
import net.leelink.communityboss.housekeep.fragment.StaffListFragment;
import net.leelink.communityboss.housekeep.fragment.StaffServiceFragment;
import net.leelink.communityboss.utils.Utils;



public class StaffManageActivity extends BaseActivity implements OnOrderListener, View.OnClickListener {
private TabLayout tablayout;
private RelativeLayout rl_back;
private StaffCheckFragment staffCheckFragment;
private StaffListFragment staffListFragment;
private StaffServiceFragment staffServiceFragment;
    private FragmentManager fm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_manage);
        init();

    }

    public void init(){
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        tablayout = findViewById(R.id.tablayout);
        tablayout.addTab(tablayout.newTab().setText("员工审核"));
        tablayout.addTab(tablayout.newTab().setText("员工管理"));
        tablayout.addTab(tablayout.newTab().setText("添加服务"));
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        staffCheckFragment = (StaffCheckFragment) fm.findFragmentByTag("check");
        if (staffCheckFragment == null) {
            staffCheckFragment = new StaffCheckFragment();
        }
        ft.add(R.id.fragment_view, staffCheckFragment, "check");
        ft.commit();
        tablayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                switch (tab.getPosition()){
                    case 0:
                        FragmentTransaction ft = getFragmentTransaction();
                        if (staffCheckFragment == null) {
                            ft.add(R.id.fragment_view, new StaffCheckFragment(), "check");
                        } else {
                            ft.show(staffCheckFragment);
                        }
                        Utils.setStatusTextColor(true, StaffManageActivity.this);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft.commit();
                        break;
                    case 1:
                        FragmentTransaction ft1 = getFragmentTransaction();
                        if (staffListFragment == null) {
                            ft1.add(R.id.fragment_view, new StaffListFragment(), "list");
                        } else {
                            ft1.show(staffListFragment);
                        }
                        Utils.setStatusTextColor(true, StaffManageActivity.this);
                        ft1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft1.commit();
                        break;
                    case 2:
                        FragmentTransaction ft2 = getFragmentTransaction();
                        if (staffServiceFragment == null) {
                            ft2.add(R.id.fragment_view, new StaffServiceFragment(), "service");
                        } else {
                            ft2.show(staffServiceFragment);
                        }
                        Utils.setStatusTextColor(true, StaffManageActivity.this);
                        ft2.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft2.commit();
                        break;
                    default:
                        break;
                }

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }



    @Override
    public void onItemClick(View view) {

    }

    @Override
    public void onButtonClick(View view, int position) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_back:
                finish();
                break;
        }
    }

    protected FragmentTransaction getFragmentTransaction() {
        // TODO Auto-generated method stub
        FragmentManager fm = getSupportFragmentManager();
        staffCheckFragment = (StaffCheckFragment) fm.findFragmentByTag("check");
        staffListFragment = (StaffListFragment) fm.findFragmentByTag("list");
        staffServiceFragment = (StaffServiceFragment) fm.findFragmentByTag("service");
        FragmentTransaction ft = fm.beginTransaction();
        /** 如果存在hide掉 */
        if (staffCheckFragment != null)
            ft.hide(staffCheckFragment);
        if (staffListFragment != null)
            ft.hide(staffListFragment);
        if (staffServiceFragment != null)
            ft.hide(staffServiceFragment);
        return ft;
    }
}
