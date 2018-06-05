package com.webmajstr.gpstrackerexample;

import android.app.Activity;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.util.List;

import io.objectbox.Box;

public class LogService {

    private Activity activity;

    public LogService(Activity activity) {
        this.activity = activity;
    }

    public void exportToFile() {

        // Get data from DB
        Box<LogEntity> logBox = ((App) activity.getApplication()).getBoxStore().boxFor(LogEntity.class);
        List<LogEntity> logs = logBox.getAll();

        // Save data to file
        String fileName = "tracker-" + String.valueOf(System.currentTimeMillis() / 1000L) + ".log";
        File dir = new File(android.os.Environment.getExternalStorageDirectory().getAbsolutePath() + "/gpsTracker");
        dir.mkdirs();
        File file = new File(dir, fileName);

        try {
            FileOutputStream f = new FileOutputStream(file);
            PrintWriter pw = new PrintWriter(f);
            for (LogEntity logEntity : logs) {
                pw.println(logEntity.toString());
            }
            pw.flush();
            pw.close();
            f.close();
            Toast.makeText(activity, "Log successfully saved to " + file.toString(), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void clear() {
        Box<LogEntity> logBox = ((App) activity.getApplication()).getBoxStore().boxFor(LogEntity.class);
        logBox.removeAll();
        Toast.makeText(activity, "Log emptied", Toast.LENGTH_LONG).show();
    }
}
