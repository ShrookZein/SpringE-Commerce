package com.global.ecommerce.security;

import com.global.ecommerce.DTO.AuthDto.AccessTokenDto;
import com.global.ecommerce.DTO.AuthDto.JWTResponseDto;
import com.global.ecommerce.DTO.AuthDto.LoginRequestDto;
import com.global.ecommerce.DTO.AuthDto.SignUpRequestDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@CrossOrigin()
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;


    @PostMapping("/login")
    public ResponseEntity<?> login (@Valid @RequestBody LoginRequestDto loginRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        JWTResponseDto jwtResponseDto = authService.login(loginRequest.getEmail(), loginRequest.getPassword());

        return ResponseEntity.ok(jwtResponseDto);
    }
    @PostMapping("/signUp")
    public ResponseEntity<?>signUp(@Valid @RequestBody SignUpRequestDto signupRequest, BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return ResponseEntity.badRequest().body(bindingResult.getAllErrors());
        }
        JWTResponseDto jwtResponseDto = authService.signUp(signupRequest.getFullName(),signupRequest.getEmail(), signupRequest.getPassword());

        return ResponseEntity.ok(jwtResponseDto);
    }


    @PostMapping("/refresh-token")
    public ResponseEntity<AccessTokenDto> refreshAccessToken(@RequestParam String refreshToken) {

        AccessTokenDto dto = authService.refreshAccessToken(refreshToken);

        return ResponseEntity.ok(dto);
    }


    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestParam String refreshToken) {

        authService.logoutUser(refreshToken);

        return ResponseEntity.ok(null);
    }
}
