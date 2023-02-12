package com.example.prozet.modules.project.domain.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.prozet.enum_pakage.ProjectMemberType;
import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.projectMember.domain.entity.ProjectMemberEntity;
import com.example.prozet.utils.UtilsClass;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.querydsl.core.types.dsl.DateTemplate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProjectListDTO {

    private Long idx;
    private String projectKey;
    private String title;
    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String startDate;
    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private String endDate;
    private String projectMemberType;

    public ProjectListDTO(Long idx, String projectKey,
            String title,
            LocalDateTime startDate,
            LocalDateTime endDate,
            ProjectMemberType projectMemberType) {
        this.idx = idx;
        this.projectKey = projectKey;
        this.title = title;
        this.startDate = UtilsClass.date(startDate);
        this.endDate = UtilsClass.date(endDate);
        this.projectMemberType = projectMemberType.projectMemberType();
    }

}
