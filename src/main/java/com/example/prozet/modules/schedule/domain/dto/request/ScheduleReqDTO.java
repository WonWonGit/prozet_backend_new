package com.example.prozet.modules.schedule.domain.dto.request;

import java.time.LocalDateTime;

import com.example.prozet.modules.schedule.domain.entity.ScheduleEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ScheduleReqDTO {

    private String title;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public ScheduleEntity toEntity() {
        return ScheduleEntity.builder()
                .content(content)
                .startDate(startDate)
                .endDate(endDate)
                .title(title)
                .build();
    }

}
