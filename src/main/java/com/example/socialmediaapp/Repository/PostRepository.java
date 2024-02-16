package com.example.socialmediaapp.Repository;

import com.example.socialmediaapp.Entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findPostByHashtag(String hashtag);
    List<Post> findPostByUserUserId(Integer userId);
}
