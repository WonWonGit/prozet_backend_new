package com.example.prozet.modules.projectInformation.domain.dto.response;

import java.time.LocalDate;
import java.time.LocalDateTime;

import com.example.prozet.modules.file.domain.dto.response.FileMasterDTO;
import com.example.prozet.modules.project.domain.dto.response.ProjectResDTO;
import com.example.prozet.modules.projectInformation.domain.entity.ProjectInfoEntity;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProjectInfoResDTO {

    private Long idx;
    private String title;
    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime endDate;
    private FileMasterDTO fileMaster;

    @JsonIgnore
    private ProjectResDTO projectResDTO;

    public ProjectInfoEntity toEntity() {
        return ProjectInfoEntity.builder()
                .idx(idx)
                .title(title)
                .content(content)
                .startDate(startDate)
                .endDate(endDate)
                .fileMasterEntity(fileMaster != null ? fileMaster.toEntity() : null)
                .build();

    }

}
