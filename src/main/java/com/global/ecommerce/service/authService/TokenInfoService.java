package com.global.ecommerce.service.authService;

import com.global.ecommerce.Entity.TokenInfo;
import com.global.ecommerce.repository.TokenInfoRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2; // Import the annotation
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Log4j2 // Add the annotation for Log4j2
public class TokenInfoService {

    private final TokenInfoRepo tokenInfoRepo;

    public TokenInfo findById(Long id) {
        log.info("Finding TokenInfo by ID: {}", id);
        TokenInfo tokenInfo = tokenInfoRepo.findById(id).orElse(null);
        if (tokenInfo == null) {
            log.warn("TokenInfo not found for ID: {}", id);
        } else {
            log.debug("TokenInfo found: {}", tokenInfo);
        }
        return tokenInfo;
    }

    public Optional<TokenInfo> findByRefreshToken(String refreshToken) {
        log.info("Finding TokenInfo by refresh token: {}", refreshToken);
        Optional<TokenInfo> tokenInfo = tokenInfoRepo.findByRefreshToken(refreshToken);
        if (!tokenInfo.isPresent()) {
            log.warn("TokenInfo not found for refresh token: {}", refreshToken);
        } else {
            log.debug("TokenInfo found for refresh token: {}", tokenInfo.get());
        }
        return tokenInfo;
    }

    public Optional<TokenInfo> findByAccessToken(String accessToken) {
        log.info("Finding TokenInfo by access token: {}", accessToken);
        Optional<TokenInfo> tokenInfo = tokenInfoRepo.findByAccessToken(accessToken);
        if (!tokenInfo.isPresent()) {
            log.warn("TokenInfo not found for access token: {}", accessToken);
        } else {
            log.debug("TokenInfo found for access token: {}", tokenInfo.get());
        }
        return tokenInfo;
    }

    public TokenInfo save(TokenInfo entity) {
        log.info("Saving TokenInfo: {}", entity);
        TokenInfo savedTokenInfo = tokenInfoRepo.save(entity);
        log.debug("TokenInfo saved successfully with ID: {}", savedTokenInfo.getId());
        return savedTokenInfo;
    }

    public void deleteById(Long id) {
        log.info("Deleting TokenInfo by ID: {}", id);
        tokenInfoRepo.deleteById(id);
        log.debug("TokenInfo deleted successfully for ID: {}", id);
    }
}







//package com.global.ecommerce.service.authService;
//import java.util.Optional;
//
//import com.global.ecommerce.Entity.TokenInfo;
//import com.global.ecommerce.repository.TokenInfoRepo;
//import org.springframework.stereotype.Service;
//import lombok.RequiredArgsConstructor;
//
//@Service
//@RequiredArgsConstructor
//public class TokenInfoService {
//    private final TokenInfoRepo tokenInfoRepo;
//
//    public TokenInfo findById(Long id) {
//
//        return tokenInfoRepo.findById(id).orElse(null);
//    }
//
//    public Optional<TokenInfo> findByRefreshToken(String refreshToken) {
//
//        return tokenInfoRepo.findByRefreshToken(refreshToken);
//    }
//    public Optional<TokenInfo> findByAccessToken(String accessToken) {
//
//        return tokenInfoRepo.findByAccessToken(accessToken);
//    }
//
//    public TokenInfo save(TokenInfo entity) {
//
//        return tokenInfoRepo.save(entity);
//    }
//
//    public void deleteById (Long id) {
//
//        tokenInfoRepo.deleteById(id);
//    }
//}



