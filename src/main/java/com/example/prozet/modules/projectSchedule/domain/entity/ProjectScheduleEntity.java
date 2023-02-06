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

import com.example.prozet.modules.project.domain.entity.ProjectEntity;
import com.example.prozet.modules.projectMember.domain.entity.ProjectMemberEntity;
import com.example.prozet.modules.schedule.domain.entity.ScheduleEntity;

@Entity
@Table(name = "PROJECT_SCHEDULE")
public class ProjectScheduleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @ManyToOne
    @JoinColumn(name = "PROJECT_MEMBER_IDX", referencedColumnName = "idx")
    private ProjectMemberEntity projectMemberEntity;

    @ManyToOne
    @JoinColumn(name = "SCHEDULE_IDX", referencedColumnName = "idx")
    private ScheduleEntity scheduleEntity;

    @ManyToOne
    @JoinColumn(name = "PROJECT_IDX", referencedColumnName = "idx")
    private ProjectEntity projectEntity;

}
