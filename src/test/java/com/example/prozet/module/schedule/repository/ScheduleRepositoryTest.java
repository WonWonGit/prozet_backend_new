package com.example.prozet.module.schedule.repository;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import com.example.prozet.config.QueryDSLConfigTest;
import com.example.prozet.modules.schedule.domain.entity.ScheduleEntity;
import com.example.prozet.modules.schedule.repository.ScheduleRepository;

@DataJpaTest
@ActiveProfiles("test")
@Import(QueryDSLConfigTest.class)
public class ScheduleRepositoryTest {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Test
    public void saveScheduleTest() {

        ScheduleEntity scheduleEntity = ScheduleEntity.builder()
                .content("content")
                .title("todo")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();

        ScheduleEntity scheduleEntityPS = scheduleRepository.save(scheduleEntity);

        assertThat(scheduleEntityPS.getTitle()).isEqualTo("todo");

    }

}
