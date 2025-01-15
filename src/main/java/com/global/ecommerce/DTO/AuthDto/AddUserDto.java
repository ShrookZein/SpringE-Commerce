package com.global.ecommerce.DTO.AuthDto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class AddUserDto {

    private String fullName;
    private String userName;
    private String email;
    private String password;
}
