package com.example.prozet.module.member.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.io.FileInputStream;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.prozet.modules.member.domain.dto.request.MemberInfoSaveReqDTO;
import com.example.prozet.modules.member.domain.dto.request.MemberInfoUpdateDTO;
import com.example.prozet.modules.member.domain.dto.request.MemberRequireDTO;
import com.example.prozet.modules.member.domain.dto.response.MemberInfoResDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class MemberApiContollerTest {

        @Autowired
        MockMvc mockMvc;

        private static String url = "/v1/api/member";

        ObjectMapper objectMapper = new ObjectMapper();

        @Value("${jwt.access_header_string}")
        private String accessHeader;

        @Value("${jwt.refresh_header_string}")
        private String refreshHeader;

        private static String BEARER = "Bearer ";

        private static String path = "src/test/resources/rocketlauncher.png";
        private static String fileName = "rocketlauncher.png";
        private static String contentType = "image/png";

        public String accessToken() throws JsonProcessingException, Exception {

                Map<String, Object> map = new HashMap<>();
                map.put("email", "test@google.com");
                map.put("sub", "123123");

                MvcResult result = mockMvc.perform(post("/login/google")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(map)))
                                .andExpect(status().isOk()).andReturn();

                String access = result.getResponse().getHeader(accessHeader);

                return access;

        }

        private MemberInfoSaveReqDTO getMemberInfoSaveDto() {
                return MemberInfoSaveReqDTO.builder()
                                .job("job")
                                .email("email@gmail.com")
                                .name("name")
                                .build();
        }

        @Test
        @DisplayName("파일 없이 멤버 정보 입력")
        @WithMockUser(username = "google_123123", value = "user")
        public void memberInfoSaveWithoutFileTest() throws JsonProcessingException,
                        Exception {

                MemberInfoSaveReqDTO memberInfoSaveReqDTO = getMemberInfoSaveDto();

                String accessToken = accessToken();

                String memberInfoSaveReqDTOJson = objectMapper.writeValueAsString(memberInfoSaveReqDTO);

                MockMultipartFile memberInfo = new MockMultipartFile("memberInfo", "",
                                "application/json",
                                memberInfoSaveReqDTOJson.getBytes());

                mockMvc.perform(multipart(url + "/information")
                                .file(memberInfo)
                                .header(accessHeader, BEARER + accessToken)
                                .contentType(MediaType.MULTIPART_FORM_DATA)).andExpect(status().isOk());
        }

        @BeforeEach
        @DisplayName("파일 & 멤버 정보 입력")
        @WithMockUser(username = "google_123123", value = "user")
        public void memberInfoSaveWithFileTest() throws JsonProcessingException, Exception {

                MemberInfoSaveReqDTO memberInfoSaveReqDTO = getMemberInfoSaveDto();

                String accessToken = accessToken();

                String memberInfoSaveReqDTOJson = objectMapper.writeValueAsString(memberInfoSaveReqDTO);

                MockMultipartFile memberInfo = new MockMultipartFile("memberInfo", "", MediaType.APPLICATION_JSON_VALUE,
                                memberInfoSaveReqDTOJson.getBytes());

                MockMultipartFile multipartFile = new MockMultipartFile("imgProfile", fileName, contentType,
                                new FileInputStream(path));

                mockMvc.perform(multipart(url + "/information")
                                .file(memberInfo)
                                .file(multipartFile)
                                .header(accessHeader, BEARER + accessToken)
                                .contentType(MediaType.MULTIPART_FORM_DATA)).andExpect(status().isOk());
        }

        @Test
        @DisplayName("멤버 수정")
        @WithMockUser(username = "google_123123", value = "user")
        public void updateMemberInfoTest() throws Exception {

                String accessToken = accessToken();

                MemberRequireDTO memberRequireDTO = MemberRequireDTO.builder().email("test@email").name("name").build();
                MemberInfoUpdateDTO memberInfoUpdateDTO = MemberInfoUpdateDTO.builder().job("job").build();

                MockMultipartFile memberInfo = new MockMultipartFile("memberInfo", "", MediaType.APPLICATION_JSON_VALUE,
                                objectMapper.writeValueAsString(memberInfoUpdateDTO).getBytes());

                MockMultipartFile memberRequireInfo = new MockMultipartFile("memberRequireInfo", "",
                                MediaType.APPLICATION_JSON_VALUE,
                                objectMapper.writeValueAsString(memberRequireDTO).getBytes());

                mockMvc.perform(
                                MockMvcRequestBuilders
                                                .multipart(HttpMethod.PUT, url + "/information/google_123123")
                                                .file(memberInfo)
                                                .file(memberRequireInfo)
                                                .header(accessHeader, BEARER + accessToken)
                                                .contentType(MediaType.MULTIPART_FORM_DATA))
                                .andExpect(status().isOk());
        }

}
