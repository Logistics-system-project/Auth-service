package com.spring.dozen.auth.infra;

import com.spring.dozen.auth.domain.entity.User;
import com.spring.dozen.auth.domain.enums.Role;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


@ActiveProfiles("test")
@Transactional
@SpringBootTest
class UserRepositoryImplTest {

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private UserRepositoryImpl userRepository;

    @DisplayName("findByIdIn 메서드는 주어진 ID 목록에 해당하는 사용자들을 정확히 반환해야 한다")
    @Test
    void findByIdIn_ShouldReturnMatchingUsers() {
        // Given
        User user1 = User.create("user1", "password1", "user1@example.com", Role.HUB_DELIVERY_STAFF);
        User user2 = User.create("user2", "password2", "user2@example.com", Role.HUB_DELIVERY_STAFF);
        User user3 = User.create("user3", "password3", "user3@example.com", Role.HUB_DELIVERY_STAFF);

        entityManager.persist(user1);
        entityManager.persist(user2);
        entityManager.persist(user3);
        entityManager.flush();

        // When
        List<User> foundUsers = userRepository.findByIdIn(Arrays.asList(user1.getId(), user3.getId()));

        // Then
        assertThat(foundUsers).hasSize(2);
        assertThat(foundUsers).extracting(User::getUsername).containsExactlyInAnyOrder("user1", "user3");
        assertThat(foundUsers).extracting(User::getId).containsExactlyInAnyOrder(user1.getId(), user3.getId());
    }

    @DisplayName("findByIdIn 메서드는 존재하는 ID와 존재하지 않는 ID가 주어졌을 때 존재하는 사용자만 반환해야 한다")
    @Test
    void findByIdIn_WithOneExistingAndOneNonExistingId_ShouldReturnOnlyExistingUser() {
        // Given
        User user1 = User.create("user1", "password1", "user1@example.com", Role.HUB_DELIVERY_STAFF);

        entityManager.persist(user1);
        entityManager.flush();

        Long wrongUserId = 999L;
        // When
        List<User> foundUsers = userRepository.findByIdIn(Arrays.asList(user1.getId(), wrongUserId));

        // Then
        assertThat(foundUsers).hasSize(1);
    }

    @DisplayName("findByIdIn 메서드는 존재하지 않는 ID들만 주어졌을 때 빈 리스트를 반환해야 한다")
    @Test
    void findByIdIn_No_User_ShouldReturnEmptyList() {
        // Given
        Long wrongUserId1 = 999L;
        Long wrongUserId2 = 9999L;

        // When
        List<User> foundUsers = userRepository.findByIdIn(Arrays.asList(wrongUserId1, wrongUserId2));

        // Then
        assertThat(foundUsers).hasSize(0);
        assertThat(foundUsers).isEmpty();
    }

}