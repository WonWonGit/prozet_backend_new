package com.example.prozet.modules.schedule.domain.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.example.prozet.modules.projectSchedule.domain.entity.ProjectScheduleEntity;
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
    @OneToMany(mappedBy = "scheduleEntity")
    private List<ProjectScheduleEntity> projectSchedule;

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
