package com.example.prozet.security.jwt;

import java.net.http.HttpRequest;
import java.util.Date;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.example.prozet.enum_pakage.JwtCode;
import com.example.prozet.security.auth.PrincipalDetails;

@Component
public class JwtProvider {

    @Value("${jwt.secret_key}")
    private String SECRET_KEY;

    @Value("${jwt.access_expiration_time}")
    private Long ACCESS_EXPIRATION_TIME;

    @Value("${jwt.refresh_expiration_time}")
    private Long REFRESH_EXPIRATION_TIME;

    @Value("${jwt.access_header_string}")
    private String ACCESS_HEADER_STRING;

    @Value("${jwt.refresh_header_string}")
    private String REFRESH_HEADER_STRING;

    public static String TOKEN_PREFIX = "Bearer ";


    public String createAccessToken(PrincipalDetails principalDetails){
        
        String accessToken = JWT.create()
                                .withSubject(principalDetails.getUsername())
                                .withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_EXPIRATION_TIME))
                                .withClaim("username", principalDetails.getUsername())
                                .withClaim("role", principalDetails.getRole())
                                .sign(Algorithm.HMAC512(SECRET_KEY));                        

        return accessToken;
    }

    public String createRefreshToken(PrincipalDetails principalDetails){
        
        String refreshToken = JWT.create()
                                 .withSubject(principalDetails.getUsername())
                                 .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_EXPIRATION_TIME))
                                //  .withClaim("username", principalDetails.getUsername())
                                 .sign(Algorithm.HMAC512(SECRET_KEY));                        

        return refreshToken;
    }

    public void setHeaderRefreshToken(HttpServletResponse response, String refreshToken){    
        response.setHeader(REFRESH_HEADER_STRING, refreshToken);
    }

    public void setHeaderAccessToken(HttpServletResponse response, String accessToken){
        response.setHeader(ACCESS_HEADER_STRING, accessToken);
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request){

        return Optional.ofNullable(request.getHeader(REFRESH_HEADER_STRING)).filter(
            refreshToken -> refreshToken.startsWith(TOKEN_PREFIX)
        ).map(refreshToken -> refreshToken.replace(TOKEN_PREFIX, ""));

    }

    public Optional<String> extractAccessToken(HttpServletRequest request){
        
        return Optional.ofNullable(request.getHeader(ACCESS_HEADER_STRING)).filter(
            accessToken -> accessToken.startsWith(TOKEN_PREFIX)
        ).map(accessToken -> accessToken.replace(TOKEN_PREFIX, ""));
    
    }

    public Optional<String> extractUsername(String token){
        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(SECRET_KEY)).build()
                                                            .verify(token).getClaim("username")
                                                            .asString());
        }catch (Exception e){
            return Optional.empty();
        }
    }

    public JwtCode isTokenValid(String token){
        
        try{
            JWT.require(Algorithm.HMAC512(SECRET_KEY)).build().verify(token);
            return JwtCode.ACCESS;
        
        }catch(TokenExpiredException e){
            return JwtCode.EXPIRED;
        
        }catch(Exception e){
            return JwtCode.DENINED;
            
        }
    }

    

    

    
    
}
