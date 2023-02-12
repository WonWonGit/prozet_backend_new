package com.example.prozet.modules.projectSchedule.domain.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.example.prozet.enum_pakage.ScheduleType;
import com.example.prozet.modules.project.domain.entity.ProjectEntity;
import com.example.prozet.modules.projectMember.domain.entity.ProjectMemberEntity;
import com.example.prozet.modules.projectSchedule.domain.dto.response.ProjectScheduleResDTO;
import com.example.prozet.modules.schedule.domain.dto.response.ScheduleResDTO;
import com.example.prozet.modules.schedule.domain.entity.ScheduleEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PROJECT_SCHEDULE")
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne
    @JoinColumn(name = "PROJECT_MEMBER_IDX", referencedColumnName = "idx")
    private ProjectMemberEntity projectMemberEntity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "SCHEDULE_IDX", referencedColumnName = "idx")
    private ScheduleEntity scheduleEntity;

    @ManyToOne(optional = false)
    @JoinColumn(name = "PROJECT_IDX", referencedColumnName = "idx")
    private ProjectEntity projectEntity;

    private ScheduleType scheduleType;

    public ProjectScheduleResDTO toProjectScheduleResDTO() {
        return ProjectScheduleResDTO.builder()
                .idx(idx)
                .projectMemberFindResDTO(projectMemberEntity.toProjectMemberFindResDTO())
                .scheduleResDTO(scheduleEntity.toScheduleResDTO())
                .scheduleType(scheduleType)
                .build();
    }

}
