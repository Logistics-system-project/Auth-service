package com.spring.dozen.auth.domain.entity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 *
 * List<User>를 감싸는 일급 컬렉션
 * */
public class Users {

    private final List<User> users;

    private Users(List<User> users) {
        this.users = Collections.unmodifiableList(users);
    }

    public static Users from(List<User> users) {
        return new Users(users);
    }

    public List<User> getUsers() {
        // return new ArrayList<>(users);
        return Collections.unmodifiableList(this.users);
    }

    // 메소드 명으로 hasNotEnoughUsers()? hasNotBothSenderAndReceiver()?
    public boolean hasNotEnoughUsers() {
        return users.size() <= 1;

    }

    public Optional<String> getSlackIdById(Long userId) {
        return users.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst()
                .map(User::getSlackId);
    }
}
