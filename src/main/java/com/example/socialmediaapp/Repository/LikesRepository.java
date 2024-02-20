package com.example.socialmediaapp.Repository;

import com.example.socialmediaapp.Entity.Likes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikesRepository extends JpaRepository<Likes, Integer> {
    List<Likes> findLikesByUserUserId(Integer userId);
}
