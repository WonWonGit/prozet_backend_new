package com.example.prozet;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import com.example.prozet.security.jwt.JwtProvider;
import com.fasterxml.jackson.databind.ObjectMapper;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class JwtControllerTest {

    @Autowired
    MockMvc mockMvc;

    ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    JwtProvider jwtProvider;

    private Map<String, Object> getGoogleLoginData(){
       
        Map<String, Object> map = new HashMap<>();
        map.put("email", "test@google.com");
        map.put("sub", "123123");

        return map;
    }

    @Value("${jwt.access_header_string}")
    private String accessHeader;

    @Value("${jwt.refresh_header_string}")
    private String refreshHeader;

    @Test
    @DisplayName("로그인 및 회원가입 테스트")    
    public void loginTest() throws Exception{

        Map<String, Object> map = getGoogleLoginData();

        MvcResult result = mockMvc.perform(post("/login/google")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(map)))
                            .andExpect(status().isOk()).andReturn();
        
        String access = result.getResponse().getHeader(accessHeader);
        String refresh = result.getResponse().getHeader(refreshHeader);

        assertThat(access).isNotEmpty();
        assertThat(refresh).isNotEmpty();
                    
    }
    
}
