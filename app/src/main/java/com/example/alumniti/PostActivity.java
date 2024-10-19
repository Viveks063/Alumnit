package com.example.alumniti;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class PostActivity extends AppCompatActivity {
    private static final int IMAGE_PICK_CODE = 1000;
    private Uri imageUri;
    private EditText captionEditText;
    private ImageView selectedImageView;
    private Button selectImageButton;
    private Button postButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);

        captionEditText = findViewById(R.id.captionEditText);
        selectedImageView = findViewById(R.id.selectedImageView);
        selectImageButton = findViewById(R.id.selectImageButton);
        postButton = findViewById(R.id.postButton);

        selectImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, IMAGE_PICK_CODE);
        });

        postButton.setOnClickListener(v -> {
            String caption = captionEditText.getText().toString().trim();
            if (imageUri != null) {
                uploadImage(imageUri, caption);
            } else {
                Toast.makeText(PostActivity.this, "Please select an image", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == IMAGE_PICK_CODE && resultCode == RESULT_OK && data != null) {
            imageUri = data.getData(); // Store the URI in the class variable
            selectedImageView.setImageURI(imageUri);
            selectedImageView.setVisibility(View.VISIBLE);
        }
    }

    private void uploadImage(Uri imageUri, String caption) {
        String postId = String.valueOf(System.currentTimeMillis()); // Unique ID for the post
        StorageReference storageReference = FirebaseStorage.getInstance().getReference("posts/images").child(postId);

        storageReference.putFile(imageUri).addOnSuccessListener(taskSnapshot -> {
            storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                String imageUrl = uri.toString();
                createPost(caption, imageUrl);
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(PostActivity.this, "Image upload failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }

    private void createPost(String caption, String imageUrl) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        long timestamp = System.currentTimeMillis();

        Post post = new Post(userId, caption, imageUrl, timestamp);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("posts");
        reference.push().setValue(post).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(PostActivity.this, "Post created successfully", Toast.LENGTH_SHORT).show();
                finish(); // Close the activity or navigate as needed
            } else {
                Toast.makeText(PostActivity.this, "Post creation failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
