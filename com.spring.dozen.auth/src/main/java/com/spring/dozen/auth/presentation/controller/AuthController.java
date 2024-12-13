package com.spring.dozen.auth.presentation.controller;

import com.spring.dozen.auth.application.dto.UserSignUpResponse;
import com.spring.dozen.auth.application.exception.AuthErrorCode;
import com.spring.dozen.auth.application.exception.AuthException;
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
        log.info("signUp.UserSignUpRequest: {}", request);
        return ApiResponse.success(authService.signUp(request.toServiceDto()));
    }

    @PostMapping("/sign-up/hub-manager")
    public ApiResponse<UserSignUpResponse> signUpForHubManager(@Valid @RequestBody UserSignUpRequest request,
                                                               @RequestHeader(value = "X-User-Id", required = true) String userId,
                                                               @RequestHeader(value = "X-Role", required = true) String role) {
        log.info("signUpForHubManager.UserSignUpRequest: {}, RequestHeader.userId: {}, RequestHeader.role: {}", request, userId, role);
        if (!"MASTER".equals(role)) {
            throw new AuthException(AuthErrorCode.FORBIDDEN_ACCESS);
        }
        return ApiResponse.success(authService.signUpForHubManager(request.toServiceDto()));
    }

    /**
     * 회원 존재 여부 검증 API 입니다.
     * ApiResponse 로 감싸지 않고 ResponseEntity 로 감싸서 gateway 에서 곧장 boolean 값을 사용합니다.
     */
    @GetMapping("/verify/{userId}")
    public ResponseEntity<Boolean> verifyUser(@PathVariable("userId") Long userId) {
        log.info("verifyUser userId : {}", userId);
        return ResponseEntity.ok(authService.verifyUser(userId));
    }

    @PostMapping("/sign-in")
    public ApiResponse<String> signIn(@RequestBody UserSignInRequest request) {
        log.info("signIn.UserSignInRequest: {}", request);
        return ApiResponse.success(authService.signIn(request.toServiceDto()));
    }

}
