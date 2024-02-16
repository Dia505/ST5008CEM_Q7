package com.example.socialmediaapp.Repository;

import com.example.socialmediaapp.Entity.FriendRequest;
import com.example.socialmediaapp.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FriendRequestRepository extends JpaRepository<FriendRequest, Integer> {
    List<FriendRequest> findFriendRequestByReceiver(User receiver);
}
