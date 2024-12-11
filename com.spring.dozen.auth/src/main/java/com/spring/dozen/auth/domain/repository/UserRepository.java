package com.spring.dozen.auth.domain.repository;

import com.spring.dozen.auth.domain.entity.User;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * infra 계층과 분리하기 위한 인터페이스
 * */
@Repository
public interface UserRepository {
    List<User> findAll();

    Optional<User> findById(Long id);

    User save(User user);
}
