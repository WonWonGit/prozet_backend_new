package com.example.prozet.modules.schedule.domain.dto.request;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ScheduleEditReqDTO {

    private Long idx;
    private String title;
    private String content;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

}
