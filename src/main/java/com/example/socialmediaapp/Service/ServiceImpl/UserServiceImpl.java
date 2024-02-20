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
class FollowingMatrixService {
    public boolean[][] followingMatrix1;

    public FollowingMatrixService() {
        followingMatrix1 = new boolean[100][100];
    }

    public boolean[][] getFollowingMatrix() {
        return followingMatrix1;
    }

    public synchronized void updateFollowingMatrix(int senderIndex, int receiverIndex) {
        followingMatrix1[senderIndex][receiverIndex] = true;
        followingMatrix1[receiverIndex][senderIndex] = true;

        ArrayList list= new ArrayList<>();

        for(int i = 0; i< followingMatrix1.length; i++) {
            for(int j = 0; j<followingMatrix1[0].length; j++) {
                list.add(i + " + " + j + " = " + followingMatrix1[i][j]);
            }

        }
        System.out.println(list);
    }
}

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final FollowingMatrixService followingMatrixService;

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
    @Transactional
    public void follow(User sender, User receiver, Integer requestId) {
        Optional<FriendRequest> friendRequestOptional = friendRequestRepository.findById(requestId);
        if (friendRequestOptional.isPresent()) {
            FriendRequest friendRequest = friendRequestOptional.get();
            friendRequest.setStatus("accepted");
            friendRequestRepository.save(friendRequest);

            // Create a new friend request from receiver to sender with accepted status
            FriendRequest friendRequestReverse = new FriendRequest();
            friendRequestReverse.setSender(receiver);
            friendRequestReverse.setReceiver(sender);
            friendRequestReverse.setStatus("accepted");
            friendRequestRepository.save(friendRequestReverse);

            int senderIndex = getIndexInFollowingMatrix(sender);
            int receiverIndex = getIndexInFollowingMatrix(receiver);
            followingMatrixService.updateFollowingMatrix(senderIndex, receiverIndex);
            userRepository.save(sender);
        } else {
            System.out.println("The friend request does not exist");
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
        List<User> friendList = new ArrayList<>();

        // Using graph
        int senderIndex = getIndexInFollowingMatrix(user);

        for (int i = 0; i < followingMatrixService.followingMatrix1.length; i++) {
            if (i != senderIndex && followingMatrixService.followingMatrix1[senderIndex][i] && followingMatrixService.followingMatrix1[i][senderIndex]) {
                System.out.println("friend: " + i);
                User friend = userRepository.findById(i).orElseThrow(() -> new RuntimeException("Friend not found"));
                friendList.add(friend);
            }
        }

        // Using database
        List<FriendRequest> friendRequests = friendRequestRepository.findFriendRequestBySender(user);
        for (FriendRequest request : friendRequests) {
            if ("accepted".equals(request.getStatus())) {
                if (!request.getSender().equals(user)) {
                    friendList.add(request.getSender());
                }
                else if (!request.getReceiver().equals(user)) {
                    friendList.add(request.getReceiver());
                }
            }
        }

        System.out.println(friendList);
        return friendList;
    }

}
