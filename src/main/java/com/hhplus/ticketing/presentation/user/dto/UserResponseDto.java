package com.hhplus.ticketing.presentation.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@AllArgsConstructor
@Builder
@Data
public class UserResponseDto {

    private int balance;

    // 작성 에정
    private static UserResponseDto from(){
        return UserResponseDto.builder().build();
    }

}
