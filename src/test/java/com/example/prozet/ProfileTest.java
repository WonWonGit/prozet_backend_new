package com.example.prozet;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class ProfileTest {

    @Value("${profileTest.hello}")
    private String hello;

    @Test
    public void profileSettingTest() {
        assertThat(hello).isEqualTo("testSetting");
    }

}
