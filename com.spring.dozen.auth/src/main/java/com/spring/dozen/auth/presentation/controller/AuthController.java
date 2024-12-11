package com.spring.dozen.auth.presentation.controller;

import com.spring.dozen.auth.presentation.dto.ApiResponse;
import com.spring.dozen.auth.application.dto.UserSignUpResponseDto;
import com.spring.dozen.auth.application.service.AuthService;
import com.spring.dozen.auth.presentation.dto.UserSignUpRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@RestController
public class AuthController {

    private final AuthService authService;

    @PostMapping("/sign-up/hub-delivery-staff")
    public ApiResponse<UserSignUpResponseDto> signUpHubDeliveryStaff(@RequestBody UserSignUpRequestDto requestDto) {
        return ApiResponse.success(authService.signUp(requestDto.toServiceDto()));
    }

    @PostMapping("/sign-up/company-delivery-staff")
    public ApiResponse<UserSignUpResponseDto> signUpCompanyDeliveryStaff(@RequestBody UserSignUpRequestDto requestDto) {
        return ApiResponse.success(authService.signUp(requestDto.toServiceDto()));
    }

}
