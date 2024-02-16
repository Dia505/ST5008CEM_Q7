package com.example.socialmediaapp.Service.ServiceImpl;

import com.example.socialmediaapp.Dto.UserDto;
import com.example.socialmediaapp.Entity.FriendRequest;
import com.example.socialmediaapp.Entity.User;
import com.example.socialmediaapp.Repository.FriendRequestRepository;
import com.example.socialmediaapp.Repository.UserRepository;
import com.example.socialmediaapp.Service.UserService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
    public Optional<User> findUserById(Integer userId) {
        return userRepository.findById(userId);
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

            User user = userRepository.findById(sender.getUserId()).orElseThrow(() -> new RuntimeException("User not found"));
            updateFollowingMatrix(user, receiver);
        }
        else {
            System.out.println("The friend request does not exist");
        }
    }

    private void updateFollowingMatrix(User user, User receiver) {
        boolean followingMatrix[][] = user.getFollowingMatrix();
        int senderIndex = getIndexInFollowingMatrix(user);
        int receiverIndex = getIndexInFollowingMatrix(receiver);

        if (senderIndex != -1 && receiverIndex != -1) {
            followingMatrix[senderIndex][receiverIndex] = true;
            followingMatrix[receiverIndex][senderIndex] = true;
            user.setFollowingMatrix(followingMatrix);

            for(int i = 0; i<4; i++) {
                for(int j = 0; j<4; j++) {
                    System.out.print(i + " + " + j + " = " + followingMatrix[i][j]);
                }
                System.out.println();
            }

            userRepository.save(user);
        }
    }

    private int getIndexInFollowingMatrix(User user) {
        return user.getUserId();
    }

    @Override
    public List<FriendRequest> getFriendRequestByReceiver(User receiver) {
        return friendRequestRepository.findFriendRequestByReceiver(receiver);
    }

    @Override
    @Transactional
    public List<User> findFriends(Integer userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        boolean followingMatrix[][] = user.getFollowingMatrix();
        List<User> friendList = new ArrayList<>();

        int senderIndex = getIndexInFollowingMatrix(user);

        for (int i = 0; i < followingMatrix.length; i++) {
            if (followingMatrix[senderIndex][i]) {
                System.out.println("friend: " + i);
                User friend = userRepository.findById(i).orElseThrow(() -> new RuntimeException("Friend not found"));
                friendList.add(friend);
            }
        }

        System.out.println(friendList);
        return friendList;
    }
}
