package com.phemie.scnu.laolekang.Health.Map;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.phemie.scnu.laolekang.R;

public class MapActivity extends AppCompatActivity implements View.OnClickListener {

    public TextView locate;
    public String locationDescribe;//当前详细地址描述
    public double Latitude;//当前位置的经度
    public double Longitude;//当前位置的纬度
    ImageButton rreturn;
    private LocationClient mLocationClient;
    private BaiduMap mBaiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.map_layout);

        locate = (TextView) findViewById(R.id.locate);
        rreturn = (ImageButton) findViewById(R.id.rreturn);

        rreturn.setOnClickListener(this);

        // 获取LocationClient
        mLocationClient = new LocationClient(this);

        LocationClientOption option = new LocationClientOption();
        option.setCoorType("bd09ll");
        option.setIsNeedAddress(true);//使用location.getAddrStr();获取详细地址信息要用到
        mLocationClient.setLocOption(option);

        // 获取BaiduMap
        MapView mapView = (MapView) findViewById(R.id.mapView);
        mBaiduMap = mapView.getMap();

        // 显示出当前位置的小图标
        mBaiduMap.setMyLocationEnabled(true);

        MyLocationListener mListener = new MyLocationListener();
        mLocationClient.registerLocationListener(mListener);
        mLocationClient.start();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rreturn:
                finish();
                break;
        }
    }


    @Override
    protected void onDestroy() {
        mLocationClient.stop();
        super.onDestroy();
    }

    private class MyLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {

            // 只是完成了定位
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(location.getRadius())
                    // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(location.getDirection())
                    .latitude(location.getLatitude())
                    .longitude(location.getLongitude()).build();

            Latitude = location.getLatitude();
            Longitude = location.getLongitude();


            //设置图标在地图上的位置
            mBaiduMap.setMyLocationData(locData);

            locationDescribe = location.getAddrStr();
            locate.setText(locationDescribe);//在TextView中设置位置信息


            // 开始移动百度地图的定位地点到中心位置
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, 16.0f);
            mBaiduMap.animateMapStatus(u);
        }

    }

}


