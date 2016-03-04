package com.example.linweijie.mygooglemap;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private GoogleMap mMap;
    private TextView tv_result;
    private EditText et_address;

    private ArrayList<Marker> markerArrayList = new ArrayList<>();
    private Marker currentMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_result = (TextView) findViewById(R.id.tv_result);

        Button bt_check = (Button) findViewById(R.id.bt_check);
        Button bt_sreach = (Button) findViewById(R.id.bt_sreach);
        Button bt_add = (Button) findViewById(R.id.bt_add);
        Button bt_remove = (Button) findViewById(R.id.bt_remove);
        Button bt_me = (Button) findViewById(R.id.bt_me);
        et_address = (EditText) findViewById(R.id.et_address);

        bt_check.setOnClickListener(this);
        bt_sreach.setOnClickListener(this);
        bt_add.setOnClickListener(this);
        bt_remove.setOnClickListener(this);
        bt_me.setOnClickListener(this);

        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                PLog.d(Pub.TAG, "Tag is Click");
                currentMarker = marker;
                return true;
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_check:
                checkGooglePlayServices();
                break;
            case R.id.bt_sreach:
                break;
            case R.id.bt_add:
                String tmp = et_address.getText().toString();

                if (tmp.equals("台北101")) {
                    AddMark(new LatLng(25.033611, 121.565000),
                            "台北101",
                            "於1999年動工，2004年12月31日完工啟用，樓高509.2公尺。",
                            BitmapDescriptorFactory.fromResource(android.R.drawable.ic_menu_mylocation));
                }
                if (tmp.equals("台北車站")) {
                    AddMark(new LatLng(25.047924, 121.517081),
                            "台北車站",
                            "peter",
                            null);
                }
                if (tmp.equals("美麗華")) {
                    AddMark(new LatLng(25.082779, 121.55746),
                            "美麗華",
                            "peter",
                            BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                }
                break;
            case R.id.bt_remove:
                break;
            case R.id.bt_me:
                getMyLocation();
                break;
        }
    }

    /**
     * 加入和移除地圖標籤
     * */
    // TODO 尚未思考完全
    private void AddMark(LatLng lotation, String title, String content, BitmapDescriptor icon) {

        // 檢查 mark 是否存在
        boolean isExist = false;
        for (int i = 0; i < markerArrayList.size(); ++i) {
            if (markerArrayList.get(i).getTitle().equals(title)) {
                isExist = true;
            }
        }

        // 在地圖上標示 mark
        if (!isExist) {
            MarkerOptions markerOpt = new MarkerOptions();
            markerOpt.position(lotation);
            markerOpt.title(title);
            markerOpt.snippet(content);
            markerOpt.draggable(false);
            markerOpt.anchor(0.5f, 0.5f);//設為圖片中心
            if (icon != null) {
                markerOpt.icon(icon);
            }

            markerArrayList.add(mMap.addMarker(markerOpt));
        } else {
            PLog.d(Pub.TAG, "Tag \"" + title + "\" is Exist");
        }
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(lotation, 16));
    }

    private void RemoveMark() {
    }


    /**
     * 檢查網路和定位狀態, 並抓取所在座標資訊
     * */
    private void showAlertDialog(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("無法偵測所在座標, 需開啟網路或定位？")
                .setCancelable(false)
                .setPositiveButton("前往設定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                Intent callGPSSettingIntent = new Intent(
                                        android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(callGPSSettingIntent);
                            }
                        });
        alertDialogBuilder.setNegativeButton("取消",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }
    private boolean isNetworkEnabled(){
        boolean result=false;
        try{
            ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getNetworkInfo(0);
            if (netInfo != null && netInfo.getState()==NetworkInfo.State.CONNECTED) {
                PLog.d(Pub.TAG, "Network is Enabled in your devide");
                result= true;
            }else {
                netInfo = cm.getNetworkInfo(1);
                if(netInfo!=null && netInfo.getState()==NetworkInfo.State.CONNECTED){
                    PLog.d(Pub.TAG, "Network is Enabled in your devide");
                    result= true;
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return result;
    }
    private boolean isGPSEnabled() {
        boolean result = false;
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            PLog.d(Pub.TAG, "GPS is Enabled in your devide");
            result = true;
        }

        return result;
    }
    private Location getMyLocation() {
        Location location = null;
        LocationManager locationManager = (LocationManager) this.getSystemService(LOCATION_SERVICE);
        boolean gps = isGPSEnabled();
        boolean network = isNetworkEnabled();

        if (!gps && !network) {
            showAlertDialog();
        } else {
            if (network) {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location==null){
                    PLog.d(Pub.TAG, "網路連線不穩, 無法取得座標");
                }
            }
            if (gps && location==null) {
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location==null){
                    PLog.d(Pub.TAG, "GPS 定位不穩, 無法取得座標");
                }
            }
        }

        if (location != null){
            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18);
            mMap.animateCamera(cameraUpdate);
        }
        return location;
    }


    /**
     * 檢查是否支援 GooglePlayServices
     * */
    private void checkGooglePlayServices(){
        int result = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        switch (result) {
            case ConnectionResult.SUCCESS:
                PLog.d(Pub.TAG, "SUCCESS");
                tv_result.setText("結果：" + "SUCCESS");
                break;

            case ConnectionResult.SERVICE_INVALID:
                PLog.d(Pub.TAG, "SERVICE_INVALID");
                GooglePlayServicesUtil.getErrorDialog(ConnectionResult.SERVICE_INVALID, this, 0).show();
                tv_result.setText("結果：" + "SERVICE_INVALID");
                break;

            case ConnectionResult.SERVICE_MISSING:
                PLog.d(Pub.TAG, "SERVICE_MISSING");
                GooglePlayServicesUtil.getErrorDialog(ConnectionResult.SERVICE_MISSING, this, 0).show();
                tv_result.setText("結果：" + "SERVICE_MISSING");
                break;

            case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
                PLog.d(Pub.TAG, "SERVICE_VERSION_UPDATE_REQUIRED");
                GooglePlayServicesUtil.getErrorDialog(ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED, this, 0).show();
                tv_result.setText("結果：" + "SERVICE_VERSION_UPDATE_REQUIRED");
                break;

            case ConnectionResult.SERVICE_DISABLED:
                PLog.d(Pub.TAG, "SERVICE_DISABLED");
                GooglePlayServicesUtil.getErrorDialog(ConnectionResult.SERVICE_DISABLED, this, 0).show();
                tv_result.setText("結果：" + "SERVICE_DISABLED");
                break;
        }
    }
}
