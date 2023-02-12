package com.example.prozet.modules.project.utils;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import com.example.prozet.enum_pakage.AccessType;
import com.example.prozet.enum_pakage.StateType;
import com.example.prozet.modules.member.domain.dto.response.MemberResDTO;
import com.example.prozet.modules.projectMember.domain.dto.response.ProjectMemberResDTO;
import com.example.prozet.utils.UtilsClass;

public class ProjectUtil {

    public static String createProjectKey() {

        String date = UtilsClass.getCurrentDate();

        return UUID.randomUUID().toString().substring(0, 8).concat("_").concat(date);
    }

    public static boolean projectMemberAccessEditCheck(List<ProjectMemberResDTO> projectMemberResDTO, String username) {

        if (projectMemberResDTO != null) {
            Optional<ProjectMemberResDTO> existCheck = projectMemberResDTO.stream()
                    .filter(member -> member.getMemberResDTO().getUsername().equals(username)).findFirst();

            if (existCheck.isPresent()) {
                return existCheck.get().getAccess().equals(AccessType.EDIT);
            }
        }

        return false;

    }

    public static boolean projectOwnerCheck(ProjectMemberResDTO memberResDTO, String username) {

        if (memberResDTO.getMemberResDTO().getUsername().equals(username)) {
            return true;
        } else {
            return false;
        }

    }
}
