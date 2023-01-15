package com.example.prozet.modules.member.domain.dto.request;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.web.multipart.MultipartFile;

import com.example.prozet.modules.member.domain.entity.MemberEntity;
import com.example.prozet.modules.member.domain.entity.MemberInfoEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoSaveReqDTO {

    @NotBlank(message = "Email is required")
    @Email(message = "It is not Email format")
    private String email;
    @NotBlank(message = "Name is required")
    private String name;
    private String job;
    private String github;
    private String blog;

    public MemberInfoEntity toEntity() {
        return MemberInfoEntity.builder()
                .blog(blog)
                .github(github)
                .job(job)
                .build();
    }

    public void setMemberInfo(String email, String name) {
        this.email = email;
        this.name = name;
    }

}
