package com.example.socialmediaapp.Controller;

import com.example.socialmediaapp.Dto.PostDto;
import com.example.socialmediaapp.Entity.Post;
import com.example.socialmediaapp.Service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @PostMapping("/upload-post")
    public String insertData(@ModelAttribute PostDto postDto) {
        System.out.println(postDto);
        postService.uploadPost(postDto);
        return "Data saved";
    }

    @GetMapping("/get-all-posts")
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    @GetMapping("/get-post-by-userId/{userId}")
    public List<Post> getPostByUserId(@PathVariable("userId") Integer userId) {
        return postService.getPostsByUserId(userId);
    }

    @GetMapping("/get-post-by-hashtag/{hashtag}")
    public List<Post> getPostByHashtag(@PathVariable("hashtag") String hashtag) {
        return postService.getPostsByHashtag(hashtag);
    }

    // To save like received by a post
    @PostMapping("/like/{postId}")
    public ResponseEntity<String> likePost(@PathVariable("postId") Integer postId) {
        postService.likePost(postId);
        return ResponseEntity.ok("Post liked successfully");
    }
}
