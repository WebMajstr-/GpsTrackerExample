package com.webmajstr.gpstrackerexample;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import io.objectbox.Box;

public class ForegroundService extends Service {

    private static final String MAIN_CHANNEL_ID = "main_channel";


    private LocationManager locationManager;
    private LocationListener locationListener;
    private GpsStatus.Listener listener;

    private Box<LogEntity> logBox;

    public ForegroundService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        showForegroundNotification();

        logBox = ((App) getApplication()).getBoxStore().boxFor(LogEntity.class);
        log("Service started");

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        registerLocationUpdates();
        getGPSStatus();
    }

    private void showForegroundNotification() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel(MAIN_CHANNEL_ID, "Channel Name", importance);
            mChannel.setDescription("Channel description");
            mChannel.setShowBadge(false);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = (NotificationManager) getSystemService(
                    NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(mChannel);
        }

        Intent i = new Intent(this, MainActivity.class);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 0, i, 0);

        NotificationCompat.Builder notification = new NotificationCompat.Builder(this, MAIN_CHANNEL_ID)
                .setContentText("Service running")
                .setContentTitle(getText(R.string.app_name))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentIntent(contentIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_SERVICE)
                .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);

        // actually start foreground activity
        startForeground(1, notification.build());
    }

    @SuppressLint("MissingPermission")
    private void getGPSStatus() {

        // Define a listener that responds to location updates
        listener = new GpsStatus.Listener() {
            public void onGpsStatusChanged(int event) {
                if (event == GpsStatus.GPS_EVENT_SATELLITE_STATUS) {

                    GpsStatus status = locationManager.getGpsStatus(null);
                    Iterable<GpsSatellite> satellites = status.getSatellites();

                    int all = 0;
                    int used = 0;
                    for (GpsSatellite sat : satellites) {
                        all++;
                        if (sat.usedInFix())
                            used++;
                    }

                    log("Satelites;" + String.valueOf(used) + "/" + String.valueOf(all));
                } else {
                    log("onGpsStatusChanged;" + String.valueOf(event));
                }
            }
        };

        locationManager.addGpsStatusListener(listener);
    }

    @SuppressLint("MissingPermission")
    private void registerLocationUpdates() {

        // Define a listener that responds to location updates
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                log("onLocationChanged;" + location.toString());
            }

            public void onStatusChanged(String provider, int status,
                                        Bundle extras) {
                log("onStatusChanged;" + String.valueOf(status) + ";" + bundle2string(extras));
            }

            public void onProviderEnabled(String provider) {
                log("onProviderEnabled;" + provider);
            }

            public void onProviderDisabled(String provider) {
                log("onProviderDisabled;" + provider);
            }
        };

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
    }

    @Override
    public void onDestroy() {

        log("Service stopped");
        locationManager.removeUpdates(locationListener);
        locationManager.removeGpsStatusListener(listener);
        locationManager = null;

        // stop foreground
        stopForeground(true);

        super.onDestroy();
    }

    private void log(String log) {
        logBox.put(new LogEntity(log));
    }

    public static String bundle2string(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        String string = "Bundle{";
        for (String key : bundle.keySet()) {
            string += " " + key + " => " + bundle.get(key) + ";";
        }
        string += " }Bundle";
        return string;
    }
}
