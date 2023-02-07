package com.example.prozet.modules.schedule.domain.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.example.prozet.modules.schedule.domain.dto.response.ScheduleResDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "SCHEDULE")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private String title;
    private String content;

    public ScheduleResDTO toScheduleResDTO() {
        return ScheduleResDTO.builder()
                .idx(idx)
                .content(content)
                .title(title)
                .startDate(startDate)
                .endDate(endDate)
                .build();
    }

}
