package com.spring.dozen.auth.application.dto;

import com.spring.dozen.auth.application.exception.AuthException;
import com.spring.dozen.auth.domain.entity.User;
import com.spring.dozen.auth.domain.enums.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class UserSlackIdsResponseTest {

    // Reflection 사용해서 id 세팅해주기
    private void setId(User user, Long id) throws Exception {
        Field idField = User.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(user, id);
    }

    @DisplayName("of 메서드는 senderUserId와 receiverUserId와 User 리스트로부터 올바른 UserSlackIdsResponse 를 생성해야 한다")
    @Test
    void of_ShouldCreateCorrectUserSlackIdsResponse() throws Exception {
        // given
        User sender = User.create("sender", "password", "sender@example.com", Role.HUB_DELIVERY_STAFF);
        setId(sender, 1L);

        User receiver = User.create("receiver", "password", "receiver@example.com", Role.HUB_DELIVERY_STAFF);
        setId(receiver, 2L);

        List<User> users = Arrays.asList(sender, receiver);

        // when
        UserSlackIdsResponse response = UserSlackIdsResponse.of(sender.getId(), receiver.getId(), users);

        // then
        assertThat(response.senderUserId()).isEqualTo(1L);
        assertThat(response.senderSlackId()).isEqualTo("sender@example.com");
        assertThat(response.receiverUserId()).isEqualTo(2L);
        assertThat(response.receiverSlackId()).isEqualTo("receiver@example.com");
    }


    @DisplayName("of 메서드는 User 리스트에 수신자 정보가 없을 때 예외를 발생한다")
    @Test
    void of_ShouldReturnAuthExceptionWhenReceiverNotFound() throws Exception {
        // given
        User sender = User.create("existing", "password", "existing@example.com", Role.HUB_DELIVERY_STAFF);
        setId(sender, 1L);

        List<User> users = List.of(sender);
        Long wrongUserId = 999L;

        // when & then
        assertThatThrownBy(() -> UserSlackIdsResponse.of(sender.getId(), wrongUserId, users))
                .isInstanceOf(AuthException.class)
                .hasMessage("수신자 정보가 존재하지 않습니다.");
    }

    @DisplayName("of 메서드는 User 리스트에 발신자 정보가 없을 때 예외를 발생한다")
    @Test
    void of_ShouldReturnAuthExceptionWhenSenderNotFound() throws Exception {
        // given
        User receiver = User.create("receiver", "password", "receiver@example.com", Role.HUB_DELIVERY_STAFF);
        setId(receiver, 1L);

        List<User> users = List.of(receiver);
        Long wrongUserId = 999L;

        // when & then
        assertThatThrownBy(() -> UserSlackIdsResponse.of(wrongUserId, receiver.getId(), users))
                .isInstanceOf(AuthException.class)
                .hasMessage("발신자 정보가 존재하지 않습니다.");
    }

}