package com.global.ecommerce.DTO.AuthDto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Setter
@Getter
public class LoginRequestDto {
    @NotEmpty
    @Email
    private String email;

    @NotEmpty
    @Pattern(
            regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d)(?=.*[@#$%^&+=])[A-Za-z\\d@$!%*?&]{8,20}$",
            message = "Password must be 8-20 characters long, include at least one uppercase letter, one lowercase letter, one digit, and one special character."
    )
    private String password;
}
