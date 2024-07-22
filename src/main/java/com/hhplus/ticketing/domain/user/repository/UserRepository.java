package com.hhplus.ticketing.domain.user.repository;

import com.hhplus.ticketing.domain.user.entity.Users;

import java.util.Optional;

public interface UserRepository {

    Optional<Users> fineByUserId(long userId);

}
