package com.example.prozet.modules.projectSchedule.domain.dto.response;

import com.example.prozet.enum_pakage.ScheduleType;
import com.example.prozet.modules.projectMember.domain.dto.response.ProjectMemberFindResDTO;
import com.example.prozet.modules.schedule.domain.dto.response.ScheduleResDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProjectScheduleResDTO {

    private Long idx;
    private ProjectMemberFindResDTO projectMemberFindResDTO;
    private ScheduleResDTO scheduleResDTO;
    private ScheduleType scheduleType;

}
