package com.example.socialmediaapp.Service.ServiceImpl;

import com.example.socialmediaapp.Dto.PostDto;
import com.example.socialmediaapp.Entity.Post;
import com.example.socialmediaapp.Repository.PostRepository;
import com.example.socialmediaapp.Repository.UserRepository;
import com.example.socialmediaapp.Service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Value("${upload.path}")
    private String uploadPath;

    @Override
    public String uploadPost(PostDto postDto) {
        Post post = new Post();

        if(postDto.getPostId() != null) {
            post = postRepository.findById(postDto.getPostId()).orElseThrow(() ->new NullPointerException("error"));
        }
        post.setTitle(postDto.getTitle());
        post.setHashtag(postDto.getHashtag());
        post.setLikes(0);
        post.setUser(userRepository.findById(postDto.getUserId()).orElseThrow(() -> new NullPointerException("User not found")));

        String fileName = UUID.randomUUID().toString()+"_"+postDto.getPostImage().getOriginalFilename();
        Path filePath = Paths.get(uploadPath, fileName);

        String trimmedPath = StringUtils.trimAllWhitespace(filePath.toString());

        try {
            System.out.println("Source Path: " + postDto.getPostImage().getOriginalFilename());
            System.out.println("Destination Path: " + filePath);

            Files.copy(postDto.getPostImage().getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        }
        catch(IOException e) {
            throw new RuntimeException(e);
        }

        post.setPostImage(fileName);
        postRepository.save(post);
        return "Post uploaded";
    }

    @Override
    public List<Post> getAllPosts() {
        return postRepository.findAll();
    }

    @Override
    public List<Post> getPostsByUserId(Integer userId) {
        return postRepository.findPostByUserUserId(userId);
    }

    @Override
    public List<Post> getPostsByHashtag(String hashtag) {
        return postRepository.findPostByHashtag(hashtag);
    }

    @Override
    public void likePost(Integer postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        // Increment the like count
        post.setLikes(post.getLikes() + 1);

        // Save the updated post entity
        postRepository.save(post);
    }
}
