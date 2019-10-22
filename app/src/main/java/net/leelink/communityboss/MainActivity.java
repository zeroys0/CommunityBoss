package net.leelink.communityboss;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;

import net.leelink.communityboss.activity.BaseActivity;
import net.leelink.communityboss.fragment.CompleteOrderFragment;
import net.leelink.communityboss.fragment.MineFragment;
import net.leelink.communityboss.fragment.TakeOrderFragment;
import net.leelink.communityboss.fragment.UntakeOrderFragment;
import net.leelink.communityboss.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {
private BottomNavigationBar nv_bottom;
private UntakeOrderFragment untakeOrderFragment;
private TakeOrderFragment takeOrderFragment;
private CompleteOrderFragment completeOrderFragment;
private MineFragment mineFragment;
FragmentManager fm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
    }

    public void init(){
        nv_bottom = findViewById(R.id.nv_bottom);
        nv_bottom.setTabSelectedListener(MainActivity.this);
        nv_bottom.setMode(BottomNavigationBar.MODE_FIXED);
        nv_bottom.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);
        nv_bottom.setBarBackgroundColor(R.color.white);
        nv_bottom
                .addItem(new BottomNavigationItem(R.mipmap.untake_order_selected, "未接单").setInactiveIcon(getResources().getDrawable(R.mipmap.untake_order_unselect)).setActiveColorResource(R.color.blue))
                .addItem(new BottomNavigationItem(R.mipmap.take_order_selected, "已接单").setInactiveIcon(getResources().getDrawable(R.mipmap.take_order_unselect)).setActiveColorResource(R.color.blue))
                .addItem(new BottomNavigationItem(R.mipmap.complete_order_selected, "已处理").setInactiveIcon(getResources().getDrawable(R.mipmap.complete_order_unselect)).setActiveColorResource(R.color.blue))
                .addItem(new BottomNavigationItem(R.mipmap.mine_selected, "我的").setInactiveIcon(getResources().getDrawable(R.mipmap.mine_unselect)).setActiveColorResource(R.color.blue))
                .setFirstSelectedPosition(0)
                .initialise();
        fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        untakeOrderFragment = (UntakeOrderFragment) fm.findFragmentByTag("untake");
        if (untakeOrderFragment == null) {
            untakeOrderFragment = new UntakeOrderFragment();
        }
        ft.add(R.id.fragment_view, untakeOrderFragment, "untake");
        ft.commit();



        int a = 0;
        List<String> list = new ArrayList();
        list.add("友鹏财管");
        list.add("阿里发发");
        list.add("新的商店");

    }


    @Override
    public void onTabSelected(int position) {
        FragmentTransaction ft = getFragmentTransaction();
        switch (position) {
            case 0:
                if (untakeOrderFragment == null) {
                    ft.add(R.id.fragment_view, new UntakeOrderFragment(), "untake");
                } else {
                    ft.show(untakeOrderFragment);
                }
                Utils.setStatusTextColor(true, MainActivity.this);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
                Toast.makeText(this, "click 0", Toast.LENGTH_SHORT).show();
                break;
            case 1:
                if(takeOrderFragment ==null) {
                    ft.add(R.id.fragment_view, new TakeOrderFragment(),"take");
                } else {
                    ft.show(takeOrderFragment);
                }
                Utils.setStatusTextColor(true,MainActivity.this);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
                break;
            case 2:
                if(completeOrderFragment ==null) {
                    ft.add(R.id.fragment_view, new CompleteOrderFragment(),"complete");
                } else {
                    ft.show(completeOrderFragment);
                }
                Utils.setStatusTextColor(true,MainActivity.this);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
                break;
            case 3:
                if(mineFragment ==null) {
                    ft.add(R.id.fragment_view, new MineFragment(),"mine");
                } else {
                    ft.show(mineFragment);
                }
                Utils.setStatusTextColor(true,MainActivity.this);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
                break;
            default:
                break;
        }
    }

    @Override
    public void onTabUnselected(int position) {

    }

    @Override
    public void onTabReselected(int position) {

    }
    protected FragmentTransaction getFragmentTransaction() {
        // TODO Auto-generated method stub
        FragmentManager fm = getSupportFragmentManager();
        untakeOrderFragment = (UntakeOrderFragment) fm.findFragmentByTag("untake");
        takeOrderFragment = (TakeOrderFragment) fm.findFragmentByTag("take");
        completeOrderFragment = (CompleteOrderFragment) fm.findFragmentByTag("complete");
        mineFragment = (MineFragment) fm.findFragmentByTag("mine");
        FragmentTransaction ft = fm.beginTransaction();
        /** 如果存在hide掉 */
        if (untakeOrderFragment != null)
            ft.hide(untakeOrderFragment);
        if (takeOrderFragment != null)
            ft.hide(takeOrderFragment);
        if (completeOrderFragment != null)
            ft.hide(completeOrderFragment);
        if(mineFragment != null)
            ft.hide(mineFragment);
        return ft;
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}