package com.example.prozet.module.member.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.example.prozet.enum_pakage.Provider;
import com.example.prozet.enum_pakage.Role;
import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.member.repository.MemberRepository;
import com.example.prozet.security.auth.PrincipalDetails;
import com.example.prozet.security.auth.PrincipalDetailsService;
import com.example.prozet.security.jwt.JwtProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MemberControllerTest {

        @Autowired
        MockMvc mockMvc;

        private static String url = "/v1/api/member";

        ObjectMapper objectMapper = new ObjectMapper();

        @Autowired
        JwtProvider jwtProvider;

        @Value("${jwt.access_header_string}")
        private String accessHeader;

        @Value("${jwt.refresh_header_string}")
        private String refreshHeader;

        private static String BEARER = "Bearer ";

        @Autowired
        PrincipalDetailsService principalDetailsService;

        @Autowired
        MemberRepository memberRepository;

        public String accessToken(String username) throws JsonProcessingException, Exception {
                MemberEntity memberEntity = MemberEntity.builder()
                                .name("name")
                                .username(username)
                                .provider(Provider.GOOGLE)
                                .role(Role.USER)
                                .email("email@gmail.com").build();

                MemberEntity memberEntityPS = memberRepository.save(memberEntity);
                PrincipalDetails principalDetails = new PrincipalDetails(memberEntityPS);

                return jwtProvider.createAccessToken(principalDetails);

        }

        @Test
        @DisplayName("멤버 정보")
        public void getMemberInfoTest() throws Exception {

                String accessToken = accessToken("getMemberInfoTest");

                mockMvc.perform(get(url + "/information/getMemberInfoTest")
                                .header(accessHeader, BEARER + accessToken)).andExpect(status().isOk());
        }

}
