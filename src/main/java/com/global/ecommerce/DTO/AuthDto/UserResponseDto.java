package com.global.ecommerce.DTO.AuthDto;

import com.global.ecommerce.Entity.RoleModel;
import com.global.ecommerce.Entity.walletEntity.Wallet;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
//@Builder
public class UserResponseDto {
    private String fullName;
    private String email;
    private List<RoleModel> roles;
    private Wallet wallet;
    private boolean isEnabled;

    private boolean isCredentialsNonExpired;

    private boolean isAccountNonLocked;

    private boolean isAccountNonExpired;
}
