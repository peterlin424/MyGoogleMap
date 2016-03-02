package com.example.linweijie.mygooglemap;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private GoogleMap mMap;
    private LatLng TPA101 = new LatLng(25.033681, 121.564726);
    private Marker marker_tpa101;

    private TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tv_result = (TextView)findViewById(R.id.tv_result);

        Button bt_check = (Button)findViewById(R.id.bt_check);
        Button bt_sreach = (Button)findViewById(R.id.bt_sreach);
        Button bt_add = (Button)findViewById(R.id.bt_add);
        Button bt_remove = (Button)findViewById(R.id.bt_remove);
        Button bt_me = (Button)findViewById(R.id.bt_me);

        bt_check.setOnClickListener(this);
        bt_sreach.setOnClickListener(this);
        bt_add.setOnClickListener(this);
        bt_remove.setOnClickListener(this);
        bt_me.setOnClickListener(this);

        mMap = ((MapFragment)getFragmentManager().findFragmentById(R.id.map)).getMap();
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_check:
                checkGooglePlayServices();
                break;
            case R.id.bt_sreach:
                break;
            case R.id.bt_add:
                break;
            case R.id.bt_remove:
                marker_tpa101.setVisible(false);
                break;
            case R.id.bt_me:
                if (marker_tpa101 == null){
                    marker_tpa101 = mMap.addMarker(new MarkerOptions().position(TPA101).title("台北101").snippet("WeJump Peter"));
                } else {
                    marker_tpa101.setVisible(true);
                }
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(TPA101, 16));
                break;
        }
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
