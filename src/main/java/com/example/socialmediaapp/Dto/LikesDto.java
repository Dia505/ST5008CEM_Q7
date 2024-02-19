package com.example.socialmediaapp.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LikesDto {
    private Integer likeId;
    private Integer userId;
    private Integer postId;
}
