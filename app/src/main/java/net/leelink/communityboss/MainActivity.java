package net.leelink.communityboss;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.allenliu.versionchecklib.v2.AllenVersionChecker;
import com.allenliu.versionchecklib.v2.builder.UIData;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.lcodecore.tkrefreshlayout.utils.DensityUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import net.leelink.communityboss.activity.BaseActivity;
import net.leelink.communityboss.app.CommunityBossApplication;
import net.leelink.communityboss.fragment.CompleteOrderFragment;
import net.leelink.communityboss.fragment.MineFragment;
import net.leelink.communityboss.fragment.TakeOrderFragment;
import net.leelink.communityboss.fragment.UntakeOrderFragment;
import net.leelink.communityboss.utils.ToastUtil;
import net.leelink.communityboss.utils.Urls;
import net.leelink.communityboss.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
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
        checkVersion();
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
        setBottomNavigationItem(8,20);


        int a = 0;
        List<String> list = new ArrayList();
        list.add("友鹏财管");
        list.add("阿里发发");
        list.add("新的商店");

    }

    public void checkVersion() {
        OkGo.<String>get(Urls.VERSION)
                .tag(this)
                .params("appName", "乐聆商家版")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        try {
                            String body = response.body();
                            JSONObject json = new JSONObject(body);
                            Log.d("获取版本信息", json.toString());
                            if (json.getInt("status") == 200) {
                                json = json.getJSONObject("data");
                                if(Utils.getVersionCode(MainActivity.this)<json.getInt("version")) {
                                    AllenVersionChecker
                                            .getInstance()
                                            .downloadOnly(
                                                    UIData.create().setDownloadUrl(json.getString("apkUrl"))
                                                            .setTitle("检测到新的版本")
                                                            .setContent("检测到您当前不是最新版本,是否要更新?")
                                            )
                                            .executeMission(MainActivity.this);
                                }
                            } else {
                                Toast.makeText(MainActivity.this, json.getString("message"), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


    }

    private void setBottomNavigationItem(int space, int imgLen) {
        float contentLen = 36;
        Class barClass = nv_bottom.getClass();
        Field[] fields = barClass.getDeclaredFields();
        for (int i = 0; i < fields.length; i++) {
            Field field = fields[i];
            field.setAccessible(true);
            if (field.getName().equals("mTabContainer")) {
                try { //反射得到 mTabContainer
                    LinearLayout mTabContainer = (LinearLayout) field.get(nv_bottom);
                    for (int j = 0; j < mTabContainer.getChildCount(); j++) {
                        //获取到容器内的各个 Tab
                        View view = mTabContainer.getChildAt(j);
                        //获取到Tab内的各个显示控件
                        // 获取到Tab内的文字控件
                        TextView labelView = (TextView) view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_title);
                        //计算文字的高度DP值并设置，setTextSize为设置文字正方形的对角线长度，所以：文字高度（总内容高度减去间距和图片高度）*根号2即为对角线长度，此处用DP值，设置该值即可。
                        labelView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, (float) (Math.sqrt(2) * (contentLen - imgLen - space)));
                        //获取到Tab内的图像控件
                        ImageView iconView = (ImageView) view.findViewById(com.ashokvarma.bottomnavigation.R.id.fixed_bottom_navigation_icon);
                        //设置图片参数，其中，MethodUtils.dip2px()：换算dp值
                        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int) DensityUtil.dp2px(this, imgLen), (int) DensityUtil.dp2px(this, imgLen));
                        params.gravity = Gravity.CENTER;
                        iconView.setLayoutParams(params);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            }
        }
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

    private long exitTime = 0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK
                && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                ToastUtil.show(getApplicationContext(), "再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                CommunityBossApplication.getInstance().exit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
