package com.zopsmart.zsupdate;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.zopsmart.updatehelper.UpdateHelper;
import com.zopsmart.updatehelper.interfaces.UpdateListener;
import com.zopsmart.updatehelper.pojo.UpdateConfig;


public class MainActivity extends AppCompatActivity {

    TextView tvStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvStatus = findViewById(R.id.tvStatus);
        UpdateHelper updateHelper = new UpdateHelper(this);
        updateHelper.listenForUpdates(getPackageName(), new UpdateListener() {
            @Override
            public void error(Throwable e) {
                tvStatus.setText(e.getMessage());
            }

            @Override
            public void success(UpdateConfig updateConfig) {
                tvStatus.setText(updateConfig.toString());
            }
        });
    }
}
