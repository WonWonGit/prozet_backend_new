package com.example.prozet.module.stack.controller;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.beans.factory.annotation.Value;

import com.example.prozet.modules.stack.domain.dto.response.StackFindResDTO;
import com.example.prozet.modules.stack.service.StackService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class StackControllerTest {

    @Autowired
    MockMvc mockMvc;

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
    public void getStackListTest() throws JsonProcessingException, Exception {

        when(stackService.getStackList(anyString())).thenReturn(getStackList());

        mockMvc.perform(get("/v1/api/stack/projectKey")
            .header(accessHeader, BEARER + accessToken())
        ).andExpect(status().isOk());

    }

    public Map<String, List<StackFindResDTO>> getStackList() {

        StackFindResDTO stackFindResDTO1 = StackFindResDTO.builder()
                .category("Backend")
                .categoryIdx(1)
                .name("Spring")
                .icon("icon")
                .build();

        StackFindResDTO stackFindResDTO2 = StackFindResDTO.builder()
                .category("Frontend")
                .categoryIdx(2)
                .name("React")
                .icon("icon")
                .build();

        List<StackFindResDTO> stackFindResDTOs = new ArrayList<>();
        stackFindResDTOs.add(stackFindResDTO1);
        stackFindResDTOs.add(stackFindResDTO2);

        Map<String, List<StackFindResDTO>> stackGroup = stackFindResDTOs.stream().collect(
                Collectors.groupingBy(StackFindResDTO::getCategory));

        return stackGroup;

    }

}
