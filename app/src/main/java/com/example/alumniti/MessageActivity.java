package com.example.alumniti;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MessageActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private RecyclerView mainUserRecyclerView;
    private UserAdpter adapter;
    private FirebaseDatabase database;
    private ArrayList<Users> usersArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        getSupportActionBar().hide();

        FirebaseApp.initializeApp(this);
        auth = FirebaseAuth.getInstance();

        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(MessageActivity.this, login.class));
            finish();
            return;
        }

        initializeUI();
        setupRecyclerView();
        fetchDataFromDatabase();
    }

    private void initializeUI() {
        database = FirebaseDatabase.getInstance();
        mainUserRecyclerView = findViewById(R.id.mainUserRecyclerView);
    }

    private void setupRecyclerView() {
        usersArrayList = new ArrayList<>();
        mainUserRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new UserAdpter(this, usersArrayList);
        mainUserRecyclerView.setAdapter(adapter);
    }

    private void fetchDataFromDatabase() {
        DatabaseReference reference = database.getReference("user");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                usersArrayList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Users user = dataSnapshot.getValue(Users.class);
                    if (user != null) {
                        usersArrayList.add(user);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
