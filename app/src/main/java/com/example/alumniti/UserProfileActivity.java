package com.example.alumniti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class UserProfileActivity extends AppCompatActivity {

    private ImageView profilePic;
    private TextView userName;

    private RecyclerView postsRecyclerView;
    private PostsAdapter postsAdapter;
    private ArrayList<Post> postsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        getSupportActionBar().hide();

        profilePic = findViewById(R.id.profilePic);
        userName = findViewById(R.id.userName);

        postsRecyclerView = findViewById(R.id.postsRecyclerView);



        String userId = getIntent().getStringExtra("uid");
        String username = getIntent().getStringExtra("nameeee");
        String imageUrl = getIntent().getStringExtra("reciverImg");
        String bio = getIntent().getStringExtra("bio");

        userName.setText(username);
        if (imageUrl != null) {
            Glide.with(this).load(imageUrl).into(profilePic);
        }


        postsArrayList = new ArrayList<>();
        postsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        postsAdapter = new PostsAdapter(this, postsArrayList);
        postsRecyclerView.setAdapter(postsAdapter);

        fetchPosts(userId);
    }

    private void fetchPosts(String userId) {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("posts");
        reference.orderByChild("userId").equalTo(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                postsArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Post post = dataSnapshot.getValue(Post.class);
                    if (post != null) {
                        postsArrayList.add(post);
                    }
                }
                postsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UserProfileActivity.this, "Failed to load posts", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
