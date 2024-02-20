package com.example.socialmediaapp.Service;

import com.example.socialmediaapp.Dto.PostDto;
import com.example.socialmediaapp.Entity.Post;

import java.util.List;

public interface PostService {
    String uploadPost(PostDto postDto);
    List<Post> getAllPosts();
    // Function to return posts posted by a user
    List<Post> getPostsByUserId(Integer userId);
    List<Post> getPostsByHashtag(String hashtag);
    // Function to save likes of a post
    void likePost(Integer postId);
}
