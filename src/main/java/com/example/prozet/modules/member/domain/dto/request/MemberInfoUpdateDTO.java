package com.example.prozet.modules.member.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberInfoUpdateDTO {

    private String job;
    private String github;
    private String blog;
    private int fileIdx;
    private String displayName;

}
