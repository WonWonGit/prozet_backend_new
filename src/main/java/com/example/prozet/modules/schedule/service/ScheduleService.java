package com.example.prozet.modules.schedule.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.prozet.common.ErrorCode;
import com.example.prozet.common.CustomException;
import com.example.prozet.modules.schedule.domain.dto.request.ScheduleEditReqDTO;
import com.example.prozet.modules.schedule.domain.dto.request.ScheduleReqDTO;
import com.example.prozet.modules.schedule.domain.dto.response.ScheduleResDTO;
import com.example.prozet.modules.schedule.domain.entity.ScheduleEntity;
import com.example.prozet.modules.schedule.repository.ScheduleRepository;

@Service
@Transactional(readOnly = true)
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Transactional
    public ScheduleResDTO saveSchedule(ScheduleReqDTO scheduleReqDTO) {

        ScheduleEntity scheduleEntity = scheduleReqDTO.toEntity();

        ScheduleEntity scheduleEntityPS = scheduleRepository.save(scheduleEntity);

        if (scheduleEntityPS != null) {
            return scheduleEntityPS.toScheduleResDTO();
        }

        return null;

    }

    public ScheduleResDTO editSchedule(ScheduleEditReqDTO scheduleEditReqDTO) {

        ScheduleEntity scheduleEntity = scheduleRepository.findByIdx(scheduleEditReqDTO.getIdx())
                .orElseThrow(() -> new CustomException(ErrorCode.SCHEDULE_NOT_EXIST));

        scheduleEntity.editSchedule(scheduleEditReqDTO);

        return scheduleEntity.toScheduleResDTO();
    }

}
