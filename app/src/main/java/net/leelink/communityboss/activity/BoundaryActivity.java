package net.leelink.communityboss.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.CircleOptions;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.maps.model.Text;
import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.cncoderx.wheelview.OnWheelChangedListener;
import com.cncoderx.wheelview.WheelView;


import net.leelink.communityboss.R;

import java.util.ArrayList;
import java.util.List;

public class BoundaryActivity extends BaseActivity implements View.OnClickListener {
    MapView map_view;
    AMap aMap;
    RelativeLayout rl_back;
    TextView tv_done;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boundary);
        map_view = findViewById(R.id.map_view);
        map_view.onCreate(savedInstanceState);// 此方法须覆写，虚拟机需要在很多情况下保存地图绘制的当前状态。

        init();
    }

    public void init() {
        rl_back = findViewById(R.id.rl_back);
        rl_back.setOnClickListener(this);
        tv_done = findViewById(R.id.tv_done);
        tv_done.setOnClickListener(this);
        LatLng latLng = new LatLng(39.984059, 116.307771);

        if(aMap==null) {
            aMap = map_view.getMap();
        }
        aMap.addMarker(new MarkerOptions().position(latLng).title("店铺").snippet("恐龙乐园"));
        aMap.addCircle(new CircleOptions().
                center(latLng).
                radius(10000).
                fillColor(Color.argb(60, 1, 1, 1)).
                strokeColor(Color.argb(90, 1, 1, 1)).
                strokeWidth(15));
        WheelView wheelView = (WheelView) findViewById(R.id.wheel3d);
        wheelView.setOnWheelChangedListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView view, int oldIndex, int newIndex) {
                CharSequence text = view.getItem(newIndex);
                Log.i("WheelView", String.format("index: %d, text: %s", newIndex, text));
                aMap.clear();
                LatLng latLng = new LatLng(39.984059, 116.307771);
                aMap.addMarker(new MarkerOptions().position(latLng).title("店铺").snippet("恐龙乐园"));
                aMap.addCircle(new CircleOptions().
                        center(latLng).
                        radius(newIndex*2000).
                        fillColor(Color.argb(60, 1, 1, 1)).
                        strokeColor(Color.argb(90, 1, 1, 1)).
                        strokeWidth(15));
            }
        });


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.rl_back:
                aMap.clear();
                break;
            case R.id.tv_done:
                updateLimit();
                break;
        }
    }

    public void updateLimit(){

    }
}
