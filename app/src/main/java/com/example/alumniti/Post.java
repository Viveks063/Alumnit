package com.example.alumniti;

import org.w3c.dom.Comment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
z
public class Post {
    private String userId;
    private String content;
    private String imageUrl;
    private long timestamp;
    private List<String> likes; // List of user IDs who liked the post
    private List<Comment> comments; // List of comments on the post

    // Default constructor required for calls to DataSnapshot.getValue(Post.class)
    public Post() {
        this.likes = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    public Post(String userId, String content, String imageUrl, long timestamp) {
        this.userId = userId;
        this.content = content;
        this.imageUrl = imageUrl;
        this.timestamp = timestamp;
        this.likes = new ArrayList<>();
        this.comments = new ArrayList<>();
    }

    public String getUserId() {
        return userId;
    }

    public String getContent() {
        return content;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public List<String> getLikes() {
        return likes;
    }

    public List<Comment> getComments() {
        return comments;
    }

    // Method to add a like
    public void addLike(String userId) {
        if (!likes.contains(userId)) {
            likes.add(userId);
        }
    }

    // Method to remove a like
    public void removeLike(String userId) {
        likes.remove(userId);
    }

    // Method to add a comment
    public void addComment(Comment comment) {
        comments.add(comment);
    }

    public <K> Map.Entry<K, List<String>> child(String likes) {
    }
}
