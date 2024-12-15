package com.spring.dozen.auth.application.dto;

import com.spring.dozen.auth.application.exception.AuthErrorCode;
import com.spring.dozen.auth.application.exception.AuthException;
import com.spring.dozen.auth.domain.entity.User;

import java.util.List;

public record UserSlackIdsResponse(
        Long senderUserId,
        String senderSlackId,
        Long receiverUserId,
        String receiverSlackId
) {
    public static UserSlackIdsResponse of(Long senderUserId, Long receiverUserId, List<User> users) {
        String senderSlackId = users.stream()
                .filter(user -> user.getId().equals(senderUserId))
                .findFirst()
                .map(User::getSlackId)
                .orElseThrow(() -> new AuthException(AuthErrorCode.SENDER_NOT_FOUND));

        String receiverSlackId = users.stream()
                .filter(user -> user.getId().equals(receiverUserId))
                .findFirst()
                .map(User::getSlackId)
                .orElseThrow(() -> new AuthException(AuthErrorCode.RECEIVER_NOT_FOUND));

        return new UserSlackIdsResponse(
                senderUserId,
                senderSlackId,
                receiverUserId,
                receiverSlackId
        );
    }

    public static UserSlackIdsResponse of(Long senderUserId, String senderSlackId,
                                          Long receiverUserId, String receiverSlackId) {
        return new UserSlackIdsResponse(
                senderUserId,
                senderSlackId,
                receiverUserId,
                receiverSlackId
        );
    }
}
