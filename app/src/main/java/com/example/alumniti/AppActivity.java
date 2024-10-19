package com.example.alumniti;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class AppActivity extends AppCompatActivity {

    ImageView imglogout, cumbut, setbut, connect, like, comment, share;
    private RecyclerView recyclerView;
    private PostsAdapter postsAdapter;
    private ArrayList<Post> postsArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app);
        getSupportActionBar().hide();

        cumbut = findViewById(R.id.jonBut);
        setbut = findViewById(R.id.settingBut);
        imglogout = findViewById(R.id.logoutimg);
        connect = findViewById(R.id.connect);
        like = findViewById(R.id.likebutton);
        comment = findViewById(R.id.commentbutton);
        share = findViewById(R.id.sharebutton);



        recyclerView = findViewById(R.id.postsrecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        postsArrayList = new ArrayList<>();
        postsAdapter = new PostsAdapter(this, postsArrayList);
        recyclerView.setAdapter(postsAdapter);


        fetchPostsFromFirebase();

        setupListeners();
    }

    private void fetchPostsFromFirebase() {
        DatabaseReference postsRef = FirebaseDatabase.getInstance().getReference("posts");
        postsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                postsArrayList.clear();
                for (DataSnapshot postSnapshot : dataSnapshot.getChildren()) {
                    Post post = postSnapshot.getValue(Post.class);
                    if (post != null) {
                        postsArrayList.add(post);
                    }
                }
                postsArrayList.sort((post1, post2) -> Long.compare(post2.getTimestamp(), post1.getTimestamp()));
                postsAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setupListeners() {
        imglogout.setOnClickListener(v -> {
            Dialog dialog = new Dialog(AppActivity.this, R.style.dialoge);
            dialog.setContentView(R.layout.dialog_layout);
            Button no = dialog.findViewById(R.id.nobnt);
            Button yes = dialog.findViewById(R.id.yesbnt);
            yes.setOnClickListener(v1 -> {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(AppActivity.this, login.class));
                finish();
            });
            no.setOnClickListener(v12 -> dialog.dismiss());
            dialog.show();
        });

        if (FirebaseAuth.getInstance().getCurrentUser() == null) {
            startActivity(new Intent(AppActivity.this, login.class));
            finish();
            return;
        }

        setbut.setOnClickListener(v -> {
            Intent intent = new Intent(AppActivity.this, PostActivity.class);
            startActivity(intent);
        });

        connect.setOnClickListener(v -> {
            Intent intent = new Intent(AppActivity.this, MessageActivity.class);
            startActivity(intent);
        });

        cumbut.setOnClickListener(v -> {
            Intent intent = new Intent(AppActivity.this, JobActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 10 && resultCode == RESULT_OK) {

        }
    }
}
