package com.example.mymark;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private TextView tv_location;
    private Button bt_locate;
    static double latitude = 0, longitude = 0;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Log.e(TAG,"Fucked");
        bt_locate = (Button) findViewById(R.id.bt_find_location);
        Log.e(TAG,"Fucked");
        Log.e(TAG,bt_locate.toString());
        bt_locate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
//            OnGPS();
//        } else {
//            getLocation();
//        }
            }
        });

        Log.e(TAG,"Fucked");
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        getLocation();

        Log.e(TAG, "onClick: latitude = " + latitude);
        Log.e(TAG, "onClick: longitude = " + longitude);

        Bundle bundle = new Bundle();
        bundle.putString("lat", latitude + "");
        bundle.putString("long", longitude + "");

//        if (savedInstanceState == null) {
//            getSupportFragmentManager().beginTransaction()
//                    .setReorderingAllowed(true)
//                    .add(R.id.fragment_container_view, MapsFragment.class, bundle)
//                    .commit();
//        }
        MapsFragment obj = new MapsFragment();
        obj.setArguments(bundle);

    }

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                HomeActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                HomeActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            Location locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (locationGPS != null) {
                latitude = locationGPS.getLatitude();
                longitude = locationGPS.getLongitude();
                //tv_location.setText("Your Location: " + " " + "Latitude: " + latitude + " " + "Longitude: " + longitude);
            } else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
        Log.e(TAG,"Fucked");
    }

}