package com.webmajstr.gpstrackerexample;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    private LogService logService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        logService = new LogService(this);

        String[] perms = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION};
        if (!EasyPermissions.hasPermissions(this, perms)) {
            EasyPermissions.requestPermissions(this, "Allow the permissions", 1, perms);
        }
    }

    @OnClick(R.id.startService)
    public void startService(View view) {
        Intent intent = new Intent(this, ForegroundService.class);
        startService(intent);
    }

    @OnClick(R.id.stopService)
    public void stopService(View view) {
        Intent intent = new Intent(this, ForegroundService.class);
        stopService(intent);
    }

    @OnClick(R.id.saveLog)
    public void saveLog(View view) {
        logService.exportToFile();
    }

    @OnClick(R.id.emptyLog)
    public void emptyLog(View view) {
        logService.clear();
    }

}
