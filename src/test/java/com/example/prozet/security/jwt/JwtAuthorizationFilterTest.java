package com.example.prozet.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.transaction.annotation.Transactional;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.prozet.common.CustomException;
import com.example.prozet.common.ErrorCode;
import com.example.prozet.enum_pakage.Provider;
import com.example.prozet.enum_pakage.Role;
import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.member.repository.MemberRepository;
import com.example.prozet.modules.memberAuth.domain.dto.request.MemberAuthReqDto;
import com.example.prozet.modules.memberAuth.service.MemberAuthService;
import com.example.prozet.security.auth.PrincipalDetails;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class JwtAuthorizationFilterTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    MemberAuthService memberAuthService;

    @Value("${jwt.access_header_string}")
    private String ACCESS_HEADER_STRING;

    @Value("${jwt.refresh_header_string}")
    private String REFRESH_HEADER_STRING;

    @Value("${jwt.secret_key}")
    private String SECRET;

    @Value("${jwt.access_expiration_time}")
    private Long ACCESS_EXPIRATION_TIME;

    @Value("${jwt.refresh_expiration_time}")
    private Long REFRESH_EXPIRATION_TIME;

    private static final String BEARER = "Bearer ";

    private static String username = "test_username";

    @Autowired
    JwtProvider jwtProvider;

    @Autowired
    MemberRepository memberRepository;

    ObjectMapper objectMapper = new ObjectMapper();

    public Map<Object, String> getToken() {

        MemberEntity memberEntity = memberRepository.findByUsername(username).orElseThrow();

        String access = jwtProvider.createAccessToken(new PrincipalDetails(memberEntity));
        String refresh = jwtProvider.createRefreshToken(new PrincipalDetails(memberEntity));

        Map<Object, String> map = new HashMap<>();
        map.put(ACCESS_HEADER_STRING, access);
        map.put(REFRESH_HEADER_STRING, refresh);

        return map;
    }

    private String getExpriedAccessToken(PrincipalDetails principalDetails) {
        return JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() - ACCESS_EXPIRATION_TIME))
                .withClaim("username", principalDetails.getUsername())
                .withClaim("role", principalDetails.getRole())
                .sign(Algorithm.HMAC512(SECRET));
    }

    private String getExpriedRefreshToken(PrincipalDetails principalDetails) {
        return JWT.create()
                .withSubject(principalDetails.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() - REFRESH_EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(SECRET));
    }

    @BeforeEach
    public void saveRefreshToken() {

        MemberEntity memberEntity = MemberEntity.builder()
                .name("test")
                .email("test@google.com")
                .username(username)
                .provider(Provider.GOOGLE)
                .role(Role.USER)
                .build();
        memberRepository.save(memberEntity);

        MemberAuthReqDto memberAuthReqDto = MemberAuthReqDto.builder()
                .refreshToken(jwtProvider.createRefreshToken(new PrincipalDetails(memberEntity))).build();

        memberAuthService.saveRefreshToken(memberAuthReqDto, memberEntity);
    }

    /**
     * AccessToken : not exist
     * RefreshToken : not exist
     */
    @Test
    public void accessForbiddenTest() throws Exception {
        mockMvc.perform(get("/v1/api/member")).andExpect(status().isForbidden());
    }

    /**
     * AccessToken : valid
     * RefreshToken : not exist
     */
    @Test
    public void validAccessToken() throws Exception {

        Map<Object, String> result = getToken();

        String access = result.get(ACCESS_HEADER_STRING);

        mockMvc.perform(get("/v1/api/member")
                .header(ACCESS_HEADER_STRING, BEARER + access))
                .andExpect(status().isNotFound());

    }

    /**
     * AccessToken : dinied
     * RefreshToken : not exist
     */
    @Test
    public void invalidAccessToken() throws Exception {

        Map<Object, String> token = getToken();

        String access = token.get(ACCESS_HEADER_STRING);

        mockMvc.perform(get("/v1/api/member")
                .header(ACCESS_HEADER_STRING, BEARER + access + "DDD")).andExpect(status().isUnauthorized());

    }

    /**
     * AccessToken : expired
     * RefreshToken : not exist
     */
    @Test
    public void expiredAccessToken() throws Exception {

        // Map<Object, String> token = getToken();

        MemberEntity memberEntity = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));

        String access = getExpriedAccessToken(new PrincipalDetails(memberEntity));

        mockMvc.perform(get("/v1/api/member")
                .header(ACCESS_HEADER_STRING, BEARER + access)).andExpect(status().isUnauthorized());

    }

    /**
     * AccessToken: not exist
     * RefreshToken : valid
     * return -> new accessToken!
     */
    @Test
    public void getNewAccessTokenFromRefreshTokenTest() throws Exception {
        Map<Object, String> token = getToken();

        String refresh = token.get(REFRESH_HEADER_STRING);

        MvcResult result = mockMvc.perform(get("/v1/api/member")
                .header(REFRESH_HEADER_STRING, BEARER + refresh)).andExpect(status().isOk()).andReturn();

        String newAccessToken = result.getResponse().getHeader(ACCESS_HEADER_STRING);
        String username = jwtProvider.extractUsername(newAccessToken).orElseThrow();

        assertThat(result.getResponse().getHeader(ACCESS_HEADER_STRING)).isNotEmpty();
        assertThat(username).isEqualTo(username);
    }

    /**
     * AccessToken : not exist
     * RefreshToken : expired
     * 400 error
     */
    @Test
    public void getNewAccessTokenFromExpiredRefreshTokenTest() throws Exception {

        MemberEntity memberEntity = memberRepository.findByUsername(username)
                .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_EXIST));

        String refresh = getExpriedRefreshToken(new PrincipalDetails(memberEntity));

        MvcResult result = mockMvc.perform(get("/v1/api/member")
                .header(REFRESH_HEADER_STRING, BEARER + refresh)).andExpect(status().isUnauthorized()).andReturn();

    }

    /**
     * AccessToken : not exist
     * RefreshToken : invalid
     * 400 error
     */
    @Test
    public void getNewAccessTokenFromInvalidRefreshTokenTest() throws Exception {
        Map<Object, String> token = getToken();

        String refresh = token.get(REFRESH_HEADER_STRING);

        mockMvc.perform(get("/v1/api/member")
                .header(REFRESH_HEADER_STRING, BEARER + refresh + "DDD")).andExpect(status().isUnauthorized())
                .andDo(MockMvcResultHandlers.print());

    }

}
