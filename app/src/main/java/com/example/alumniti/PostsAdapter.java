package com.example.alumniti;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class PostsAdapter extends RecyclerView.Adapter<PostsAdapter.ViewHolder> {
    private Context context;
    private ArrayList<Post> postsArrayList;

    public PostsAdapter(Context context, ArrayList<Post> postsArrayList) {
        this.context = context;
        this.postsArrayList = postsArrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_post, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Post post = postsArrayList.get(position);

        // Set the post content (caption)
        holder.postContent.setText(post.getContent());

       // holder.likeCount.setText(String.valueOf(post.getLikes().size()));

        // Check if the post has an image
        if (post.getImageUrl() != null && !post.getImageUrl().isEmpty()) {
            holder.postImage.setVisibility(View.VISIBLE);
            Glide.with(context).load(post.getImageUrl()).into(holder.postImage);
        } else {
            holder.postImage.setVisibility(View.GONE);
        }

        // Set the timestamp
        holder.postTimestamp.setText(formatTimestamp(post.getTimestamp()));

        // Handle like button click
        holder.likeButton.setOnClickListener(v -> {
            toggleLike(post, holder);
        });

        // Handle share button click
        holder.shareButton.setOnClickListener(v -> {
            sharePost(post);
        });
    }

    private void toggleLike(Post post, ViewHolder holder) {
        String userId = getCurrentUserId(); // Implement this to get the current user ID
        if (post.getLikes().contains(userId)) {
            post.getLikes().remove(userId); // Remove like
            holder.likeButton.clearColorFilter(); // Reset to original
        } else {
            post.getLikes().add(userId); // Add like
            post.child("likes").setValue(post.getLikes());
            holder.likeButton.setColorFilter(context.getResources().getColor(R.color.purple_200), android.graphics.PorterDuff.Mode.SRC_IN);
        }
        notifyDataSetChanged(); // Refresh UI
    }

    private void sharePost(Post post) {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, post.getContent());
        context.startActivity(Intent.createChooser(shareIntent, "Share Post"));
    }

    private String formatTimestamp(long timestamp) {
        return new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date(timestamp));
    }

    @Override
    public int getItemCount() {
        return postsArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView likeButton; // Change to ImageView
        public View commentButton; // Added for comment button
        public View shareButton; // Added for share button
        TextView postContent;
        ImageView postImage;
        TextView postTimestamp;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            postContent = itemView.findViewById(R.id.postContent);
            postImage = itemView.findViewById(R.id.postImage);
            postTimestamp = itemView.findViewById(R.id.postTimestamp);
            likeButton = itemView.findViewById(R.id.likebutton); // Initialize like button
            commentButton = itemView.findViewById(R.id.commentbutton); // Initialize comment button
            shareButton = itemView.findViewById(R.id.sharebutton); // Initialize share button
        }
    }

    private String getCurrentUserId() {
        // Implement this to return the current user's ID
        return "currentUserId"; // Placeholder
    }
}
