package com.hhplus.ticketing.infrastructure.userQueue;

import com.hhplus.ticketing.domain.userQueue.entity.UserQueue;
import com.hhplus.ticketing.domain.userQueue.repository.UserQueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserQueueRepositoryImpl implements UserQueueRepository {

    private final UserQueueJpaRepository userQueueJpaRepository;

    @Override
    public Optional<UserQueue> getTokenInfo(String token) {
        return userQueueJpaRepository.findByToken(token);
    }

    @Override
    public Optional<UserQueue> checkQueue(long userId) {
        LocalDateTime currentDate = LocalDateTime.now();
        return userQueueJpaRepository.findByUserIdAndExpiryDateAfter(userId, currentDate);
    }

    @Override
    public UserQueue save(UserQueue userQueue) {
        return userQueueJpaRepository.save(userQueue);
    }

    @Override
    public Optional<Long> getLatestQueueId() {
        return userQueueJpaRepository.findTopByStatusOrderByQueueIdDesc(UserQueue.Status.PROCESSING);
    }

    @Override
    public Optional<UserQueue> getUserIdByToken(String token) {
        return userQueueJpaRepository.findByToken(token);
    }
}
