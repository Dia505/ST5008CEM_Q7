package com.example.socialmediaapp.Controller;

import com.example.socialmediaapp.Dto.LoginDto;
import com.example.socialmediaapp.Service.LoginService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class LoginController {
    private final LoginService loginService;

    @PostMapping("/login")
    public ResponseEntity<Integer> login(@RequestBody LoginDto loginDto) {
        Integer userId = loginService.login(loginDto);
        return ResponseEntity.ok(userId);
    }
}
