package com.example.prozet.module.stack.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
import com.example.prozet.modules.stack.domain.dto.response.StackCategoryFindResDTO;
import com.example.prozet.modules.stack.service.StackCategoryService;
import com.example.prozet.modules.stack.service.StackService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class StackCategoryTest {

    @Autowired
    MockMvc mockMvc;

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
    public void getStackCategoryListTest() throws JsonProcessingException, Exception {

        when(stackCategoryService.getStackCategory(anyString())).thenReturn(getStackCategoryResDTO());

        mockMvc.perform(get("/v1/api/stackcategory/projectKey")
            .header(accessHeader, BEARER + accessToken())
        ).andExpect(status().isOk());

    }

    public List<StackCategoryFindResDTO> getStackCategoryResDTO() {

        StackCategoryFindResDTO stackCategoryFindResDTO1 = StackCategoryFindResDTO.builder()
                .category("backend")
                .stackType(StackType.DEFAULTSTACK)
                .idx(1)
                .build();

        StackCategoryFindResDTO stackCategoryFindResDTO2 = StackCategoryFindResDTO.builder()
                .category("frontend")
                .stackType(StackType.DEFAULTSTACK)
                .idx(2)
                .build();

        List<StackCategoryFindResDTO> stackCategoryList = new ArrayList<>();

        stackCategoryList.add(stackCategoryFindResDTO1);
        stackCategoryList.add(stackCategoryFindResDTO2);

        return stackCategoryList;

    }

}
