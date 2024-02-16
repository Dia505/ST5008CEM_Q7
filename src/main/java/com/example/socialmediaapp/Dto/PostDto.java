package com.example.socialmediaapp.Dto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Integer postId;
    private MultipartFile postImage;
    private String title;
    private String hashtag;
    private Integer likes;
    private Integer userId;
}
