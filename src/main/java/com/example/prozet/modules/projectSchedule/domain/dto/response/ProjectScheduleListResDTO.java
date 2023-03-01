package com.example.prozet.modules.projectSchedule.domain.dto.response;

import java.util.List;

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
public class ProjectScheduleListResDTO {

    private Long idx;
    private List<ProjectMemberFindResDTO> projectMemberFindResDTO;
    private ScheduleResDTO scheduleResDTO;
    private ScheduleType scheduleType;

    public ProjectScheduleListResDTO(List<ProjectScheduleResDTO> projectScheduleResDTOs) {
        this.idx = projectScheduleResDTOs.get(0).getIdx();
        this.projectMemberFindResDTO = projectScheduleResDTOs.stream()
                .map(ProjectScheduleResDTO::getProjectMemberFindResDTO).toList();
        this.scheduleResDTO = projectScheduleResDTOs.get(0).getScheduleResDTO();
        this.scheduleType = projectScheduleResDTOs.get(0).getScheduleType();
    }

}
