package com.example.mymark;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class DatabaseAction {
    private FirebaseDatabase database;
    private DatabaseReference dbRef;
    private FirebaseAuth mAuth;
    private String uid;
    public String[] myinterest;
    public String[] othersinterest;
    String name="";
    int commoninterests=0;
    int tempcommoninterests=0;
    User user;

    public DatabaseAction(String uid){
        this.database = FirebaseDatabase.getInstance();
        this.mAuth = FirebaseAuth.getInstance();
        this.dbRef = database.getReference("Username");
        this.uid = uid;
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

    public void findUsers(String uid, double latitude, double longitude, SecondCallback secondCallback) throws IOException {


        dbRef.orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ArrayList<String> a=new ArrayList<String>();
                for (DataSnapshot dsp : snapshot.getChildren()){

                    Map<String, Object> data = (Map<String, Object>) dsp.getValue();
                    double newlatitude=Double.parseDouble(data.get("latitude").toString().trim());
                    double newlongitude=Double.parseDouble(data.get("longitude").toString().trim());
                    String n=data.get("uid").toString().trim();

                    Log.e("user",n+" "+uid);
                    if(uid.trim().equals(n.trim()))//&& distance(latitude,longitude,newlatitude,newlongitude)*1609.34<=1000 && findCommonInterests(findInterests(uid),findInterests(n))>commoninterests)
                    {
                    } else{
                        if (distance(latitude,longitude,newlatitude,newlongitude)*1609.34<=1000){
                            a.add(n);
                        }
                    }

                    }
                Log.e("wow",a.toString());
                secondCallback.onCallback(a);


                }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    private double distance(double lat1, double lon1, double lat2, double lon2) {
        double theta = lon1 - lon2;
        double dist = Math.sin(deg2rad(lat1))
                * Math.sin(deg2rad(lat2))
                + Math.cos(deg2rad(lat1))
                * Math.cos(deg2rad(lat2))
                * Math.cos(deg2rad(theta));
        dist = Math.acos(dist);
        dist = rad2deg(dist);
        dist = dist * 60 * 1.1515;
        return (dist);
    }

    private double deg2rad(double deg) {
        return (deg * Math.PI / 180.0);
    }

    private double rad2deg(double rad) {
        return (rad * 180.0 / Math.PI);
    }
    private int findCommonInterests(String[] arr1, String[] arr2)
    {
        int c=0;
        for (int i = 0; i < arr1.length; i++) {
            for (int j = 0; j < arr2.length; j++) {
                if (arr1[i] == arr2[j]) {

                    // add common elements
                    c++;
                }
            }
        }
        return c;
    }
    public String[] findInterests(String uid, MyCallback myCallback)
    {
        dbRef.orderByChild("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dsp : snapshot.getChildren()){
                    Map<String, Object> data = (Map<String, Object>) dsp.getValue();
                    String n=data.get("uid").toString().trim();
                    if(uid.trim().equals(n.trim()))//&& distance(latitude,longitude,newlatitude,newlongitude)*1609.34<=1000 && findCommonInterests(findInterests(uid),findInterests(n))>commoninterests)
                    {
                        Log.e("name",uid);
                        myinterest=process((ArrayList<String>) data.get("interests"));
                        //user = new User((String) data.get("email"), (String) data.get("name"), (String) data.get("bio"),process((ArrayList<String>) data.get("interests")), String.valueOf(data.get("latitude")), String.valueOf(data.get("longitude")), String.valueOf(data.get("uri")), String.valueOf("uid"));
                        //Log.e("interests", Arrays.toString(myinterest));
                        //setMyInterests(myinterest);
                        myCallback.onCallback(myinterest);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return myinterest;
    }
    public static String[] process(ArrayList<String> s) {
        String[] toReturn = new String[s.size()];
        for(int i = 0;i < s.size();i++) {
            toReturn[i] = s.get(i);
        }
        return toReturn;
    }

}
