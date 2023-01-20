package com.example.prozet.module.stack.controller;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.example.prozet.enum_pakage.StackType;
import com.example.prozet.modules.project.service.ProjectService;
import com.example.prozet.modules.stack.domain.dto.request.StackReqDTO;
import com.example.prozet.modules.stack.domain.dto.response.StackCategoryResDTO;
import com.example.prozet.modules.stack.domain.dto.response.StackResDTO;
import com.example.prozet.modules.stack.service.StackService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class StackApiControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    private StackService stackService;

    @MockBean
    private ProjectService projectService;

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
    public void saveStackTest() throws Exception {

        StackCategoryResDTO stackCategoryResDTO = StackCategoryResDTO.builder()
                .idx(1)
                .category("backend")
                .projectResDTO(null)
                .stackType(StackType.DEFAULTSTACK).build();

        StackResDTO stackResDTO = StackResDTO.builder()
                .icon("iconUrl")
                .name("stack")
                .stackCategory(stackCategoryResDTO)
                .stackType(StackType.CUSTOMSTACK)
                .build();

        StackReqDTO stackReqDTO = StackReqDTO.builder().iconUrl("iconUrl").StackCategoryIdx(1).name("stack").build();

        when(stackService.saveStack(any(), any(), any())).thenReturn(stackResDTO);

        MockMultipartFile stackRequest = new MockMultipartFile("stackReqDTO", "", MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsString(stackReqDTO).getBytes());

        mockMvc.perform(multipart("/v1/api/stack")
                .file(stackRequest)
                .header(accessHeader, BEARER + accessToken())
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk());

    }

}
