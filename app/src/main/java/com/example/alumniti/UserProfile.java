package com.example.alumniti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserProfile extends AppCompatActivity {

    private ImageView profilePic;
    private TextView userName, userBio;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        profilePic = findViewById(R.id.profilePic);
        userName = findViewById(R.id.userName);
        userBio = findViewById(R.id.userBio);

        // Assuming you pass the user ID through Intent
        String userId = getIntent().getStringExtra("USER_ID");

        // Check if userId is not null
        if (userId != null) {
            databaseReference = FirebaseDatabase.getInstance().getReference("user").child(userId);
            loadUserProfile();
        } else {
            // Handle the case where userId is null
            finish(); // Optionally finish the activity if no user ID is provided
        }
    }

    private void loadUserProfile() {
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User user = snapshot.getValue(User.class);
                if (user != null) {
                    userName.setText(user.getName());
                    userBio.setText(user.getBio());
                    Glide.with(UserProfile.this).load(user.getProfilePicUrl()).into(profilePic);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });
    }
}
