package com.example.socialmediaapp.Service.ServiceImpl;

import com.example.socialmediaapp.Dto.LikesDto;
import com.example.socialmediaapp.Entity.Likes;
import com.example.socialmediaapp.Repository.LikesRepository;
import com.example.socialmediaapp.Repository.PostRepository;
import com.example.socialmediaapp.Repository.UserRepository;
import com.example.socialmediaapp.Service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeServiceImpl implements LikeService {
    private final LikesRepository likesRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;

    @Override
    public String saveLike(LikesDto likesDto) {
        Likes like = new Likes();
        like.setUser(userRepository.findById(likesDto.getUserId()).orElseThrow(() -> new NullPointerException("User not found")));
        like.setPost(postRepository.findById(likesDto.getPostId()).orElseThrow(() -> new NullPointerException("Post not found")));
        likesRepository.save(like);
        return "Like saved successfully";
    }
}
