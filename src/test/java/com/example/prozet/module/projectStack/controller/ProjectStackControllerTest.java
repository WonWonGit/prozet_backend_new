package com.example.prozet.module.projectStack.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.json.JSONArray;
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

import com.example.prozet.enum_pakage.StackType;
import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.project.domain.dto.response.ProjectResDTO;
import com.example.prozet.modules.project.domain.entity.ProjectEntity;
import com.example.prozet.modules.project.service.ProjectService;
import com.example.prozet.modules.projectStack.domain.entity.ProjectStackEntity;
import com.example.prozet.modules.projectStack.service.ProjectStackService;
import com.example.prozet.modules.stack.domain.dto.response.StackUnmappedResDTO;
import com.example.prozet.modules.stack.domain.entity.StackCategoryEntity;
import com.example.prozet.modules.stack.domain.entity.StackEntity;
import com.example.prozet.modules.stack.service.StackService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProjectStackControllerTest {

        @Autowired
        MockMvc mockMvc;

        @MockBean
        private ProjectService projectService;

        @MockBean
        private ProjectStackService projectStackService;

        @MockBean
        private StackService stackService;

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
    public void saveProjectStackTest() throws JsonProcessingException, Exception {

        when(stackService.findByIdx(anyLong())).thenReturn(getStackEntity().toStackResDTO());
        when(projectService.findByProjectKey(any())).thenReturn(getProjectEntity().toProjectResDTO());
        when(projectStackService.saveProjectStack(any(), any())).thenReturn(getProjectStackEntity().toProjectStackUnmmapedResDTO());

        List<Long> stackIdxList = new ArrayList<>();
        stackIdxList.add(1l);
        stackIdxList.add(2l);

        mockMvc.perform(post("/v1/api/project/stack/projectKey")
            .header(accessHeader, BEARER + accessToken())
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(stackIdxList)))
            .andExpect(status().isOk())
            .andDo(MockMvcResultHandlers.print());

    }

        @Test
        @WithMockUser(username = "google_123123", value = "user")
        public void editProjectStackTest() throws Exception {

                ProjectResDTO projectResDTO = getProjectEntity().toProjectResDTO();

                JSONArray stackIdxList = new JSONArray();
                stackIdxList.put(1L);
                stackIdxList.put(2L);

                when(projectService.findByProjectKey(anyString())).thenReturn(projectResDTO);
                when(projectStackService.findProjectStack(anyLong(), any()))
                                .thenReturn(getProjectStackEntity().toProjectStackResDTO());

                ProjectStackEntity projectStackEntity = getProjectStackEntity();
                projectStackEntity.editCheckedYn(projectStackEntity.getCheckedYn());

                when(projectStackService.editProjectStack(any()))
                                .thenReturn(projectStackEntity.toProjectStackUnmmapedResDTO());

                mockMvc.perform(put("/v1/api/project/stack/projectKey")
                                .header(accessHeader, BEARER + accessToken())
                                .content(stackIdxList.toString())
                                .contentType(MediaType.APPLICATION_JSON_VALUE))
                                .andExpect(status().isOk())
                                .andDo(MockMvcResultHandlers.print());

        }

        // ********** Create Model ***********/

        public ProjectEntity getProjectEntity() {

                ProjectEntity projectEntity = ProjectEntity.builder()
                                .projectKey("projectKey")
                                .deleteYn("N")
                                .build();

                return projectEntity;

        }

        public StackEntity getStackEntity() {

                StackCategoryEntity stackCategoryEntity = StackCategoryEntity.builder()
                                .idx(1)
                                .category("backend")
                                .projectEntity(null)
                                .stackType(StackType.DEFAULTSTACK).build();

                StackEntity stackEntity = StackEntity.builder()
                                .icon("iconUrl")
                                .name("stack")
                                .stackCategory(stackCategoryEntity)
                                .stackType(StackType.CUSTOMSTACK)
                                .projectEntity(getProjectEntity())
                                .build();

                return stackEntity;

        }

        public ProjectStackEntity getProjectStackEntity() {
                ProjectStackEntity projectStackEntity = ProjectStackEntity.builder()
                                .stackEntity(getStackEntity())
                                .checkedYn("Y")
                                .projectEntity(getProjectEntity())
                                .build();

                return projectStackEntity;
        }

}
