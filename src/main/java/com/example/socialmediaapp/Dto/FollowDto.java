package com.example.socialmediaapp.Dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class FollowDto {
    private Integer sender;
    private Integer receiver;
    private Boolean isLike;
}
