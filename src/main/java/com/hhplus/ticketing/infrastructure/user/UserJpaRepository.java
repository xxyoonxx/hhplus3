package com.hhplus.ticketing.infrastructure.user;

import com.hhplus.ticketing.domain.user.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserJpaRepository extends JpaRepository<Users, Long> {

}
