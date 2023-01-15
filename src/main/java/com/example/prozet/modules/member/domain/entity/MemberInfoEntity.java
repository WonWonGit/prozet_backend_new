package com.example.prozet.modules.member.domain.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.example.prozet.modules.file.domain.entity.FileMasterEntity;
import com.example.prozet.modules.member.domain.dto.request.MemberInfoUpdateDTO;
import com.example.prozet.modules.member.domain.dto.response.MemberInfoResDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "MEMBER_INFOMATION")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfoEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idx;

    @OneToOne
    @JoinColumn(name = "member_idx", referencedColumnName = "idx")
    private MemberEntity memberEntity;

    @OneToOne
    @JoinColumn(name = "file_master_idx", referencedColumnName = "idx", nullable = true)
    private FileMasterEntity fileMasterEntity;

    private String job;
    private String github;
    private String blog;

    public void saveJoinEntity(FileMasterEntity fileMasterEntity, MemberEntity memberEntity) {
        this.memberEntity = memberEntity;
        this.fileMasterEntity = fileMasterEntity;
    }

    public void updateMemberInfo(MemberInfoUpdateDTO updateDTO) {
        this.blog = updateDTO.getBlog();
        this.job = updateDTO.getJob();
        this.github = updateDTO.getGithub();
    }

    public MemberInfoResDTO toMemberInfoResDto() {
        return MemberInfoResDTO.builder().idx(idx)
                .github(github)
                .job(job)
                .blog(blog)
                .member(memberEntity.toMemberResDto())
                .fileMaster(fileMasterEntity != null ? fileMasterEntity.toFileMasterDTO() : null)
                .build();
    }

}
