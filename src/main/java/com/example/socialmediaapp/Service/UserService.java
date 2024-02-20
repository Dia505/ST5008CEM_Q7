package com.example.socialmediaapp.Service;

import com.example.socialmediaapp.Dto.UserDto;
import com.example.socialmediaapp.Entity.FriendRequest;
import com.example.socialmediaapp.Entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    String saveUser(UserDto userDto);
    List<User> getAllUsers();
    List<User> findUserByFullName(String fullName);
    Optional<User> findUserById(Integer userId);
    // Function to send friend requests
    void sendFriendRequest(User sender, User receiver);
    // Function to accept friend requests
    void follow(User sender, User receiver, Integer requestId);
    // Function to return friend requests received by a user
    List<FriendRequest> getFriendRequestByReceiver(User receiver);
    // Function to return list of friends of a user
    List<User> findFriends(Integer userId);
}
