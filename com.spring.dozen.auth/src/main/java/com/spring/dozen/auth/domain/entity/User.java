package com.spring.dozen.auth.domain.entity;

import com.spring.dozen.auth.domain.enums.Role;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder(access = AccessLevel.PRIVATE)
@Entity
@Table(
        name = "p_user",
        uniqueConstraints = {
                @UniqueConstraint(name = "UK_USER", columnNames = {"username"})
        }
)
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(name = "username", length = 10, nullable = false)
    private String username;

    @Column(name = "password", length = 60, nullable = false)
    private String password;

    @Column(name = "slack_id", length = 30, nullable = false)
    private String slackId;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    // 승인 여부
    @Column(name = "is_approved", nullable = false)
    @ColumnDefault("false")
    private Boolean isApproved;

    public static User create(String username, String password, String slackId, Role role, Boolean isApproved) {
        return User.builder()
                .username(username)
                .password(password)
                .slackId(slackId)
                .role(role)
                .isApproved(isApproved)
                .build();
    }

    public static User create(String username, String password, String slackId, Role role) {
        return create(username, password, slackId, role, false);
    }
}


