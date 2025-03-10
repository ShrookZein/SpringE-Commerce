package com.global.ecommerce.security;

import com.global.ecommerce.service.authService.TokenInfoService;
import com.global.ecommerce.service.authService.UserService;
import io.jsonwebtoken.*;
import lombok.NonNull;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;

@Log4j2
@Component
public class JwtTokenUtils {
    private static String TOKEN_SECRET;
    private static Long ACCESS_TOKEN_VALIDITY;
    private static Long REFRESH_TOKEN_VALIDITY;
    @Autowired
    UserService userService;
    @Autowired
    TokenInfoService tokenInfoService;

    public JwtTokenUtils(@Value("${auth.secret}") String secret, @Value("${auth.access.expiration}") Long accessValidity
            , @Value("${auth.refresh.expiration}") Long refreshValidity) {
        Assert.notNull(accessValidity, "Validity must not be null");
        Assert.hasText(secret, "Validity must not be null or empty");

        TOKEN_SECRET = secret;
        ACCESS_TOKEN_VALIDITY = accessValidity;
        REFRESH_TOKEN_VALIDITY = refreshValidity ;
    }

    public static String generateToken(final String email, final String tokenId , boolean isRefresh) {
        return Jwts.builder()
                .setId(tokenId)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setIssuer("app-Service")
                .setExpiration(calcTokenExpirationDate(isRefresh))
                .claim("created", Calendar.getInstance().getTime())
                .signWith(SignatureAlgorithm.HS512, TOKEN_SECRET).compact();
    }


    private static Date calcTokenExpirationDate(boolean isRefresh) {
        return new Date(System.currentTimeMillis() + (isRefresh ? REFRESH_TOKEN_VALIDITY : ACCESS_TOKEN_VALIDITY) * 1000);
    }

    public String getUserNameFromToken(String token) {
        Claims claims = getClaims(token);
        return claims.getSubject();
    }
    public Long getUserIdFromToken(@NonNull HttpServletRequest req){
        final String authHeader = req.getHeader("Authorization");
        System.out.println(authHeader);
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("token not vaild");
        }
        final String jwt = authHeader.substring(7);
        if(tokenInfoService.findByAccessToken(jwt).isEmpty()){
            throw new RuntimeException("Token not existing");
        }
        String userEmail= getUserNameFromToken(jwt);
        Long userId=userService.findByEmail(userEmail).getId();
        System.out.println(jwt);
        return userId;
    }

    public String getTokenIdFromToken(String token) {
        return getClaims(token).getId();
    }

    public boolean isTokenValid(String token, AppUserDetail userDetails) {
        log.info("isTokenExpired >>> " + isTokenExpired(token));
        String email = getUserNameFromToken(token);
        log.info("username from token >>> " + email);
        log.info("userDetails.getUsername >>> " + userDetails.getUsername());
        log.info("username =  >>> userDetails.getUsername >>> " + email.equals(userDetails.getUsername()));
        boolean isEmailEqual = email.equalsIgnoreCase(userDetails.getUsername()) ;
        return (isEmailEqual && !isTokenExpired(token));
    }

    public boolean isTokenExpired(String token) {
        Date expiration = getClaims(token).getExpiration();
        return expiration.before(new Date());
    }

    private Claims getClaims(String token) {

        return Jwts.parser().setSigningKey(TOKEN_SECRET).parseClaimsJws(token).getBody();
    }


    public boolean validateToken(String token , HttpServletRequest httpServletRequest){

        try {
            Jwts.parser().setSigningKey(TOKEN_SECRET).parseClaimsJws(token);
            return true;
        }catch (SignatureException ex){
            log.info("Invalid JWT Signature");
            //  throw new SecurityException("Invalid JWT Signature");
        }catch (MalformedJwtException ex){
            log.info("Invalid JWT token");
            httpServletRequest.setAttribute("expired",ex.getMessage());
            //  throw new SecurityException("Invalid JWT token");
        }catch (ExpiredJwtException ex){
            log.info("Expired JWT token");
            httpServletRequest.setAttribute("expired",ex.getMessage());
            //  throw new SecurityException("security.token_expired");
        }catch (UnsupportedJwtException ex){
            log.info("Unsupported JWT exception");
            //   throw new SecurityException("Unsupported JWT exception");
        }catch (IllegalArgumentException ex){
            log.info("Jwt claims string is empty");
            //   throw new SecurityException("Jwt claims string is empty");
        }
        return false;
    }
}
