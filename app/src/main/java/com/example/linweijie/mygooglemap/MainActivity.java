package com.example.linweijie.mygooglemap;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView tv_result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button bt_check = (Button)findViewById(R.id.bt_check);
        bt_check.setOnClickListener(this);

        tv_result = (TextView)findViewById(R.id.tv_result);
    }

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

    @Override
    public void onClick(View v) {
        checkGooglePlayServices();
    }
}
