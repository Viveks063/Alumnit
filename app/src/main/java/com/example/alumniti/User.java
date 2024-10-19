package com.example.alumniti;

import java.util.List;

public class User {
    private String bio;
    private String profilePicUrl;

    private String name;
    private List<String> posts;

    // Required public no-argument constructor
    public User() {
    }

    // Getters and setters
    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getProfilePicUrl() {
        return profilePicUrl;
    }

    public void setProfilePicUrl(String profilePicUrl) {
        this.profilePicUrl = profilePicUrl;
    }

    public List<String> getPosts() {
        return posts;
    }

    public void setPosts(List<String> posts) {
        this.posts = posts;
    }

    public String getName() {
        return name;
    }
}

