package com.example.prozet.modules.projectSchedule.domain.dto.request;

import java.util.List;

import com.example.prozet.enum_pakage.ScheduleType;
import com.example.prozet.modules.schedule.domain.dto.request.ScheduleReqDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectScheduleSaveReqDTO {

    private ScheduleReqDTO scheduleReqDTO;
    private List<Long> projectMemberIdx;
    private String projectKey;
    private ScheduleType scheduleType;

}
