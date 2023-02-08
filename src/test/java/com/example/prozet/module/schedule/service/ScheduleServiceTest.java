package com.example.prozet.module.schedule.service;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.prozet.modules.schedule.domain.dto.request.ScheduleReqDTO;
import com.example.prozet.modules.schedule.domain.dto.response.ScheduleResDTO;
import com.example.prozet.modules.schedule.domain.entity.ScheduleEntity;
import com.example.prozet.modules.schedule.repository.ScheduleRepository;
import com.example.prozet.modules.schedule.service.ScheduleService;

@ExtendWith(MockitoExtension.class)
public class ScheduleServiceTest {

    @InjectMocks
    private ScheduleService scheduleService;

    @Mock
    private ScheduleRepository scheduleRepository;

    @Test
    public void saveScheduleTest() {

        ScheduleReqDTO scheduleReqDTO = ScheduleReqDTO.builder()
                .content("content")
                .title("title")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();

        when(scheduleRepository.save(any())).thenReturn(getScheduleEntity());

        ScheduleResDTO scheduleResDTO = scheduleService.saveSchedule(scheduleReqDTO);

        assertThat(scheduleResDTO.getTitle()).isEqualTo("title");

    }

    // ******************* DATA ******************** */
    public ScheduleEntity getScheduleEntity() {
        ScheduleEntity scheduleEntity = ScheduleEntity.builder()
                .title("title")
                .content("content")
                .startDate(LocalDateTime.now())
                .endDate(LocalDateTime.now())
                .build();

        return scheduleEntity;
    }

}
