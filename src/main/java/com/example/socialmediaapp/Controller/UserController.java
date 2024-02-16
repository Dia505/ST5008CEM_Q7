package com.example.socialmediaapp.Controller;

import com.example.socialmediaapp.Dto.UserDto;
import com.example.socialmediaapp.Entity.FriendRequest;
import com.example.socialmediaapp.Entity.User;
import com.example.socialmediaapp.Repository.UserRepository;
import com.example.socialmediaapp.Service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;
    private final UserRepository userRepository;

    public UserController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @PostMapping("/save-user")
    // Parameter for the data entered from front end
    public String createData(@RequestBody UserDto userDto) {
        userService.saveUser(userDto);
        return "Created data";
    }

    @GetMapping("/get-all-users")
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/get-user-by-name/{fullName}")
    public List<User> getUserByName(@PathVariable("fullName") String fullName) {
        return userService.findUserByFullName(fullName);
    }

    @GetMapping("/get-user-by-id/{userId}")
    public Optional<User> getUserById(@PathVariable("userId") Integer userId) {
        return userService.findUserById(userId);
    }

    @PostMapping("/send-friend-request/{senderId}/{receiverId}")
    public ResponseEntity<String> sendFriendRequest(@PathVariable Integer senderId, @PathVariable Integer receiverId) {
        User sender = userRepository.findById(senderId).orElseThrow();
        User receiver = userRepository.findById(receiverId).orElseThrow();
        userService.sendFriendRequest(sender, receiver);
        return ResponseEntity.ok("Friend request sent successfully.");
    }

    @PostMapping("/follow/{senderId}/{receiverId}/{requestId}")
    public ResponseEntity<String> followUser(@PathVariable Integer senderId, @PathVariable Integer receiverId, @PathVariable("requestId") Integer requestId) {
        User sender = userRepository.findById(senderId).orElseThrow();
        User receiver = userRepository.findById(receiverId).orElseThrow();
        userService.follow(sender, receiver, requestId);
        return ResponseEntity.ok("User followed successfully.");
    }

    @GetMapping("/get-friend-requests-by-receiver/{receiverId}")
    public List<FriendRequest> getFriendRequestsByReceiver(@PathVariable Integer receiverId) {
        User receiver = userRepository.findById(receiverId).orElseThrow();
        return userService.getFriendRequestByReceiver(receiver);
    }

    @GetMapping("/get-friend-list/{userId}")
    public ResponseEntity<List<User>> getUserFriends(@PathVariable Integer userId) {
        List<User> friendList = userService.findFriends(userId);
        return ResponseEntity.ok(friendList);
    }
}
