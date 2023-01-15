package com.example.prozet.module.mail.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.example.prozet.modules.email.service.EmailService;
import com.example.prozet.modules.projectMember.service.ProjectMemberService;
import com.example.prozet.redis.RedisUtil;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

@SpringBootTest
@AutoConfigureMockMvc
public class EmailControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Test
    public void confirmMailTest() throws Exception {

        mockMvc.perform(get("/confirm-invite")
                .queryParam("token", "token")
                .queryParam("projectKey", "projectKey"))
                .andExpect(status().isUnauthorized());

    }

}
