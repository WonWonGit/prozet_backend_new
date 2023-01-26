package com.example.prozet.module.stack.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.example.prozet.enum_pakage.StackType;
import com.example.prozet.modules.member.domain.dto.response.MemberResDTO;
import com.example.prozet.modules.project.domain.dto.response.ProjectResDTO;
import com.example.prozet.modules.project.service.ProjectService;
import com.example.prozet.modules.projectInformation.domain.dto.response.ProjectInfoResDTO;
import com.example.prozet.modules.stack.domain.dto.response.StackCategoryResDTO;
import com.example.prozet.modules.stack.service.StackCategoryService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class StackCategoryApiControllerTest {

        @Autowired
        MockMvc mockMvc;

        @MockBean
        private ProjectService projectService;

        @MockBean
        private StackCategoryService stackCategoryService;

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
        public void saveStackCategoryTest() throws JsonProcessingException, Exception {

                MemberResDTO owner = MemberResDTO.builder().username("google_123123").build();

                ProjectInfoResDTO projectInfoResDTO = ProjectInfoResDTO.builder()
                                .title("projectTitle")
                                .build();

                ProjectResDTO projectResDTO = ProjectResDTO.builder()
                                .owner(owner)
                                .projectInfoResDTO(projectInfoResDTO)
                                .projectKey("projectKey")
                                .build();

                StackCategoryResDTO stackCategoryResDTO = StackCategoryResDTO.builder().category("category")
                                .projectResDTO(projectResDTO).stackType(StackType.CUSTOMSTACK).build();

                when(projectService.findByProjectKey(anyString())).thenReturn(projectResDTO);
                when(stackCategoryService.saveStackCategory(any(), any())).thenReturn(stackCategoryResDTO);

                String stackCategory = "stack";

                mockMvc.perform(post("/v1/api/stackCategory/12112")
                                .header(accessHeader, BEARER + accessToken())
                                .content(stackCategory))
                                .andExpect(status().isOk());

        }

}
