package com.example.socialmediaapp.Controller;

import com.example.socialmediaapp.Dto.PostDto;
import com.example.socialmediaapp.Entity.Post;
import com.example.socialmediaapp.Repository.PostRepository;
import com.example.socialmediaapp.Service.PostService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/post")
public class PostController {
    private final PostRepository postRepository;
    private final PostService postService;

    public PostController(PostRepository postRepository, PostService postService) {
        this.postRepository = postRepository;
        this.postService = postService;
    }

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
}
