package com.spring.dozen.auth.application.dto;

public record UserSlackIdsResponse(
        Long senderUserId,
        String senderSlackId,
        Long receiverUserId,
        String receiverSlackId
) {

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
