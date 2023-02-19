package com.example.prozet.module.projectMember.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import com.example.prozet.enum_pakage.AccessType;
import com.example.prozet.enum_pakage.ProjectMemberType;
import com.example.prozet.enum_pakage.Provider;
import com.example.prozet.enum_pakage.StateType;
import com.example.prozet.modules.member.domain.dto.response.MemberResDTO;
import com.example.prozet.modules.project.domain.dto.response.ProjectResDTO;
import com.example.prozet.modules.project.service.ProjectService;
import com.example.prozet.modules.projectInformation.domain.dto.response.ProjectInfoResDTO;
import com.example.prozet.modules.projectMember.domain.dto.request.ProjectMemberEditReqDTO;
import com.example.prozet.modules.projectMember.domain.dto.request.ProjectMemberReqDTO;
import com.example.prozet.modules.projectMember.domain.dto.response.ProjectMemberResDTO;
import com.example.prozet.modules.projectMember.service.ProjectMemberService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProjectMemberApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ProjectService projectService;

    @MockBean
    private ProjectMemberService projectMemberService;

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

        mockMvc.perform(post("/v1/api/projectmember")
                .header(accessHeader, BEARER + accessToken())
                .content(objectMapper.writeValueAsString(projectMemberReqDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isInternalServerError());

    }

    @Test
    @WithMockUser(username = "google_123123", value = "user")
    public void editProjectMemberAccessTest() throws JsonProcessingException, Exception {

        ProjectMemberEditReqDTO projectMemberEditReqDTO = ProjectMemberEditReqDTO.builder()
                .projectKey("projectKey")
                .accessType(AccessType.READONLY)
                .build();

        ProjectResDTO projectResDTO = getProjectResDTO();

        when(projectService.findByProjectKey(anyString())).thenReturn(projectResDTO);
        when(projectMemberService.editProjectMemberAccess(anyLong(), any(), anyString()))
                .thenReturn(getEditedProjectMemberResDTO(projectMemberEditReqDTO));

        mockMvc.perform(put("/v1/api/projectmember/1")
                .header(accessHeader, BEARER + accessToken())
                .content(objectMapper.writeValueAsString(projectMemberEditReqDTO))
                .contentType(MediaType.APPLICATION_JSON_VALUE)).andExpect(status().isOk())
                .andDo(MockMvcResultHandlers.print());

    }

    @Test
    @WithMockUser(username = "google_123123", value = "user")
    public void deleteProjectMemberTest() throws JsonProcessingException, Exception {

        mockMvc.perform(put("/v1/api/project/member/delete", 1L)
                .header(accessHeader, BEARER + accessToken()))
                .andExpect(status().isBadRequest());

    }

    public ProjectResDTO getProjectResDTO() {

        MemberResDTO memberResDTO = MemberResDTO.builder()
                .username("google_123123")
                .name("name")
                .displayName("displayName")
                .provider(Provider.GOOGLE)
                .email("google@google.com")
                .build();

        ProjectMemberResDTO owner = ProjectMemberResDTO.builder()
                .access(AccessType.EDIT)
                .projectMemberType(ProjectMemberType.OWNER)
                .memberResDTO(memberResDTO)
                .state(StateType.ACCEPTED)
                .deleteYn("N")
                .build();

        ProjectResDTO projectResDTO = ProjectResDTO.builder()
                .projectKey("projectKey")
                .owner(owner)
                .build();

        return projectResDTO;

    }

    public ProjectMemberResDTO getEditedProjectMemberResDTO(ProjectMemberEditReqDTO projectMemberEditReqDTO) {

        MemberResDTO memberResDTO = MemberResDTO.builder()
                .username("google_1222222")
                .name("name")
                .displayName("displayName")
                .provider(Provider.GOOGLE)
                .email("google@google.com")
                .build();

        ProjectMemberResDTO projectMemberResDTO = ProjectMemberResDTO.builder()
                .access(projectMemberEditReqDTO.getAccessType())
                .memberResDTO(memberResDTO)
                .state(StateType.ACCEPTED)
                .projectMemberType(ProjectMemberType.TEAMMEMBER)
                .deleteYn("N")
                .build();

        return projectMemberResDTO;

    }

}
