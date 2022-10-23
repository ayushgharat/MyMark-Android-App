package com.example.mymark;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.StringBufferInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference dbRef;
    private FirebaseDatabase database;
    private User user;
    private FirebaseStorage storage;
    private CircleImageView dp_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        database = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        dbRef = database.getReference("Username");
        storage = FirebaseStorage.getInstance();

        dp_view = (CircleImageView) findViewById(R.id.circle_image_profile_picture);

        collectDataFromDatabase();

        // Create a reference to a file from a Google Cloud Storage URI

    }

    private void collectDataFromDatabase() {
        dbRef.child(mAuth.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //user = snapshot.getValue(User.class);
                Log.e(TAG, "onDataChange: " + snapshot.getValue());
                Map<String, Object> map = (HashMap<String, Object>) snapshot.getValue();

                user = new User((String) map.get("email"), (String) map.get("name"), (String) map.get("bio"),process((ArrayList<String>) map.get("interests")), String.valueOf(map.get("latitude")), String.valueOf(map.get("longitude")), String.valueOf(map.get("uri")));
                populateCircleImageView(user.getUri());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void populateCircleImageView(String uri) {
        StorageReference gsReference = storage.getReferenceFromUrl(uri);
        final long ONE_MEGABYTE = 1024 * 1024;
        gsReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                // Data for "images/island.jpg" is returns, use this as needed
                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                dp_view.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
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