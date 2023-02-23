package com.example.prozet.modules.projectSchedule.domain.dto.request;

import java.util.List;

import com.example.prozet.enum_pakage.ScheduleType;
import com.example.prozet.modules.schedule.domain.dto.request.ScheduleEditReqDTO;

public class ProjectScheduleEditReqDTO {

    private ScheduleEditReqDTO scheduleEditReqDTO;
    private List<Long> deleteProjectMemberIdx;
    private List<Long> addProjectMemberIdx;
    private ScheduleType scheduleType;

}
