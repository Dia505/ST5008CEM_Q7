package com.example.socialmediaapp.Controller;

import com.example.socialmediaapp.Dto.LikesDto;
import com.example.socialmediaapp.Service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/like")
public class LikeController {
    private final LikeService likeService;

    @PostMapping("/save-like")
    public ResponseEntity<String> saveLike(@RequestBody LikesDto likesDto) {
        String response = likeService.saveLike(likesDto);
        return ResponseEntity.ok(response);
    }
}
