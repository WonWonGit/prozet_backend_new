package com.example.prozet.modules.projectSchedule.domain.dto.request;

import java.util.List;

import com.example.prozet.enum_pakage.ScheduleType;
import com.example.prozet.modules.schedule.domain.dto.request.ScheduleEditReqDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectScheduleEditReqDTO {

    private ScheduleEditReqDTO scheduleEditReqDTO;
    private List<Long> deleteProjectMemberIdx;
    private List<Long> addProjectMemberIdx;
    private ScheduleType scheduleType;

}
