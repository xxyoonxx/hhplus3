package com.hhplus.ticketing.presentation.payment;

import com.hhplus.ticketing.presentation.payment.dto.PaymentResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payment")
@RequiredArgsConstructor
public class PaymentController {

    /**
     * 결제 처리
     * @param request
     * @return
     */
    @PostMapping("/")
    public PaymentResponseDto payCharge(HttpServletRequest request){
        return new PaymentResponseDto("DONE");
    }

}
