package com.spring.dozen.auth.presentation.controller;

import com.spring.dozen.auth.application.dto.UserSignUpResponse;
import com.spring.dozen.auth.application.service.AuthService;
import com.spring.dozen.auth.presentation.dto.ApiResponse;
import com.spring.dozen.auth.presentation.dto.UserSignInRequest;
import com.spring.dozen.auth.presentation.dto.UserSignUpRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    /**
     * 회원 존재 여부 검증 API 입니다.
     * ApiResponse 로 감싸지 않고 ResponseEntity 로 감싸서 gateway 에서 곧장 boolean 값을 사용합니다.
     */
    @GetMapping("/verify/{userId}")
    public ResponseEntity<Boolean> verifyUser(@PathVariable("userId") Long userId) {
        log.info("AuthController.verifyUser userId : {}", userId);
        return ResponseEntity.ok(authService.verifyUser(userId));
    }

}
