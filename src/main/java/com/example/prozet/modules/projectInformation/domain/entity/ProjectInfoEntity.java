package com.example.prozet.modules.projectInformation.domain.entity;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.example.prozet.modules.file.domain.entity.FileMasterEntity;
import com.example.prozet.modules.projectInformation.domain.dto.request.ProjectInfoUpdateReqDTO;
import com.example.prozet.modules.projectInformation.domain.dto.response.ProjectInfoResDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PROJECT_INFO")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ProjectInfoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    private String title;
    private String content;
    @Column(name = "start_date")
    private LocalDateTime startDate;
    @Column(name = "end_date")
    private LocalDateTime endDate;
    @OneToOne
    @JoinColumn(name = "file_master_idx", referencedColumnName = "idx")
    FileMasterEntity fileMasterEntity;

    public void saveFileMasterEntity(FileMasterEntity fileMasterEntity) {
        this.fileMasterEntity = fileMasterEntity;
    }

    public ProjectInfoResDTO toProjectInfoResDTO() {
        return ProjectInfoResDTO.builder()
                .idx(idx)
                .title(title)
                .content(content)
                .startDate(startDate)
                .endDate(endDate)
                .fileMaster(fileMasterEntity != null ? fileMasterEntity.toFileMasterDTO() : null)
                .build();

    }

    public void updateProjectInfo(ProjectInfoUpdateReqDTO projectInfoUpdateReqDTO) {
        this.title = projectInfoUpdateReqDTO.getTitle();
        this.content = projectInfoUpdateReqDTO.getContent();
        this.startDate = projectInfoUpdateReqDTO.getStartDate();
        this.endDate = projectInfoUpdateReqDTO.getEndDate();
    }

}
