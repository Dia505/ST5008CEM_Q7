package com.example.socialmediaapp.Controller;

import com.example.socialmediaapp.Entity.Post;
import com.example.socialmediaapp.Service.ServiceImpl.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/recommend")
public class RecommendationController {
    private final RecommendationService recommendationService;

    @GetMapping("/recommendations/{userId}")
    public ResponseEntity<List<Post>> recommendPosts(@PathVariable("userId") Integer userId) {
        // Call the recommendation service to get recommended posts for the user
        List<Post> recommendedPosts = recommendationService.recommendPostsByHashtag(userId);
        return new ResponseEntity<>(recommendedPosts, HttpStatus.OK);
    }
}
