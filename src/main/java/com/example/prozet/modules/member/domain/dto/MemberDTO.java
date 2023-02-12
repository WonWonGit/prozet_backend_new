package com.example.prozet.modules.member.domain.dto;

import com.example.prozet.enum_pakage.Provider;
import com.example.prozet.enum_pakage.Role;
import com.example.prozet.modules.member.domain.entity.MemberEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDTO {

    private Long idx;
    private String username;
    private String name;
    private String email;
    private Provider provider;
    private Role role;
    private String displayName;

    public MemberEntity toMemberEntity() {
        return MemberEntity.builder()
                .idx(idx)
                .username(username)
                .displayName(displayName)
                .name(name)
                .email(email)
                .provider(provider)
                .role(role)
                .build();
    }

}
