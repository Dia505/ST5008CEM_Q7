package com.example.socialmediaapp.Service;

import com.example.socialmediaapp.Dto.LikesDto;

public interface LikeService {
    // Function to save likes done by a user
    String saveLike(LikesDto likesDto);
}
