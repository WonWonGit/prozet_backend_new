package com.example.prozet.modules.projectInformation.domain.dto.request;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.validation.constraints.NotBlank;

import org.springframework.format.annotation.DateTimeFormat;

import com.example.prozet.modules.projectInformation.domain.entity.ProjectInfoEntity;
import com.example.prozet.utils.UtilsClass;
import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProjectInfoReqDTO {

    @NotBlank(message = "title is require")
    private String title;

    private String content;

    @DateTimeFormat(pattern = "yyyy-MM-ddT00:00:00")
    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime startDate;

    @DateTimeFormat(pattern = "yyyy-MM-ddT00:00:00")
    // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDateTime endDate;

    public ProjectInfoEntity toEntity() {
        return ProjectInfoEntity.builder()
                .title(title)
                .content(content)
                .startDate(startDate)
                .endDate(startDate)
                .build();
    }

}
