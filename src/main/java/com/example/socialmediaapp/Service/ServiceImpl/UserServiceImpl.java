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
// Separate following matrix class to deal with the adjacency matrix
class FollowingMatrixService {
    public boolean[][] followingMatrix1;

    public FollowingMatrixService() {
        followingMatrix1 = new boolean[100][100];
    }

    public boolean[][] getFollowingMatrix() {
        return followingMatrix1;
    }

    // Function to update the matrix for every follow
    // synchronized used to allow only one thread to execute the method, to avoid any conflicts
    public synchronized void updateFollowingMatrix(int senderIndex, int receiverIndex) {
        // With a receiver accepting a sender's request, they both will follow each other
        followingMatrix1[senderIndex][receiverIndex] = true;
        followingMatrix1[receiverIndex][senderIndex] = true;

        ArrayList matrixList= new ArrayList<>();

        for(int i = 0; i< followingMatrix1.length; i++) {
            for(int j = 0; j<followingMatrix1[0].length; j++) {
                matrixList.add(i + " + " + j + " = " + followingMatrix1[i][j]);
            }

        }
        System.out.println(matrixList);
    }
}

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final FriendRequestRepository friendRequestRepository;
    private final FollowingMatrixService followingMatrixService;

    // Function to save user details
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

    // Function to send friend request
    @Override
    public void sendFriendRequest(User sender, User receiver) {
        FriendRequest friendRequest = new FriendRequest();
        friendRequest.setSender(sender);
        friendRequest.setReceiver(receiver);
        // The status of friend request is kept as "pending" until not accepted
        friendRequest.setStatus("pending");
        friendRequestRepository.save(friendRequest);
    }

    @Override
    @Transactional
    // Function that sets friend request status as accepted and has the users follow each other
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

            // To update the matrix with new follow
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

        // Returning friend list using graph
        int senderIndex = getIndexInFollowingMatrix(user);

        for (int i = 0; i < followingMatrixService.followingMatrix1.length; i++) {
            if (i != senderIndex && followingMatrixService.followingMatrix1[senderIndex][i] && followingMatrixService.followingMatrix1[i][senderIndex]) {
                System.out.println("friend: " + i);
                User friend = userRepository.findById(i).orElseThrow(() -> new RuntimeException("Friend not found"));
                friendList.add(friend);
            }
        }

        // Returning friend list using database
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
