package com.example.prozet.modules.projectMember.domain.entity;

import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.CreationTimestamp;

import com.example.prozet.enum_pakage.AccessType;
import com.example.prozet.enum_pakage.ProjectMemberType;
import com.example.prozet.enum_pakage.StateType;
import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.project.domain.entity.ProjectEntity;
import com.example.prozet.modules.projectMember.domain.dto.response.ProjectMemberFindResDTO;
import com.example.prozet.modules.projectMember.domain.dto.response.ProjectMemberResDTO;
import com.example.prozet.modules.projectSchedule.domain.entity.ProjectScheduleEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "PROJECT_MEMBER")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ProjectMemberEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;
    @Enumerated(EnumType.STRING)
    private AccessType access;
    @Enumerated(EnumType.STRING)
    private StateType state;
    @Enumerated(EnumType.STRING)
    private ProjectMemberType projectMemberType;
    @CreationTimestamp
    private LocalDateTime createDate;
    private String deleteYn;
    private LocalDateTime deleteDate;

    @ManyToOne
    @JoinColumn(name = "project_idx", referencedColumnName = "idx")
    private ProjectEntity projectEntity;

    @OneToOne
    @JoinColumn(name = "member_idx", referencedColumnName = "idx")
    private MemberEntity memberEntity;

    public ProjectMemberResDTO toProjectMemberResDTO() {

        return ProjectMemberResDTO.builder()
                .idx(idx)
                .access(access)
                .state(state)
                .memberResDTO(memberEntity != null ? memberEntity.toMemberResDto() : null)
                // .projectResDTO(projectEntity != null ? projectEntity.toProjectResDTO() :
                // null)
                .deleteYn(deleteYn)
                .projectMemberType(projectMemberType)
                .build();
    }

    public ProjectMemberFindResDTO toProjectMemberFindResDTO() {

        return ProjectMemberFindResDTO.builder()
                .idx(idx)
                .access(access)
                .state(state)
                .memberResDTO(memberEntity != null ? memberEntity.toMemberResDto() : null)
                .deleteYn(deleteYn)
                .build();
    }

    public void inviteAccept() {
        this.state = StateType.ACCEPTED;
    }

    public void editMemberAccess(AccessType access) {
        this.access = access;
    }

    public void memberDelete() {
        this.deleteYn = "Y";
        this.deleteDate = LocalDateTime.now();
    }

}
