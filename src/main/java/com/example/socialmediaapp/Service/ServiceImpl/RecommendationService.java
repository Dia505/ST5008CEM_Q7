package com.example.socialmediaapp.Service.ServiceImpl;

import com.example.socialmediaapp.Entity.Likes;
import com.example.socialmediaapp.Entity.Post;
import com.example.socialmediaapp.Repository.LikesRepository;
import com.example.socialmediaapp.Repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final LikesRepository likesRepository;
    private final PostRepository postRepository;

    public List<Post> recommendPostsByHashtag(Integer userId){
        // Find recommended posts
        List<Post> recommendedPosts = findRecommendedPosts(userId);

        // Filter out user's own posts from recommended posts
        recommendedPosts = recommendedPosts.stream()
                .filter(post -> !post.getUser().getUserId().equals(userId)) // Filter out user's own posts
                .collect(Collectors.toList());

        return recommendedPosts;
    }

    private List<Post> findRecommendedPosts(Integer userId) {
        // Find user's liked posts and analyze hashtags
        List<Likes> userLikedPosts = likesRepository.findLikesByUserUserId(userId);
        List<String> commonHashtags = analyzeLikedPosts(userLikedPosts);

        // Find posts with similar hashtags
        return findSimilarPostsByHashtags(commonHashtags);
    }

    private List<String> analyzeLikedPosts(List<Likes> userLikedPosts) {
        Map<String, Integer> hashtagCountMap = new HashMap<>();
        for (Likes like : userLikedPosts) {
            Post post = like.getPost();
            String hashtag = post.getHashtag();
            hashtagCountMap.put(hashtag, hashtagCountMap.getOrDefault(hashtag, 0) + 1);
        }

        return hashtagCountMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }

    // Method to find posts with similar hashtags
    private List<Post> findSimilarPostsByHashtags(List<String> hashtags) {
        // Query posts with hashtags similar to the ones in the list
        List<Post> similarHashtags = new ArrayList<>();
        for (String hashtag : hashtags) {
            similarHashtags.addAll(postRepository.findPostByHashtag(hashtag));
        }
        return similarHashtags;
    }

}
