package com.webmajstr.gpstrackerexample;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;
import io.objectbox.Box;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

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

        Box<LogEntity> logBox = ((App) getApplication()).getBoxStore().boxFor(LogEntity.class);
        List<LogEntity> logs = logBox.getAll();

        saveToFile(logs);
    }

    @OnClick(R.id.emptyLog)
    public void emptyLog(View view) {
        Box<LogEntity> logBox = ((App) getApplication()).getBoxStore().boxFor(LogEntity.class);
        logBox.removeAll();
        Toast.makeText(this, "Log emptied", Toast.LENGTH_LONG).show();
    }

    private void saveToFile(List<LogEntity> logs) {
        File root = android.os.Environment.getExternalStorageDirectory();

        Date date = new Date();

        File dir = new File(root.getAbsolutePath() + "/gpsTracker");
        dir.mkdirs();
        File file = new File(dir, "tracker-" + String.valueOf(System.currentTimeMillis() / 1000L) + ".log");

        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            for (LogEntity logEntity : logs) {
                pw.println(logEntity.toString());
            }
            pw.flush();
            pw.close();
            f.close();
            Toast.makeText(this, "Log successfully saved to " + file.toString(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
