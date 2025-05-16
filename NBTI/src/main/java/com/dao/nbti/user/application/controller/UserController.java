package com.dao.nbti.user.application.controller;

import com.dao.nbti.common.dto.ApiResponse;
import com.dao.nbti.user.application.dto.UserCreateRequest;
import com.dao.nbti.user.application.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private UserService userService;
    @GetMapping("/signup")
    public ResponseEntity<ApiResponse<Void>> signup(UserCreateRequest userCreateRequest){
        userService.createUser(userCreateRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(null));
    }
}
