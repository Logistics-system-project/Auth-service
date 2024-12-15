package com.spring.dozen.auth.application.service;

import com.spring.dozen.auth.application.dto.UserSlackIdsResponse;
import com.spring.dozen.auth.application.dto.UserUpdate;
import com.spring.dozen.auth.application.dto.UserUpdateResponse;
import com.spring.dozen.auth.application.exception.AuthErrorCode;
import com.spring.dozen.auth.application.exception.AuthException;
import com.spring.dozen.auth.domain.entity.User;
import com.spring.dozen.auth.domain.entity.Users;
import com.spring.dozen.auth.domain.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * 유저 조회, 수정, 삭제
 * */
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public UserUpdateResponse updateUser(Long userId, UserUpdate updateRequest) {
        log.info("updateUser.userId: {}, UserUpdate: {}", userId, updateRequest);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_FOUND));
        user.update(passwordEncoder.encode(updateRequest.password()), updateRequest.slackId());
        return UserUpdateResponse.from(user);
    }

    @Transactional
    public void deleteUser(Long userId, Long requestUserId) {
        // requestUserId 는 MASTER 의 ID 밖에 들어올 수 없습니다. 삭제는 MASTER 만 가능해서
        log.info("deleteUser.userId: {}, requestUserId: {}", userId, requestUserId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AuthException(AuthErrorCode.USER_NOT_FOUND));
        // 이미 삭제된 유저는 예외 발생
        if (user.getIsDeleted() != null && user.getIsDeleted()) {
            throw new AuthException(AuthErrorCode.ALREADY_DELETED);
        }
        user.deleteBase(requestUserId);
    }

    /**
     *
     * 슬랙 메시지를 보내기 위한 발신자, 수신자 정보를 조회
     */
    public UserSlackIdsResponse getUsersForSlack(Long senderUserId, Long receiverUserId) {
        log.info("getUsersForSlack senderUserId: {}, receiverUserId: {}", senderUserId, receiverUserId);

        // 일급 컬렉션 적용
        Users users = Users.from(userRepository.findByIdIn(List.of(senderUserId, receiverUserId)));
        if(users.hasNotBothSenderAndReceiver()) {
            throw new AuthException(AuthErrorCode.USER_NOT_FOUND);
        }
        String senderSlackId = users.getSlackIdById(senderUserId)
                .orElseThrow(() -> new AuthException(AuthErrorCode.SENDER_NOT_FOUND));
        String receiverSlackId = users.getSlackIdById(receiverUserId)
                .orElseThrow(() -> new AuthException(AuthErrorCode.RECEIVER_NOT_FOUND));
        return UserSlackIdsResponse.of(senderUserId, senderSlackId, receiverUserId, receiverSlackId);
    }
}
