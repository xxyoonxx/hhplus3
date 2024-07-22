package com.hhplus.ticketing.infrastructure.user;

import com.hhplus.ticketing.domain.user.entity.Users;
import com.hhplus.ticketing.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {

    private final UserJpaRepository userJpaRepository;

    @Override
    public Optional<Users> fineByUserId(long userId) {
        return userJpaRepository.findById(userId);
    }
}
