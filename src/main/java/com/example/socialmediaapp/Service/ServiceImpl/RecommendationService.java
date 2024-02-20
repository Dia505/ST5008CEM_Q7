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
// Class to establish recommend system in application
public class RecommendationService {
    private final LikesRepository likesRepository;
    private final PostRepository postRepository;

    public List<Post> recommendPostsByHashtag(Integer userId){
        // Find recommended posts
        List<Post> recommendedPosts = findRecommendedPosts(userId);

        // Filter out user's own posts from recommended posts
        recommendedPosts = recommendedPosts.stream()
                .filter(post -> !post.getUser().getUserId().equals(userId))
                .collect(Collectors.toList());

        return recommendedPosts;
    }

    // Function to return recommended posts based on a users likes for particular hashtags
    private List<Post> findRecommendedPosts(Integer userId) {
        // Return likes by user id
        List<Likes> userLikedPosts = likesRepository.findLikesByUserUserId(userId);
        // Collect common hashtags liked by user
        List<String> commonHashtags = analyzeLikedPosts(userLikedPosts);

        // Find posts with similar hashtags
        return findSimilarPostsByHashtags(commonHashtags);
    }

    // Function to determine the hashtags preferred by user
    private List<String> analyzeLikedPosts(List<Likes> userLikedPosts) {
        // Hash map where key:hashtag and value:count of occurrence of each hashtag
        Map<String, Integer> hashtagCountMap = new HashMap<>();

        // Iterate over the list of posts liked by user
        for (Likes like : userLikedPosts) {
            // Retrieve posts via like id by that user
            Post post = like.getPost();
            // Retrieve hashtag of that post
            String hashtag = post.getHashtag();
            // Update of hash map by putting the key and value pairs
            // If the hashtag is new for hashmap, the count is initialized as 1
            // else, the count is incremented by 1
            hashtagCountMap.put(hashtag, hashtagCountMap.getOrDefault(hashtag, 0) + 1);
        }

        // Hashmap converted to a stream of map entries. Every entry represents the hashtag - count pair
        // This conversion facilitates sorting, mapping and collecting data in desired format
        return hashtagCountMap.entrySet().stream()
                // Map entries are sort in descending order, depending on values
                // So the hashtags with most likes come first
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                // To extract keys (hashtag) from map entries
                .map(Map.Entry::getKey)
                // Creates list of the hashtags
                .collect(Collectors.toList());
    }

    // Method to find posts with similar hashtags
    private List<Post> findSimilarPostsByHashtags(List<String> hashtags) {
        List<Post> similarHashtags = new ArrayList<>();
        // Based on provided hashtag list, iterate over it and collect posts with those hashtags
        for (String hashtag : hashtags) {
            similarHashtags.addAll(postRepository.findPostByHashtag(hashtag));
        }
        return similarHashtags;
    }

}
