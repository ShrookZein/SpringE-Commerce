package com.global.ecommerce.security;


import com.global.ecommerce.DTO.AuthDto.AccessTokenDto;
import com.global.ecommerce.DTO.AuthDto.JWTResponseDto;
import com.global.ecommerce.DTO.AuthDto.UserResponseDto;
import com.global.ecommerce.Entity.RoleModel;
import com.global.ecommerce.Entity.TokenInfo;
import com.global.ecommerce.Entity.User;
import com.global.ecommerce.Entity.walletEntity.Wallet;
import com.global.ecommerce.repository.UserRepo;
import com.global.ecommerce.service.authService.RoleService;
import com.global.ecommerce.service.authService.TokenInfoService;
import com.global.ecommerce.service.authService.UserService;
import com.global.ecommerce.service.walletService.WalletService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Log4j2
@RequiredArgsConstructor
public class AuthService {

    private final AuthenticationManager authManager;

    private final HttpServletRequest httpRequest;

    private final TokenInfoService tokenInfoService;

    private final JwtTokenUtils jwtTokenUtils;
    private final UserRepo userRepo;
    private final UserService userService;
    private final RoleService roleService;
    @Autowired
    private WalletService walletService;
    @Autowired
    private ModelMapper modelMapper;

    public JWTResponseDto login(String login, String password) {
        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(login, password));

        log.debug("Valid userDetails credentials.");

        AppUserDetail userDetails = (AppUserDetail) authentication.getPrincipal();

        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.debug("SecurityContextHolder updated. [login={}]", login);


        TokenInfo tokenInfo = createLoginToken(login, userDetails.getId());
        User user=userRepo.findByEmail(login).get();

//        ResponseUserDto responseUserDto=ResponseUserDto.builder()
//                .fullName().email().user.roles().build();
        UserResponseDto userResponseDto=modelMapper.map(user, UserResponseDto.class);

        return JWTResponseDto.builder()
                .accessToken(tokenInfo.getAccessToken())
                .refreshToken(tokenInfo.getRefreshToken())
                .userResponseDto(userResponseDto)
                .build();
    }
    @Transactional
    public JWTResponseDto signUp(String fullName,String email, String password) {
        Optional<User> user = userRepo.findByEmail(email);
        User savedUser=null;
        if (user.isPresent()) {
            throw new UsernameNotFoundException("This Email Already Exists");
//            return ResponseEntity.badRequest();
        }
        else{
            List<RoleModel> userRoles = new ArrayList<>();
            userRoles.add(roleService.findByName("user"));
//            userService.save(new User(null, fullName,email, password,userRoles,null,true,true,true,true));
             savedUser=userService.save(new User(null, fullName,email, password,userRoles,null,true,true,true,true));
            // Create wallet and link it to the saved user
            Wallet wallet = walletService.createWallet(savedUser);
            savedUser.setWallet(wallet);
//            userService.save(savedUser);
        }

        Authentication authentication = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(email, password));

        log.debug("Valid userDetails credentials.");

        AppUserDetail userDetails = (AppUserDetail) authentication.getPrincipal();

        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.debug("SecurityContextHolder updated. [login={}]", email);


        TokenInfo  tokenInfo = createLoginToken(email, userDetails.getId());


//        User user1=userRepo.findByEmail(email).get();

        UserResponseDto userResponseDto=modelMapper.map(savedUser, UserResponseDto.class);

        return JWTResponseDto.builder()
                .accessToken(tokenInfo.getAccessToken())
                .refreshToken(tokenInfo.getRefreshToken())
                .userResponseDto(userResponseDto)
                .build();
    }

    public TokenInfo createLoginToken(String email, Long userId) {
        String userAgent = httpRequest.getHeader(HttpHeaders.USER_AGENT);
        InetAddress ip = null;
        try {
            ip = InetAddress.getLocalHost();
        } catch (UnknownHostException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        String accessTokenId = UUID.randomUUID().toString();
        String accessToken = JwtTokenUtils.generateToken(email, accessTokenId, false);
        log.info("Access token created. [tokenId={}]", accessTokenId);

        String refreshTokenId = UUID.randomUUID().toString();
        String refreshToken = JwtTokenUtils.generateToken(email, refreshTokenId, true);
        log.info("Refresh token created. [tokenId={}]", accessTokenId);

        TokenInfo tokenInfo = new TokenInfo(accessToken, refreshToken);
        tokenInfo.setUser(new User(userId));
        tokenInfo.setUserAgentText(userAgent);
        tokenInfo.setLocalIpAddress(ip.getHostAddress());
        tokenInfo.setRemoteIpAddress(httpRequest.getRemoteAddr());
        // tokenInfo.setLoginInfo(createLoginInfoFromRequestUserAgent());
        return tokenInfoService.save(tokenInfo);
    }


    public AccessTokenDto refreshAccessToken(String refreshToken) {
        if (jwtTokenUtils.isTokenExpired(refreshToken)) {
            return null;
        }
        String email = jwtTokenUtils.getUserNameFromToken(refreshToken);
        Optional<TokenInfo> refresh = tokenInfoService.findByRefreshToken(refreshToken);
        if (!refresh.isPresent()) {
            return null;
        }

        return new AccessTokenDto(JwtTokenUtils.generateToken(email, UUID.randomUUID().toString(), false));

    }


    public void logoutUser(String accessToken) {
        Optional<TokenInfo> tokenInfo = tokenInfoService.findByAccessToken(accessToken);
        if (tokenInfo.isEmpty()) {
            throw new RuntimeException("Token not existing");
        }
        tokenInfoService.deleteById(tokenInfo.get().getId());
    }
}