package com.hhplus.ticketing.infrastructure.userQueue;

import com.hhplus.ticketing.common.exception.CustomException;
import com.hhplus.ticketing.domain.userQueue.UserQueueErrorCode;
import com.hhplus.ticketing.domain.userQueue.entity.UserQueue;
import com.hhplus.ticketing.domain.userQueue.repository.UserQueueRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.awt.print.Pageable;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
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
    public Optional<UserQueue> checkQueue(long userId, LocalDateTime currentDate) {
        return userQueueJpaRepository.findByUserIdAndStatusNotAndExpiryDateAfter(userId, UserQueue.Status.EXPIRED, currentDate);
    }

    @Override
    public UserQueue save(UserQueue userQueue) {
        return userQueueJpaRepository.save(userQueue);
    }

    @Override
    public Optional<Long> getLatestQueueId() {
        return userQueueJpaRepository.findTopByStatusOrderByQueueIdDesc(UserQueue.Status.PROCESSING)
                .map(UserQueue::getQueueId);
    }

    @Override
    public List<UserQueue> getExpiredQueues(LocalDateTime currentDate) {
        return userQueueJpaRepository.findByExpiryDateBeforeAndStatusNot(currentDate, UserQueue.Status.EXPIRED);
    }

    @Override
    public UserQueue getUserInfo(long userId) {
        return userQueueJpaRepository.findByUserId(userId);
    }

    @Override
    public int countByStatus(UserQueue.Status status) {
        return userQueueJpaRepository.countByStatus(status);
    }

    @Override
    public List<UserQueue> enterUserQueue(int newEnterCnt) {
        PageRequest pageReqeust = PageRequest.of(0, newEnterCnt);
        return userQueueJpaRepository.findByStatusAndExpiryDateAfterOrderByQueueIdAsc(UserQueue.Status.WAITING, LocalDateTime.now(), pageReqeust);
    }

}
