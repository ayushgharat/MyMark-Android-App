package com.example.mymark;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    private FirebaseDatabase database;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        dbRef = database.getReference("Username");

        collectDataFromDatabase();
    }

    private void collectDataFromDatabase() {
        dbRef.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //user = snapshot.getValue(User.class);
                Log.e(TAG, "onDataChange: " + snapshot.getValue());
                Map<String, Object> map = (HashMap<String, Object>) snapshot.getValue();

                user = new User((String) map.get("email"), (String) map.get("name"), (String) map.get("bio"),process((ArrayList<String>) map.get("interests")), String.valueOf(map.get("latitude")), String.valueOf(map.get("longitude")));
                Toast.makeText(ProfileActivity.this, user.getInterests()[3], Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public static String[] process(ArrayList<String> s) {
        String[] toReturn = new String[s.size()];
        for(int i = 0;i < s.size();i++) {
            toReturn[i] = s.get(i);
        }
        return toReturn;
    }
}