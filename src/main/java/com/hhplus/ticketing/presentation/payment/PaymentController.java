package com.hhplus.ticketing.presentation.payment;

import com.hhplus.ticketing.application.payment.service.PaymentService;
import com.hhplus.ticketing.presentation.payment.dto.BalanceRequestDto;
import com.hhplus.ticketing.presentation.payment.dto.BalanceResponseDto;
import com.hhplus.ticketing.presentation.payment.dto.PaymentRequestDto;
import com.hhplus.ticketing.presentation.payment.dto.PaymentResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "결제 API")
@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    /**
     * 결제 처리
     * @param authorization
     * @return
     */
    @Operation(summary = "결제")
    @PostMapping("/")
    public ResponseEntity<PaymentResponseDto> payCharge(@RequestHeader("Authorization") String authorization, @RequestBody PaymentRequestDto paymentRequestDto){
        PaymentResponseDto payCharge = PaymentResponseDto.from(paymentService.createPayment(authorization, paymentRequestDto));
        return ResponseEntity.ok(payCharge);
    }

    /**
     * 잔액 조회
     * @param userId
     * @return
     */
    @Operation(summary = "잔액 조회")
    @GetMapping("/{userId}/balance")
    public ResponseEntity<BalanceResponseDto> getBalance(@PathVariable long userId) {
        BalanceResponseDto balance = BalanceResponseDto.from(paymentService.getBalance(userId));
        return ResponseEntity.ok(balance);
    }

    /**
     * 잔액 충전
     * @param userId
     * @param requestDto
     * @return
     */
    @Operation(summary = "잔액 충전")
    @PatchMapping("/{userId}/charge")
    public ResponseEntity<BalanceResponseDto> chargeBalance(@PathVariable long userId, @RequestBody BalanceRequestDto requestDto) {
        BalanceResponseDto balance = BalanceResponseDto.from(paymentService.chargeBalance(userId, requestDto));
        return ResponseEntity.ok(balance);
    }

}
