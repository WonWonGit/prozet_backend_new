package com.example.prozet.modules.project.domain.entity;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.project.domain.dto.response.ProjectListDTO;
import com.example.prozet.modules.project.domain.dto.response.ProjectResDTO;
import com.example.prozet.modules.projectInformation.domain.dto.response.ProjectInfoResDTO;
import com.example.prozet.modules.projectInformation.domain.entity.ProjectInfoEntity;
import com.example.prozet.modules.projectMember.domain.entity.ProjectMemberEntity;
import com.example.prozet.modules.projectSchedule.domain.entity.ProjectScheduleEntity;
import com.example.prozet.modules.projectStack.domain.entity.ProjectStackEntity;
import com.example.prozet.modules.schedule.domain.entity.ScheduleEntity;
import com.example.prozet.modules.stack.domain.entity.StackCategoryEntity;
import com.example.prozet.modules.stack.domain.entity.StackEntity;
import com.example.prozet.utils.UtilsClass;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PROJECT")
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
// @DynamicInsert
public class ProjectEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    @Column(name = "project_key")
    private String projectKey;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    // 기본 정보 입력완료할 경우 Y로 바꿔주기
    @Column(name = "delete_date")
    private LocalDateTime deleteDate;
    @Column(name = "delete_yn")
    // @ColumnDefault("N")
    private String deleteYn;

    // @OneToOne(optional = false)
    // @JoinColumn(name = "member_idx", referencedColumnName = "idx")
    // private MemberEntity owner;

    @OneToOne
    @JoinColumn(name = "projectInfo_idx", referencedColumnName = "idx")
    private ProjectInfoEntity projectInformation;

    @OneToMany(mappedBy = "projectEntity")
    private List<ProjectMemberEntity> projectMemberEntity;

    @OneToMany(mappedBy = "projectEntity")
    private List<ProjectStackEntity> projectStackEntitiy;

    @OneToMany(mappedBy = "projectEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StackCategoryEntity> stackCategoryEntity;

    @OneToMany(mappedBy = "projectEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<StackEntity> stackEntity;

    @OneToMany(mappedBy = "projectEntity", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProjectScheduleEntity> projectScheduleEntity;

    public void saveProjectInfoEntity(ProjectInfoEntity projectInfoEntity) {
        this.projectInformation = projectInfoEntity;
    }

    public ProjectResDTO toProjectResDTO() {
        return ProjectResDTO.builder()
                .idx(idx)
                .projectKey(projectKey)
                .deleteYn(deleteYn)
                // .owner(owner.toMemberResDto())
                .projectInfoResDTO(projectInformation != null ? projectInformation.toProjectInfoResDTO() : null)
                .build();
    }

    // private Long idx;
    // private String projectKey;
    // private String title;
    // // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    // private String startDate;
    // // @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    // private String endDate;
    // private String projectMemberType;

    // public ProjectListDTO toProjectListDTO(){
    // return ProjectListDTO.builder()
    // .idx(idx)
    // .projectKey(projectKey)
    // .title(projectInformation.getTitle())
    // .startDate(UtilsClass.date(projectInformation.getStartDate()))
    // .endDate(UtilsClass.date(projectInformation.getEndDate()))
    // .projectMemberType()

    // }

    // public ProjectResDTO toProjectResDTO(ProjectInfoResDTO projectInfoResDTO) {
    // return ProjectResDTO.builder()
    // .idx(idx)
    // .projectKey(projectKey)
    // // .owner(owner.toMemberResDto())
    // .projectInfoResDTO(projectInfoResDTO != null ? projectInfoResDTO : null)
    // .deleteYn(deleteYn)
    // .build();
    // }

}
