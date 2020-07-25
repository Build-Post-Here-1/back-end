package com.postit.userdata.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "posts")
public class Posts {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long postid;

    private String title;

    @Column(length = 900)
    private String contents;

    @OneToMany(mappedBy = "posts", cascade = CascadeType.ALL)
    @JsonIgnoreProperties(value = "posts")
    private Set<SavedPosts> savedposts = new HashSet<>();

    public Posts(String title, String contents) {
        this.title = title;
        this.contents = contents;
    }

    public Posts() {
    }

    public Set<SavedPosts> getSavedposts() {
        return savedposts;
    }

    public void setSavedposts(Set<SavedPosts> savedPosts) {
        this.savedposts = savedPosts;
    }

    public long getPostid() {
        return postid;
    }

    public void setPostid(long postid) {
        this.postid = postid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }
}