package com.example.prozet.module.projectMember.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.example.prozet.enum_pakage.AccessType;
import com.example.prozet.modules.project.domain.dto.response.ProjectResDTO;
import com.example.prozet.modules.projectMember.domain.dto.request.ProjectMemberReqDTO;
import com.example.prozet.modules.projectMember.domain.dto.response.ProjectMemberResDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProjectMemberApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Value("${jwt.access_header_string}")
    private String accessHeader;

    private static String BEARER = "Bearer ";

    ObjectMapper objectMapper = new ObjectMapper();

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
    public void savaProjectMemberTest() throws JsonProcessingException, Exception {

        ProjectMemberReqDTO projectMemberReqDTO = ProjectMemberReqDTO.builder().projectKey("projectKey")
                .username("savaProjectMemberTest").access(AccessType.EDIT).build();

        mockMvc.perform(post("/v1/api/project/member")
                .header(accessHeader, BEARER + accessToken())
                .content(objectMapper.writeValueAsString(projectMemberReqDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError());

    }

    @Test
    @WithMockUser(username = "google_123123", value = "user")
    public void editProjectMemberAccessTest() throws JsonProcessingException, Exception {

        mockMvc.perform(put("/v1/api/project/member", 1L)
                .header(accessHeader, BEARER + accessToken())
                .content(objectMapper.writeValueAsString(AccessType.READONLY))
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isInternalServerError());

    }

    @Test
    @WithMockUser(username = "google_123123", value = "user")
    public void deleteProjectMemberTest() throws JsonProcessingException, Exception {

        mockMvc.perform(put("/v1/api/project/member/delete", 1L)
                .header(accessHeader, BEARER + accessToken()))
                .andExpect(status().isBadRequest());

    }

}
