package com.spring.dozen.auth.presentation.controller;

import com.spring.dozen.auth.application.dto.UserSlackIdsResponse;
import com.spring.dozen.auth.application.dto.UserUpdateResponse;
import com.spring.dozen.auth.application.exception.AuthErrorCode;
import com.spring.dozen.auth.application.exception.AuthException;
import com.spring.dozen.auth.application.service.UserService;
import com.spring.dozen.auth.presentation.dto.ApiResponse;
import com.spring.dozen.auth.presentation.dto.UserUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/users")
@RestController
public class UserController {

    private final UserService userService;

    @PutMapping("/{userId}")
    public ApiResponse<UserUpdateResponse> updateUser(@PathVariable("userId") Long userId,
                                                      @Valid @RequestBody UserUpdateRequest request,
                                                      @RequestHeader(value = "X-User-Id", required = true) String requestUserId,
                                                      @RequestHeader(value = "X-Role", required = true) String role) {
        log.info("updateUser.UserSignUpRequest: {}, userId: {}, RequestHeader.userId: {}, RequestHeader.role: {}", request, userId, requestUserId, role);
        if (!"MASTER".equals(role)) {
            throw new AuthException(AuthErrorCode.FORBIDDEN_ACCESS);
        }
        return ApiResponse.success(userService.updateUser(userId, request.toServiceDto()));
    }

    @DeleteMapping("/{userId}")
    public ApiResponse<Void> deleteUser(@PathVariable("userId") Long userId,
                                        @RequestHeader(value = "X-User-Id", required = true) String requestUserId,
                                        @RequestHeader(value = "X-Role", required = true) String role) {
        log.info("deleteUser.userId: {}, RequestHeader.userId: {}, RequestHeader.role: {}", userId, requestUserId, role);
        if (!"MASTER".equals(role)) {
            throw new AuthException(AuthErrorCode.FORBIDDEN_ACCESS);
        }
        userService.deleteUser(userId, Long.parseLong(requestUserId));
        return ApiResponse.success();
    }

    /**
     * 슬랙 메시지를 위한 발신자, 수신자 정보 조회
     * */
    @GetMapping("/slack-ids")
    public UserSlackIdsResponse getUsersForSlack(@RequestParam("senderId") Long senderId,
                                                 @RequestParam("receiverId") Long receiverId) {
        log.info("getUsersForSlack senderId: {}, receiverIdL {}", senderId, receiverId);
        return userService.getUsersForSlack(senderId, receiverId);
    }
}
