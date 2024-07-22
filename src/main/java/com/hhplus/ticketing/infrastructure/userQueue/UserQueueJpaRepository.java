package com.hhplus.ticketing.infrastructure.userQueue;

import com.hhplus.ticketing.domain.userQueue.entity.UserQueue;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface UserQueueJpaRepository extends JpaRepository<UserQueue, Long> {

    Optional<UserQueue> findByToken(String token);

    Optional<UserQueue> findByUserIdAndExpiryDateAfter(long userId, LocalDateTime currentDate);

    Optional<Long> findTopByStatusOrderByQueueIdDesc(UserQueue.Status status);

}
