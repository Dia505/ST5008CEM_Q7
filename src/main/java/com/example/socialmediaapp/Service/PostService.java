package com.example.socialmediaapp.Service;

import com.example.socialmediaapp.Dto.PostDto;
import com.example.socialmediaapp.Entity.Post;

import java.util.List;

public interface PostService {
    String uploadPost(PostDto postDto);
    List<Post> getAllPosts();
    List<Post> getPostsByUserId(Integer userId);
    List<Post> getPostsByHashtag(String hashtag);
    void likePost(Integer postId);
}
