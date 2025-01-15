package com.global.ecommerce.DTO.AuthDto;

import com.global.ecommerce.DTO.AuthDto.UserResponseDto;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class JWTResponseDto {

    private String accessToken;

    private String refreshToken;
//    private User user;
    private UserResponseDto userResponseDto;
}
