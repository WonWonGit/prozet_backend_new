package com.example.prozet.modules.schedule.domain.dto.response;

import java.time.LocalDateTime;

import com.example.prozet.modules.schedule.domain.entity.ScheduleEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class ScheduleResDTO {

    private Long idx;
    private String title;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public ScheduleEntity toEntity() {
        return ScheduleEntity.builder()
                .idx(idx)
                .title(title)
                .content(content)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

}
