package com.example.mymark;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class LocationService extends Service {
    public Context context = this;
    public Handler handler = null;
    public static Runnable runnable = null;
    private static final int NOTIF_ID = 1;
    private static final String NOTIF_CHANNEL_ID = "Channel_Id";
    private LocationManager locationManager;
    static double latitude = 0, longitude = 0;
    private LocationCallback locationCallback;
    private FusedLocationProviderClient client;
    private LocationRequest locationRequest;
    private String uid;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Intent notifyIntent = new Intent(this, HomeActivity.class);
// Set the Activity to start in a new, empty task

// Create the PendingIntent
        PendingIntent notifyPendingIntent = PendingIntent.getActivity(
                this, 0, notifyIntent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        final NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        String channelId = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ? createNotificationChannel(notificationManager) : "";
        final NotificationCompat.Builder notification = new NotificationCompat.Builder(this,channelId)
                .setContentTitle("Connect")
                .setContentIntent(notifyPendingIntent)
                .setContentText("Find People Near You!")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setOngoing(true);

        // notificationManager.notify(1,notification.build()); <-- remove this
        startForeground(101,notification.build());
    }

    private String createNotificationChannel(NotificationManager notificationManager){
        String channelId = "my_service_channelid";
        String channelName = "Foreground Service";
        NotificationChannel channel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            channel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_HIGH);
        }
        // omitted the LED color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel.setImportance(NotificationManager.IMPORTANCE_NONE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationManager.createNotificationChannel(channel);
        }
        return channelId;
    }

    public void getLocation() {
        client = LocationServices.getFusedLocationProviderClient(LocationService.this);
        locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(500);
        locationRequest.setFastestInterval(500);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                   // HomeActivity ha = new HomeActivity();
                    //ha.updateCoordinates(uid, location.getLatitude(), location.getLongitude());
                }
            }
        };

        if (ActivityCompat.checkSelfPermission(LocationService.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(LocationService.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        client.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper());
    }


    @Override
    public void onDestroy() {
        /* IF YOU WANT THIS SERVICE KILLED WITH THE APP THEN UNCOMMENT THE FOLLOWING LINE */
        //handler.removeCallbacks(runnable);=
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(),this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        startService(restartServiceIntent);
        super.onTaskRemoved(rootIntent);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

            uid = (String) intent.getExtras().get("uid");
            Log.e(TAG, "onStartCommand: " + uid );
            handler = new Handler();
        runnable = new Runnable() {
            public void run() {
                getLocation();
                handler.postDelayed(runnable, 5000);
            }
        };
        handler.postDelayed(runnable, 5000);
        return START_STICKY;
    }
}