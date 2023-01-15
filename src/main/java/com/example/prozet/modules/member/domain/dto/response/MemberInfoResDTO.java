package com.example.prozet.modules.member.domain.dto.response;

import com.example.prozet.modules.file.domain.dto.response.FileMasterDTO;
import com.example.prozet.modules.member.domain.entity.MemberInfoEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
// @AllArgsConstructor
@NoArgsConstructor
public class MemberInfoResDTO {

    private Long idx;
    private MemberResDTO member;
    private FileMasterDTO fileMaster;
    private String job;
    private String github;
    private String blog;

    public MemberInfoEntity toEntity() {
        return MemberInfoEntity.builder()
                .idx(idx)
                .blog(blog)
                .github(github)
                .job(job)
                .memberEntity(member.toEntity())
                .fileMasterEntity(fileMaster.toEntity())
                .build();
    }

    public MemberInfoResDTO(Long idx, MemberResDTO member, FileMasterDTO fileMaster, String job, String github,
            String blog) {
        this.idx = idx;
        this.member = member;
        this.fileMaster = fileMaster;
        this.job = job;
        this.github = github;
        this.blog = blog;
    }

}