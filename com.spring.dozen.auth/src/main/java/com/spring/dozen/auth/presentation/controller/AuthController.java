package com.spring.dozen.auth.presentation.controller;

import com.spring.dozen.auth.application.dto.UserSignUpResponse;
import com.spring.dozen.auth.application.service.AuthService;
import com.spring.dozen.auth.presentation.dto.ApiResponse;
import com.spring.dozen.auth.presentation.dto.UserSignInRequest;
import com.spring.dozen.auth.presentation.dto.UserSignUpRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up")
    public ApiResponse<UserSignUpResponse> signUp(@Valid @RequestBody UserSignUpRequest request) {
        log.info("AuthController.SignUp.UserSignUpRequest: {}", request);
        return ApiResponse.success(authService.signUp(request.toServiceDto()));
    }

    @PostMapping("/sign-in")
    public ApiResponse<String> signIn(@RequestBody UserSignInRequest request) {
        log.info("AuthController.signIn.UserSignInRequest: {}", request);
        return ApiResponse.success(authService.signIn(request.toServiceDto()));
    }

}
