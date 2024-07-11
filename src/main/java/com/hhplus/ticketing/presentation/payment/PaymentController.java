package com.hhplus.ticketing.presentation.payment;

import com.hhplus.ticketing.domain.payment.service.PaymentService;
import com.hhplus.ticketing.presentation.payment.dto.BalanceRequestDto;
import com.hhplus.ticketing.presentation.payment.dto.BalanceResponseDto;
import com.hhplus.ticketing.presentation.payment.dto.PaymentResponseDto;
import com.hhplus.ticketing.presentation.queue.dto.QueueRequestDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * 결제 처리
     * @param request
     * @return
     */
    @PostMapping("/")
    public PaymentResponseDto payCharge(HttpServletRequest request){
        return new PaymentResponseDto("DONE");
    }

    /**
     * 잔액 조회
     * @param userId
     * @return
     */
    @GetMapping("/{userId}/balance")
    public BalanceResponseDto getBalance(@PathVariable long userId) {
        BalanceResponseDto balance = BalanceResponseDto.from(paymentService.getBalance(userId));
        return balance;
    }

    @PatchMapping("/{userId}/charge")
    public BalanceResponseDto chargeBalance(@PathVariable long userId, @RequestBody BalanceRequestDto requestDto) {
        BalanceResponseDto balance = BalanceResponseDto.from(paymentService.chargeBalance(userId,requestDto));
        return balance;
    }

}
