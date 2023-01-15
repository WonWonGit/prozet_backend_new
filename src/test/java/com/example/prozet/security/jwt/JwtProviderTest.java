package com.example.prozet.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

import javax.management.RuntimeErrorException;
import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.transaction.annotation.Transactional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.prozet.common.CustomException;
import com.example.prozet.common.ErrorCode;
import com.example.prozet.enum_pakage.JwtCode;
import com.example.prozet.enum_pakage.Provider;
import com.example.prozet.enum_pakage.Role;
import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.member.repository.MemberRepository;
import com.example.prozet.security.auth.PrincipalDetails;


@SpringBootTest
@Transactional
public class JwtProviderTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    JwtProvider jwtProvider;

    @Value("${jwt.secret_key}")
    private String SECRET;

    @Value("${jwt.access_expiration_time}")
    private Long ACCESS_EXPIRATION_TIME;

    @Value("${jwt.refresh_expiration_time}")
    private Long REFRESH_EXPIRATION_TIME;

    @Value("${jwt.access_header_string}")
    private String ACCESS_HEADER_STRING;

    @Value("${jwt.refresh_header_string}")
    private String REFRESH_HEADER_STRING;

    private static final String BEARER = "Bearer ";

    @Autowired
    EntityManager em;

    // private static String LOGIN_URL = "/login";

    private String username = "test_username";


    private void clear(){
        em.flush();
        em.clear();
    }
    
    
    @BeforeEach
    private void init(){
        MemberEntity memberEntity = MemberEntity.builder()
                                                .name("test")
                                                .email("test@google.com")
                                                .username(username)
                                                .provider(Provider.GOOGLE)
                                                .role(Role.USER)
                                                .build();
        memberRepository.save(memberEntity);
        clear();
    }

    private PrincipalDetails getPrincipalDetails(){
        MemberEntity memberEntity = memberRepository.findByUsername(username).orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));
        return new PrincipalDetails(memberEntity);
    }

    private DecodedJWT getVerify(String token){
        return JWT.require(Algorithm.HMAC512(SECRET)).build().verify(token);
    }

    private HttpServletRequest setRequest(String refreshToken, String accessToken){
        
        MockHttpServletRequest mockHttpServletRequest = new MockHttpServletRequest();
        mockHttpServletRequest.addHeader(ACCESS_HEADER_STRING, BEARER+accessToken);
        mockHttpServletRequest.addHeader(REFRESH_HEADER_STRING, BEARER+refreshToken);

        return mockHttpServletRequest;
    }

    private String getExpriedAccessToken(PrincipalDetails principalDetails){
        return JWT.create()
                    .withSubject(principalDetails.getUsername())
                    .withExpiresAt(new Date(System.currentTimeMillis() - ACCESS_EXPIRATION_TIME))
                    .withClaim("username", principalDetails.getUsername())
                    .withClaim("role", principalDetails.getRole())
                    .sign(Algorithm.HMAC512(SECRET));
    }

    // private String getExpriedRefreshToken(PrincipalDetails principalDetails){
    //     return JWT.create()
    //                 .withSubject(principalDetails.getUsername())
    //                 .withExpiresAt(new Date(System.currentTimeMillis() - REFRESH_EXPIRATION_TIME))
    //                 .withClaim("username", principalDetails.getUsername())
    //                 .sign(Algorithm.HMAC512(SECRET));
    // }

    @Test
    public void createAccessToken(){
       
        PrincipalDetails principalDetails = getPrincipalDetails();

        String accessToken = jwtProvider.createAccessToken(principalDetails);

        DecodedJWT decodedAccessToken = getVerify(accessToken);

        String findUsername = decodedAccessToken.getClaim("username").asString();
        String findRole =  decodedAccessToken.getClaim("role").asString();

        assertThat(findUsername).isEqualTo(username);
        assertThat(findRole).isEqualTo(principalDetails.getRole());

    }

    @Test
    public void createRefreshToken(){

         PrincipalDetails principalDetails = getPrincipalDetails();

        String refreshToken = jwtProvider.createRefreshToken(principalDetails);

        DecodedJWT decodedRefreshToken = getVerify(refreshToken);

        String findUsername = decodedRefreshToken.getClaim("username").asString();
       
        assertThat(findUsername).isNull();

    }

    @Test 
    public void setHeaderRefreshTokenTest(){
        
        //given
        MockHttpServletResponse response = new MockHttpServletResponse();
        PrincipalDetails principalDetails = getPrincipalDetails();
        String refreshToken = jwtProvider.createRefreshToken(principalDetails);
       
        //when
        jwtProvider.setHeaderRefreshToken(response, refreshToken);
        String headerRefreshToken = response.getHeader(REFRESH_HEADER_STRING);
        
        assertThat(headerRefreshToken).isEqualTo(refreshToken);

    }   

    @Test 
    public void setHeaderAccessTokenTest(){
        
        //given
        MockHttpServletResponse response = new MockHttpServletResponse();
        PrincipalDetails principalDetails = getPrincipalDetails();
        String accessToken = jwtProvider.createAccessToken(principalDetails);
       
        //when
        jwtProvider.setHeaderAccessToken(response, accessToken);
        String headerAccessToken = response.getHeader(ACCESS_HEADER_STRING);
        
        assertThat(headerAccessToken).isEqualTo(accessToken);

    }

    @Test
    public void extractRefreshTokenTest() throws Exception{

        //given
        PrincipalDetails principalDetails = getPrincipalDetails();
        String accessToken = jwtProvider.createAccessToken(principalDetails);
        String refreshToken = jwtProvider.createRefreshToken(principalDetails);
        HttpServletRequest request = setRequest(refreshToken, accessToken);

        //when
        String extractRefreshToken = jwtProvider.extractRefreshToken(request).orElseThrow(() -> new CustomException(ErrorCode.TOKEN_NULL_ERROR));

        assertThat(extractRefreshToken).isEqualTo(refreshToken);
    }

    @Test
    public void extractAccessTokenTest() throws Exception{

        //given
        PrincipalDetails principalDetails = getPrincipalDetails();
        String accessToken = jwtProvider.createAccessToken(principalDetails);
        String refreshToken = jwtProvider.createRefreshToken(principalDetails);
        HttpServletRequest request = setRequest(refreshToken, accessToken);

        //when
        String extractRefreshToken = jwtProvider.extractAccessToken(request).orElseThrow(() -> new CustomException(ErrorCode.TOKEN_NULL_ERROR));

        assertThat(extractRefreshToken).isEqualTo(accessToken);
        assertThat(getVerify(extractRefreshToken).getClaim("username").asString()).isEqualTo(principalDetails.getUsername());
        assertThat(getVerify(extractRefreshToken).getClaim("role").asString()).isEqualTo(principalDetails.getRole());
    }

    @Test
    public void extractUsername() throws Exception{
        //given
        PrincipalDetails principalDetails = getPrincipalDetails();
        String accessToken = jwtProvider.createAccessToken(principalDetails);

        String extracetedUsername = jwtProvider.extractUsername(accessToken).orElseThrow(() -> new CustomException(ErrorCode.INVALIDED_ACCESS_TOKEN_ERROR));
        assertThat(extracetedUsername).isEqualTo(principalDetails.getUsername());
        
    }

    @Test
    public void isTokenValidTest(){

        //given
        PrincipalDetails principalDetails = getPrincipalDetails();
        String validAccessToken = jwtProvider.createAccessToken(principalDetails);
        String invalidAccessToken = jwtProvider.createAccessToken(principalDetails) + "dd";
        String expiredAccessToken =  getExpriedAccessToken(principalDetails);

        //when
        JwtCode access = jwtProvider.isTokenValid(validAccessToken);
        JwtCode denined = jwtProvider.isTokenValid(invalidAccessToken);
        JwtCode expired = jwtProvider.isTokenValid(expiredAccessToken);

        assertThat(access).isEqualTo(JwtCode.ACCESS);
        assertThat(denined).isEqualTo(JwtCode.DENINED);
        assertThat(expired).isEqualTo(JwtCode.EXPIRED);

    }

    






}
