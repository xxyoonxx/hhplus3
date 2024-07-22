package com.hhplus.ticketing.infrastructure.userQueue;

import com.hhplus.ticketing.domain.userQueue.entity.UserQueue;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface UserQueueJpaRepository extends JpaRepository<UserQueue, Long> {

    Optional<UserQueue> findByToken(String token);
    Optional<UserQueue> findByUserIdAndStatusNotAndExpiryDateAfter(long userId, UserQueue.Status status, LocalDateTime currentDate);
    Optional<UserQueue> findTopByStatusOrderByQueueIdDesc(UserQueue.Status status);
    List<UserQueue> findByExpiryDateBeforeAndStatusNot(LocalDateTime currentDate, UserQueue.Status status);
    UserQueue findByUserId(long userId);
    List<UserQueue> findByStatusAndExpiryDateAfterOrderByQueueIdAsc(UserQueue.Status status, LocalDateTime currentDate, PageRequest pageReqeust);
    int countByStatus(UserQueue.Status status);

}
