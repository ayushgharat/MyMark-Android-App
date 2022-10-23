package com.example.mymark;

import static android.content.ContentValues.TAG;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.location.LocationRequest;
import android.location.LocationListener;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private TextView tv_location;
    private Button bt_locate;
    private ImageButton ib_profile, ib_logout;
    static double latitude = 0, longitude = 0;
    LocationRequest locationRequest;
    FusedLocationProviderClient client;

    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private FirebaseAuth mAuth;
    private String uid;

    public HomeActivity () {
        FirebaseApp.initializeApp(HomeActivity.this);
        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        dbRef = database.getReference("Username");
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ib_profile = (ImageButton) findViewById(R.id.ib_profile_page);
        ib_logout = (ImageButton) findViewById(R.id.ib_sign_out);

        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        getLocation();

        Bundle bundle = new Bundle();
        bundle.putString("lat", latitude + "");
        bundle.putString("long", longitude + "");
        updateGoogleFragment(bundle);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        dbRef = database.getReference("Username");
        uid = mAuth.getUid();

        ib_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
                startActivity(intent);
            }
        });

        ib_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = new Intent(this, LocationService.class);
        intent.putExtra("uid", mAuth.getUid());
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        }
        else {
            startService(intent);
        }

        updateCoordinates(uid, latitude, longitude);
    }

    public void updateGoogleFragment(Bundle bundle) {
        MapsFragment obj = new MapsFragment();
        obj.setArguments(bundle);
    }

    public void updateCoordinates(String uid, double latitude, double longitude) {
        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("Username");
        Bundle bundle = new Bundle();
        bundle.putString("lat", latitude + "");
        bundle.putString("long", longitude + "");
        updateGoogleFragment(bundle);
        dbRef.child(uid).child("latitude").setValue(latitude);
        dbRef.child(uid).child("longitude").setValue(longitude);
    }

    private void addEmailToFirebase(String email) {
        dbRef.child(mAuth.getUid()).setValue(email);
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
    }

}