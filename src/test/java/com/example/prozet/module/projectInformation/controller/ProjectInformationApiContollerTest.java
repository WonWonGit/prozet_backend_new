package com.example.prozet.module.projectInformation.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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

import com.example.prozet.modules.member.repository.MemberRepository;
import com.example.prozet.modules.projectInformation.domain.dto.request.ProjectInfoUpdateReqDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProjectInformationApiContollerTest {

        @Autowired
        MockMvc mockMvc;

        @Autowired
        MemberRepository memberRepository;

        @Value("${jwt.access_header_string}")
        private String accessHeader;

        private static String BEARER = "Bearer ";

        private static String url = "/v1/api/project";

        ObjectMapper objectMapper = new ObjectMapper();

        // private static String path = "src/test/resources/rocketlauncher.png";
        // private static String fileName = "rocketlauncher.png";
        // private static String contentType = "image/png";

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

        @Test
        @WithMockUser(username = "google_123123", value = "user")
        public void updateProjectInfo() throws Exception {

                ProjectInfoUpdateReqDTO projectInfoUpdateReqDTO = ProjectInfoUpdateReqDTO.builder()
                                .content("new content")
                                .title("title").startDate(LocalDateTime.now())
                                .endDate(LocalDateTime.now())
                                .build();

                MockMultipartFile projectInfoReqeust = new MockMultipartFile("projectInfo",
                                "",
                                MediaType.APPLICATION_JSON_VALUE,
                                objectMapper.registerModule(new JavaTimeModule())
                                                .writeValueAsString(projectInfoUpdateReqDTO)
                                                .getBytes());

                mockMvc.perform(MockMvcRequestBuilders
                                .multipart(HttpMethod.PUT, url + "/information/" + UUID.randomUUID().toString())
                                .file(projectInfoReqeust)
                                .header(accessHeader, BEARER + accessToken())
                                .contentType(MediaType.MULTIPART_FORM_DATA))
                                .andExpect(status().isInternalServerError());

        }

}
