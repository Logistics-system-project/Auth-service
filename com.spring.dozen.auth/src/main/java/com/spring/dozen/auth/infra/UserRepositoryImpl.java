package com.spring.dozen.auth.infra;

import com.spring.dozen.auth.domain.entity.User;
import com.spring.dozen.auth.domain.repository.UserRepository;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * JpaRepository 를 Infra 계층에
 * 만약, 여기다가 QueryDSL 도 사용한다면, 여기다가 구현을 하고 UserRepository 선언부만 작성해서 사용한다.
 */

public interface UserRepositoryImpl extends JpaRepository<User, Long>, UserRepository {
}
