package com.hhplus.ticketing.presentation.user;

import com.hhplus.ticketing.presentation.user.dto.UserRequestDto;
import com.hhplus.ticketing.presentation.user.dto.UserResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    /**
     * 잔액 조회
     * @param userId
     * @return
     */
    @GetMapping("/{userId}/balance")
    public UserResponseDto getBalance(@PathVariable long userId){
        return new UserResponseDto(1000);
    }

    /**
     * 잔액 충전
     * @param userId
     * @param userRequestDto
     * @return
     */
    @PatchMapping("/{userId}/charge")
    public UserResponseDto chargeBalance(@PathVariable long userId, @RequestBody UserRequestDto userRequestDto){
        return new UserResponseDto(11000);
    }

}
