package com.example.socialmediaapp.Service.ServiceImpl;

import com.example.socialmediaapp.Dto.UserDto;
import com.example.socialmediaapp.Entity.FriendRequest;
import com.example.socialmediaapp.Entity.User;
import com.example.socialmediaapp.Repository.FriendRequestRepository;
import com.example.socialmediaapp.Repository.UserRepository;
import com.example.socialmediaapp.Service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;

    @Override
    public String saveUser(UserDto userDto) {
        User user = new User();
        user.setEmail(userDto.getEmail());
        user.setFullName(userDto.getFullName());
        user.setAddress(userDto.getAddress());
        user.setPassword(userDto.getPassword());

        userRepository.save(user);

        return "Account created";
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public List<User> findUserByFullName(String fullName) {
        return userRepository.findUserByFullName(fullName);
    }

    @Override
    public void sendFriendRequest(User sender, User receiver) {
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(sender);
        friendRequest.setReceiver(receiver);
        friendRequest.setStatus("pending");
        friendRequestRepository.save(friendRequest);
    }

    @Override
    public void follow(User sender, User receiver, Integer requestId) {
        Optional<FriendRequest> friendRequestOptional = friendRequestRepository.findById(requestId);
        if (friendRequestOptional.isPresent()) {
            FriendRequest friendRequest = friendRequestOptional.get();
            friendRequest.setStatus("accepted");
            friendRequestRepository.save(friendRequest);

            sender.follow(receiver);
        } else {
            System.out.println("The friend request does not exist");
        }
    }

    @Override
    public List<FriendRequest> getFriendRequestByReceiver(User receiver) {
        return friendRequestRepository.findFriendRequestByReceiver(receiver);
    }
}
