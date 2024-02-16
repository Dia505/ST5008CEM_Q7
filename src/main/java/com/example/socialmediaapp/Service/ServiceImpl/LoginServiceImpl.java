package com.example.socialmediaapp.Service.ServiceImpl;

import com.example.socialmediaapp.Dto.LoginDto;
import com.example.socialmediaapp.Entity.User;
import com.example.socialmediaapp.Repository.UserRepository;
import com.example.socialmediaapp.Service.LoginService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class LoginServiceImpl implements LoginService {
    private final UserRepository userRepository;

    public LoginServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Integer login(LoginDto loginDto) {
        Optional<User> userOptional = userRepository.getUserByEmail(loginDto.getEmail());

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (user.getPassword().equals(loginDto.getPassword())) {
                return user.getUserId(); // Return the user ID upon successful login
            } else {
                throw new RuntimeException("Invalid password"); // Throw an exception for invalid password
            }
        } else {
            throw new RuntimeException("User not found"); // Throw an exception for user not found
        }
    }
}
