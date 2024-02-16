package com.example.socialmediaapp.Service;

import com.example.socialmediaapp.Dto.UserDto;
import com.example.socialmediaapp.Entity.FriendRequest;
import com.example.socialmediaapp.Entity.User;

import java.util.List;

public interface UserService {
    String saveUser(UserDto userDto);
    List<User> getAllUsers();
    List<User> findUserByFullName(String fullName);
    void sendFriendRequest(User sender, User receiver);
    void follow(User sender, User receiver, Integer requestId);
    List<FriendRequest> getFriendRequestByReceiver(User receiver);
}
