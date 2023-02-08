package com.example.prozet.modules.member.domain.dto.response;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

import com.example.prozet.enum_pakage.Provider;
import com.example.prozet.enum_pakage.Role;
import com.example.prozet.modules.member.domain.entity.MemberEntity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MemberResDTO {

    private Long idx;
    private String username;
    private String name;
    private String email;
    @Enumerated(EnumType.STRING)
    private Provider provider;
    @Enumerated(EnumType.STRING)
    private Role role;
    private String displayName;

    public MemberEntity toEntity() {
        return MemberEntity.builder()
                .idx(idx)
                .username(username)
                .name(name)
                .email(email)
                .provider(provider)
                .role(role)
                .displayName(displayName)
                .build();
    }

}
