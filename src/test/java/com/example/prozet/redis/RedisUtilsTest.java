package com.example.prozet.redis;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.Duration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.test.context.ActiveProfiles;

import com.example.prozet.config.EmbeddedRedisConfig;

@DataRedisTest
@Import(EmbeddedRedisConfig.class)
@ActiveProfiles("test")
public class RedisUtilsTest {

    @Autowired
    private StringRedisTemplate redisTemplate;

    @BeforeEach
    public void setDataTest() {

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        Duration expireDuration = Duration.ofMillis(5L);
        valueOperations.set("key", "value", expireDuration);

    }

    @Test
    public void getDataTest() {

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String value = valueOperations.get("key");

        assertThat(value).isEqualTo("value");

    }

    @Test
    public void deleteDataTest() {

        redisTemplate.delete("key");
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String value = valueOperations.get("key");

        assertThat(value).isEqualTo(null);

    }

}
